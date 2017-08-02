package cazador.furnaceoverhaul.proxy;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.init.MBlocks;
import cazador.furnaceoverhaul.init.MItems;

public class ClientProxy extends CommonProxy{

	@Override
	public void init() {
		MItems.registerRenders();
		MBlocks.registerRenders();
		
	}
	
	@Override
	public void registerModelBakeryVariants() {
		ModelBakery.registerItemVariants(MItems.kit, 
				new ResourceLocation(Reference.MOD_ID, "kit_iron"), 
				new ResourceLocation(Reference.MOD_ID, "kit_gold"), 
				new ResourceLocation(Reference.MOD_ID, "kit_diamond"), 
				new ResourceLocation(Reference.MOD_ID, "kit_emerald"), 
				new ResourceLocation(Reference.MOD_ID, "kit_endest"), 
				new ResourceLocation(Reference.MOD_ID, "kit_zenith"));
		ModelBakery.registerItemVariants(MItems.upgrade, 
				new ResourceLocation(Reference.MOD_ID, "upgrade_upgrade"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_efficiency"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_oreprocessing"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_liquidfuel"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_electricfuel"), 
				new ResourceLocation(Reference.MOD_ID, "upgrade_electricprovider"));
		ModelBakery.registerItemVariants(Item.getItemFromBlock(MBlocks.ironfurnace), 
				new ResourceLocation(Reference.MOD_ID, "ironfurnace"), 
				new ResourceLocation(Reference.MOD_ID, "goldfurnace"),
				new ResourceLocation(Reference.MOD_ID, "diamondfurnace"),
				new ResourceLocation(Reference.MOD_ID, "emeraldfurnace"),
				new ResourceLocation(Reference.MOD_ID, "endestfurnace"),
				new ResourceLocation(Reference.MOD_ID, "zenithfurnace"));
	}
	
}
