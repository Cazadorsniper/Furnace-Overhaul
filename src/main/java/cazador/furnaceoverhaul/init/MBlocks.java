package cazador.furnaceoverhaul.init;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.blocks.IronFurnace;
import cazador.furnaceoverhaul.blocks.item.ItemBaseFurnace;
import cazador.furnaceoverhaul.handler.EnumHandler;

public class MBlocks {
	
	  public static Block ironfurnace;
	  public static Block lit_ironfurnace;
	    
	  public static void init(){
		  ironfurnace = new IronFurnace("furnace", false);
		  lit_ironfurnace = new IronFurnace("lit_furnace", true);
	  }

	public static void register(){
		registerBlock(ironfurnace, new ItemBaseFurnace(ironfurnace));
		registerBlock(lit_ironfurnace, new ItemBaseFurnace(lit_ironfurnace));
	  }
	
	  public static void registerRenders(){    
		  for(int c = 0; c < EnumHandler.KitTypes.values().length; c++){
		    	registerRender(ironfurnace, c, "furnace_" + EnumHandler.KitTypes.values()[c].getName());
		    	registerRender(lit_ironfurnace, c, "lit_furnace_" + EnumHandler.KitTypes.values()[c].getName());
		    }
		   
		  }
	
	public static void registerBlock(Block block, ItemBlock itemBlock) {
		ForgeRegistries.BLOCKS.registerAll(block);
		ForgeRegistries.ITEMS.registerAll(itemBlock.setRegistryName(block.getRegistryName()));
	}
	
	public static void registerBlock(Block block) {
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
	//standard  
	public static void registerRender(Block block){
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, block.getUnlocalizedName().substring(5)), "inventory"));
	}
	
	//meta  
	public static void registerRender(Block block, int meta, String fileName) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, fileName), "inventory"));
	}
	
}
