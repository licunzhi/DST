package com.example.vue.service;

import com.example.vue.entity.ZkClientEntity;
import org.apache.curator.framework.CuratorFramework;

/**
 * @ClassName ZookeeperService
 * @function [zk接口层]
 * @Author lcz
 * @Date 2021/09/04 19:40
 */
public interface ZookeeperService {

    CuratorFramework createZkClient(ZkClientEntity zkClientEntity);
}
