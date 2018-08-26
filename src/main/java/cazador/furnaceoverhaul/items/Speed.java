package cazador.furnaceoverhaul.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Speed extends BlankUpgrade{

	public Speed(String unlocalizedname) {
		super(unlocalizedname);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(TextFormatting.BLUE + "Speeds up furnaces");
		tooltip.add(TextFormatting.RED + "Doesn't work in Zenith Furnace");
	}

}
