package cazador.furnaceoverhaul.items;

import java.util.List;

import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.blocks.BlockIronFurnace;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKit extends Item {

	final BlockIronFurnace prev;
	final BlockIronFurnace next;

	public ItemKit(String name, BlockIronFurnace prev, BlockIronFurnace next) {
		this.setTranslationKey(FurnaceOverhaul.MODID + "." + name);
		this.setRegistryName(new ResourceLocation(FurnaceOverhaul.MODID, name));
		this.setMaxStackSize(1);
		this.setCreativeTab(FurnaceOverhaul.FO_TAB);
		this.prev = prev;
		this.next = next;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		if (prev != null) tooltip.add(I18n.format("info.furnaceoverhaul.kit", prev.getLocalizedName(), next.getLocalizedName()));
	}

	/**
	 * Handles conversion of the previous furnace into the next furnace.  Preserves tile data.
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (prev == null) return EnumActionResult.FAIL;
		if (world.isRemote) return world.getBlockState(pos).getBlock() == prev ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		else {
			IBlockState state = world.getBlockState(pos);
			if (state.getBlock() != prev) return EnumActionResult.FAIL;
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntityIronFurnace) {
				NBTTagCompound tag = new NBTTagCompound();
				te.writeToNBT(tag);
				tag.removeTag("id");
				((TileEntityIronFurnace) te).clear();
				boolean burning = state == ((TileEntityIronFurnace) te).getLitState();
				EnumFacing face = state.getValue(BlockIronFurnace.FACING);
				world.setBlockState(pos, next.getDefaultState().withProperty(BlockIronFurnace.FACING, face).withProperty(BlockIronFurnace.BURNING, burning));
				world.getTileEntity(pos).readFromNBT(tag);
				player.getHeldItem(hand).shrink(1);
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
	}

}
