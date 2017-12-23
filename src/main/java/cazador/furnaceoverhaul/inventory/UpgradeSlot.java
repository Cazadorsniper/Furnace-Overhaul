package cazador.furnaceoverhaul.inventory;

import javax.annotation.Nullable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cazador.furnaceoverhaul.init.ModItems;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;

public class UpgradeSlot extends Slot{

	public UpgradeSlot(IInventory inventoryIn, int index, int x, int y) {
		super(inventoryIn, index, x, y);
	}
	
	 public boolean isItemValid(@Nullable ItemStack stack) {
        return super.isItemValid(stack) && 
        		stack.getItem() == ModItems.blankupgrade ||
        		stack.getItem() == ModItems.efficiency ||
        		stack.getItem() == ModItems.electricfuel ||
        		stack.getItem() == ModItems.electricprovider ||
        		stack.getItem() == ModItems.liquidfuel ||
        		stack.getItem() == ModItems.oreprocessing;
        	
    }
	 
	 public int getItemStackLimit(ItemStack stack){
	        return 1;
	    }
	 
}
