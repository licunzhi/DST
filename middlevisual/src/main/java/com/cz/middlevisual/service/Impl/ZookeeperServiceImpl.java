package com.cz.middlevisual.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.cz.middlevisual.constant.Constant;
import com.cz.middlevisual.exception.ServiceException;
import com.cz.middlevisual.model.ConnectInfo;
import com.cz.middlevisual.model.NodeInfo;
import com.cz.middlevisual.service.ZookeeperService;
import com.cz.middlevisual.vo.zookeeper.NodeAcls;
import com.cz.middlevisual.vo.zookeeper.NodeMetadata;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: DST
 * @description: ZookeeperService 实现类
 * @author: Cai.Min
 * @create: 2021-09-06 20:11
 **/
@Slf4j
@Service
public class ZookeeperServiceImpl implements ZookeeperService {

    Map<String,CuratorFramework> zkClientMap;

    Map<String,NodeInfo> zkCache = new HashMap<>();

    @Override
    public synchronized boolean connect(ConnectInfo connectInfo) {
        //判断是否存在连接
        isExistConnected(connectInfo);
        Pair<Integer, Integer> connectTime = initConnectTime(connectInfo);
        CuratorFramework zooKeeper = null;
        try {
            zooKeeper = CuratorFrameworkFactory.newClient(connectInfo.getIp() + Constant.COLON + connectInfo.getPort(), new ExponentialBackoffRetry(connectTime.getKey(), connectTime.getValue()));
            zooKeeper.start();
            zkClientMap.put(connectInfo.getIp()+ Constant.COLON +connectInfo.getPort(),zooKeeper);
        } catch (Exception e) {
            log.error("初始化ZooKeeper连接异常：】={}", e);
            throw new ServiceException("初始化ZooKeeper连接异常....】={}", e);
        }
        log.info("【新增ZooKeeper节点的连接状态为：】={}", zooKeeper.getState());
        return true;
    }

    @Override
    public Object retrieve(String path,ConnectInfo connectInfo) {
        CuratorFramework curatorFramework = getCurator(connectInfo);
        try {
            return StrUtil.str(curatorFramework.getData().forPath(path), Constant.UTF8);
        } catch (Exception e) {
            throw new ServiceException("获取路径[" + path + "]数据失败" + e.getMessage());
        }
    }

    @Override
    public NodeInfo create(String path, String data, String nodeModel,ConnectInfo connectInfo) {
        CuratorFramework curator = getCurator(connectInfo);
        try {
            curator.create().creatingParentContainersIfNeeded().withMode(Constant.NodeModel.getCreateModel(nodeModel)).forPath(path, StrUtil.utf8Bytes(data));
            updateZkCache(connectInfo);
            return zkCache.get(connectInfo.getIp()+ Constant.COLON +connectInfo.getPort());
        } catch (Exception e) {
            throw new ServiceException("新增数据失败" + e.getMessage());
        }
    }


    private synchronized void updateZkCache(ConnectInfo connectInfo) {
        NodeInfo nodeInfo = retrieveWithChild("", connectInfo);
        zkCache.put(connectInfo.getIp()+ Constant.COLON +connectInfo.getPort(),nodeInfo);
    }

