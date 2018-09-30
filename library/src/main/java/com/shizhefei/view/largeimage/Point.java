package com.shizhefei.view.largeimage;

import java.io.Serializable;

/**
 * Created by iilee on 2017/12/22.
 */

public class Point implements Serializable{
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
