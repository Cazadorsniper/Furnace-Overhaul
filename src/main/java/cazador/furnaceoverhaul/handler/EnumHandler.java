package cazador.furnaceoverhaul.handler;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import cazador.furnaceoverhaul.handler.EnumHandler.KitTypes;
import cazador.furnaceoverhaul.tile.TileEntityDiamondFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEmeraldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityEndestFurnace;
import cazador.furnaceoverhaul.tile.TileEntityGoldFurnace;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import cazador.furnaceoverhaul.tile.TileEntityZenithFurnace;

public class EnumHandler {

	public static enum KitTypes implements IStringSerializable {
		
		IRON("iron", 0, TileEntityIronFurnace.class),
		GOLD("gold", 1, TileEntityGoldFurnace.class),
		DIAMOND("diamond", 2, TileEntityDiamondFurnace.class),
		EMERALD("emerald", 3, TileEntityEmeraldFurnace.class),
		ENDEST("endest", 4, TileEntityEndestFurnace.class),
		ZENITH("zenith", 5, TileEntityZenithFurnace.class);
		
		private int ID;
		private String name;
		public final Class<? extends TileEntityIronFurnace> clazz;
		
		private KitTypes(String name, int ID, Class<? extends TileEntityIronFurnace> clazz) {
			this.ID = ID;
			this.name = name;
			this.clazz = clazz;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getID() {
			return ID;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		public final Class<? extends TileEntityIronFurnace> Class(){
			return clazz;
		}

		public static TileEntityIronFurnace makeEntity(int meta) {
			try{
		      return (TileEntityIronFurnace)values()[meta].clazz.newInstance();
		    }
		    catch (InstantiationException e){
		      e.printStackTrace();
		    }
		    catch (IllegalAccessException e){
		      e.printStackTrace();
		    }
		    return null;
		}
		
		
	}
	
	public static enum UpgradeTypes implements IStringSerializable {
		UPGRADE("upgrade", 0),
		EFFICIENCY("efficiency", 1),
		OREPROCESSING("oreprocessing", 2),
		LIQUIDFUEL("liquidfuel", 3),
		ELECTRICFUEL("electricfuel", 4),
		ELECTRICPROVIDER("electricprovider", 5);

		
		private int ID;
		private String name;
		
		private UpgradeTypes(String name, int ID) {
			this.ID = ID;
			this.name = name;
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
		public int getID() {
			return ID;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
	}
	
}
