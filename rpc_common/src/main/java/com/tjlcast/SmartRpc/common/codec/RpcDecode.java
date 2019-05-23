package com.tjlcast.SmartRpc.common.codec;

import com.tjlcast.SmartRpc.common.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author by tangjialiang
 *         时间 2019/5/23.
 *         说明 RPC 解码器
 */
public class RpcDecode extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public RpcDecode(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        list.add(SerializationUtil.deserialize(data, genericClass));
    }
}
