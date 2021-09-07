package com.cz.middlevisual.controller;

import com.cz.middlevisual.base.BaseController;
import com.cz.middlevisual.base.BaseResult;
import com.cz.middlevisual.model.ConnectInfo;
import com.cz.middlevisual.model.NodeInfo;
import com.cz.middlevisual.service.ZookeeperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public BaseResult create(NodeInfo nodeInfo){

        Object o = zookeeperService.create(nodeInfo.getPath(), nodeInfo.getData());
        return BaseResult.successResultCreate(o);
    }

    @PostMapping(value = "/retrieve")
    @ApiOperation(value = "查询zookeeper数据",notes = "返回查询结果")
    public BaseResult retrieve(NodeInfo nodeInfo){
        Object retrieve = zookeeperService.retrieve(nodeInfo.getPath());
        return BaseResult.successResultCreate(retrieve);
    }

    @PostMapping(value = "/connect")
    @ApiOperation(value = "新建zookeeper连接",notes = "返回查询结果")
    public BaseResult connect(ConnectInfo connectInfo){
        boolean connect = zookeeperService.connect(connectInfo);
        return BaseResult.successResultCreate(connect);
    }

    @PostMapping(value = "/retrieveAll")
    @ApiOperation(value = "查询zookeeper数据并返回Tree",notes = "返回查询结果")
    public BaseResult retrieveWithChilder(NodeInfo nodeInfo){
        Object result = zookeeperService.retrieveWithChilder(nodeInfo.getPath());
        return BaseResult.successResultCreate(result);
    }

}
