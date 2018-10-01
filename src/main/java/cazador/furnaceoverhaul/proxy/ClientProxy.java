package cazador.furnaceoverhaul.proxy;

import cazador.furnaceoverhaul.init.ModBlocks;
import cazador.furnaceoverhaul.init.ModItems;

public class ClientProxy extends CommonProxy{

	@Override
	public void init() {
		ModItems.registerRenders();
		ModBlocks.registerRenders();
		
	}
	
}
