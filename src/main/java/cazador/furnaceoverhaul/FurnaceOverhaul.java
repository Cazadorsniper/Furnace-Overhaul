package cazador.furnaceoverhaul;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cazador.furnaceoverhaul.handler.GuiHandler;
import cazador.furnaceoverhaul.init.ModObjects;
import cazador.furnaceoverhaul.net.MessageSyncTE;
import cazador.furnaceoverhaul.tile.TileEntityDiamondFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEmeraldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEndFurnace;
import cazador.furnaceoverhaul.tile.TileEntityGoldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import cazador.furnaceoverhaul.tile.TileEntityZenithFurnace;
import cazador.furnaceoverhaul.utils.OreProcessingRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = FurnaceOverhaul.MODID, name = FurnaceOverhaul.MODNAME, version = FurnaceOverhaul.VERSION)
public class FurnaceOverhaul {

	public static final String MODID = "furnaceoverhaul";
	public static final String MODNAME = "Furnace Overhaul";
	public static final String VERSION = "2.1.2";
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	public static Object2IntMap<String> FLUID_FUELS = new Object2IntOpenHashMap<>();

	@Instance
	public static FurnaceOverhaul INSTANCE;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		NETWORK.registerMessage(MessageSyncTE.Handler.class, MessageSyncTE.class, 0, Side.CLIENT);
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		String[] fuels = cfg.getStringList("Fluid Fuels", "general", new String[] { "lava@20" }, "A list a fluid fuels, in the format name@time, where time is burn ticks per millibucket.");
		for (String s : fuels) {
			String[] split = s.split("@");
			if (split.length != 2) {
				LOGGER.info("Ignoring invalid fluid fuel config entry {}!", s);
				continue;
			}
			try {
				FLUID_FUELS.put(split[0], Integer.parseInt(split[1]));
			} catch (NumberFormatException e) {
				LOGGER.info("Ignoring invalid fluid fuel config entry {}!", s);
				continue;
			}
		}
		if (cfg.hasChanged()) cfg.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.registerTileEntity(TileEntityIronFurnace.class, ModObjects.IRON_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityGoldFurnace.class, ModObjects.GOLD_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityDiamondFurnace.class, ModObjects.DIAMOND_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityEmeraldFurnace.class, ModObjects.EMERALD_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityEndFurnace.class, ModObjects.END_FURNACE.getRegistryName());
		GameRegistry.registerTileEntity(TileEntityZenithFurnace.class, ModObjects.ZENITH_FURNACE.getRegistryName());
	}

	@EventHandler
	public void postInit(FMLLoadCompleteEvent event) {
		OreProcessingRegistry.registerDefaults();
	}

	public static final CreativeTabs FO_TAB = new CreativeTabs(MODID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModObjects.IRON_FURNACE);
		}
	};

}
