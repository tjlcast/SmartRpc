package com.tjlcast.SmartRpc.common.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author by tangjialiang
 *         时间 2019/5/22.
 *         说明 rpc的返回体
 */
@AllArgsConstructor
public class RpcResponse {

    public RpcResponse() {}

    @Getter
    @Setter
    private String requestId;

    @Getter
    @Setter
    private Exception exception;

    @Getter
    @Setter
    private Object result;
}
