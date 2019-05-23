package com.tjlcast.tjlcast.SmartRpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author by tangjialiang
 *         时间 2019/5/22.
 *         说明 RPC 服务端处理器（用于处理 RPC 请求）
 */
public class RpcServerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlers;

    public RpcServerHandler(Map<String, Object> handlers) {
        this.handlers = handlers;

        // todo
    }
}
