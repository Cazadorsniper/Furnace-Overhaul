package cazador.furnaceoverhaul.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageFurnace extends EnergyStorage {

	    public EnergyStorageFurnace(int capacity)
	    {
	        super(capacity, capacity, capacity, 0);
	    }

	    public EnergyStorageFurnace(int capacity, int maxTransfer)
	    {
	        super(capacity, maxTransfer, maxTransfer, 0);
	    }

	    public EnergyStorageFurnace(int capacity, int maxReceive, int maxExtract)
	    {
	        super(capacity, maxReceive, maxExtract, 0);
	    }

	    public EnergyStorageFurnace(int capacity, int maxReceive, int maxExtract, int energy)
	    {
	    	super(capacity, maxReceive, maxExtract, energy);
	    }
	    
	    public void setEnergy(int energy) {
	        this.energy = energy;
	    }

	    public void consumePower(int energy) {
	        this.energy -= energy;
	        if (this.energy < 0) {
	            this.energy = 0;
	        }
	    }
}

