package com.cz.middlevisual.controller;

import com.cz.middlevisual.base.BaseController;
import com.cz.middlevisual.base.BaseResult;
import com.cz.middlevisual.model.ConnectInfo;
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
@RequestMapping("/emp")
@Api(value = "Zookeeper 相关功能接口")
public class ZookeeperController extends BaseController {

    @Autowired
    ZookeeperService zookeeperService;

    @PostMapping(value = "/create")
    @ApiOperation(value = "新增一个数据节点",notes = "新增之后返回对象")
    public BaseResult create(String data){
        return BaseResult.successResultCreate(data);
    }

    @PostMapping(value = "/retrieve")
    @ApiOperation(value = "查询zookeeper数据",notes = "返回查询结果")
    public BaseResult retrieve(String data){

        return BaseResult.successResultCreate(data);
    }

    @PostMapping(value = "/connect")
    @ApiOperation(value = "查询zookeeper数据",notes = "返回查询结果")
    public BaseResult connect(ConnectInfo connectInfo){

        return BaseResult.successResultCreate(zookeeperService.connect(connectInfo));
    }



}
