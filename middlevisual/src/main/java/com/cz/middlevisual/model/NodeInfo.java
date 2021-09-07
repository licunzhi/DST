package com.cz.middlevisual.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
