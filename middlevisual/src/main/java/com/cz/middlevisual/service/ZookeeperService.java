package com.cz.middlevisual.service;

import com.cz.middlevisual.model.ConnectInfo;

/**
 * @program: DST
 * @description: zookeeper 服务层
 * @author: Cai.Min
 * @create: 2021-09-06 20:10
 **/
public interface ZookeeperService {

    /**
     * 建立连接
     * @param connectInfo
     * @return
     */
    boolean connect(ConnectInfo connectInfo);
    /**
     * 根据路径索引结果
     * @param path
     * @return
     */
    Object retrieve(String path);
    /**
     * 根据路径创建节点
     * @param path
     * @param data
     * @return
     */
    Object create(String path, String data);

    /**
     * 获取节点下所有节点
     * @return
     */
    Object retrieveWithChilder(String path);
}
