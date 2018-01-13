package cazador.furnaceoverhaul.api;

import net.minecraft.item.ItemStack;



public abstract interface IUpgrade {

	public abstract int getItemBurnTime(ItemStack stack);
	
	public abstract int smeltItem();
}
