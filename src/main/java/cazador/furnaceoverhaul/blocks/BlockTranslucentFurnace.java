package cazador.furnaceoverhaul.blocks;

import java.util.function.Supplier;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.text.TextFormatting;

public class BlockTranslucentFurnace extends BlockIronFurnace {

	public BlockTranslucentFurnace(String name, TextFormatting infoColor, int cookTime, Supplier<TileEntity> teFunc) {
		super(name, infoColor, cookTime, teFunc);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

}
