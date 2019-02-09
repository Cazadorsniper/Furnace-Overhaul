package cazador.furnaceoverhaul.handler;

import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.inventory.ContainerFO;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFO extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(FurnaceOverhaul.MODID, "textures/gui/container/guifurnace.png");
	private final InventoryPlayer playerInventory;
	private final TileEntityIronFurnace te;

	public GuiFO(InventoryPlayer player, TileEntityIronFurnace te) {
		super(new ContainerFO(player, te));
		this.te = te;
		this.playerInventory = player;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

		if (te.isBurning()) {
			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		int l = this.getCookProgressScaled(24);
		this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);

		int k = this.getEnergyStoredScaled(58);
		this.drawTexturedModalRect(this.guiLeft + 151, this.guiTop + 13, 177, 32, 16, 58 - k);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String s = this.te.getBlockType().getLocalizedName();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
		this.fontRenderer.drawString(Integer.toString(this.te.getEnergy()), 142, 74, 4210752);
	}

	private int getEnergyStoredScaled(int pixels) {
		int cur = te.getEnergy();
		int max = TileEntityIronFurnace.MAX_ENERGY_STORED;
		return getPixels(cur, max, pixels);
	}

	private int getCookProgressScaled(int pixels) {
		int cur = te.getCurrentCookTime();
		int max = te.getCookTime();
		return getPixels(cur, max, pixels);
	}

	private int getBurnLeftScaled(int pixels) {
		if (te.isElectric() && te.getEnergy() >= te.getEnergyUse()) return pixels;
		return getPixels(te.getBurnTime(), Math.max(1, te.getFuelLength()), pixels);
	}

	public TileEntityIronFurnace getTE() {
		return te;
	}

	static int getPixels(float a, float b, int pixels) {
		return (int) Math.floor(a / b * pixels);
	}

}