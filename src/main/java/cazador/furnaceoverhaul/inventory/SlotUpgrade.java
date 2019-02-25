package cazador.furnaceoverhaul.inventory;

import cazador.furnaceoverhaul.items.ItemUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotUpgrade extends SlotItemHandler {

	public SlotUpgrade(IItemHandler inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return isStackValid(stack);
	}

	public static boolean isStackValid(ItemStack stack) {
		return stack.getItem() instanceof ItemUpgrade;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

}
