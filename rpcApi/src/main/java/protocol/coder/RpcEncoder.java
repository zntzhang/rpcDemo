package protocol.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RpcEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) {
        byte[] bytes = Object2Byte(o);
        byteBuf.writeBytes(bytes);
        channelHandlerContext.flush();
    }

    private byte[] Object2Byte(Object o) {
        byte[] bytes = null;
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = null;

        try {
            oo = new ObjectOutputStream(bo);
            oo.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                oo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bytes = bo.toByteArray();
        return bytes;
    }
}
