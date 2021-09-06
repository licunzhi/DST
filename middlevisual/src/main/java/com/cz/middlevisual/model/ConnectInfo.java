package com.cz.middlevisual.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: DST
 * @description: zookeeper连接信息
 * @author: Cai.Min
 * @create: 2021-09-06 20:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectInfo {

    String ip;
    String port;
    Integer timeout;
    Integer retriesTimes;

}
