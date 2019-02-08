package cazador.furnaceoverhaul;

import cazador.furnaceoverhaul.init.ModObjects;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = FurnaceOverhaul.MODID)
public class ClientEvents {

	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		for (Item i : ModObjects.modelList) {
			ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
		}
	}
}
