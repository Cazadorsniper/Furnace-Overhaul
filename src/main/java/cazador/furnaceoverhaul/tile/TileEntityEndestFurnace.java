package cazador.furnaceoverhaul.tile;

import javax.annotation.Nullable;

import cazador.furnaceoverhaul.handler.EnumHandler.KitTypes;
import net.minecraft.item.ItemStack;

public class TileEntityEndestFurnace extends TileEntityIronFurnace {
		
	public TileEntityEndestFurnace(){
	      super(KitTypes.ENDEST);
	    }
	
    @Override
    public int getCookTime(@Nullable ItemStack stack){
    	if(KitTypes.ENDEST != null){
    		
    	}
        return 10;
    }

    @Override
    public String getGuiID() {
    	return "furnaceoverhaul:endestfurnace";
    }
}
