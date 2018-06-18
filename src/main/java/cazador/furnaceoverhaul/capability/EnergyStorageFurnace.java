package cazador.furnaceoverhaul.capability;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageFurnace extends EnergyStorage {

		public EnergyStorageFurnace(int capacity) {
			super(capacity);
		}

		public EnergyStorageFurnace(int capacity, int maxReceive)
		{
			
			super(capacity, maxReceive, 0);
		}

		//Use this to use energy by the machine.
		public int extractEnergyByFurnace(int maxExtract, boolean simulate)
		{
			int energyExtracted = Math.min(energy, maxExtract);
			if (!simulate)
				energy -= energyExtracted;
			return energyExtracted;
		}

}
