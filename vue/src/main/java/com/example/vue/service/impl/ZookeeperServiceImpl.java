package com.example.vue.service.impl;

import com.example.vue.entity.ZkClientEntity;
import com.example.vue.service.ZookeeperService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ZookeeperServiceImpl
 * @function [业务功能]
 * @Author lcz
 * @Date 2021/09/04 19:40
 */
@Service
public class ZookeeperServiceImpl implements ZookeeperService {

    @Override
    public CuratorFramework createZkClient(ZkClientEntity zkClientEntity)  {
        // 用于重连策略，5000毫秒是初始化的间隔时间，3代表尝试重连次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(5000, 5);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkClientEntity.getZkUrl(), retryPolicy);
        //必须调用start开始连接ZooKeeper
        client.start();
        return client;
    }
}
