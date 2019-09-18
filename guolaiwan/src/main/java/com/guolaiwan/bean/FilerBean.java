package com.guolaiwan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: 陈冠希
 * 日期: 2018/4/12
 * 描述:
 */

public class FilerBean implements Serializable {
    private List<Children> children;
    private String name;
    private String type;
    public void setChildren(List<Children> children) {
        this.children = children;
    }
    public List<Children> getChildren() {
        return children;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public class Children {

        private int id;
        private String uuid;
        private String updateTime;
        private String classCode;
        private String classmodularCode;
        private String className;
        private int classIsv;
        private int classSort;
        private int comId;
        private String comName;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
        public String getUuid() {
            return uuid;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
        public String getUpdateTime() {
            return updateTime;
        }

        public void setClassCode(String classCode) {
            this.classCode = classCode;
        }
        public String getClassCode() {
            return classCode;
        }

        public void setClassmodularCode(String classmodularCode) {
            this.classmodularCode = classmodularCode;
        }
        public String getClassmodularCode() {
            return classmodularCode;
        }

        public void setClassName(String className) {
            this.className = className;
        }
        public String getClassName() {
            return className;
        }

        public void setClassIsv(int classIsv) {
            this.classIsv = classIsv;
        }
        public int getClassIsv() {
            return classIsv;
        }

        public void setClassSort(int classSort) {
            this.classSort = classSort;
        }
        public int getClassSort() {
            return classSort;
        }

        public void setComId(int comId) {
            this.comId = comId;
        }
        public int getComId() {
            return comId;
        }

        public void setComName(String comName) {
            this.comName = comName;
        }
        public String getComName() {
            return comName;
        }

    }

}