    @Override
    public NodeInfo retrieveWithChild(String path,ConnectInfo connectInfo) {
        CuratorFramework curator = getCurator(connectInfo);
        NodeInfo nodeInfo = new NodeInfo();
        try {

            if (StrUtil.isEmpty(path)) {
                path = "/";
            }
            //包装NodeInfo并返回
            Stat stat = StrUtil.equals("/", path) ? null : curator.checkExists().forPath(path);
            NodeMetadata nodeMetadata = stat != null ? new NodeMetadata(stat) : new NodeMetadata();

            return packNodeInfo(path, nodeInfo, curator.getChildren().forPath(path), StrUtil.str(curator.getData().forPath(path), Constant.UTF8), nodeMetadata,connectInfo);
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
    private NodeInfo packNodeInfo(String path, NodeInfo nodeInfo, List<String> nodeList, String file, NodeMetadata nodeMetadata,ConnectInfo connectInfo) {

        nodeInfo.setNodeMetadata(nodeMetadata);
        nodeInfo.setNodeAclsList(acls(path,connectInfo));
        nodeInfo.setData(file == null ? "" : file);
        nodeInfo.setId(nodeMetadata.getCzxid() != null ? Long.parseLong(nodeMetadata.getCzxid()) : -1);
        nodeInfo.setPath(path);
        nodeInfo.setNodeName(path.substring(path.lastIndexOf("/") + 1));
        /* folder or file -> num of child */
        nodeInfo.setFileType(nodeMetadata.getNumChildren() > 0 ? Constant.FileType.FOLDER : Constant.FileType.FILE);
        if (StrUtil.equals("/", path)) {
            nodeInfo.setFileType(Constant.FileType.FOLDER);
        }

        if (!CollectionUtils.isEmpty(nodeList)) {
            List<NodeInfo> nodeInfoList = new ArrayList<>();
            nodeList.forEach(item -> {
                String newPath = "";
                if (!Constant.SPRIT.equals(path)) {
                    newPath = path + Constant.SPRIT + item;
                } else {
                    newPath = path + item;
                }
                nodeInfoList.add(retrieveWithChild(newPath,connectInfo));
            });
            nodeInfo.setChildren(nodeInfoList);
        }
        return nodeInfo;
    }

    @Override
    public NodeInfo updateData(NodeInfo nodeInfo) {
        CuratorFramework curatorFramework = getCurator(nodeInfo.getConnectInfo());
        ConnectInfo connectInfo =  nodeInfo.getConnectInfo();
        try {
            Stat stat = curatorFramework.checkExists().forPath(nodeInfo.getPath());
            if (ObjectUtils.isEmpty(stat)) {
                log.error("节点获取失败，请更新已经存在的节点");
                throw new ServiceException("节点获取失败，请更新已经存在的节点");
            }
            curatorFramework.setData().forPath(nodeInfo.getPath(), StrUtil.utf8Bytes(nodeInfo.getData()));
            updateZkCache(connectInfo);

            return zkCache.get(connectInfo.getIp()+ Constant.COLON +connectInfo.getPort());

        } catch (Exception e) {
            log.error("更新节点失败，请重新更新节点" + e.getMessage());
            throw new ServiceException("更新节点失败，请重新更新节点" + e.getMessage());
        }
    }


    @Override
    public NodeMetadata metadata(NodeInfo nodeInfo) {
        CuratorFramework curator = getCurator(nodeInfo.getConnectInfo());
        try {
            Stat stat = curator.checkExists().forPath(nodeInfo.getPath());
            if (stat != null) {
                return new NodeMetadata(stat);
            }
        } catch (Exception e) {
            throw new ServiceException("获取节点元数据失败" + e.getMessage());
        }
        return new NodeMetadata();
    }

    @Override
    public List<NodeAcls> acls(String path,ConnectInfo connectInfo) {
        List<NodeAcls> nodeAclsList = new ArrayList<>();
        CuratorFramework curator = getCurator(connectInfo);
        try {
            List<ACL> aclList = curator.getACL().forPath(path);
            nodeAclsList = aclList.stream().map(NodeAcls::new).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("获取节点访问控制列表失败" + e.getMessage());
        }
        return nodeAclsList;
    }

    @Override
    public Boolean delete(NodeInfo nodeInfo,ConnectInfo connectInfo) {
        CuratorFramework curator = getCurator(connectInfo);
        try {
            curator.delete().deletingChildrenIfNeeded().forPath(nodeInfo.getPath());
            updateZkCache(connectInfo);
        } catch (Exception e) {
            log.error("递归删除子节点失败" + e.getMessage());
            throw new ServiceException("递归删除子节点失败" + e.getMessage());
        }
        return true;
    }

    @Override
    public synchronized void addCache(NodeInfo nodeInfo,ConnectInfo connectInfo) {
        String  zkCacheKey = connectInfo.getIp()+ Constant.COLON +connectInfo.getPort();
        if (!zkCache.containsKey(zkCacheKey)) {
            nodeInfo.setConnectInfo(connectInfo);
            zkCache.put(zkCacheKey,nodeInfo);
        }
    }

    @Override
    public NodeInfo getDataWithChild(NodeInfo nodeInfo) {
        Pair<Boolean, NodeInfo> dataWithCache = getDataWithCache(nodeInfo.getConnectInfo());
        if (!dataWithCache.getKey()) {
            return dataWithCache.getValue();
        }
        //说明缓存中没有key 添加数据Key
        NodeInfo retrieveWithChild = retrieveWithChild(nodeInfo.getPath(),nodeInfo.getConnectInfo());
        addCache(retrieveWithChild,nodeInfo.getConnectInfo());
        return retrieveWithChild;
    }

    @Override
    public NodeInfo refresh(ConnectInfo connectInfo) {
        updateZkCache(connectInfo);

        return zkCache.get(connectInfo.getIp()+ Constant.COLON +connectInfo.getPort());
    }

    /**
     * 根据ConnectInfo获取连接信息
     * @param connectInfo
     */
    private synchronized Pair<Boolean,NodeInfo> getDataWithCache(ConnectInfo connectInfo) {
        String  zkCacheKey = connectInfo.getIp()+ Constant.COLON +connectInfo.getPort();
        if (zkCache.containsKey(zkCacheKey)) {
            return new Pair<Boolean,NodeInfo>(Constant.TRUE,zkCache.get(zkCacheKey));
        }
        return new Pair<Boolean,NodeInfo>(Constant.TRUE,null);
    }

    /**
     * 根据条件获取zkList中的客户端
     *
     * @return
     */
    private CuratorFramework getCurator(ConnectInfo connectInfo) {
        if (CollectionUtils.isEmpty(zkClientMap)) {
            throw new ServiceException("未查询到该连接信息，请先建立连接");
        }
        String zkClienCacheKey =  connectInfo.getIp()+ Constant.COLON +connectInfo.getPort();
        return zkClientMap.get(zkClienCacheKey);
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

        if (!CollectionUtils.isEmpty(zkClientMap)) {
           String zkClienCacheKey =  connectInfo.getIp()+ Constant.COLON +connectInfo.getPort();
            if (zkClientMap.containsKey(zkClienCacheKey)) {
                throw new ServiceException("[" + zkClienCacheKey + "]该节点已连接请检查节点状态");
            }
        } else {
            zkClientMap = new HashMap<>();
        }
    }


}
