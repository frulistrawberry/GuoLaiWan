package com.guolaiwan.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: chengx
 * 日期: 2016/11/17.
 * 描述:
 */
public class ImageFloder implements Serializable {
    private static final long serialVersionUID = -6414842285278869653L;

    private String dir; //图片文件夹路径
    private String firstImagePath; //第一张图片的路径
    private String dirName; //文件夹名称
    private int imageCount; //图片数量
    private List<String> imags;//文件夹里的图片地址

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<String> getImags() {
        return imags;
    }

    public void setImags(List<String> imags) {
        this.imags = imags;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }
}
