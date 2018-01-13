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
	
}
