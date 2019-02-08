package cazador.furnaceoverhaul.tile;

public class TileEntityGoldFurnace extends TileEntityIronFurnace {

	@Override
	protected int getDefaultCookTime() {
		return 130;
	}

	@Override
	protected int getSpeedyCookTime() {
		return 110;
	}

	@Override
	protected int getEnergyUse() {
		return 500;
	}
}
