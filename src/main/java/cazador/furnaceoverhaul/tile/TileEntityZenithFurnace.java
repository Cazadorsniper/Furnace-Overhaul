package cazador.furnaceoverhaul.tile;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import cazador.furnaceoverhaul.blocks.ZenithFurnace;

public class TileEntityZenithFurnace extends TileEntityIronFurnace {
	
	@Override
    public int getCookTime(ItemStack stack){
        return 1;
    }

    @Override
    public String getGuiID() {
    	return "furnaceoverhaul:zenithfurnace";
    }
    
    @Override
    public void update(){
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()){
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote){
            ItemStack itemstack = (ItemStack)this.furnaceItemStacks.get(1);

            if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack)this.furnaceItemStacks.get(0)).isEmpty()){
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
                                this.furnaceItemStacks.set(1, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()){
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime){
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime((ItemStack)this.furnaceItemStacks.get(0));
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
                ZenithFurnace.setState(this.isBurning(), this.world, this.pos);
            }
        }

        if (flag1){
            this.markDirty();
        }
    }
}
