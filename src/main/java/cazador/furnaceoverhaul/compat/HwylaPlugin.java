package cazador.furnaceoverhaul.compat;

import java.util.List;

import javax.annotation.Nonnull;

import cazador.furnaceoverhaul.blocks.BlockIronFurnace;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.ItemStackHandler;

@WailaPlugin
public class HwylaPlugin implements IWailaPlugin, IWailaDataProvider {

	@Override
	public void register(IWailaRegistrar reg) {
		reg.registerBodyProvider(this, TileEntityIronFurnace.class);
		reg.registerNBTProvider(this, TileEntityIronFurnace.class);
	}

	@Nonnull
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if (!config.getConfig("fo.furnacedisplay") || !accessor.getBlockState().getValue(BlockIronFurnace.BURNING)) return currenttip;

		int cookTime = accessor.getNBTData().getShort("current_cook_time");

		ItemStackHandler h = new ItemStackHandler(3);
		h.deserializeNBT(accessor.getNBTData().getCompoundTag("inv"));

		String renderStr = "";

		if (!h.getStackInSlot(0).isEmpty()) {
			String name = h.getStackInSlot(0).getItem().getRegistryName().toString();
			renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(0).getCount()), String.valueOf(h.getStackInSlot(0).getItemDamage()));
		} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

		if (accessor.getNBTData().hasKey("fluid")) {
			FluidStack s = FluidStack.loadFluidStackFromNBT(accessor.getNBTData().getCompoundTag("fluid"));
			h.setStackInSlot(1, FluidUtil.getFilledBucket(s));
			String name = h.getStackInSlot(1).getItem().getRegistryName().toString();
			if (h.getStackInSlot(1).hasTagCompound()) {
				renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(1).getCount()), String.valueOf(h.getStackInSlot(1).getItemDamage()), h.getStackInSlot(1).getTagCompound().toString());
			} else renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(1).getCount()), String.valueOf(h.getStackInSlot(1).getItemDamage()));

		} else if (!h.getStackInSlot(1).isEmpty()) {
			String name = h.getStackInSlot(1).getItem().getRegistryName().toString();
			renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(1).getCount()), String.valueOf(h.getStackInSlot(1).getItemDamage()));
		} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

		renderStr += SpecialChars.getRenderString("waila.progress", String.valueOf(cookTime), String.valueOf(200));

		if (!h.getStackInSlot(2).isEmpty()) {
			String name = h.getStackInSlot(2).getItem().getRegistryName().toString();
			renderStr += SpecialChars.getRenderString("waila.stack", "1", name, String.valueOf(h.getStackInSlot(2).getCount()), String.valueOf(h.getStackInSlot(2).getItemDamage()));
		} else renderStr += SpecialChars.getRenderString("waila.stack", "2");

		currenttip.add(renderStr);

		return currenttip;
	}

	@Nonnull
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
		return te instanceof TileEntityIronFurnace ? ((TileEntityIronFurnace) te).writeHwylaData(tag) : tag;
	}

}
