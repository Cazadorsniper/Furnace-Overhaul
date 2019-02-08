package cazador.furnaceoverhaul.items;

import java.util.List;

import cazador.furnaceoverhaul.FurnaceOverhaul;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUpgrade extends Item {

	final String tooltipKey;

	public ItemUpgrade(String name, String tooltipKey) {
		this.setTranslationKey(FurnaceOverhaul.MODID + "." + name);
		this.setRegistryName(new ResourceLocation(FurnaceOverhaul.MODID, name));
		this.setMaxStackSize(1);
		this.setCreativeTab(FurnaceOverhaul.FO_TAB);
		this.tooltipKey = tooltipKey;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (tooltipKey != null) tooltip.add(I18n.format(tooltipKey));
	}

}
