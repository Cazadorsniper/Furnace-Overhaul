package cazador.furnaceoverhaul.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import cazador.furnaceoverhaul.init.ModBlocks;
import cazador.furnaceoverhaul.tile.TileEntityEmeraldFurnace;

public class EmeraldFurnace extends IronFurnace {

	public EmeraldFurnace(String unlocalizedname, boolean isBurning) {
		super(unlocalizedname, isBurning);
	}

	public static void setState(boolean lit, World world, BlockPos pos){
        IBlockState iblockstate = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);
        keepInventory = true;
        
        if (lit) {
            world.setBlockState(pos, ModBlocks.lit_emeraldfurnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        else {
            world.setBlockState(pos, ModBlocks.emeraldfurnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        keepInventory = true;

        if (te != null){
            te.validate();
            world.setTileEntity(pos, te);
        }
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityEmeraldFurnace();
	}
	
}
