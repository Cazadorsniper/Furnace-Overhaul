package cazador.furnaceoverhaul;

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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = FurnaceOverhaul.MODID, name = FurnaceOverhaul.MODNAME, version = FurnaceOverhaul.VERSION)
public class FurnaceOverhaul {

	public static final String MODID = "furnaceoverhaul";
	public static final String MODNAME = "Furnace Overhaul";
	public static final String VERSION = "2.0.1";

	@Instance
	public static FurnaceOverhaul INSTANCE;

	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
		NETWORK.registerMessage(MessageSyncTE.Handler.class, MessageSyncTE.class, 0, Side.CLIENT);
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
	public void postInit(FMLPostInitializationEvent event) {
		OreProcessingRegistry.init();
	}

	public static final CreativeTabs FO_TAB = new CreativeTabs(MODID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModObjects.IRON_FURNACE);
		}
	};

}
