package cazador.furnaceoverhaul.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.blocks.DiamondFurnace;
import cazador.furnaceoverhaul.blocks.EmeraldFurnace;
import cazador.furnaceoverhaul.blocks.EndFurnace;
import cazador.furnaceoverhaul.blocks.GoldFurnace;
import cazador.furnaceoverhaul.blocks.IronFurnace;
import cazador.furnaceoverhaul.blocks.ZenithFurnace;

public class ModBlocks {
	
	  public static Block ironfurnace;
	  public static Block lit_ironfurnace;
	  public static Block goldfurnace;
	  public static Block lit_goldfurnace;
	  public static Block diamondfurnace;
	  public static Block lit_diamondfurnace;
	  public static Block emeraldfurnace;
	  public static Block lit_emeraldfurnace;
	  public static Block endfurnace;
	  public static Block lit_endfurnace;
	  public static Block zenithfurnace;
	  public static Block lit_zenithfurnace;
	    
	  public static void init(){
		  ironfurnace = new IronFurnace("iron_furnace", false);
		  lit_ironfurnace = new IronFurnace("lit_iron_furnace", true);
		  goldfurnace = new GoldFurnace("gold_furnace", false);
		  lit_goldfurnace = new GoldFurnace("lit_gold_furnace", true);
		  diamondfurnace = new DiamondFurnace("diamond_furnace", false);
		  lit_diamondfurnace = new DiamondFurnace("lit_diamond_furnace", true);
		  emeraldfurnace = new EmeraldFurnace("emerald_furnace", false);
		  lit_emeraldfurnace = new EmeraldFurnace("lit_emerald_furnace", true);
		  endfurnace = new EndFurnace("end_furnace", false);
		  lit_endfurnace = new EndFurnace("lit_end_furnace", true);
		  zenithfurnace = new ZenithFurnace("zenith_furnace", false);
		  lit_zenithfurnace = new ZenithFurnace("lit_zenith_furnace", true);
	  }

	  public static void register(){
		registerBlock(ironfurnace);
		registerBlock(lit_ironfurnace);
		registerBlock(goldfurnace);
		registerBlock(lit_goldfurnace);
		registerBlock(diamondfurnace);
		registerBlock(lit_diamondfurnace);
		registerBlock(emeraldfurnace);
		registerBlock(lit_emeraldfurnace);
		registerBlock(endfurnace);
		registerBlock(lit_endfurnace);
		registerBlock(zenithfurnace);
		registerBlock(lit_zenithfurnace);
	  }
	
	  public static void registerRenders(){    
		registerRender(ironfurnace);
		registerRender(lit_ironfurnace);
		registerRender(goldfurnace);
		registerRender(lit_goldfurnace);
		registerRender(diamondfurnace);
		registerRender(lit_diamondfurnace);
		registerRender(emeraldfurnace);
		registerRender(lit_emeraldfurnace);
		registerRender(endfurnace);
		registerRender(lit_endfurnace);
		registerRender(zenithfurnace);
		registerRender(lit_zenithfurnace);
		   
		  }
	
	public static void registerBlock(Block block) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
	//standard
	public static void registerRender(Block block){
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, block.getUnlocalizedName().substring(5)), "inventory"));
	}
	
}
