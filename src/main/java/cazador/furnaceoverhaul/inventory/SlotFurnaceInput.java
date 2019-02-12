package cazador.furnaceoverhaul.inventory;

import cazador.furnaceoverhaul.utils.OreProcessingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFurnaceInput extends SlotItemHandler {

	public SlotFurnaceInput(IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return !OreProcessingRegistry.getSmeltingResult(stack).isEmpty();
	}
}