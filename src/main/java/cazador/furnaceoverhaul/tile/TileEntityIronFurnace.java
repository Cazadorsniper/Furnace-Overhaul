package cazador.furnaceoverhaul.tile;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import cazador.furnaceoverhaul.blocks.IronFurnace;
import cazador.furnaceoverhaul.init.ModItems;
import cazador.furnaceoverhaul.inventory.ContainerFO;
import cazador.furnaceoverhaul.inventory.UpgradeSlot;

public class TileEntityIronFurnace extends TileEntityLockable implements ITickable, ISidedInventory {
	
	public static final int[] SLOTS_TOP = {0};
    public static final int[] SLOTS_BOTTOM = {2, 1};
    public static final int[] SLOTS_SIDES = {1};
    public static final int[] SLOT_UPGRADE = new int[] {3, 4, 5};
    public NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public int cookTime;
    public int totalCookTime;
    public String furnaceCustomName;
	
	public int getSizeInventory(){
        return this.furnaceItemStacks.size();
    }

    public boolean isEmpty(){
        for (ItemStack itemstack : this.furnaceItemStacks){
            if (!itemstack.isEmpty()){
                return false;
            }
        }

        return true;
    }

    public ItemStack getStackInSlot(int index){
        return (ItemStack)this.furnaceItemStacks.get(index);
    }

    public ItemStack decrStackSize(int index, int count){
        return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
    }

