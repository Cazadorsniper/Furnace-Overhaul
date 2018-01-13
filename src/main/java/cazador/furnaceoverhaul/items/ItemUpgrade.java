package cazador.furnaceoverhaul.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.handler.EnumHandler;
import cazador.furnaceoverhaul.handler.EnumHandler.UpgradeTypes;

public class ItemUpgrade extends Item {
	
	public ItemUpgrade() {
		this.setUnlocalizedName("upgrade" + EnumHandler.UpgradeTypes.values().length);
		this.setRegistryName("upgrade");
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(FurnaceOverhaul.FurnaceOverhaulTab);
	}
	
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)){
			for(int c = 0; c < UpgradeTypes.values().length; c++) {
				items.add(new ItemStack(this, 1, c));
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		for(int c = 0; c < UpgradeTypes.values().length; c++) {
			if(stack.getItemDamage() == c) {
				return this.getUnlocalizedName() + "." + UpgradeTypes.values()[c].getName();
			}
			else {
				continue;
			}
		}
		return this.getUnlocalizedName() + "." + UpgradeTypes.UPGRADE.getName();
	}

}
