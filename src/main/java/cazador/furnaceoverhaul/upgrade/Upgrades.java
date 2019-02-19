package cazador.furnaceoverhaul.upgrade;

import cazador.furnaceoverhaul.init.ModObjects;
import net.minecraft.item.crafting.Ingredient;

/**
 * Holder class for Upgrades.
 * @author Shadows
 *
 */
public class Upgrades {

	public static final Upgrade ELECTRIC_FUEL = new Upgrade(Ingredient.fromItem(ModObjects.ELECTRIC_FUEL_UPGRADE));
	public static final Upgrade EFFICIENCY = new Upgrade(Ingredient.fromItem(ModObjects.EFFICIENCY_UPGRADE));
	public static final Upgrade SPEED = new Upgrade(Ingredient.fromItem(ModObjects.SPEED_UPGRADE));
	public static final Upgrade PROCESSING = new Upgrade(Ingredient.fromItem(ModObjects.PROCESSING_UPGRADE));
	public static final Upgrade ORE_PROCESSING = new Upgrade(Ingredient.fromItem(ModObjects.ORE_PROCESSING_UPGRADE));
	public static final Upgrade LIQUID_FUEL = new Upgrade(Ingredient.fromItem(ModObjects.LIQUID_FUEL_UPGRADE));

}
