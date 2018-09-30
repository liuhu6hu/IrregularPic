package com.shizhefei.view.largeimage;

import com.tangmob.Pos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyi on 2018/4/20.
 */

public class Area {
    private int imageWidth;
    private int imageHeight;
    private List<Pos> posList;
    private long height;
    private double areaRect;

    public Area(int imageWidth, int imageHeight, List<Pos> posList, long height, double areaRect) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.posList = posList;
        this.height = height;
        this.areaRect = areaRect;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public List<Pos> getPosList() {
        return posList;
    }

    public void setPosList(List<Pos> posList) {
        this.posList = posList;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public double getAreaRect() {
        return areaRect;
    }

    public void setAreaRect(double areaRect) {
        this.areaRect = areaRect;
    }
}
