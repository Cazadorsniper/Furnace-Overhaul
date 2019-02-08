package cazador.furnaceoverhaul.tile;

public class TileEntityEndFurnace extends TileEntityIronFurnace {

	@Override
	protected int getEnergyUse() {
		return 200;
	}

	@Override
	protected int getDefaultCookTime() {
		return 30;
	}

	@Override
	protected int getEfficientCookTime() {
		return 10;
	}

}