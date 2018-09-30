package com.example.lagerimage_test.ui;

import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lagerimage_test.MyApplication;
import com.example.lagerimage_test.R;
import com.example.lagerimage_test.util.AppUtils;
import com.example.lagerimage_test.util.CropImageUtil;
import com.example.lagerimage_test.util.LicenseUtil;
import com.shizhefei.view.largeimage.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private Paint paint;
    private ImageView ivogo;
    private List<Point> listPoint;
    private String patch;
    private Rect mRect;
    private int w;
    private int h;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        toolbarTop.setTitle("");

        setSupportActionBar(toolbarTop);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
        ivogo = (ImageView) findViewById(R.id.iv_logo);
        TextView  about=(TextView) findViewById(R.id.about);
        TextView  tvVersion=(TextView) findViewById(R.id.tv_version_name);
        about.setText(MyApplication.getCommonDate().getAbout());
        tvVersion.setText(AppUtils.getAppVersionName());
    }

    @Override
    protected void onDestroy() {
        LicenseUtil.dismissDialog();
        super.onDestroy();
    }
}
