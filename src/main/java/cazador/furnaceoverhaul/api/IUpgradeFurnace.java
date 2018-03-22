package cazador.furnaceoverhaul.api;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IUpgradeFurnace {

	public abstract void upgradeFurnaceTier(World world, EntityPlayer player, EnumHand hand, BlockPos pos, ItemStack stack);
}
