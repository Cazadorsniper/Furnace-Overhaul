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
	  public static Block goldfurnace;
	  public static Block diamondfurnace;
	  public static Block emeraldfurnace;
	  public static Block endfurnace;
	  public static Block zenithfurnace;
	    
	  public static void init(){
		  ironfurnace = new IronFurnace("iron_furnace");
		  goldfurnace = new GoldFurnace("gold_furnace");
		  diamondfurnace = new DiamondFurnace("diamond_furnace");
		  emeraldfurnace = new EmeraldFurnace("emerald_furnace");
		  endfurnace = new EndFurnace("end_furnace");
		  zenithfurnace = new ZenithFurnace("zenith_furnace");
	  }

	  public static void register(){
		registerBlock(ironfurnace);
		registerBlock(goldfurnace);
		registerBlock(diamondfurnace);
		registerBlock(emeraldfurnace);
		registerBlock(endfurnace);
		registerBlock(zenithfurnace);
	  }
	
	  public static void registerRenders(){    
		registerRender(ironfurnace);
		registerRender(goldfurnace);
		registerRender(diamondfurnace);
		registerRender(emeraldfurnace);
		registerRender(endfurnace);
		registerRender(zenithfurnace);
		   
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
