package cazador.furnaceoverhaul.inventory;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;

public class ContainerFO extends Container {

	private final IInventory te;
	
	private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;
	private int storage;
	
    public ContainerFO(InventoryPlayer playerInventory, IInventory te){
        this.te = te;
        this.addSlotToContainer(new Slot(te, 0, 56, 17));
        this.addSlotToContainer(new SlotFurnaceFuel(te, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, te, 2, 116, 35));
        this.addSlotToContainer(new UpgradeSlot(te, 3, 12, 13));
        this.addSlotToContainer(new UpgradeSlot(te, 4, 12, 34));
        this.addSlotToContainer(new UpgradeSlot(te, 5, 12, 55));
        
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 9; ++j){
            	int x = 8 + j * 18;
                int y = i * 18 + 84;
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x, y));
            }
        }

        for (int k = 0; k < 9; ++k){
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
    
    public void addListener(IContainerListener listener){
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.te);
    }

    public void detectAndSendChanges(){
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i){
            IContainerListener icontainerlistener = (IContainerListener)this.listeners.get(i);

            if (this.cookTime != this.te.getField(2)){
                icontainerlistener.sendWindowProperty(this, 2, this.te.getField(2));
            }

            if (this.furnaceBurnTime != this.te.getField(0)){
                icontainerlistener.sendWindowProperty(this, 0, this.te.getField(0));
            }

            if (this.currentItemBurnTime != this.te.getField(1)){
                icontainerlistener.sendWindowProperty(this, 1, this.te.getField(1));
            }

            if (this.totalCookTime != this.te.getField(3)){
                icontainerlistener.sendWindowProperty(this, 3, te.getField(3));
            }
            
            if (this.storage != this.te.getField(4))  {
            	icontainerlistener.sendWindowProperty(this, 4, this.te.getField(4));
            }
        }

        this.cookTime = this.te.getField(2);
        this.furnaceBurnTime = this.te.getField(0);
        this.currentItemBurnTime = this.te.getField(1);
        this.totalCookTime = this.te.getField(3);
        this.storage = this.te.getField(4);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data){
       this.te.setField(id, data);
    }

    public boolean canInteractWith(EntityPlayer player){
        return this.te.isUsableByPlayer(player);
    }

    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            //Upgrade slots
            else if (index == 3 || index == 4 || index == 5) {
            	 if (!this.mergeItemStack(itemstack1, 3, 5, true))
                 {
                     return ItemStack.EMPTY;
                 }
            	 slot.onSlotChange(itemstack1, itemstack);
            }
            
            else if (index != 1 && index != 0)
            {
                if (!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty())
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (TileEntityIronFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 36 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
