package com.example.vue.entity;

import lombok.Data;

/**
 * @ClassName ZkClientEntity
 * @function [业务功能]
 * @Author lcz
 * @Date 2021/09/04 19:39
 */
@Data
public class ZkClientEntity {
    // zk的请求地址
    private String zkUrl;

    //zk链接连接的初始化事件间隔
    private int baseSleepTimeMs = 5000;

    // 重试次数
    private int maxRetries = 1;
}
