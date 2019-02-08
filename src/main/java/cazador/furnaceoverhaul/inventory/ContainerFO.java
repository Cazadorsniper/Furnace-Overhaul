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
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFO extends Container {

	public static final int SLOTS_TE = 0;
	public static final int SLOTS_TE_SIZE = 6;
	public static final int SLOTS_INVENTORY = SLOTS_TE_SIZE;
	public static final int SLOTS_HOTBAR = SLOTS_INVENTORY + 3 * 9;

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
		ItemStack slotStack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			slotStack = stack.copy();

			if (index >= SLOTS_INVENTORY && index <= SLOTS_HOTBAR + 9) {
				if (TileEntityFurnace.isItemFuel(stack)) {
					int s = TileEntityIronFurnace.SLOT_FUEL;
					if (!mergeItemStack(stack, s, s + 1, false)) { return ItemStack.EMPTY; }
				}
				if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, false)) { return ItemStack.EMPTY; }
			} else if (index >= SLOTS_HOTBAR && index < SLOTS_HOTBAR + 9) {
				if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false)) { return ItemStack.EMPTY; }
			} else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, true)) { return ItemStack.EMPTY; }

			slot.onSlotChanged();
			if (stack.getCount() == slotStack.getCount()) { return ItemStack.EMPTY; }
			slot.onTake(player, stack);
		}
		return slotStack;
	}
}
