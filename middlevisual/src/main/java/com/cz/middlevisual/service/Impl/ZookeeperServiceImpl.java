package com.cz.middlevisual.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.cz.middlevisual.constant.Constant;
import com.cz.middlevisual.exception.ServiceException;
import com.cz.middlevisual.model.ConnectInfo;
import com.cz.middlevisual.service.ZookeeperService;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: DST
 * @description: ZookeeperService 实现类
 * @author: Cai.Min
 * @create: 2021-09-06 20:11
 **/
@Slf4j
@Service
public class ZookeeperServiceImpl implements ZookeeperService {

    List<CuratorFramework> zkClientList;

    @Override
    public synchronized boolean connect(ConnectInfo connectInfo) {
        //判断是否存在连接
        isExistConnected(connectInfo);
        Pair<Integer, Integer> connectTime = initConnectTime(connectInfo);
        CuratorFramework zooKeeper = null;
        try {
            zooKeeper = CuratorFrameworkFactory.newClient(connectInfo.getIp() + Constant.COLON + connectInfo.getPort(), new ExponentialBackoffRetry(connectTime.getKey(), connectTime.getValue()));
            zooKeeper.start();
            zkClientList.add(zooKeeper);
        } catch (Exception e) {
            log.error("初始化ZooKeeper连接异常：】={}", e);
            throw new ServiceException("初始化ZooKeeper连接异常....】={}", e);
        }
        log.info("【新增ZooKeeper节点的连接状态为：】={}", zooKeeper.getState());
        return true;
    }

    @Override
    public Object retrieve(String path) {
        CuratorFramework curatorFramework =  getCurator();
        try {
            return StrUtil.str(curatorFramework.getData().forPath(path),Constant.UTF8);
        } catch (Exception e) {
            throw new ServiceException( "获取路径["+ path +"]数据失败"+ e.getMessage());
        }
    }

    @Override
    public Object create(String path, String data) {
        CuratorFramework curator = getCurator();
        String result;
        try {
            result = curator.create().forPath(path, StrUtil.utf8Bytes(data));
        } catch (Exception e) {
            throw new ServiceException( "新增数据失败"+ e.getMessage());
        }
        return result;
    }

    @Override
    public Object retrieveWithChilder(String path) {
        CuratorFramework curator = getCurator();
        try {
            return curator.getChildren().forPath(path);
        } catch (Exception e) {
            throw new ServiceException( "新增Tree数据失败"+ e.getMessage());
        }
    }

    /**
     * 根据条件获取zkList中的客户端
     * @return
     */
    private CuratorFramework getCurator() {
        if (CollectionUtils.isEmpty(zkClientList)) {
            throw new ServiceException("未查询到该连接信息，请先建立连接");
        }
       return zkClientList.get(0);
    }


    /**
     * 初始化连接超时时间和连接次数
     *
     * @param connectInfo
     * @return
     */
    private Pair<Integer, Integer> initConnectTime(ConnectInfo connectInfo) {
        Integer timeout = Constant.ZOOKEEPER_TIMEOUT;
        Integer retriesTimes = Constant.RETRIES_TIMES;
        if (!ObjectUtils.isEmpty(connectInfo.getTimeout())) {
            timeout = connectInfo.getTimeout();
        }
        if (!ObjectUtils.isEmpty(connectInfo.getRetriesTimes())) {
            retriesTimes = connectInfo.getRetriesTimes();
        }
        return new Pair<>(timeout, retriesTimes);

    }

    /**
     * 判断当前连接是否已经连接成功
     *
     * @param connectInfo
     */
    private void isExistConnected(ConnectInfo connectInfo) {

        if (!CollectionUtils.isEmpty(zkClientList)) {
            zkClientList.forEach(item -> {
                String connectString =
                item.getZookeeperClient().getCurrentConnectionString();
                String[] split = connectString.split(Constant.COLON);
                if(split[0].equals(connectInfo.getIp())&&split[1].equals(connectInfo.getPort())){
                    throw new ServiceException( "["+connectString + "]该节点已连接请检查节点状态");
                }
            });
        }else{
            zkClientList = new ArrayList<>();
        }
    }


}
