package cazador.furnaceoverhaul.proxy;

import cazador.furnaceoverhaul.utils.OreDoublingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.tile.TileEntityDiamondFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEmeraldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEndestFurnace;
import cazador.furnaceoverhaul.tile.TileEntityGoldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import cazador.furnaceoverhaul.tile.TileEntityZenithFurnace;

public class CommonProxy {

	public void registerModelBakeryVariants() {
	}
	
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityIronFurnace.class, Reference.MOD_ID + "ironfurnace");
		GameRegistry.registerTileEntity(TileEntityGoldFurnace.class, Reference.MOD_ID + "goldfurnace");
		GameRegistry.registerTileEntity(TileEntityDiamondFurnace.class, Reference.MOD_ID + "diamondfurnace");
		GameRegistry.registerTileEntity(TileEntityEmeraldFurnace.class, Reference.MOD_ID + "emeraldfurnace");
		GameRegistry.registerTileEntity(TileEntityEndestFurnace.class, Reference.MOD_ID + "endestfurnace");
		GameRegistry.registerTileEntity(TileEntityZenithFurnace.class, Reference.MOD_ID + "zenithfurnace");
	}

	public void init() {
		
	}

	public void postInit(){
		OreDoublingRegistry.init();
	}

}
