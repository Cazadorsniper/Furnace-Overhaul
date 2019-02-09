package cazador.furnaceoverhaul.tile;

public class TileEntityZenithFurnace extends TileEntityIronFurnace {

	@Override
	public int getEnergyUse() {
		return 100;
	}

	@Override
	protected int getDefaultCookTime() {
		return 1;
	}

	@Override
	protected int getSpeedyCookTime() {
		return 1;
	}

}