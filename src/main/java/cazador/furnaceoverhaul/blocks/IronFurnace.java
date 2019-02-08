package cazador.furnaceoverhaul.blocks;

import java.util.List;
import java.util.Random;

import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.handler.GuiHandler;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IronFurnace extends Block {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool BURNING = PropertyBool.create("burning");

	protected final TextFormatting infoColor;
	protected final int cookTime;

	public IronFurnace(String name, TextFormatting infoColor, int cookTime) {
		super(Material.IRON);
		setTranslationKey(FurnaceOverhaul.MODID + "." + name);
		this.setRegistryName(FurnaceOverhaul.MODID, name);
		setCreativeTab(FurnaceOverhaul.FO_TAB);
		this.setHardness(2.0F);
		this.setResistance(9.0F);
		this.setHarvestLevel("pickaxe", 1);
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURNING, false));
		this.infoColor = infoColor;
		this.cookTime = cookTime;

	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state.getValue(BURNING) == true) {
			return 8;
		} else if (state.getValue(BURNING) == false) ;
		return 0;

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(infoColor + I18n.format("info.furnaceoverhaul.cooktime", cookTime));
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityIronFurnace();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, BURNING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.HORIZONTALS[meta & 0b0011]).withProperty(BURNING, (meta & 0b0100) == 0b0100);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BURNING) ? 1 : 0 << 2 + state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return 0; //TODO: FIXME
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking() && !world.isRemote) {
			player.openGui(FurnaceOverhaul.INSTANCE, GuiHandler.GUI_FURNACE, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);

		if (te instanceof TileEntityIronFurnace && world.getBlockState(pos).getBlock() != state.getBlock()) {
			//TODO: Update for IItemHandler
			world.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(world, pos, state);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (state.getValue(BURNING)) {
			EnumFacing facing = state.getValue(FACING);
			double d0 = pos.getX() + 0.5D;
			double d1 = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
			double d2 = pos.getZ() + 0.5D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			if (rand.nextDouble() < 0.1D) world.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			Vec3d offset = new Vec3d(facing.getDirectionVec()).scale(0.52D);
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + offset.x, d1, d2 + d4 + offset.z, 0.0D, 0.0D, 0.0D);
			world.spawnParticle(EnumParticleTypes.FLAME, d0 + offset.x, d1, d2 + d4 + offset.z, 0.0D, 0.0D, 0.0D);
		}
	}

}
