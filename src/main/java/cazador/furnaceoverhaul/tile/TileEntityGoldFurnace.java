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
	public int getEnergyUse() {
		return 500;
	}
}
