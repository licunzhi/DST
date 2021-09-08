package com.cz.middlevisual.vo.zookeeper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName NodeAcls
 * @function [业务功能]
 * @Author lcz
 * @Date 2021/09/08 10:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeAcls {
    private int perms;
    //private org.apache.zookeeper.data.Id id;
    private String permissions;
    private String scheme;
    private String id;

    public NodeAcls(ACL acl) {
        this.perms = acl.getPerms();
        List<String> permsList = new ArrayList<>();
        if ((perms & ZooDefs.Perms.READ) == ZooDefs.Perms.READ) {
            permsList.add("READ");
        }
        if ((perms & ZooDefs.Perms.WRITE) == ZooDefs.Perms.WRITE)
        {
            permsList.add("WRITE");
        }
        if ((perms & ZooDefs.Perms.CREATE) == ZooDefs.Perms.CREATE)
        {
            permsList.add("CREATE");
        }
        if ((perms & ZooDefs.Perms.DELETE) == ZooDefs.Perms.DELETE)
        {
            permsList.add("DELETE");
        }
        if ((perms & ZooDefs.Perms.ADMIN) == ZooDefs.Perms.ADMIN)
        {
            permsList.add("ADMIN");
        }
        this.permissions = String.join(",", permsList);
        this.scheme = acl.getId().getScheme();
        this.id = acl.getId().getId();
    }
}
