package cazador.furnaceoverhaul.proxy;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.GameData;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.tile.TileEntityDiamondFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEmeraldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEndestFurnace;
import cazador.furnaceoverhaul.tile.TileEntityGoldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import cazador.furnaceoverhaul.tile.TileEntityZenithFurnace;
import cazador.furnaceoverhaul.utils.OreDoublingRegistry;

public class CommonProxy {

	public void registerModelBakeryVariants() {
	}
	
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityIronFurnace.class, Reference.MOD_ID + ":iron_furnace");
		GameRegistry.registerTileEntity(TileEntityGoldFurnace.class, Reference.MOD_ID + ":gold_furnace");
		GameRegistry.registerTileEntity(TileEntityDiamondFurnace.class, Reference.MOD_ID + ":diamond_furnace");
		GameRegistry.registerTileEntity(TileEntityEmeraldFurnace.class, Reference.MOD_ID + ":emerald_furnace");
		GameRegistry.registerTileEntity(TileEntityEndestFurnace.class, Reference.MOD_ID + ":endest_furnace");
		GameRegistry.registerTileEntity(TileEntityZenithFurnace.class, Reference.MOD_ID + ":zenith_furnace");
		
	}

	public void init() {
		
	}
	
	public void postInit(){
		OreDoublingRegistry.init();
	}

}