package cazador.furnaceoverhaul.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.handler.EnumHandler;
import cazador.furnaceoverhaul.items.ItemKit;
import cazador.furnaceoverhaul.items.ItemUpgrade;
import cazador.furnaceoverhaul.utils.Utils;

public class MItems {
	
	public static Item upgrade;
	public static Item kit;
	
	public static void init() {
		upgrade = new ItemUpgrade();
		kit = new ItemKit();
	}
	
	public static void register() {
		registerItem(upgrade);
		registerItem(kit);
	}

	public static void registerItem(Item item) {
		ForgeRegistries.ITEMS.register(item);
	}
	
	public static void registerRenders() {
		for(int c = 0; c < EnumHandler.UpgradeTypes.values().length; c++) {
			registerRender(upgrade, c, "upgrade_" + EnumHandler.UpgradeTypes.values()[c].getName());
			}
		for(int c = 0; c < EnumHandler.KitTypes.values().length; c++) {
			registerRender(kit, c, "kit_" + EnumHandler.KitTypes.values()[c].getName());
			}
		}
	
	//meta
	public static void registerRender(Item item, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, fileName), "inventory"));
		//Utils.getLogger().info("Registered model for " + item.getUnlocalizedName().substring(5));
	}
	//normal
	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, item.getUnlocalizedName().substring(5)), "inventory"));
	}
	
	
}

