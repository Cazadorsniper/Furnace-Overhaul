package cazador.furnaceoverhaul.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.Reference;

public class BlankUpgrade extends Item {

	public BlankUpgrade(String unlocalizedname) {
		this.setUnlocalizedName(unlocalizedname);
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, unlocalizedname));
		this.setMaxDamage(0);
		this.setCreativeTab(FurnaceOverhaul.FurnaceOverhaulTab);
	}

}
