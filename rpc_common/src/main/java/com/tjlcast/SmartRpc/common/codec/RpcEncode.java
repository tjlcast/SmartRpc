package com.tjlcast.SmartRpc.common.codec;

import com.tjlcast.SmartRpc.common.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author by tangjialiang
 *         时间 2019/5/22.
 *         说明 RPC 编码器
 */
public class RpcEncode extends MessageToByteEncoder {

    private Class<?> genericClass;

    public RpcEncode(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf outBuf) throws Exception {
        if (genericClass.isInstance(o)) {
            byte[] data = SerializationUtil.serialize(o);
            outBuf.writeByte(data.length);
            outBuf.writeBytes(data);
        }
    }
}
