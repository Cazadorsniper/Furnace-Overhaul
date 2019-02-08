package cazador.furnaceoverhaul.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import cazador.furnaceoverhaul.inventory.ContainerFO;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;

public class GuiHandler implements IGuiHandler{
	
	public static final int GUI_FURNACE = 0;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,	int x, int y, int z) {
		
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
	    switch (id)
	    {
	    case GUI_FURNACE: 
	      if ((tileEntity instanceof TileEntityIronFurnace)) {
	        return new ContainerFO(player.inventory, (TileEntityIronFurnace)tileEntity);
	      }
	      
	    }
	    return null;
	    
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,	int x, int y, int z) {
		  
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
	    switch (id)
	    {
	    case GUI_FURNACE: 
	      if ((tileEntity instanceof TileEntityIronFurnace)) {
	        return new GuiFO(player.inventory, (TileEntityIronFurnace)tileEntity);
	      }
	      
	    }
	    
	    return null;
	  }

}
