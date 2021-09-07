package com.cz.middlevisual.vo.zookeeper;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.zookeeper.data.Stat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName NodeMetadata
 * @function [元数据信息展示对象]
 * @Author lcz
 * @Date 2021/09/07 22:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NodeMetadata implements Serializable {

    private static final long serialVersionUID = -5667032711827724176L;

    @ApiModelProperty(notes = "权限版本号")
    private String aclVersion;
    @ApiModelProperty(notes = "节点创建时间")
    private String ctime;
    @ApiModelProperty(notes = "创建节点的事务ID")
    private String czxid;
    @ApiModelProperty(notes = "子节点的版本号")
    private String cversion;
    @ApiModelProperty(notes = "节点存储的数据的长度")
    private String dataLength;
    @ApiModelProperty(notes = "用于临时节点，保存临时节点的ID，如果为持久行节点，则其值为0")
    private String ephemeralOwner;
    @ApiModelProperty(notes = "节点更新时间")
    private String mtime;
    @ApiModelProperty(notes = "最后一次更新节点的事务ID")
    private String mzxid;
    @ApiModelProperty(notes = "当前节点的子节点的个数")
    private int numChildren;
    @ApiModelProperty(notes = "子节点最后一次被更新的事务ID")
    private String pzxid;
    @ApiModelProperty(notes = "数据版本号")
    private String dataVersion;

    public NodeMetadata(Stat stat) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS z");
        this.aclVersion = String.valueOf(stat.getAversion());
        this.ctime = format.format(new Date(stat.getCtime()));
        this.cversion = String.valueOf(stat.getCversion());
        //this.czxid = "0x" + Long.toHexString(stat.getCzxid());
        this.czxid = String.valueOf(stat.getCzxid());
        this.dataLength = String.valueOf(stat.getDataLength());
        //this.ephemeralOwner = "0x" + Long.toHexString(stat.getEphemeralOwner());
        this.ephemeralOwner = String.valueOf(stat.getEphemeralOwner());
        this.mtime = format.format(new Date(stat.getMtime()));
        //this.mzxid = "0x" + Long.toHexString(stat.getMzxid());
        this.mzxid = String.valueOf(stat.getMzxid());
        this.numChildren = stat.getNumChildren();
        //this.pzxid = "0x" + Long.toHexString(stat.getPzxid());
        this.pzxid = String.valueOf(stat.getPzxid());
        this.dataVersion = String.valueOf(stat.getVersion());
    }
}
