package cazador.furnaceoverhaul.blocks;

import java.util.List;

import cazador.furnaceoverhaul.init.ModBlocks;
import cazador.furnaceoverhaul.tile.TileEntityDiamondFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DiamondFurnace extends IronFurnace {

	public DiamondFurnace(String unlocalizedname) {
		super(unlocalizedname);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.AQUA + "Cook time 90 ticks");
	}
	
	public static void setState(boolean active, World world, BlockPos pos){
        IBlockState iblockstate = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);
        keepInventory = true;
        
        if (active) {
            world.setBlockState(pos, ModBlocks.diamondfurnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(ACTIVE, true), 3);
        }
        else {
            world.setBlockState(pos, ModBlocks.diamondfurnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(ACTIVE, false), 3);
        }

        keepInventory = true;

        if (te != null){
            te.validate();
            world.setTileEntity(pos, te);
        }
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDiamondFurnace();
	}

	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.TRANSLUCENT;
    }
	
}
