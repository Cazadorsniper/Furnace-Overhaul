package cazador.furnaceoverhaul.blocks;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cazador.furnaceoverhaul.init.ModBlocks;
import cazador.furnaceoverhaul.tile.TileEntityEmeraldFurnace;

public class EmeraldFurnace extends IronFurnace {

	public EmeraldFurnace(String unlocalizedname) {
		super(unlocalizedname);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.GREEN + "Cook time 50 ticks");
	}

	public static void setState(boolean active, World world, BlockPos pos){
        IBlockState iblockstate = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);
        keepInventory = true;
        
        if (active) {
            world.setBlockState(pos, ModBlocks.emeraldfurnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(ACTIVE, true), 3);
        }
        else {
            world.setBlockState(pos, ModBlocks.emeraldfurnace.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)).withProperty(ACTIVE, false), 3);
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
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer(){
        return BlockRenderLayer.TRANSLUCENT;
    }
	
}
