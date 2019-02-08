package cazador.furnaceoverhaul.items;

import java.util.List;

import cazador.furnaceoverhaul.FurnaceOverhaul;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKit extends Item {

	public ItemKit(String name) {
		this.setTranslationKey(FurnaceOverhaul.MODID + "." + name);
		this.setRegistryName(new ResourceLocation(FurnaceOverhaul.MODID, name));
		this.setMaxStackSize(1);
		this.setCreativeTab(FurnaceOverhaul.FO_TAB);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.RED + "Right-click function not implemented");
	}

}
