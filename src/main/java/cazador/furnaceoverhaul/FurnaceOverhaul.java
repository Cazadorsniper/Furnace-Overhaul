package cazador.furnaceoverhaul;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import cazador.furnaceoverhaul.handler.GuiHandler;
import cazador.furnaceoverhaul.init.ModBlocks;
import cazador.furnaceoverhaul.init.ModItems;
import cazador.furnaceoverhaul.proxy.CommonProxy;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS)
public class FurnaceOverhaul {
	
	@Mod.Instance
	public static FurnaceOverhaul instance;
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
	ModBlocks.init();
	ModItems.init();
	ModBlocks.register();
	ModItems.register();
	
	proxy.init();
	
	NetworkRegistry.INSTANCE.registerGuiHandler(FurnaceOverhaul.instance, new GuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event){
	proxy.registerModelBakeryVariants();
	proxy.registerTileEntities();
	
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
	
	}
	
	public static CreativeTabs FurnaceOverhaulTab = new CreativeTabs("FurnaceOverhaul"){
		
		@Override
		public ItemStack getTabIconItem(){
			return new ItemStack(ModBlocks.ironfurnace);
		}
	};

}

