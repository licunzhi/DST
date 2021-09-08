package com.cz.middlevisual.model;

import com.cz.middlevisual.vo.zookeeper.NodeAcls;
import com.cz.middlevisual.vo.zookeeper.NodeMetadata;
import io.swagger.annotations.ApiModelProperty;
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
    /**全路径*/
    String path;
    /**节点名称*/
    String nodeName;

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
    List<NodeInfo> children;

    /**节点元数据对象信息*/
    @ApiModelProperty(hidden = true)
    NodeMetadata nodeMetadata;
    @ApiModelProperty(hidden = true)
    List<NodeAcls> nodeAclsList;
}
