package com.example.lagerimage_test.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.example.lagerimage_test.listener.CropImageListener;
import com.shizhefei.view.largeimage.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class CropImageUtil {

    private static CropImageUtil mCropImageUtile;
    private final Activity activity;
    private CropImageListener mCropImageListener = null;

    private CropImageUtil(Activity activity) {
        this.activity = activity;
    }

    private CropImageUtil(Activity activity, CropImageListener mCropImageListener) {
        this.activity = activity;
        this.mCropImageListener = mCropImageListener;
    }

    public static CropImageUtil getNewInstance(Activity activity) {
        mCropImageUtile = new CropImageUtil(activity);
        return mCropImageUtile;
    }

    public Generate generateImage(String patch, List<Point> list, CropImageListener mCropImageListener) {
        return new Generate(patch, list, mCropImageListener);
    }

    public synchronized Bitmap generateImage(String patch, List<Point> list) {
        Rect mRect = createRect(list);
        Bitmap shapeLayerBitmap = createAreaImage(mRect, list);
        Bitmap showLayerBitmap = getBitmapFromPatch(mRect, patch);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Bitmap finalBmp = Bitmap.createBitmap(mRect.width(), mRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBmp);
        if (null != shapeLayerBitmap) {
            canvas.drawBitmap(shapeLayerBitmap, 0, 0, paint);
        }
        if (null != showLayerBitmap) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(showLayerBitmap, 0, 0, paint);
        }
        return finalBmp;
    }


    private Bitmap createAreaImage(Rect mRect, List<Point> listPoint) {
        int w = mRect.width();
        int h = mRect.height();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1);
        CornerPathEffect cornerPathEffect = new CornerPathEffect(3);
        paint.setPathEffect(cornerPathEffect);
        paint.setColor(Color.parseColor("#FF0000"));
        Path path = new Path();
        path.moveTo(listPoint.get(0).getX() - mRect.left, listPoint.get(0).getY() - mRect.top);
        for (int i = 1; i < listPoint.size(); i++) {
            path.lineTo(listPoint.get(i).getX() - mRect.left, listPoint.get(i).getY() - mRect.top);
        }
        path.close();
        canvas.drawPath(path, paint);
        return bitmap;
    }

    private Rect createRect(List<Point> listPoint) {
        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        int right = 0;
        int bottom = 0;
        for (Point point : listPoint) {
            int x = point.getX();
            int y = point.getY();
            if (right < x) {
                right = x;
            }
            if (left > x) {
                left = x;
            }
            if (top > y) {
                top = y;
            }
            if (bottom < y) {
                bottom = y;
            }
        }
        Rect mRect = new Rect(left, top, right, bottom);
        return mRect;
    }

    /**
     * file转Bitmap
     */
    private Bitmap getBitmapFromPatch(Rect mRect, String path) {

        BitmapRegionDecoder bitmapRegionDecoder = null;
        try {
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(new FileInputStream(new File(path)), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmapDraw = bitmapRegionDecoder.decodeRegion(mRect, options);
        return bitmapDraw;
    }

    public class Generate extends Thread {
        private final String patch;
        private final List<Point> listPoint;
        private final CropImageListener mCropImageListener;
        Bitmap bitmap;

        public Generate(String patch, List<Point> listPoint, CropImageListener mCropImageListener) {
            super();
            this.patch = patch;
            this.listPoint = listPoint;
            this.mCropImageListener = mCropImageListener;
        }

        @Override
        public void run() {
            bitmap = generateImage(patch, listPoint);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCropImageListener.onGenerateResult(bitmap, patch);
                }
            });
        }

        @Override
        public synchronized void start() {
            mCropImageListener.onStart(patch);
            super.start();
        }
    }

}
