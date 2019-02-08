package cazador.furnaceoverhaul.tile;

import cazador.furnaceoverhaul.blocks.IronFurnace;
import cazador.furnaceoverhaul.capability.EnergyStorageFurnace;
import cazador.furnaceoverhaul.init.ModItems;
import cazador.furnaceoverhaul.inventory.ContainerFO;
import cazador.furnaceoverhaul.utils.OreDoublingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileEntityIronFurnace_2 extends TileEntityLockable implements ITickable, ISidedInventory, IItemHandler {
	public static final int[] SLOTS_INPUT = {0};
	//output slot and fuel slot
	public static final int[] SLOTS_BOTTOM = {2, 1};
	public static final int[] SLOTS_FUEL = {1};
	public static final int[] SLOTS_UPGRADE = new int[] {3, 4, 5};
	public static final int INPUT_SLOT = 1;
	public static final int FUEL_SLOT = 1;
	public static final int OUTPUT_SLOT = 1;
	public static final int UPGRADE_SLOTS = 3;
	public static final int SIZE = INPUT_SLOT+ FUEL_SLOT + OUTPUT_SLOT + UPGRADE_SLOTS;
	public NonNullList<ItemStack> slot = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
	public ItemStackHandler handler = new ItemStackHandler(6);
	private EnergyStorageFurnace storage = new EnergyStorageFurnace(50000, FE_PER_TICK_INPUT);
    public static final int FE_PER_TICK_INPUT = 1200;
    private static final int FE_PER_TICK = 600;
	private int maxCookTime = 170;
	private int cookTime = 0;
	private int clientCookTime = -1;
	private int clientEnergy = -1;
	private int totalCookTime;
	public int furnaceBurnTime;
    public int currentItemBurnTime;

	public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int time) {
        this.cookTime = time;
    }
    
    public int getCookTime(ItemStack stack){
        if(hasUpgrade(ModItems.speed) == true) {
        	return 140;
        } else
        return 170;
    }

    public int getClientProgress() {
        return clientCookTime;
    }

    public void setClientCookTime(int clientCookTime) {
        this.clientCookTime = clientCookTime;
    }

    public int getClientEnergy() {
        return clientEnergy;
    }

    public void setClientEnergy(int clientEnergy) {
        this.clientEnergy = clientEnergy;
    }

    public int getEnergy() {
        return storage.getEnergyStored();
    }
	
    public boolean hasUpgrade(Item item) {
    	for (int slot : SLOTS_UPGRADE) {
    		final ItemStack stack = this.getStackInSlot(slot);
    		if (stack.getItem() == item) {
    			return true;
    		}
    	}	
    	return false;
    }
    
    @Override
    public void update() {
    	boolean flag = this.isBurning();
        boolean flag1 = false;
        if (!world.isRemote) {

            if (storage.getEnergyStored() < FE_PER_TICK && hasUpgrade(ModItems.electricfuel) == true) {
            	IronFurnace.setState(false, world, pos);
                return;
            }

            if (cookTime > 0 && hasUpgrade(ModItems.electricfuel) == true) {
            	IronFurnace.setState(true, world, pos);
                storage.consumePower(FE_PER_TICK);
                cookTime--;
                if (cookTime <= 0) {
                    canSmelt();
                }
                markDirty();
            } else {
                smeltItem();
            }
        }
            else if (this.isBurning() && hasUpgrade(ModItems.electricfuel) == false){
                --this.furnaceBurnTime;
            }

            if (!this.world.isRemote){
                ItemStack itemstack = (ItemStack)this.slot.get(1);

                if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)this.slot.get(0)).isEmpty()){
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
                                    this.slot.set(1, item1);
                                }
                            }
                        }
                    }

                    if (this.isBurning() && this.canSmelt()){
                        ++this.cookTime;

                        if (this.cookTime == this.totalCookTime){
                            this.cookTime = 0;
                            this.totalCookTime = this.getCookTime((ItemStack)this.slot.get(0));
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

    public boolean canSmelt() {
        if (((ItemStack)this.slot.get(INPUT_SLOT)).isEmpty()){
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult((ItemStack)this.slot.get(INPUT_SLOT));
            if (itemstack.isEmpty()){
                return false;
            } else {
                ItemStack itemstack1 = (ItemStack)this.slot.get(OUTPUT_SLOT);
                if (itemstack1.isEmpty()) return true;
                if (!itemstack1.isItemEqual(itemstack)) return false;
                int result = itemstack1.getCount() + itemstack.getCount();
                return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize();
            }
        }
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack input = this.slot.get(INPUT_SLOT);
            ItemStack recipeResult = FurnaceRecipes.instance().getSmeltingResult(input);
            ItemStack output = this.slot.get(OUTPUT_SLOT);

            //Processing Upgrade
            if (output.isEmpty()) {
                ItemStack returnStack = recipeResult.copy();
                
                if (hasUpgrade(ModItems.processing) == true && hasUpgrade(ModItems.oreprocessing) == false) {
                	if(OreDoublingRegistry.getSmeltingResult(input).getCount()==1){
               		 	returnStack.setCount(returnStack.getCount());
                	} else{
               		 	returnStack.setCount(returnStack.getCount() * 2);
                	}
                }
                slot.set(OUTPUT_SLOT, returnStack);
            } if (output.getItem() == recipeResult.getItem() && hasUpgrade(ModItems.processing) == true && hasUpgrade(ModItems.oreprocessing) == false){
            	if(recipeResult.getCount()==1){
            		output.grow(recipeResult.getCount());
            	} else{
            		output.grow(recipeResult.getCount() * 2);
            	}
            }
            //Ore Processing Upgrade
             if (output.isEmpty()) {
                ItemStack returnStack = recipeResult.copy();
                
                if (hasUpgrade(ModItems.oreprocessing) == true && hasUpgrade(ModItems.processing) == false) {
                	if(OreDoublingRegistry.getSmeltingResult(input).getCount()==1){
                		 returnStack.setCount(returnStack.getCount());
                	} else{
                		 returnStack.setCount(returnStack.getCount() * 2);
                	}
                }
                slot.set(2, returnStack);
            } if (output.getItem() == recipeResult.getItem() && hasUpgrade(ModItems.oreprocessing) == true && hasUpgrade(ModItems.processing) == false){
            	if(OreDoublingRegistry.getSmeltingResult(input).getCount()==1){
            		output.grow(recipeResult.getCount());
            	} else{
            		output.grow(recipeResult.getCount() * 2);
            	}
            } else if (output.getItem() == recipeResult.getItem()) {
            	output.grow(recipeResult.getCount());
            }
            if (input.getItem() == Item.getItemFromBlock(Blocks.SPONGE) && input.getMetadata() == 1 && !((ItemStack)this.slot.get(FUEL_SLOT)).isEmpty() && ((ItemStack)this.slot.get(FUEL_SLOT)).getItem() == Items.BUCKET) {
            	this.slot.set(FUEL_SLOT, new ItemStack(Items.WATER_BUCKET));
            }
            input.shrink(1);
        }
    }
    
    public int getItemBurnTime(ItemStack stack) {
		if (hasUpgrade(ModItems.electricfuel) == true) {
			return 0;
		}
		else if (hasUpgrade(ModItems.efficiency) == true) {
			return TileEntityFurnace.getItemBurnTime(stack) * 2;
		}
			return TileEntityFurnace.getItemBurnTime(stack);
	}
    
    public static boolean isItemFuel(ItemStack stack){
        return TileEntityFurnace.getItemBurnTime(stack) > 0;
    }
    
    public boolean isBurning(){
        return this.furnaceBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory){
        return inventory.getField(0) > 0;
    }
    
    //slot stuff-----------------------------------------------------------------------------------------------------------------------
    public static void registerFixesFurnace(DataFixer fixer){
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityIronFurnace.class, new String[] {"Items"}));
    }
    
    public int getSizeInventory(){
        return this.slot.size();
    }

    public boolean isEmpty(){
        for (ItemStack itemstack : this.slot){
            if (!itemstack.isEmpty()){
                return false;
            }
        }

        return true;
    }

    public ItemStack decrStackSize(int index, int count){
        return ItemStackHelper.getAndSplit(this.slot, index, count);
    }

    public ItemStack removeStackFromSlot(int index){
        return ItemStackHelper.getAndRemove(this.slot, index);
    }

    public void setInventorySlotContents(int index, ItemStack stack){
        ItemStack itemstack = (ItemStack)this.slot.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.slot.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()){
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag){
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }
    
    public int getInventoryStackLimit(){
        return 64;
    }
    
    public ItemStack getStackInSlot(int index){
        return (ItemStack)this.slot.get(index);
    }
    
    public boolean isUsableByPlayer(EntityPlayer player){
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player){}

    public void closeInventory(EntityPlayer player){}

    public boolean isItemValidForSlot(int index, ItemStack stack){
        if (index == 2){
            return false;
        } else if (index != 1){
            return true;
        } else{
            ItemStack itemstack = (ItemStack)this.slot.get(1);
            return isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && itemstack.getItem() != Items.BUCKET;
        }
    }

    public int[] getSlotsForFace(EnumFacing side){
   	 if (side == EnumFacing.DOWN){
            return SLOTS_BOTTOM;
        } else {
            return side == EnumFacing.UP ? SLOTS_INPUT : SLOTS_FUEL;
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
        this.slot.clear();
    }
	
	//NBT------------------------------------------------------------------------------------------------------------------------------
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readRestorableFromNBT(compound);
        
    }

    public void readRestorableFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("itemsIn")) {
            handler.deserializeNBT((NBTTagCompound) compound.getTag("itemsIn"));
        }
        if (compound.hasKey("itemsOut")) {
            handler.deserializeNBT((NBTTagCompound) compound.getTag("itemsOut"));
        }
        cookTime = compound.getInteger("cookTime");
        storage.setEnergy(compound.getInteger("energy"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeRestorableToNBT(compound);
        return compound;
    }

    public void writeRestorableToNBT(NBTTagCompound compound) {
        compound.setTag("itemsIn", handler.serializeNBT());
        compound.setTag("itemsOut", handler.serializeNBT());
        compound.setInteger("cookTime", cookTime);
        compound.setInteger("energy", storage.getEnergyStored());
    }
	
	//capabilities---------------------------------------------------------------------------------------------------------------------
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		if(capability == CapabilityEnergy.ENERGY) return true;
		else return false;

	}
    
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
    	if(capability == CapabilityEnergy.ENERGY)
			return (T) this.storage;
    	if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            if (facing == EnumFacing.DOWN)
                return (T) new SidedInvWrapper(this, EnumFacing.DOWN);
            else if (facing == EnumFacing.UP)
                return (T) new SidedInvWrapper(this, EnumFacing.UP);
            else
                return (T) new SidedInvWrapper(this, EnumFacing.WEST);
    }
    return super.getCapability(capability, facing);
    }

	public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    public Container createContainer(EntityPlayer player) {
        return new ContainerFO(player.inventory, this);
    }

    public String getGuiID(){
        return "furnaceoverhaul:ironfurnace";
    }
    
}