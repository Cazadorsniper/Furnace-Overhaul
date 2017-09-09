package cazador.furnaceoverhaul.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.Reference;
import cazador.furnaceoverhaul.blocks.item.IMetaBlockName;
import cazador.furnaceoverhaul.handler.EnumHandler;
import cazador.furnaceoverhaul.handler.EnumHandler.KitTypes;
import cazador.furnaceoverhaul.handler.GuiHandler;
import cazador.furnaceoverhaul.init.MItems;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;

public abstract class BaseFurnace extends BlockContainer implements IMetaBlockName{
    
	public static final PropertyEnum TYPE = PropertyEnum.create("type", KitTypes.class);
    
	public BaseFurnace(String unlocalizedname, boolean isBurning) {
		super(Material.IRON);
		this.setUnlocalizedName(unlocalizedname + EnumHandler.KitTypes.values().length);
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, unlocalizedname));
		setCreativeTab(FurnaceOverhaul.FurnaceOverhaulTab);
		this.setHardness(2.0F);
		this.setResistance(9.0F);
		setDefaultState(this.blockState.getBaseState().withProperty(TYPE, KitTypes.IRON));
		this.isBlockContainer = true;
	}
	
	public TileEntity createNewTileEntity(World world, int meta) {
		return null;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	public BlockRenderLayer getBlockRenderLayer() {
		return BlockRenderLayer.SOLID;
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, KitTypes.values()[meta]);
	}
	
	public int getMetaFromState(IBlockState state) {
		return ((KitTypes)state.getValue(TYPE)).getMeta();
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
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, (int) (getMetaFromState(world.getBlockState(pos))));
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
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