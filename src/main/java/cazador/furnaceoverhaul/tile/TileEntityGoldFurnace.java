package cazador.furnaceoverhaul.tile;

import javax.annotation.Nullable;

import cazador.furnaceoverhaul.handler.EnumHandler.KitTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class TileEntityGoldFurnace extends TileEntityIronFurnace {
	
	public TileEntityGoldFurnace(){
		super(KitTypes.GOLD);
    }

	@Override
    public int getCookTime(@Nullable ItemStack stack){
		if(KitTypes.GOLD != null){
    		
    	}
        return 120;
    }
    
    @Override
    public String getGuiID() {
    	return "furnaceoverhaul:goldfurnace";
    }

}
