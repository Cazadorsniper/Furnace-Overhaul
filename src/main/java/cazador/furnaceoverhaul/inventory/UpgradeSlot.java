package cazador.furnaceoverhaul.inventory;

import cazador.furnaceoverhaul.items.ItemUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class UpgradeSlot extends SlotItemHandler {

	public UpgradeSlot(IItemHandler inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return super.isItemValid(stack) && stack.getItem() instanceof ItemUpgrade;

	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

}
