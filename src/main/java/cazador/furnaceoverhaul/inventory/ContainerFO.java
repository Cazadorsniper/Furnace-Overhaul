package cazador.furnaceoverhaul.inventory;

import javax.annotation.Nullable;

import cazador.furnaceoverhaul.FurnaceOverhaul;
import cazador.furnaceoverhaul.net.MessageSyncTE;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFO extends Container {

	private final TileEntityIronFurnace te;
	private final EntityPlayerMP player;

	public ContainerFO(InventoryPlayer playerInv, TileEntityIronFurnace te) {
		this.te = te;
		this.addSlotToContainer(new SlotItemHandler(te.getInventory(), 0, 56, 17));
		this.addSlotToContainer(new SlotFurnaceFuel(te.getInventory(), 1, 56, 53));
		this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, te.getInventory(), 2, 116, 35));
		this.addSlotToContainer(new UpgradeSlot(te.getInventory(), 3, 12, 13));
		this.addSlotToContainer(new UpgradeSlot(te.getInventory(), 4, 12, 34));
		this.addSlotToContainer(new UpgradeSlot(te.getInventory(), 5, 12, 55));

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				int x = 8 + j * 18;
				int y = i * 18 + 84;
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, x, y));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
		}

		if (playerInv.player instanceof EntityPlayerMP) player = (EntityPlayerMP) playerInv.player;
		else player = null;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if (player != null) FurnaceOverhaul.NETWORK.sendTo(new MessageSyncTE(te), player);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return player.getDistanceSq(te.getPos()) < 64;
	}

	@Override
	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 42, true)) { return ItemStack.EMPTY; }

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 1 && index != 0) {
				if (!FurnaceRecipes.instance().getSmeltingResult(itemstack1).isEmpty()) {
					if (!this.mergeItemStack(itemstack1, 0, 1, false)) { return ItemStack.EMPTY; }
				} else if (TileEntityFurnace.isItemFuel(itemstack1)) {
					if (!this.mergeItemStack(itemstack1, 1, 2, false)) { return ItemStack.EMPTY; }
				} else if (index >= 3 && index < 30) {
					if (!this.mergeItemStack(itemstack1, 30, 42, false)) { return ItemStack.EMPTY; }
				} else if (index >= 30 && index < 36 && !this.mergeItemStack(itemstack1, 3, 30, false)) { return ItemStack.EMPTY; }
			} else if (!this.mergeItemStack(itemstack1, 3, 42, false)) { return ItemStack.EMPTY; }

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) { return ItemStack.EMPTY; }

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}
}
