package com.tjlcast.SmartRpc.common.beans;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author by tangjialiang
 *         时间 2019/5/22.
 *         说明 rpc的请求体
 */

@AllArgsConstructor
public class RpcRequest {

    public RpcRequest() {}

    @Getter
    @Setter
    private String requestId;

    @Getter
    @Setter
    private String interfaceName;

    @Getter
    @Setter
    private String serviceVersion;

    @Getter
    @Setter
    private String methodName;

    @Getter
    @Setter
    private Class<?>[] parametersType;

    @Getter
    @Setter
    private Object[] parameters;
}
