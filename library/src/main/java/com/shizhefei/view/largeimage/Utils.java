package com.shizhefei.view.largeimage;

import com.tangmob.Mianji;
import com.tangmob.Pos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by iilee on 2017/12/22.
 */

public class Utils {
    public static float caculate(List<Point> vertex) {
        int i = 0;
        float temp = 0;
        for (; i < vertex.size() - 1; i++) {
            temp += (vertex.get(i).getX() - vertex.get(i + 1).getX()) * (vertex.get(i).getY() + vertex.get(i + 1).getY());
        }
        temp += (vertex.get(i).getX() - vertex.get(0).getX()) * (vertex.get(i).getY() + vertex.get(0).getY());
        return temp / 2;
    }

    public static double areafun(List<Point> list) {
        //S = 0.5 * ( (x0*y1-x1*y0) + (x1*y2-x2*y1) + ... + (xn*y0-x0*yn) )

        double area = 0.00;
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                Point p1 = list.get(i);
                Point p2 = list.get(i + 1);
                area += p1.getX() * p2.getY() - p2.getX() * p1.getY();
            } else {
                Point pn = list.get(i);
                Point p0 = list.get(0);
                area += pn.getX() * p0.getY() - p0.getX() * pn.getY();
            }

        }
        area = area / 2.00;

        return Math.abs(area);
    }


    public static Area calArea(String cmosValue, int contentWidth, int contentHeight, LinkedList<Point> points, Long aLong, double a) throws Exception {
        List<Pos> list = mapperPoints(points);
        double cal = Mianji.cal(cmosValue, contentWidth, contentHeight, aLong, a, list);
        return new Area(contentWidth, contentHeight, list, aLong, cal);
    }

    private static List<Pos> mapperPoints(LinkedList<Point> points) {
        List<Pos> list = new ArrayList<>();
        for (Point point
                : points) {
            Pos pos = new Pos(point.getX(), point.getY());

            list.add(pos);
        }
        return list;
    }

    public static String toString(List<Pos> posList) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \n");

        for (int i = 0; i < posList.size(); i++) {
            Pos pos = posList.get(i);
            sb.append("pos-")
                    .append(i)
                    .append(":[ x = ")
                    .append(pos.x)
                    .append(", y = ")
                    .append(pos.y)
                    .append(" ]")
                    .append("\n");
        }

        sb.append(" }");


        return sb.toString();
    }
}
