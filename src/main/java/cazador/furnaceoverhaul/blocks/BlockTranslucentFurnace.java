package cazador.furnaceoverhaul.blocks;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.text.TextFormatting;

public class BlockTranslucentFurnace extends BlockIronFurnace {

	public BlockTranslucentFurnace(String name, TextFormatting infoColor, int cookTime) {
		super(name, infoColor, cookTime);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

}
