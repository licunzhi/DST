package com.cz.middlevisual.service;

import com.cz.middlevisual.model.ConnectInfo;

/**
 * @program: DST
 * @description: zookeeper 服务层
 * @author: Cai.Min
 * @create: 2021-09-06 20:10
 **/
public interface ZookeeperService {

    boolean connect(ConnectInfo connectInfo);


}
