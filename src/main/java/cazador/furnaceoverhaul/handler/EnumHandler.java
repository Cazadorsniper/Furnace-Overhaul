package cazador.furnaceoverhaul.handler;

import net.minecraft.util.IStringSerializable;
import cazador.furnaceoverhaul.blocks.DiamondFurnace;
import cazador.furnaceoverhaul.blocks.EmeraldFurnace;
import cazador.furnaceoverhaul.blocks.EndFurnace;
import cazador.furnaceoverhaul.blocks.GoldFurnace;
import cazador.furnaceoverhaul.blocks.IronFurnace;
import cazador.furnaceoverhaul.blocks.ZenithFurnace;

public class EnumHandler {

	public static enum KitTypes implements IStringSerializable {
		
		IRON("iron", 0, IronFurnace.class),
		GOLD("gold", 1, GoldFurnace.class),
		DIAMOND("diamond", 2, DiamondFurnace.class),
		EMERALD("emerald", 3, EmeraldFurnace.class),
		ENDEST("endest", 4, EndFurnace.class),
		ZENITH("zenith", 5, ZenithFurnace.class);
		
		private final int meta;
		private String name;
		public final Class<? extends IronFurnace> clazz;
		
		private KitTypes(String name, int meta, Class<? extends IronFurnace> clazz) {
			this.name = name;
			this.meta = meta;
			this.clazz = clazz;
		}
		
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		public int getMeta() {
			return meta;
		}
		
		public final Class<? extends IronFurnace> Class(){
			return clazz;
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
