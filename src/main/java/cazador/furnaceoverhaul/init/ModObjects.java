package cazador.furnaceoverhaul.init;

import java.util.ArrayList;
import java.util.List;

import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.blocks.BlockIronFurnace;
import cazador.furnaceoverhaul.blocks.BlockTranslucentFurnace;
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

@EventBusSubscriber(modid = FurnaceOverhaul.MODID)
@ObjectHolder(FurnaceOverhaul.MODID)
public class ModObjects {

	public static final Block IRON_FURNACE = null;
	public static final Block GOLD_FURNACE = null;
	public static final Block DIAMOND_FURNACE = null;
	public static final Block EMERALD_FURNACE = null;
	public static final Block END_FURNACE = null;
	public static final Block ZENITH_FURNACE = null;

	public static final Item BLANK_UPGRADE = null;
	public static final Item EFFICIENCY_UPGRADE = null;
	public static final Item ORE_PROCESSING_UPGRADE = null;
	public static final Item LIQUID_FUEL_UPGRADE = null;
	public static final Item ELECTRIC_FUEL_UPGRADE = null;
	public static final Item SPEED_UPGRADE = null;
	public static final Item PROCESSING_UPGRADE = null;

	public static final Item BLANK_KIT = null;
	public static final Item IRON_KIT = null;
	public static final Item GOLD_KIT = null;
	public static final Item DIAMOND_KIT = null;
	public static final Item EMERALD_KIT = null;
	public static final Item END_KIT = null;
	public static final Item ZENITH_KIT = null;

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
				new ItemKit("blank_kit"),
				new ItemKit("iron_kit"),
				new ItemKit("gold_kit"),
				new ItemKit("diamond_kit"),
				new ItemKit("emerald_kit"),
				new ItemKit("end_kit"),
				new ItemKit("zenith_kit")
				);
		//Formatter::on
	}

	static void registerItems(IForgeRegistry<Item> reg, Item... items) {
		reg.registerAll(items);
		for (Item i : items)
			modelList.add(i);
	}

}
