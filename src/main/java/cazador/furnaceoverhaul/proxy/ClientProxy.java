package cazador.furnaceoverhaul.proxy;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.init.ModBlocks;
import cazador.furnaceoverhaul.init.ModItems;

public class ClientProxy extends CommonProxy{

	@Override
	public void init() {
		ModItems.registerRenders();
		ModBlocks.registerRenders();
		
	}
	
	@Override
	public void registerModelBakeryVariants() {
		ModelBakery.registerItemVariants(ModItems.kit, 
				new ResourceLocation(Reference.MOD_ID, "kit_iron"), 
				new ResourceLocation(Reference.MOD_ID, "kit_gold"), 
				new ResourceLocation(Reference.MOD_ID, "kit_diamond"), 
				new ResourceLocation(Reference.MOD_ID, "kit_emerald"), 
				new ResourceLocation(Reference.MOD_ID, "kit_endest"), 
				new ResourceLocation(Reference.MOD_ID, "kit_zenith"));
		ModelBakery.registerItemVariants(ModItems.upgrade, 
				new ResourceLocation(Reference.MOD_ID, "upgrade_upgrade"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_efficiency"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_oreprocessing"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_liquidfuel"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_electricfuel"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_electricprovider"));
	}
	
}
