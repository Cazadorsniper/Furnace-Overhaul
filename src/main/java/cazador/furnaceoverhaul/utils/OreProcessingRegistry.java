package cazador.furnaceoverhaul.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

/**
 * Handles the ore processing upgrade.
 * @author Shadows
 *
 */
public class OreProcessingRegistry {

	public static List<Pair<Ingredient, ItemStack>> recipes = new ArrayList<>();

	public static void init() {
		for (String input : OreDictionary.getOreNames()) {
			if (!input.startsWith("ore")) continue;

			String output = input.replace("ore", "ingot");

			if (!OreDictionary.doesOreNameExist(output) || OreDictionary.getOres(output).isEmpty()) {
				output = input.replace("ore", "gem");
				if (!OreDictionary.doesOreNameExist(output) || OreDictionary.getOres(output).isEmpty()) continue;
			}

			List<ItemStack> outputs = OreDictionary.getOres(output);
			ItemStack outStack = outputs.get(0);
			ItemStack preferred = ItemStack.EMPTY;

			for (ItemStack candidate : outputs) {
				if (candidate.getItem().getRegistryName().getNamespace().equals("thermalfoundation")) {
					preferred = candidate;
					break;
				}
			}
			if (!preferred.isEmpty()) outStack = preferred;
			if (!outStack.isEmpty()) {
				outStack = outStack.copy();
				outStack.setCount(2);
				registerOre(input, outStack);
			}
		}

		registerDefaults();
	}

	public static void registerOre(String input, ItemStack result) {
		recipes.add(Pair.of(new OreIngredient(input), result));
	}

	public static void registerResult(ItemStack input, ItemStack result) {
		recipes.add(Pair.of(Ingredient.fromStacks(input), result));
	}

	public static ItemStack getSmeltingResult(ItemStack stack) {
		if (stack.isEmpty()) return ItemStack.EMPTY;
		for (Pair<Ingredient, ItemStack> ent : recipes) {
			if (ent.getLeft().apply(stack)) return ent.getRight();
		}
		return FurnaceRecipes.instance().getSmeltingResult(stack);
	}

	private static void registerDefaults() {
		registerOre("sand", new ItemStack(Blocks.GLASS, 2));
		registerOre("cobblestone", new ItemStack(Blocks.STONE, 2));
		registerOre("netherrack", new ItemStack(Items.NETHERBRICK, 2));
		registerOre("cobblestone", new ItemStack(Blocks.STONE, 2));
		registerResult(new ItemStack(Items.CLAY_BALL), new ItemStack(Items.BRICK, 2));
	}

}