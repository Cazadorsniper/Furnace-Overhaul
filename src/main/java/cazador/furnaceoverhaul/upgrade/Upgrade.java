package cazador.furnaceoverhaul.upgrade;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class Upgrade {

	protected final Ingredient items;

	public Upgrade(Ingredient items) {
		this.items = items;
	}

	public boolean matches(ItemStack stack) {
		return items.apply(stack);
	}

}
