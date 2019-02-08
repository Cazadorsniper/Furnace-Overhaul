package cazador.furnaceoverhaul.net;

import cazador.furnaceoverhaul.handler.GuiFO;
import cazador.furnaceoverhaul.tile.TileEntityIronFurnace;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This class handles the sync of furnace fields when the container is open.
 * @author Shadows
 *
 */
public class MessageSyncTE implements IMessage {

	protected TileEntityIronFurnace te;
	protected int[] fromNet = new int[4];

	public MessageSyncTE() {
	};

	public MessageSyncTE(TileEntityIronFurnace te) {
		this.te = te;
	};

	@Override
	public void fromBytes(ByteBuf buf) {
		for (int i = 0; i < 4; i++)
			fromNet[i] = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		te.writeContainerSync(buf);
	}

	public static class Handler implements IMessageHandler<MessageSyncTE, IMessage> {

		@Override
		public IMessage onMessage(MessageSyncTE message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				if (Minecraft.getMinecraft().currentScreen instanceof GuiFO) {
					((GuiFO) Minecraft.getMinecraft().currentScreen).getTE().readContainerSync(message.fromNet);
				}
			});
			return null;
		}

	}

}