    public ItemStack removeStackFromSlot(int index){
        return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack){
        ItemStack itemstack = (ItemStack)this.furnaceItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.furnaceItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()){
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag){
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    public String getName(){
        return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
    }

    public boolean hasCustomName(){
        return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_){
        this.furnaceCustomName = p_145951_1_;
    }

    public static void registerFixesFurnace(DataFixer fixer){
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityIronFurnace.class, new String[] {"Items"}));
    }
    
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
        this.furnaceBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime((ItemStack)this.furnaceItemStacks.get(1));
        if (compound.hasKey("CustomName", 8)){
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short)this.furnaceBurnTime);
        compound.setInteger("CookTime", (short)this.cookTime);
        compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);
        if (this.hasCustomName()){
            compound.setString("CustomName", this.furnaceCustomName);
        }
        return compound;
    }

    public int getInventoryStackLimit(){
        return 64;
    }

    public boolean isBurning(){
        return this.furnaceBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory){
        return inventory.getField(0) > 0;
    }

    public void update(){
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()){
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote){
            ItemStack itemstack = (ItemStack)this.furnaceItemStacks.get(1);

            if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)this.furnaceItemStacks.get(0)).isEmpty()){
                if (!this.isBurning() && this.canSmelt()){
                    this.furnaceBurnTime = getItemBurnTime(itemstack);
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning()){
                        flag1 = true;

                        if (!itemstack.isEmpty()){
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);

                            if (itemstack.isEmpty()){
                                ItemStack item1 = item.getContainerItem(itemstack);
                                this.furnaceItemStacks.set(1, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()){
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime){
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime((ItemStack)this.furnaceItemStacks.get(0));
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else{
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0){
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning()){
                flag1 = true;
                IronFurnace.setState(this.isBurning(), this.world, this.pos);
            }
        }

        if (flag1){
            this.markDirty();
        }
    }
    
    @Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, nbt);
	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound nbt = pkt.getNbtCompound();
	}

	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return nbt;
	}

	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	public NBTTagCompound getTileData() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return nbt;
	}

    public int getCookTime(ItemStack stack){
        return 160;
    }

    public boolean canSmelt() {
        if (((ItemStack)this.furnaceItemStacks.get(0)).isEmpty()){
            return false;
        }
        else{
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult((ItemStack)this.furnaceItemStacks.get(0));

            if (itemstack.isEmpty()){
                return false;
            }
            else {
                ItemStack itemstack1 = (ItemStack)this.furnaceItemStacks.get(2);
                if (itemstack1.isEmpty()) return true;
                if (!itemstack1.isItemEqual(itemstack)) return false;
                int result = itemstack1.getCount() + itemstack.getCount();
                return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
            }
        }
    }

    public void smeltItem(){
        if (this.canSmelt()){
            ItemStack itemstack = (ItemStack)this.furnaceItemStacks.get(0);
            ItemStack itemstack1 = FurnaceRecipes.instance().getSmeltingResult(itemstack);
            ItemStack itemstack2 = (ItemStack)this.furnaceItemStacks.get(2);

            if (itemstack2.isEmpty()){
                this.furnaceItemStacks.set(2, itemstack1.copy());
            }
            else if (itemstack2.getItem() == itemstack1.getItem()){
                itemstack2.grow(itemstack1.getCount());
            }

            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && itemstack.getMetadata() == 1 && !((ItemStack)this.furnaceItemStacks.get(1)).isEmpty() && ((ItemStack)this.furnaceItemStacks.get(1)).getItem() == Items.BUCKET){
                this.furnaceItemStacks.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    public static int getItemBurnTime(ItemStack stack){
        if (stack.isEmpty()){
            return 0;
        }
        else{
            Item item = stack.getItem();
            if (!item.getRegistryName().getResourceDomain().equals("minecraft")){
                int burnTime = ForgeEventFactory.getItemBurnTime(stack);
                if (burnTime != 0) return burnTime;
            }
            return item == Item.getItemFromBlock(Blocks.WOODEN_SLAB) ? 150 : (item == Item.getItemFromBlock(Blocks.WOOL) ? 100 : (item == Item.getItemFromBlock(Blocks.CARPET) ? 67 : (item == Item.getItemFromBlock(Blocks.LADDER) ? 300 : (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON) ? 100 : (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD ? 300 : (item == Item.getItemFromBlock(Blocks.COAL_BLOCK) ? 16000 : (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName()) ? 200 : (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName()) ? 200 : (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName()) ? 200 : (item == Items.STICK ? 100 : (item != Items.BOW && item != Items.FISHING_ROD ? (item == Items.SIGN ? 200 : (item == Items.COAL ? 1600 : (item == Items.LAVA_BUCKET ? 20000 : (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL ? (item == Items.BLAZE_ROD ? 2400 : (item instanceof ItemDoor && item != Items.IRON_DOOR ? 200 : (item instanceof ItemBoat ? 400 : 0))) : 100)))) : 300)))))))))));
        }
    }

    public static boolean isItemFuel(ItemStack stack){
        return getItemBurnTime(stack) > 0;
    }

    public boolean isUsableByPlayer(EntityPlayer player){
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player){}

    public void closeInventory(EntityPlayer player){}

    public boolean isItemValidForSlot(int index, ItemStack stack){
        if (index == 2){
            return false;
        }
        else if (index != 1){
            return true;
        }
        else{
            ItemStack itemstack = (ItemStack)this.furnaceItemStacks.get(1);
            return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }

    public int[] getSlotsForFace(EnumFacing side){
    	 if (side == EnumFacing.DOWN){
             return SLOTS_BOTTOM;
         }
         else {
             return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
         }
    }

    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction){
        return this.isItemValidForSlot(index, itemStackIn);
    }

    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction){
        if (direction == EnumFacing.DOWN && index == 1){
            Item item = stack.getItem();

            if (item != Items.WATER_BUCKET && item != Items.BUCKET){
                return false;
            }
        }

        return true;
    }

    public String getGuiID(){
        return "furnaceoverhaul:ironfurnace";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn){
        return new ContainerFO(playerInventory, this);
    }

    public int getField(int id){
        switch (id){
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value){
        switch (id){
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount(){
        return 4;
    }

    public void clear(){
        this.furnaceItemStacks.clear();
    }
    
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);

	}
    
    public <T> T getCapability(Capability<T> capability, EnumFacing facing){
    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new SidedInvWrapper(this, facing));    
    	}
			return super.getCapability(capability, facing);
    }
    
}