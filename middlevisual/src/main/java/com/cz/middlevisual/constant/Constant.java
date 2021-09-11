package com.cz.middlevisual.constant;

import org.apache.zookeeper.CreateMode;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

/**
 * @program: DST
 * @description: 常量类
 * @author: Cai.Min
 * @create: 2021-09-06 20:22
 **/
public class Constant {

    public final static String COLON = ":";

    public final static Integer ZOOKEEPER_TIMEOUT = 3000;

    public final static Integer RETRIES_TIMES = 3;

    public final static String UTF8 = "utf-8";

    public final static String SPRIT = "/";

    public final static Boolean TRUE = true;

    public final static Boolean FALSE = false;

    public static class FileType {
        //文件
        public final static String FILE = "file";

        //文件夹
        public final static String FOLDER = "folder";
    }

    public enum NodeModel {

        /**
         * 持久化
         */
        PERSISTENT(CreateMode.PERSISTENT, "PERSISTENT"),
        /**
         * 持久化并且带序列号
         */
        PERSISTENT_SEQUENTIAL(CreateMode.PERSISTENT_SEQUENTIAL, "PERSISTENT_SEQUENTIAL"),
        /**
         * 临时节点
         */
        EPHEMERAL(CreateMode.EPHEMERAL, "EPHEMERAL"),
        /**
         * 临时并且带序列号
         */
        EPHEMERAL_SEQUENTIAL(CreateMode.EPHEMERAL_SEQUENTIAL, "EPHEMERAL_SEQUENTIAL");

        private CreateMode createMode;

        private String modeName;

        private NodeModel(CreateMode createMode, String modeName) {
            this.createMode = createMode;
            this.modeName = modeName;
        }

        public CreateMode getCreateMode() {
            return createMode;
        }

        public void setCreateMode(CreateMode createMode) {
            this.createMode = createMode;
        }

        public String getModeName() {
            return modeName;
        }

        public void setModeName(String modeName) {
            this.modeName = modeName;
        }

        public static CreateMode getCreateModel(String createModel){
            final CreateMode[] createMode = {null};
            Arrays.asList(NodeModel.values()).forEach(item->{
                if (createModel.equals(item.getModeName())) {
                    createMode[0] = item.getCreateMode();
                }
            });
           return ObjectUtils.isEmpty(createMode[0])?CreateMode.PERSISTENT:createMode[0];
        }
    }
}
