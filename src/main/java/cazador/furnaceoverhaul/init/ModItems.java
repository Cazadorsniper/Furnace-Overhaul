package cazador.furnaceoverhaul.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.items.BlankKit;
import cazador.furnaceoverhaul.items.BlankUpgrade;
import cazador.furnaceoverhaul.items.DiamondKit;
import cazador.furnaceoverhaul.items.Efficiency;
import cazador.furnaceoverhaul.items.Electricfuel;
import cazador.furnaceoverhaul.items.Electricprovider;
import cazador.furnaceoverhaul.items.EmeraldKit;
import cazador.furnaceoverhaul.items.EndestKit;
import cazador.furnaceoverhaul.items.GoldKit;
import cazador.furnaceoverhaul.items.IronKit;
import cazador.furnaceoverhaul.items.Liquidfuel;
import cazador.furnaceoverhaul.items.Oreprocessing;
import cazador.furnaceoverhaul.items.ZenithKit;

public class ModItems {
	
	public static Item blankupgrade;
	public static Item efficiency;
	public static Item oreprocessing;
	public static Item liquidfuel;
	public static Item electricfuel;
	public static Item electricprovider;
	public static Item blankkit;
	public static Item iron;
	public static Item gold;
	public static Item diamond;
	public static Item emerald;
	public static Item endest;
	public static Item zenith;
	
	public static void init() {
		blankupgrade = new BlankUpgrade("blank_upgrade");
		efficiency = new Efficiency("efficiency");
		oreprocessing = new Oreprocessing("ore_processing");
		liquidfuel = new Liquidfuel("liquid_fuel");
		electricfuel = new Electricfuel("electric_fuel");
		electricprovider = new Electricprovider("electric_provider");
		blankkit = new BlankKit("blank_kit");
		iron = new IronKit("iron_kit");
		gold = new GoldKit("gold_kit");
		diamond = new DiamondKit("diamond_kit");
		emerald = new EmeraldKit("emerald_kit");
		endest = new EndestKit("endest_kit");
		zenith = new ZenithKit("zenith_kit");
	}
	
	public static void register() {
		registerItem(blankupgrade);
		registerItem(efficiency);
		registerItem(oreprocessing);
		registerItem(liquidfuel);
		registerItem(electricfuel);
		registerItem(electricprovider);
		registerItem(blankkit);
		registerItem(iron);
		registerItem(gold);
		registerItem(diamond);
		registerItem(emerald);
		registerItem(endest);
		registerItem(zenith);
		
	}

	public static void registerItem(Item item) {
		ForgeRegistries.ITEMS.register(item);
	}
	
	public static void registerRenders() {
		registerRender(blankupgrade);
		registerRender(efficiency);
		registerRender(oreprocessing);
		registerRender(liquidfuel);
		registerRender(electricfuel);
		registerRender(electricprovider);
		registerRender(blankkit);
		registerRender(iron);
		registerRender(gold);
		registerRender(diamond);
		registerRender(emerald);
		registerRender(endest);
		registerRender(zenith);
		}
	
	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, item.getUnlocalizedName().substring(5)), "inventory"));
		//Utils.getLogger().info("Registered model for " + item.getUnlocalizedName().substring(5));
	}
	
}

