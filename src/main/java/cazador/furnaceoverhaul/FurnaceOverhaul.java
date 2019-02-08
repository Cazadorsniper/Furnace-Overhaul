package cazador.furnaceoverhaul;

import cazador.furnaceoverhaul.handler.GuiHandler;
import cazador.furnaceoverhaul.init.ModBlocks;
import cazador.furnaceoverhaul.init.ModItems;
import cazador.furnaceoverhaul.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = FurnaceOverhaul.MODID, name = FurnaceOverhaul.MODNAME, version = FurnaceOverhaul.VERSION)
public class FurnaceOverhaul {

	public static final String MODID = "furnaceoverhaul";
	public static final String MODNAME = "Furnace Overhaul";
	public static final String VERSION = "2.0.0";

	@Instance
	public static FurnaceOverhaul INSTANCE;

	@SidedProxy(clientSide = "cazador.furnaceoverhaul.proxy.ClientProxy", serverSide = "cazador.furnaceoverhaul.proxy.ServerProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
		ModItems.init();
		ModBlocks.register();
		ModItems.register();
		NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}

	public static final CreativeTabs FO_TAB = new CreativeTabs(MODID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.ironfurnace);
		}
	};

}
