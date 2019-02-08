package cazador.furnaceoverhaul.init;

import java.util.ArrayList;
import java.util.List;

import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.blocks.BlockIronFurnace;
import cazador.furnaceoverhaul.blocks.BlockTranslucentFurnace;
import cazador.furnaceoverhaul.items.ItemIronKit;
import cazador.furnaceoverhaul.items.ItemKit;
import cazador.furnaceoverhaul.items.ItemUpgrade;
import cazador.furnaceoverhaul.tile.TileEntityDiamondFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEmeraldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEndFurnace;
import cazador.furnaceoverhaul.tile.TileEntityGoldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import cazador.furnaceoverhaul.tile.TileEntityZenithFurnace;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Main object class for Furnace Overhaul.  Handles registration.
 * Fields are automatically populated by @ObjectHolder, since their field name is an all-caps version of their registry name.
 * @author Shadows
 *
 */
@EventBusSubscriber(modid = FurnaceOverhaul.MODID)
@ObjectHolder(FurnaceOverhaul.MODID)
public class ModObjects {

	public static final BlockIronFurnace IRON_FURNACE = null;
	public static final BlockIronFurnace GOLD_FURNACE = null;
	public static final BlockIronFurnace DIAMOND_FURNACE = null;
	public static final BlockIronFurnace EMERALD_FURNACE = null;
	public static final BlockIronFurnace END_FURNACE = null;
	public static final BlockIronFurnace ZENITH_FURNACE = null;

	public static final ItemUpgrade BLANK_UPGRADE = null;
	public static final ItemUpgrade EFFICIENCY_UPGRADE = null;
	public static final ItemUpgrade ORE_PROCESSING_UPGRADE = null;
	public static final ItemUpgrade LIQUID_FUEL_UPGRADE = null;
	public static final ItemUpgrade ELECTRIC_FUEL_UPGRADE = null;
	public static final ItemUpgrade SPEED_UPGRADE = null;
	public static final ItemUpgrade PROCESSING_UPGRADE = null;

	public static final ItemKit BLANK_KIT = null;
	public static final ItemKit IRON_KIT = null;
	public static final ItemKit GOLD_KIT = null;
	public static final ItemKit DIAMOND_KIT = null;
	public static final ItemKit EMERALD_KIT = null;
	public static final ItemKit END_KIT = null;
	public static final ItemKit ZENITH_KIT = null;

	public static List<Item> modelList = new ArrayList<>();

	@SubscribeEvent
	public static void blocks(Register<Block> e) {
		//Formatter::off
		registerBlocks(e.getRegistry(), 
				new BlockIronFurnace("iron_furnace", TextFormatting.WHITE, 170, TileEntityIronFurnace::new),
				new BlockIronFurnace("gold_furnace", TextFormatting.YELLOW, 130, TileEntityGoldFurnace::new),
				new BlockTranslucentFurnace("diamond_furnace", TextFormatting.AQUA, 100, TileEntityDiamondFurnace::new),
				new BlockTranslucentFurnace("emerald_furnace", TextFormatting.GREEN, 60, TileEntityEmeraldFurnace::new),
				new BlockIronFurnace("end_furnace", TextFormatting.DARK_GREEN, 30, TileEntityEndFurnace::new),
				new BlockIronFurnace("zenith_furnace", TextFormatting.RED, 1, TileEntityZenithFurnace::new));
		//Formatter::on
	}

	static void registerBlocks(IForgeRegistry<Block> reg, Block... blocks) {
		reg.registerAll(blocks);
		for (Block b : blocks) {
			Item i = new ItemBlock(b).setRegistryName(b.getRegistryName());
			modelList.add(i);
			ForgeRegistries.ITEMS.register(i);
		}
	}

	@SubscribeEvent
	public static void items(Register<Item> e) {
		//Formatter::off
		registerItems(e.getRegistry(),
				new ItemUpgrade("blank_upgrade", null),
				new ItemUpgrade("efficiency_upgrade", "info.furnaceoverhaul.efficiency"),
				new ItemUpgrade("ore_processing_upgrade", "info.furnaceoverhaul.wip"),
				new ItemUpgrade("liquid_fuel_upgrade", "info.furnaceoverhaul.wip"),
				new ItemUpgrade("electric_fuel_upgrade", "info.furnaceoverhaul.electric"),
				new ItemUpgrade("speed_upgrade", "info.furnaceoverhaul.speed"),
				new ItemUpgrade("processing_upgrade", "info.furnaceoverhaul.wip"),
				new ItemKit("blank_kit", null, null),
				new ItemIronKit(),
				new ItemKit("gold_kit", IRON_FURNACE, GOLD_FURNACE),
				new ItemKit("diamond_kit", GOLD_FURNACE, DIAMOND_FURNACE),
				new ItemKit("emerald_kit", DIAMOND_FURNACE, EMERALD_FURNACE),
				new ItemKit("end_kit", EMERALD_FURNACE, END_FURNACE),
				new ItemKit("zenith_kit", END_FURNACE, ZENITH_FURNACE)
				);
		//Formatter::on
	}

	static void registerItems(IForgeRegistry<Item> reg, Item... items) {
		reg.registerAll(items);
		for (Item i : items)
			modelList.add(i);
	}

}
