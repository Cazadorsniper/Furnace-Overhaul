package cazador.furnaceoverhaul.inventory;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cazador.furnaceoverhaul.init.MItems;

public class UpgradeSlot extends Slot{

	public UpgradeSlot(IInventory inventoryIn, int index, int x, int y) {
		super(inventoryIn, index, x, y);
	}
	
	 public boolean isItemValid(@Nullable ItemStack stack) {
        return super.isItemValid(stack) && stack.getItem() == MItems.upgrade;
        	
    }
	 
	 public int getItemStackLimit(ItemStack stack){
	        return 1;
	    }
	 
}
