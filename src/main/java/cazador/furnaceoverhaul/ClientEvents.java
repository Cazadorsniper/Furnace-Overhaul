package cazador.furnaceoverhaul;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = FurnaceOverhaul.MODID)
public class ClientEvents {

	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {

	}
}
