package cazador.furnaceoverhaul.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import cazador.furnaceoverhaul.handler.EnumHandler.KitTypes;
import cazador.furnaceoverhaul.init.MBlocks;
import cazador.furnaceoverhaul.tile.TileEntityDiamondFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEmeraldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEndestFurnace;
import cazador.furnaceoverhaul.tile.TileEntityGoldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import cazador.furnaceoverhaul.tile.TileEntityZenithFurnace;

public class IronFurnace extends BaseFurnace {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	private final boolean isBurning;
	public static boolean keepInventory;
	
	public IronFurnace(String unlocalizedname, boolean isBurning) {
		super(unlocalizedname, isBurning);
		this.isBurning = isBurning;
		if(isBurning){
		this.lightValue = 12;
		}
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, KitTypes.IRON));
	}
	
	/*public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	  {
	    TileEntity te = world.getTileEntity(pos);
	    if ((te != null) && ((te instanceof TileEntityIronFurnace))) {
	      return ((TileEntityIronFurnace)te).isBurning() ? 15 : 0;
	    }
	    return 0;
	  }*/
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING, TYPE});
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
        if (stack.hasDisplayName()){
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileEntityIronFurnace){
                ((TileEntityIronFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, (int) (getMetaFromState(world.getBlockState(pos)) / EnumFacing.values().length));
	
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return (int) (getMetaFromState(state) / EnumFacing.values().length);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
    	if (!keepInventory){
    	TileEntity te = (TileEntityIronFurnace) world.getTileEntity(pos);
		IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		for(int slot = 0; slot < handler.getSlots(); slot++) {
			ItemStack stack = handler.getStackInSlot(slot);
			InventoryHelper.dropInventoryItems(world, pos, (TileEntityIronFurnace)te);
            world.updateComparatorOutputLevel(pos, this);
			}
        }
        super.breakBlock(world, pos, state);
    }
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(TYPE, getStateFromMeta(meta * EnumFacing.HORIZONTALS.length).getValue(TYPE));
	}
	
	@Override
    public int getMetaFromState(IBlockState state){
    	KitTypes type = (KitTypes) state.getValue(TYPE);
    	EnumFacing facing = (EnumFacing) state.getValue(FACING);
    	int id = type.getID() + EnumFacing.HORIZONTALS.length + facing.getHorizontalIndex();
		return id;
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta){
		KitTypes type = KitTypes.values()[(int)(meta / EnumFacing.HORIZONTALS.length) % KitTypes.values().length];
		EnumFacing facing = EnumFacing.HORIZONTALS[meta % EnumFacing.HORIZONTALS.length];
		if (facing.getAxis() == EnumFacing.Axis.Y){
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(TYPE, type).withProperty(FACING, facing); 
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess blockAccessor, BlockPos pos) {
		TileEntity te;
		if (blockAccessor instanceof ChunkCache) {
			te = ((ChunkCache) blockAccessor).getTileEntity(pos, EnumCreateEntityType.CHECK);
		}
			IBlockState iblockstate = blockAccessor.getBlockState(pos);
		
			if (iblockstate.getBlock() == this){
            state = state.withProperty(TYPE, iblockstate.getValue(TYPE));
			}
		return state;
	}

	
    public static void setState(boolean lit, World world, BlockPos pos){
        IBlockState iblockstate = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);
        keepInventory = true;
        
        if (lit)
        {
            world.setBlockState(pos, MBlocks.lit_ironfurnace.getDefaultState().withProperty(TYPE, iblockstate.getValue(TYPE)).withProperty(FACING, iblockstate.getValue(FACING)), 3);
            world.setBlockState(pos, MBlocks.lit_ironfurnace.getDefaultState().withProperty(TYPE, iblockstate.getValue(TYPE)).withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        else
        {
            world.setBlockState(pos, MBlocks.ironfurnace.getDefaultState().withProperty(TYPE, iblockstate.getValue(TYPE)).withProperty(FACING, iblockstate.getValue(FACING)), 3);
            world.setBlockState(pos, MBlocks.ironfurnace.getDefaultState().withProperty(TYPE, iblockstate.getValue(TYPE)).withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        keepInventory = true;

        if (te != null){
            te.validate();
            world.setTileEntity(pos, te);
        }
    }

    @Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return KitTypes.makeEntity(meta);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
	    this.setDefaultFacing(world, pos, state);
	    }
	
	private void setDefaultFacing(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote){
            IBlockState iblockstate = world.getBlockState(pos.north());
            IBlockState iblockstate1 = world.getBlockState(pos.south());
            IBlockState iblockstate2 = world.getBlockState(pos.west());
            IBlockState iblockstate3 = world.getBlockState(pos.east());
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock()){
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock()){
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock()){
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock()){
                enumfacing = EnumFacing.WEST;
            }

            world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
		
	}
	
	public IBlockState withRotation(IBlockState state, Rotation rot){
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirror){
        return state.withRotation(mirror.toRotation((EnumFacing)state.getValue(FACING)));
    }
    
	@SideOnly(Side.CLIENT)
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand){
        if (this.isBurning){
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D) {
                world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (enumfacing){
                case WEST:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case EAST:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case NORTH:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case SOUTH:
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
                    world.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    } 

}
