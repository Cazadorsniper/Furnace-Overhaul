package cazador.furnaceoverhaul.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cazador.furnaceoverhaul.init.ModItems;

public class UpgradeSlot extends Slot{

	public UpgradeSlot(IInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	 public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack) && 
        		stack.getItem() == ModItems.blankupgrade ||
        		stack.getItem() == ModItems.efficiency ||
        		stack.getItem() == ModItems.electricfuel ||
        		stack.getItem() == ModItems.electricprovider ||
        		stack.getItem() == ModItems.liquidfuel ||
        		stack.getItem() == ModItems.oreprocessing ||
        		stack.getItem() == ModItems.speed;
        	
    }
	 
	 public int getItemStackLimit(ItemStack stack){
	        return 1;
	    }
	 
}
