package cazador.furnaceoverhaul.upgrade;

import cazador.furnaceoverhaul.init.ModObjects;
import net.minecraft.item.crafting.Ingredient;

public class Upgrades {

	public static final Upgrade ELECTRIC_FUEL = new Upgrade(Ingredient.fromItem(ModObjects.ELECTRIC_FUEL_UPGRADE));
	public static final Upgrade EFFICIENCY = new Upgrade(Ingredient.fromItem(ModObjects.EFFICIENCY_UPGRADE));
	public static final Upgrade SPEED = new Upgrade(Ingredient.fromItem(ModObjects.SPEED_UPGRADE));

}
