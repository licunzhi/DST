package com.example.vue.vo;

import com.example.vue.entity.ZkClientEntity;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;

/**
 * @ClassName ZkClientInitVo
 * @function [业务功能]
 * @Author lcz
 * @Date 2021/09/04 20:22
 */
@Data
public class ZkClientInitVo {

    private String zkUrl;

    private ZkClientEntity zkClientEntity;

    private CuratorFramework curatorFramework;
}
