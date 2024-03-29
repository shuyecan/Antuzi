package com.arabbit.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by net8 on 2017/6/13.
 */

public class ImageFloder {
    /**
     * 图片的文件夹路径
     */
    private String dir;

    /**
     * 第一张图片的路径
     */
    private String firstImagePath;
    /**
     * 文件夹的名称
     */
    private String name;

    public List<ImageItem> images = new ArrayList<ImageItem>();

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

}

