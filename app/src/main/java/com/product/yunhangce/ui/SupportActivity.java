package com.product.yunhangce.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.product.yunhangce.MyApplication;
import com.product.yunhangce.R;
import com.product.yunhangce.util.LicenseUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SupportActivity extends AppCompatActivity {

    private TextView callSupport;
    private Paint paint;
    private TextView supportCcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
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
        callSupport = (TextView) findViewById(R.id.call_support);
        callSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHint();
            }
        });
        supportCcontext=(TextView) findViewById(R.id.support_context);
        callSupport.setText(MyApplication.getCommonDate().getSupportTel());
        supportCcontext.setText(MyApplication.getCommonDate().getSupport());
    }

    private void DialogHint() {
        LicenseUtil.showCustomerDialog (SupportActivity.this)
                .setTitleText("技术支持")
                .setContentText("你是否马上拨打技术人员电话（"+MyApplication.getCommonDate().getSupportTel()+"）?")
                .setConfirmText(getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        diallPhone(MyApplication.getCommonDate().getSupportTel());
                    }
                })
                .setCancelText(getString(R.string.no))
                .show();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        LicenseUtil.dismissDialog();
        super.onDestroy();
    }

}
