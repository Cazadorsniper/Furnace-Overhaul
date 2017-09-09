package cazador.furnaceoverhaul.tile;

import javax.annotation.Nullable;

import cazador.furnaceoverhaul.handler.EnumHandler.KitTypes;
import net.minecraft.item.ItemStack;

public class TileEntityEmeraldFurnace extends TileEntityIronFurnace {
   
	public TileEntityEmeraldFurnace(){
	      super(KitTypes.EMERALD);
	}
	
	@Override
    public int getCookTime(ItemStack stack){
    	if(types == KitTypes.EMERALD){
    		return this.types.getMeta() + 47;
    	}
        return 50;
    }

    @Override
    public String getGuiID() {
    	return "furnaceoverhaul:emeraldfurnace";
    }
}
