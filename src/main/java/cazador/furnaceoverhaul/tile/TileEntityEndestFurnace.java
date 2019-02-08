package cazador.furnaceoverhaul.tile;

import cazador.furnaceoverhaul.blocks.EndFurnace;
import cazador.furnaceoverhaul.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class TileEntityEndestFurnace extends TileEntityIronFurnace {
	private static final int FE_PER_TICK = 200;
	
	@Override
    public int getCookTime(ItemStack stack){
		if(hasUpgrade(ModItems.speed) == true) {
        	return 10;
        } else
        return 30;
    }

    @Override
    public String getGuiID() {
    	return "furnaceoverhaul:endestfurnace";
    }
    
    @Override
    public void update(){
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (!this.world.isRemote){
        	if(hasUpgrade(ModItems.electricfuel) == true) {
        		if (this.storage.getEnergyStored() < FE_PER_TICK) {
        			return;
        		}
        		else if(hasUpgrade(ModItems.electricfuel) == true) {
        			if (!this.isBurning() && this.canSmelt()) {
        					cookTime--;
						if (cookTime <= 0) {
							storage.consumePower(FE_PER_TICK);
							smeltItem();
        	                EndFurnace.setState(this.isBurning(), this.world, this.pos);
        	            } 
        			}
                }
                if (flag1){
                    this.markDirty();
                }
        }
        
        else if (this.isBurning() && hasUpgrade(ModItems.electricfuel) == false){
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote){
            ItemStack itemstack = (ItemStack)this.slot.get(1);

            if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)this.slot.get(0)).isEmpty()){
                if (!this.isBurning() && this.canSmelt()){
                    this.furnaceBurnTime = getItemBurnTime(itemstack);
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning()){
                        flag1 = true;

                        if (!itemstack.isEmpty()){
                            Item item = itemstack.getItem();
                            itemstack.shrink(1);

                            if (itemstack.isEmpty()){
                                ItemStack item1 = item.getContainerItem(itemstack);
                                this.slot.set(1, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()){
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime){
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime((ItemStack)this.slot.get(0));
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else{
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0){
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning()){
                flag1 = true;
                EndFurnace.setState(this.isBurning(), this.world, this.pos);
            }
        }
        if (flag1){
            this.markDirty();
        	}
       }
    }
}