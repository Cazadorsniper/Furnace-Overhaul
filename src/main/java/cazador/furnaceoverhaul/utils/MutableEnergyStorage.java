package cazador.furnaceoverhaul.utils;

import net.minecraftforge.energy.EnergyStorage;

public class MutableEnergyStorage extends EnergyStorage {

	public MutableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

}
