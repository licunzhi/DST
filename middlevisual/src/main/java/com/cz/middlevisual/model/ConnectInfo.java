package com.cz.middlevisual.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class ConnectInfo {

    @ApiModelProperty(value = "127.0.0.1")
    String ip;
    @ApiModelProperty(value = "2181")
    String port;
    @ApiModelProperty(value = "5000")
    Integer timeout;
    @ApiModelProperty(value = "1")
    Integer retriesTimes;

}
