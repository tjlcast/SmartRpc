package com.tjlcast.SmartRpc.registry.zookeeper;

import com.tjlcast.SmartRpc.registry.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by tangjialiang
 *         时间 2019/5/21.
 *         说明 zk的服务注册
 */
public class ZooKeeperServiceRegistry implements ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZooKeeperServiceRegistry.class);

    private final ZkClient zkClient;

    public ZooKeeperServiceRegistry(String zkAddress) {
        this.zkClient = new ZkClient(zkAddress,
                Constant.ZK_CONNECTION_TIMEOUT,
                Constant.ZK_SESSION_TIMEOUT);

        LOGGER.debug("connect zookeeper");
    }

    @Override
    public void registry(String serviceName, String serviceAddress) {
        // 创建 registry 节点
        String registryPath = Constant.ZK_REGISTRY_PATH;
        if (!this.zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            LOGGER.debug("create registry path: {}", registryPath);
        }

        // 创建 serviceName服务 节点
        String servicePath = registryPath + "/" + serviceName;
        if (!this.zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            LOGGER.debug("create service path: {}", servicePath);
        }

        // 创建 node 节点
        String addressPath = servicePath + "/address-";
        zkClient.createEphemeralSequential(addressPath, serviceAddress);
        LOGGER.debug("create address node: {} and addr: {}", addressPath, serviceAddress);
    }
}