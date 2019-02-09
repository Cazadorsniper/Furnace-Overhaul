package cazador.furnaceoverhaul.tile;

public class TileEntityDiamondFurnace extends TileEntityIronFurnace {

	@Override
	public int getEnergyUse() {
		return 400;
	}

	@Override
	protected int getDefaultCookTime() {
		return 100;
	}

	@Override
	protected int getSpeedyCookTime() {
		return 70;
	}

}