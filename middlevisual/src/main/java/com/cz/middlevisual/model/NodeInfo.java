package com.cz.middlevisual.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: DST
 * @description: Node节点信息
 * @author: Cai.Min
 * @create: 2021-09-07 19:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeInfo {
    String path;
    String data;
    Long id;
    String fileType;
    String stat;
    /**
     * 节点模式
     *
     * PERSISTENT：持久化
     * PERSISTENT_SEQUENTIAL：持久化并且带序列号
     * EPHEMERAL：临时
     * EPHEMERAL_SEQUENTIAL：临时并且带序列号
     */
    String nodeModel;
    List<NodeInfo> childern;
}
