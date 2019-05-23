package com.tjlcast.tjlcast.SmartRpc.server;

import com.tjlcast.SmartRpc.common.util.StringUtil;
import com.tjlcast.SmartRpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by tangjialiang
 *         时间 2019/5/22.
 *         说明 ...
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    private String serviceAddress;
    private ServiceRegistry serviceRegistry;

    /**
     * 存放 服务名 与 服务对象 之间的映射关系
     */
    private Map<String, Object> handlerMap = new HashMap<>();

    public RpcServer(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcServer(String serviceAddress, ServiceRegistry serviceRegistry) {
        this(serviceAddress);
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * 扫描带有 RpcService 注解的类并初始化 handlerMap 对象 <BeanName, bean>
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String serviceVersion = rpcService.version();
                if (StringUtil.isNotEmpty(serviceVersion)) {
                    serviceName += "-" + serviceVersion;
                }
                this.handlerMap.put(serviceName, serviceBean);
            }
        }
    }

    /**
     * 启动服务器
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // 创建并初始化 Netty 服务器 BootStrap 对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel channel) throws Exception {
                                    ChannelPipeline pipeline = channel.pipeline();
                                    pipeline.addLast(null);
                                    pipeline.addLast(null);
                                    pipeline.addLast(null);
//                                    ChannelPipeline pipeline = channel.pipeline();
//                                    pipeline.addLast(new RpcDecoder(RpcRequest.class)); // 解码 RPC 请求
//                                    pipeline.addLast(new RpcEncoder(RpcResponse.class)); // 编码 RPC 响应
//                                    pipeline.addLast(new RpcServerHandler(handlerMap)); // 处理 RPC 请求
                                }
                            }
                    );
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            // 得到rpc服务器的服务地址[ip:port] 并启动网络模块
            String[] split = StringUtil.split(this.serviceAddress, ":");
            String ip = split[0];
            int port = Integer.parseInt(split[1]);
            ChannelFuture future = bootstrap.bind(ip, port).sync();

            // 进行服务注册
            for (Map.Entry<String, Object> entry : this.handlerMap.entrySet()) {
                String serviceName = entry.getKey();
                this.serviceRegistry.registry(serviceName, serviceAddress);
                LOGGER.debug("registry service: {}=>{}", serviceName, serviceAddress);
            }
            LOGGER.debug("server is starting on port {}", port);

            future.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
