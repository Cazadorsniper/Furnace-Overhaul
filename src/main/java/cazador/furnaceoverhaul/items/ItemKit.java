package cazador.furnaceoverhaul.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.blocks.BaseFurnace;
import cazador.furnaceoverhaul.blocks.IronFurnace;
import cazador.furnaceoverhaul.handler.EnumHandler;
import cazador.furnaceoverhaul.handler.EnumHandler.KitTypes;

public class ItemKit extends Item {
	
	public ItemKit() {
		this.setUnlocalizedName("kit" + EnumHandler.KitTypes.values().length);
        this.setRegistryName("kit");
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(FurnaceOverhaul.FurnaceOverhaulTab);
	}
	
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)){
			for(int c = 0; c < KitTypes.values().length; c++) {
				items.add(new ItemStack(this, 1, c));
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		for(int c = 0; c < KitTypes.values().length; c++) {
			if(stack.getItemDamage() == c) {
				return this.getUnlocalizedName() + "." + KitTypes.values()[c].getName();
			}
			else {
				continue;
			}
		}
		return this.getUnlocalizedName() + "." + KitTypes.IRON.getName();
	}
	
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player){
		return false;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		if(state != null && player.isSneaking()) {
			if(state.getBlock() instanceof BaseFurnace) {
				BaseFurnace furnace = (BaseFurnace) state.getBlock();
				ItemStack heldStack = player.getHeldItem(hand);
				furnace.updateFurnaceTier(world, player, hand, pos, heldStack);
			}
		}
		return EnumActionResult.PASS;
	}
	
}