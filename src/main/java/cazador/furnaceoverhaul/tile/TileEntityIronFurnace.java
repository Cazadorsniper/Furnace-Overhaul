package cazador.furnaceoverhaul.tile;

import java.util.Map.Entry;

import cazador.furnaceoverhaul.blocks.BlockIronFurnace;
import cazador.furnaceoverhaul.upgrade.Upgrade;
import cazador.furnaceoverhaul.upgrade.Upgrades;
import cazador.furnaceoverhaul.utils.MutableEnergyStorage;
import cazador.furnaceoverhaul.utils.OreProcessingRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityIronFurnace extends TileEntity implements ITickable {

	//Constants
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_FUEL = 1;
	public static final int SLOT_OUTPUT = 2;
	public static final int[] SLOT_UPGRADE = { 3, 4, 5 };
	public static final int MAX_FE_TRANSFER = 1200;
	public static final int MAX_ENERGY_STORED = 50000;

	//Item Handling, RangedWrappers are for sided i/o
	protected final ItemStackHandler inv = new ItemStackHandler(6);
	private final RangedWrapper TOP = new RangedWrapper(inv, SLOT_INPUT, SLOT_INPUT + 1);
	private final RangedWrapper SIDES = new RangedWrapper(inv, SLOT_FUEL, SLOT_FUEL + 1);
	private final RangedWrapper BOTTOM = new RangedWrapper(inv, SLOT_OUTPUT, SLOT_OUTPUT + 1);

	//Main TE Fields.
	protected MutableEnergyStorage energy = new MutableEnergyStorage(MAX_ENERGY_STORED, MAX_FE_TRANSFER, getEnergyUse());
	protected ItemStack recipeKey = ItemStack.EMPTY;
	protected ItemStack recipeOutput = ItemStack.EMPTY;
	protected ItemStack failedMatch = ItemStack.EMPTY;
	protected int burnTime = 0;
	protected int currentCookTime = 0;
	protected int fuelLength = 0;

	@ItemStackHolder(value = "minecraft:sponge", meta = 1)
	public static final ItemStack WET_SPONGE = ItemStack.EMPTY;

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		inv.deserializeNBT(tag.getCompoundTag("inv"));
		energy.setEnergy(tag.getInteger("energy"));
		burnTime = tag.getInteger("burn_time");
		fuelLength = tag.getInteger("fuel_length");
		currentCookTime = tag.getInteger("current_cook_time");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("inv", inv.serializeNBT());
		compound.setInteger("energy", energy.getEnergyStored());
		compound.setInteger("burn_time", burnTime);
		compound.setInteger("fuel_length", fuelLength);
		compound.setInteger("current_cook_time", currentCookTime);
		return compound;
	}

	/**
	 * Receives data from the server and sets it to the client TE.
	 * Should only be called on client.
	 */
	public void readContainerSync(int[] fromNet) {
		energy.setEnergy(fromNet[0]);
		burnTime = fromNet[1];
		fuelLength = fromNet[2];
		currentCookTime = fromNet[3];
	}

	/**
	 * Writes data that needs to be synced to a byte buffer.  Called from {@link Container#detectAndSendChanges}
	 */
	public void writeContainerSync(ByteBuf buf) {
		buf.writeInt(energy.getEnergyStored());
		buf.writeInt(burnTime);
		buf.writeInt(fuelLength);
		buf.writeInt(currentCookTime);
	}

	/**
	 * Main logic method for Iron Furnaces.  Does all the furnace things.
	 */
	@Override
	public final void update() {
		if (world.isRemote) return;

		ItemStack fuel = ItemStack.EMPTY;
		boolean canSmelt = canSmelt();

		if (!this.isBurning() && (isElectric() || !(fuel = inv.getStackInSlot(SLOT_FUEL)).isEmpty())) {
			if (canSmelt) burnFuel(fuel, false);
		}

		boolean wasBurning = isBurning();

		if (this.isBurning()) {
			burnTime--;
			if (canSmelt) smelt();
			else currentCookTime = 0;
		}

		if (!this.isBurning() && (isElectric() || !(fuel = inv.getStackInSlot(SLOT_FUEL)).isEmpty())) {
			if (canSmelt()) burnFuel(fuel, wasBurning);
		}

		if (wasBurning && !isBurning()) world.setBlockState(pos, getDimState());
	}

	/**
	 * @return If this furnace is burning.
	 */
	public boolean isBurning() {
		return getBurnTime() > 0;
	}

	/**
	 * Increments cook time, and tries to smelt the current item.
	 */
	protected void smelt() {
		currentCookTime++;
		if (this.currentCookTime >= this.getCookTime()) {
			this.currentCookTime = 0;
			this.smeltItem();
		}
	}

	/**
	 * Consumes fuel.
	 * @param fuel The item in the fuel slot.
	 * @param burnedThisTick If we have burned this tick, used to determine if we need to change blockstate.
	 */
	protected void burnFuel(ItemStack fuel, boolean burnedThisTick) {
		if (isElectric()) {
			fuelLength = (burnTime = energy.getEnergyStored() >= getEnergyUse() ? 1 : 0);
			if (this.isBurning()) energy.extractEnergy(getEnergyUse(), false);
		} else {
			fuelLength = (burnTime = getItemBurnTime(fuel));
			if (this.isBurning()) {
				Item item = fuel.getItem();
				fuel.shrink(1);
				if (fuel.isEmpty()) inv.setStackInSlot(SLOT_FUEL, item.getContainerItem(fuel));
			}
		}
		if (isBurning() && !burnedThisTick) world.setBlockState(pos, getLitState());
		markDirty();
	}

	/**
	 * @return If the current item in the input slot can be smelted.
	 */
	protected boolean canSmelt() {
		ItemStack input = inv.getStackInSlot(SLOT_INPUT);
		ItemStack output = inv.getStackInSlot(SLOT_OUTPUT);
		if (input.isEmpty() || input == failedMatch) return false;

		if (recipeKey.isEmpty() || !OreDictionary.itemMatches(recipeKey, input, false)) {
			boolean matched = false;
			for (Entry<ItemStack, ItemStack> e : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
				if (OreDictionary.itemMatches(e.getKey(), input, false)) {
					recipeKey = e.getKey();
					recipeOutput = e.getValue();
					matched = true;
					failedMatch = ItemStack.EMPTY;
					break;
				}
			}
			if (!matched) {
				ItemStack stack = getResult();
				if (stack.isEmpty()) {
					recipeKey = ItemStack.EMPTY;
					recipeOutput = ItemStack.EMPTY;
					failedMatch = input;
					return false;
				} else {
					recipeKey = input;
					recipeOutput = stack;
					matched = true;
					failedMatch = ItemStack.EMPTY;
				}
			}
		}

		ItemStack check = recipeOutput;
		if (hasUpgrade(Upgrades.PROCESSING)) {
			check = check.copy();
			check.grow(check.getCount());
		}

		return !recipeOutput.isEmpty() && (output.isEmpty() || (ItemHandlerHelper.canItemStacksStack(check, output) && (check.getCount() + output.getCount() <= output.getMaxStackSize())));
	}

	/**
	 * Actually smelts the item in the input slot.  Has special casing for vanilla wet sponge, because w e w vanilla.
	 */
	public void smeltItem() {
		ItemStack input = inv.getStackInSlot(SLOT_INPUT);
		ItemStack recipeOutput = getResult().copy();
		if (hasUpgrade(Upgrades.PROCESSING)) recipeOutput.grow(recipeOutput.getCount());
		ItemStack output = inv.getStackInSlot(SLOT_OUTPUT);

		if (output.isEmpty()) inv.setStackInSlot(SLOT_OUTPUT, recipeOutput);
		else if (ItemHandlerHelper.canItemStacksStack(output, recipeOutput)) output.grow(recipeOutput.getCount());

		if (input.isItemEqual(WET_SPONGE) && inv.getStackInSlot(SLOT_FUEL).getItem() == Items.BUCKET) inv.setStackInSlot(SLOT_FUEL, new ItemStack(Items.WATER_BUCKET));

		input.shrink(1);
		markDirty();
	}

	/**
	 * Prevents the TE from deleting itself if we change state, but not if the block is removed.
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	/**
	 * @param upg An upgrade.
	 * @return If this TE currently has said upgrade.
	 */
	public boolean hasUpgrade(Upgrade upg) {
		for (int slot : SLOT_UPGRADE)
			if (upg.matches(inv.getStackInSlot(slot))) return true;
		return false;
	}

	/**
	 * @param stack The item in the fuel slot.
	 * @return The burn time for this fuel, or 0, if this is an electric furnace.
	 */
	public int getItemBurnTime(ItemStack stack) {
		if (isElectric()) return 0;
		return TileEntityFurnace.getItemBurnTime(stack) * (hasUpgrade(Upgrades.EFFICIENCY) ? 2 : 1);
	}

	/**
	 * Says "I have items and energy!"
	 */
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY) return true;
		return super.hasCapability(capability, facing);
	}

	/**
	 * Returns item/energy caps based on side.  Follows vanilla furnace rules for items.
	 */
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) return CapabilityEnergy.ENERGY.cast(this.energy);
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			IItemHandler h;
			if (facing == null) h = inv;
			else if (facing == EnumFacing.DOWN) h = BOTTOM;
			else if (facing == EnumFacing.UP) h = TOP;
			else h = SIDES;
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(h);
		}
		return super.getCapability(capability, facing);
	}

	/**
	 * @return The unlit state of this TE.
	 */
	public IBlockState getDimState() {
		return world.getBlockState(pos).withProperty(BlockIronFurnace.BURNING, false);
	}

	/**
	 * @return The burning state of this TE.
	 */
	public IBlockState getLitState() {
		return world.getBlockState(pos).withProperty(BlockIronFurnace.BURNING, true);
	}

	/**
	 * @return If this TE has the electric upgrade.
	 */
	public boolean isElectric() {
		return hasUpgrade(Upgrades.ELECTRIC_FUEL);
	}

	public ItemStackHandler getInventory() {
		return inv;
	}

	private ItemStack getResult() {
		if (hasUpgrade(Upgrades.ORE_PROCESSING)) return OreProcessingRegistry.getSmeltingResult(inv.getStackInSlot(SLOT_INPUT));
		ItemStack s = FurnaceRecipes.instance().getSmeltingList().get(recipeKey);
		if (s != null) return s;
		return FurnaceRecipes.instance().getSmeltingResult(recipeKey);
	}

	/**
	 * @return The actual cook time of this furnace, taking speed into account.
	 */
	public final int getCookTime() {
		return hasUpgrade(Upgrades.SPEED) ? getSpeedyCookTime() : getDefaultCookTime();
	}

	protected int getDefaultCookTime() {
		return 170;
	}

	protected int getSpeedyCookTime() {
		return 140;
	}

	public int getEnergyUse() {
		return 600;
	}

	public int getEnergy() {
		return energy.getEnergyStored();
	}

	public int getCurrentCookTime() {
		return currentCookTime;
	}

	public int getBurnTime() {
		return burnTime;
	}

	public int getFuelLength() {
		return fuelLength;
	}

	public void clear() {
		for (int i = 0; i < 6; i++) {
			inv.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

}