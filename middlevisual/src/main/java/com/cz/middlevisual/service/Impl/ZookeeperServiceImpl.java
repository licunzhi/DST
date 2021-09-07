package com.cz.middlevisual.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.cz.middlevisual.constant.Constant;
import com.cz.middlevisual.exception.ServiceException;
import com.cz.middlevisual.model.ConnectInfo;
import com.cz.middlevisual.model.NodeInfo;
import com.cz.middlevisual.service.ZookeeperService;
import com.cz.middlevisual.vo.zookeeper.NodeMetadata;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
        CuratorFramework curatorFramework = getCurator();
        try {
            return StrUtil.str(curatorFramework.getData().forPath(path), Constant.UTF8);
        } catch (Exception e) {
            throw new ServiceException("获取路径[" + path + "]数据失败" + e.getMessage());
        }
    }

    @Override
    public Object create(String path, String data, String nodeModel) {
        CuratorFramework curator = getCurator();
        String result;
        try {
            return curator.create().creatingParentContainersIfNeeded().withMode(Constant.NodeModel.getCreateModel(nodeModel)).forPath(path, StrUtil.utf8Bytes(data));
        } catch (Exception e) {
            throw new ServiceException("新增数据失败" + e.getMessage());
        }
    }

    @Override
    public NodeInfo retrieveWithChild(String path) {
        CuratorFramework curator = getCurator();
        NodeInfo nodeInfo = new NodeInfo();
        try {

            if (StrUtil.isEmpty(path)) {
                path = "/";
            }
            //包装NodeInfo并返回
            Stat stat = curator.checkExists().forPath(path);
            NodeMetadata nodeMetadata = stat != null ? new NodeMetadata(stat) : new NodeMetadata();

            return packNodeInfo(path, nodeInfo, curator.getChildren().forPath(path), StrUtil.str(curator.getData().forPath(path), Constant.UTF8), nodeMetadata);
        } catch (Exception e) {
            throw new ServiceException("展示Tree数据失败" + e.getMessage());
        }
    }

    /**
     * @param path     文件查询路径
     * @param nodeInfo 该节点信息
     * @param nodeList 路径下所有node
     * @param file     节点的文件
     * @return node节点树形数据
     */
    private NodeInfo packNodeInfo(String path, NodeInfo nodeInfo, List<String> nodeList, String file, NodeMetadata nodeMetadata) {

        nodeInfo.setNodeMetadata(nodeMetadata);
        nodeInfo.setData(file);
        nodeInfo.setId(nodeMetadata.getCzxid() != null ? Long.parseLong(nodeMetadata.getCzxid()) : 0);
        nodeInfo.setPath(path);
        nodeInfo.setNodeName(path.substring(path.lastIndexOf("/") + 1));
        /* folder or file -> num of child */
        nodeInfo.setFileType(nodeMetadata.getNumChildren() > 0 ? Constant.FileType.FOLDER : Constant.FileType.FILE);

        if (!CollectionUtils.isEmpty(nodeList)) {
            List<NodeInfo> nodeInfoList = new ArrayList<>();
            nodeList.forEach(item -> {
                String newPath = "";
                if (!Constant.SPRIT.equals(path)) {
                    newPath = path + Constant.SPRIT + item;
                } else {
                    newPath = path + item;
                }
                nodeInfoList.add(retrieveWithChild(newPath));
            });
            nodeInfo.setChildren(nodeInfoList);
        }
        return nodeInfo;
    }

    @Override
    public Object updateData(NodeInfo nodeInfo) {
        CuratorFramework curatorFramework = getCurator();
        try {
            Stat stat = curatorFramework.checkExists().forPath("path");
            if (ObjectUtils.isEmpty(stat)) {
                log.error("节点获取失败，请更新已经存在的节点");
                throw new ServiceException("节点获取失败，请更新已经存在的节点");
            }
           return curatorFramework.setData().withVersion(stat.getVersion()+1).forPath(nodeInfo.getPath(),StrUtil.utf8Bytes(nodeInfo.getData()));

        } catch (Exception e) {
            log.error("更新节点失败，请重新更新节点"+e.getMessage());
            throw new ServiceException("更新节点失败，请重新更新节点"+e.getMessage());
        }
    }


    @Override
    public NodeMetadata metadata(NodeInfo nodeInfo) {
        /*fixme lcz 后续需要从缓存中获取，可能还要牵涉到zk状态检测的接口，避免无用请求次数*/
        CuratorFramework curatorFramework = getCurator();
        try {
            Stat stat = curatorFramework.checkExists().forPath(nodeInfo.getPath());
            if (stat != null) {
                return new NodeMetadata(stat);
            }
        } catch (Exception e) {
            throw new ServiceException("获取节点元数据失败" + e.getMessage());
        }
        return new NodeMetadata();
    }

    /**
     * 根据条件获取zkList中的客户端
     *
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
                if (split[0].equals(connectInfo.getIp()) && split[1].equals(connectInfo.getPort())) {
                    throw new ServiceException("[" + connectString + "]该节点已连接请检查节点状态");
                }
            });
        } else {
            zkClientList = new ArrayList<>();
        }
    }


}
