package com.cz.middlevisual.service.Impl;

import com.cz.middlevisual.constant.Constant;
import com.cz.middlevisual.exception.ServiceException;
import com.cz.middlevisual.model.ConnectInfo;
import com.cz.middlevisual.service.ZookeeperService;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.CountDownLatch;

/**
 * @program: DST
 * @description: ZookeeperService 实现类
 * @author: Cai.Min
 * @create: 2021-09-06 20:11
 **/
@Slf4j
@Service
public class ZookeeperServiceImpl implements ZookeeperService {

    ZooKeeper zkClient;

    @Override
    public boolean connect(ConnectInfo connectInfo) {
        ZooKeeper zooKeeper = null;
        Integer timeout = Constant.ZOOKEEPER_TIMEOUT;
        if (!ObjectUtils.isEmpty(connectInfo.getTimeout())) {
            timeout  = connectInfo.getTimeout();
        }
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            //连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
            //  可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
            zooKeeper = new ZooKeeper(connectInfo.getIp()+ Constant.COLON+connectInfo.getPort(), timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        //如果收到了服务端的响应事件,连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            log.info("【初始化ZooKeeper连接状态....】={}", zooKeeper.getState());
        } catch (Exception e) {
            log.error("初始化ZooKeeper连接异常....】={}", e);
            throw new ServiceException("初始化ZooKeeper连接异常....】={}", e);
        }
        zkClient = zooKeeper;
        return true;
    }
}
