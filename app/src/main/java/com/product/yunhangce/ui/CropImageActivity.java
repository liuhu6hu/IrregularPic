package com.product.yunhangce.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.product.yunhangce.R;
import com.product.yunhangce.listener.CropImageListener;
import com.product.yunhangce.util.CropImageUtil;
import com.product.yunhangce.util.ImgUtils;
import com.product.yunhangce.util.LicenseUtil;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.Point;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CropImageActivity extends AppCompatActivity {


    private String patch;
    private List<Point> listPoint;
    private SweetAlertDialog pDialog;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_iamge);
        titleBar();
        if (getDate()) {
            initView();
        }
    }

    private void initView() {
        CropImageUtil.getNewInstance(this).generateImage(patch, listPoint, new CropImageListener() {
            @Override
            public void onStart(String path) {
                pDialog = LicenseUtil. showCustomerDialog(CropImageActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            public void onGenerateResult(Bitmap mLineBitmap, String path) {
                LargeImageView largeImageView = (LargeImageView) findViewById(R.id.imageView);
                mBitmap = mLineBitmap;
                largeImageView.setImage(mLineBitmap);
                pDialog.dismiss();
            }
        }).start();
    }

    private void titleBar() {
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
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

    }

    /**
     * 获取传入的数据
     *
     * @return data is ok ,patch is not null and listPoint.size()>2
     */
    private boolean getDate() {
        Intent intent = getIntent();
        patch = intent.getStringExtra("path");
        listPoint = (List<Point>) intent.getSerializableExtra("point");
        if (TextUtils.isEmpty(patch) || listPoint == null || listPoint.size() < 3) {
            dataCheckDialog();
            return false;
        } else {
            return true;
        }
    }

    private void DialogHint() {
        pDialog = new SweetAlertDialog(CropImageActivity.this)
                .setTitleText("提示")
                .setContentText("是否将图片保存到手机相册？")
                .setConfirmText(getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        boolean status = ImgUtils.saveImageToGallery(CropImageActivity.this, mBitmap);
                        if (status) {
                            finish();
                        } else {
                            Toast.makeText(CropImageActivity.this, "不存图片失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setCancelText(getString(R.string.no));
                pDialog.show();


    }

    private void dataCheckDialog() {
        pDialog = new SweetAlertDialog(CropImageActivity.this)
                .setTitleText("提示")
                .setContentText("传入数据有误，请检查")
                .setConfirmText(getString(R.string.yes))

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        finish();

                    }
                });
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        DialogHint();
    }

    @Override
    protected void onDestroy() {
        LicenseUtil.dismissDialog();
        super.onDestroy();
    }

}
