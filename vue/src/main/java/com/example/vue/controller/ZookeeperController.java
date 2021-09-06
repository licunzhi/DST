package com.example.vue.controller;

import com.example.vue.entity.ZkClientEntity;
import com.example.vue.service.ZookeeperService;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ZookeeperController
 * @function [业务功能]
 * @Author lcz
 * @Date 2021/09/04 19:36
 */
@RestController
@RequestMapping("/zookeeper")
public class ZookeeperController {

    @Autowired
    private ZookeeperService zookeeperService;

    @PostMapping("/createZkClient")
    public ResponseEntity createZkClient(@RequestBody ZkClientEntity zkClientEntity) {
        CuratorFramework zkClient = zookeeperService.createZkClient(zkClientEntity);
        String state = zkClient.getState().toString();
        return null;
    }
}
