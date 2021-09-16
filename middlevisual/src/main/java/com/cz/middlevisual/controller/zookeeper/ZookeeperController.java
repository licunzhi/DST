package com.cz.middlevisual.controller.zookeeper;

import com.cz.middlevisual.base.BaseController;
import com.cz.middlevisual.base.BaseResult;
import com.cz.middlevisual.model.ConnectInfo;
import com.cz.middlevisual.model.NodeInfo;
import com.cz.middlevisual.service.ZookeeperService;
import com.cz.middlevisual.vo.zookeeper.NodeAcls;
import com.cz.middlevisual.vo.zookeeper.NodeMetadata;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: DST
 * @description: Zookeeper展示层
 * @author: Cai.Min
 * @create: 2021-09-06 19:12
 **/
@RestController
@RequestMapping("/zookeeper")
@Api(value = "Zookeeper 相关功能接口")
public class ZookeeperController extends BaseController {

    @Autowired
    ZookeeperService zookeeperService;

    @PostMapping(value = "/create")
    @ApiOperation(value = "新增一个数据节点",notes = "新增之后返回对象")
    public BaseResult create(@RequestBody NodeInfo nodeInfo){

        NodeInfo o = zookeeperService.create(nodeInfo.getPath(), nodeInfo.getData(),nodeInfo.getNodeModel(),nodeInfo.getConnectInfo());
        return BaseResult.successResultCreate(o);
    }

    @PostMapping(value = "/retrieve")
    @ApiOperation(value = "查询zookeeper数据",notes = "返回查询结果")
    public BaseResult retrieve(@RequestBody NodeInfo nodeInfo){
        Object retrieve = zookeeperService.retrieve(nodeInfo.getPath(),nodeInfo.getConnectInfo());
        return BaseResult.successResultCreate(retrieve);
    }

    @PostMapping(value = "/connect")
    @ApiOperation(value = "新建zookeeper连接",notes = "返回查询结果")
    public BaseResult connect(@RequestBody ConnectInfo connectInfo){
        boolean connect = zookeeperService.connect(connectInfo);
        return BaseResult.successResultCreate(connect);
    }

    @PostMapping(value = "/retrieveAll")
    @ApiOperation(value = "查询zookeeper数据并返回Tree",notes = "返回查询结果")
    public BaseResult retrieveWithChild(@RequestBody NodeInfo nodeInfo){
        NodeInfo result = zookeeperService.getDataWithChild(nodeInfo);
        return BaseResult.successResultCreate(result);
    }

    @PostMapping(value = "/updateData")
    @ApiOperation(value = "更新节点数据",notes = "返回查询结果")
    public BaseResult updateData(@RequestBody NodeInfo nodeInfo){
        NodeInfo result = zookeeperService.updateData(nodeInfo);
        return BaseResult.successResultCreate(result);
    }

    @PostMapping(value = "/metadata")
    @ApiOperation(value = "节点元数据信息",notes = "返回查询结果")
    public BaseResult<NodeMetadata> metadata(@RequestBody NodeInfo nodeInfo){
        NodeMetadata result = zookeeperService.metadata(nodeInfo);
        return BaseResult.successResultCreate(result);
    }

    @PostMapping(value = "/acls")
    @ApiOperation(value = "访问控制列表",notes = "返回查询结果")
    public BaseResult<List<NodeAcls>> acls(@RequestBody NodeInfo nodeInfo){
        List<NodeAcls> result = zookeeperService.acls(nodeInfo.getPath(),nodeInfo.getConnectInfo());
        return BaseResult.successResultCreate(result);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "节点删除操作[递归支持]",notes = "返回查询结果")
    public BaseResult<Boolean> delete(@RequestBody NodeInfo nodeInfo){
        Boolean result = zookeeperService.delete(nodeInfo,nodeInfo.getConnectInfo());
        return BaseResult.successResultCreate(result);
    }

    @PostMapping(value = "/refresh")
    @ApiOperation(value = "刷新缓存数据数据",notes = "返回查询结果")
    public BaseResult<NodeInfo> refresh(@RequestBody ConnectInfo connectInfo){
        NodeInfo result = zookeeperService.refresh(connectInfo);
        return BaseResult.successResultCreate(result);
    }
}
