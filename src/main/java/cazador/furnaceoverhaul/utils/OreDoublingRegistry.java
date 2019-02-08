package cazador.furnaceoverhaul.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Handles the ore processing upgrade.
 * @author Brandon3055
 *
 */
public class OreDoublingRegistry {

	public static Map<String, ItemStack> oreRecipes = new HashMap<>();
	public static Map<Ingredient, ItemStack> stackToStackRecipes = new HashMap<>();

	public static void init() {
		for (String oreName : OreDictionary.getOreNames()) {
			if (!oreName.startsWith("ore")) {
				continue;
			}

			String ingot = oreName.replace("ore", "ingot");

			if (!OreDictionary.doesOreNameExist(ingot) || OreDictionary.getOres(ingot).isEmpty() || OreDictionary.getOres(oreName).isEmpty()) {
				ingot = oreName.replace("ore", "gem");
				if (!OreDictionary.doesOreNameExist(ingot) || OreDictionary.getOres(ingot).isEmpty() || OreDictionary.getOres(oreName).isEmpty()) {
					continue;
				}
			}

			List<ItemStack> ingots = OreDictionary.getOres(ingot);
			int oreId = OreDictionary.getOreID(oreName);
			ItemStack stack = ItemStack.EMPTY;

			for (ItemStack candidate : ingots) {
				boolean invalid = false;
				for (int id : OreDictionary.getOreIDs(candidate)) {
					if (id == oreId) {
						invalid = true;
						break;
					}
				}
				if (invalid) {
					continue;
				}
				stack = candidate;
				ResourceLocation registryName = candidate.getItem().getRegistryName();
				if (registryName != null && registryName.getNamespace().equals("thermalfoundation")) {
					break;
				}
			}

			if (!stack.isEmpty()) {
				stack = stack.copy();
				stack.setCount(2);
				registerOreResult(oreName, stack);
			}
		}

		registerDEOverrides();
	}

	public static void registerOreResult(String ore, ItemStack result) {
		oreRecipes.put(ore, result);
	}

	public static void registerResult(ItemStack input, ItemStack result) {
		stackToStackRecipes.put(Ingredient.fromStacks(input), result);
	}

	public static ItemStack getSmeltingResult(ItemStack stack) {
		if (stack.isEmpty()) return ItemStack.EMPTY;

		for (Map.Entry<Ingredient, ItemStack> ent : stackToStackRecipes.entrySet())
			if (ent.getKey().apply(stack)) return ent.getValue();

		int ids[] = OreDictionary.getOreIDs(stack);
		for (int id : ids) {
			String sid = OreDictionary.getOreName(id);
			if (oreRecipes.containsKey(sid)) return oreRecipes.getOrDefault(sid, ItemStack.EMPTY);
		}

		return FurnaceRecipes.instance().getSmeltingResult(stack);
	}

	private static void registerDEOverrides() {
		registerOreResult("sand", new ItemStack(Blocks.GLASS, 2));
		registerOreResult("cobblestone", new ItemStack(Blocks.STONE, 2));
		registerOreResult("netherrack", new ItemStack(Items.NETHERBRICK, 2));
		registerOreResult("cobblestone", new ItemStack(Blocks.STONE, 2));
		registerResult(new ItemStack(Items.CLAY_BALL), new ItemStack(Items.BRICK, 2));
	}

}