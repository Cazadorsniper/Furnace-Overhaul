package cazador.furnaceoverhaul.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.blocks.item.IMetaBlockName;
import cazador.furnaceoverhaul.handler.EnumHandler;
import cazador.furnaceoverhaul.handler.EnumHandler.KitTypes;
import cazador.furnaceoverhaul.handler.GuiHandler;
import cazador.furnaceoverhaul.init.MBlocks;
import cazador.furnaceoverhaul.init.MItems;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;

public abstract class BaseFurnace extends BlockContainer implements IMetaBlockName{
    
	public static final PropertyEnum TYPE = PropertyEnum.create("type", KitTypes.class);
	public static final PropertyEnum FACING = BlockHorizontal.FACING;
	private final boolean isBurning;
	public static boolean keepInventory;
	
	public BaseFurnace(String unlocalizedname, boolean isBurning) {
		super(Material.IRON);
		this.setUnlocalizedName(unlocalizedname + EnumHandler.KitTypes.values().length);
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, unlocalizedname));
		setCreativeTab(FurnaceOverhaul.FurnaceOverhaulTab);
		this.setHardness(2.0F);
		this.setResistance(9.0F);
		setDefaultState(this.blockState.getBaseState().withProperty(TYPE, KitTypes.IRON));
		this.isBlockContainer = true;
		this.isBurning = isBurning;
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return KitTypes.makeEntity(meta);
	}
    
    public boolean hasTileEntity() {
        return true;
    }

    @Nullable
    public TileEntity createTileEntity(World world, IBlockState state) {
        return super.createTileEntity(world, state);
    }
    
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos){
	    TileEntity te = world.getTileEntity(pos);
	    if ((te != null) && ((te instanceof TileEntityIronFurnace))) {
	      return ((TileEntityIronFurnace)te).isBurning() ? 12 : 0;
	    }
	    return 0;
	  }
	
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	public BlockRenderLayer getBlockRenderLayer() {
		return BlockRenderLayer.SOLID;
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING, TYPE});
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, KitTypes.values()[meta]);
	}
	
	public int getMetaFromState(IBlockState state) {
		return ((KitTypes)state.getValue(TYPE)).getMeta();
	}
	
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(TYPE, getStateFromMeta(meta).getValue(TYPE));
	}
	
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	  {
	    EnumFacing facing = EnumFacing.NORTH;
	    boolean active = false;
	    
	    TileEntity te = world.getTileEntity(pos);
	    if ((te != null) && ((te instanceof TileEntityIronFurnace)))
	    {
	      TileEntityIronFurnace furnace = (TileEntityIronFurnace)te;
	      facing = EnumFacing.getFront(furnace.getFacing().getIndex());
	      if ((facing == EnumFacing.DOWN) || (facing == EnumFacing.UP)) {
	        facing = EnumFacing.NORTH;
	      }
	    }
	    return state.withProperty(FACING, facing);
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
    
    public void onBlockAdded(World world, BlockPos pos, IBlockState state){
	    this.setDefaultFacing(world, pos, state);
	    }	
	
	public boolean isOpaqueCube(IBlockState state) {
		return true;
	}

    public boolean hasComparatorInputOverride(IBlockState state){
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos){
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }
    
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        if(!player.isSneaking() && !world.isRemote) {
        	player.openGui(FurnaceOverhaul.instance, GuiHandler.GUI_FURNACE, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
       }

	@Override
	public String getSpecialName(ItemStack stack) {
		return KitTypes.values()[stack.getItemDamage() % KitTypes.values().length].getName();
	}
    
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
			for(int c = 0; c < KitTypes.values().length; c++) {
			list.add(new ItemStack(this, 1, c));
		}
	}
	
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, (int) (getMetaFromState(world.getBlockState(pos)) / EnumFacing.values().length));
	}
	
	/*
 	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}
	*/
	
	public int damageDropped(IBlockState state) {
		return (int) (getMetaFromState(state) / EnumFacing.values().length);
	}
	
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
	
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
        if (stack.hasDisplayName()){
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof TileEntityIronFurnace){
                ((TileEntityIronFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }
	
	public static void setState(boolean lit, World world, BlockPos pos, int meta){
        IBlockState iblockstate = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);
        keepInventory = true;
        
        if (lit) {
            world.setBlockState(pos, MBlocks.lit_ironfurnace.getDefaultState().withProperty(TYPE, KitTypes.byMetadata(meta)).withProperty(FACING, iblockstate.getValue(FACING)));
            world.setBlockState(pos, MBlocks.lit_ironfurnace.getDefaultState().withProperty(TYPE, KitTypes.byMetadata(meta)).withProperty(FACING, iblockstate.getValue(FACING)));
        }
        else {
            world.setBlockState(pos, MBlocks.ironfurnace.getDefaultState().withProperty(TYPE, KitTypes.byMetadata(meta)).withProperty(FACING, iblockstate.getValue(FACING)));
            world.setBlockState(pos, MBlocks.ironfurnace.getDefaultState().withProperty(TYPE, KitTypes.byMetadata(meta)).withProperty(FACING, iblockstate.getValue(FACING)));
        }

        keepInventory = true;

        if (te != null){
            te.validate();
            world.setTileEntity(pos, te);
        }
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
	
	public void updateFurnaceTier(World world, EntityPlayer player, EnumHand hand, BlockPos pos, ItemStack stack) {
		if(stack.getItem() == MItems.kit) {
			KitTypes newType = KitTypes.values()[stack.getItemDamage() % KitTypes.values().length];
			KitTypes currentType = (KitTypes) world.getBlockState(pos).getValue(TYPE);
			IBlockState newState = world.getBlockState(pos).withProperty(TYPE, newType);
			if(newType.getMeta() > currentType.getMeta()) {
				world.setBlockState(pos, newState, 2);
			}
			ItemStack newStack = stack.copy();
			newStack.shrink(1);
			player.setHeldItem(hand, newStack);
			if(player.getHeldItem(hand).getCount() <= 0)
				player.setHeldItem(hand, ItemStack.EMPTY);
			}
		}
}