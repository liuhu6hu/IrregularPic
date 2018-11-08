package com.product.yunhangce.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.callback.OnCancelListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.product.yunhangce.MyApplication;
import com.product.yunhangce.R;
import com.product.yunhangce.http.RetrofitUtil;
import com.product.yunhangce.http.VersionResponse;
import com.product.yunhangce.listener.MyCustomVersionDialogListener;
import com.product.yunhangce.util.AppUtils;
import com.product.yunhangce.util.ImgUtils;
import com.product.yunhangce.util.LicenseConst;
import com.product.yunhangce.util.LicenseUtil;
import com.product.yunhangce.util.SpUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.shizhefei.view.largeimage.Area;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.InputStreamBitmapDecoderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main2Activity extends AppCompatActivity {
    LargeImageView largeImageView;

    private final static String[] UNITS = {"平方米", "平方千米", "亩", "公顷"};

    EditText height;
//    EditText angle;

    Spinner cmos;

    private String cmosValue = "1/2.3";
    private double fov = 0;
    private LinearLayout support;
    private LinearLayout updateApp;
    private TextView closeApp;
    private TextView unRegister;
    private LinearLayout aboutApp;
    private DrawerLayout draw;
    private RelativeLayout drawLeft;
    private DownloadBuilder builder;
    private SweetAlertDialog pDialog;
    private String path;
    private TextView licenseInfo;
    private TextView licenseDate;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        if (!LicenseUtil.licenseOk()) {
            LicenseUtil.showSimpleLicenseDialog(this);
        }
        largeImageView = findViewById(R.id.imageView);
        try {
            largeImageView.setImage(new InputStreamBitmapDecoderFactory(getAssets().open("bg.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        height = findViewById(R.id.height);
        height.setText("100");

//        angle = findViewById(R.id.angle);
        cmos = findViewById(R.id.cmos);

        cmos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        cmosValue = "1/2.3";
                        fov = 81.9;
                        break;
                    case 1:
                        cmosValue = "1/2.3";
                        fov = 78.8;
                        break;
                    case 2:
                        cmosValue = "3/3";
                        fov = 84;
                        break;
                    case 3:
                        cmosValue = "1/2.8";
                        fov = 63.7;
                        break;
                    case 4:
                        cmosValue = "4/3";
                        fov = 72;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initView() {
        draw = (DrawerLayout) findViewById(R.id.draw);
        drawLeft = (RelativeLayout) findViewById(R.id.draw_left_layout);
        licenseInfo = (TextView) findViewById(R.id.license_info);
        licenseDate = (TextView) findViewById(R.id.license_date);
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTop);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, draw, toolbarTop, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        draw.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();//状态
        support = (LinearLayout) findViewById(R.id.support);
        updateApp = (LinearLayout) findViewById(R.id.update_app);
        aboutApp = (LinearLayout) findViewById(R.id.about_app);
        closeApp = (TextView) findViewById(R.id.close_app);
        unRegister = (TextView) findViewById(R.id.un_register);
        support.setOnClickListener(listener);
        updateApp.setOnClickListener(listener);
        aboutApp.setOnClickListener(listener);
        closeApp.setOnClickListener(listener);
        unRegister.setOnClickListener(listener);
        drawLeft.setOnClickListener(listener);
        licenseInfo.setText(MyApplication.getCommonDate().getLicenseInfo());
        licenseDate.setText(MyApplication.getCommonDate().getLicenseExpireInfo());
    }

    public void onClick(View v) {


        new AlertDialog.Builder(this)
                .setTitle("选择单位")
                .setItems(UNITS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String h = height.getText().toString();
//                        String angleText =   fov;//angle.getText().toString();
                        Long aLong;
                        double a;
                        try {
                            aLong = Long.valueOf(h);
                        } catch (Exception e) {
                            e.printStackTrace();
                            aLong = 100L;
                            h = "100";
                        }

                        try {
                            a = fov;
                        } catch (Exception e) {
                            a = 0;
                        }


                        dialog.dismiss();
                        try {


                            Area area = largeImageView.getArea(cmosValue, aLong, a);
                            switch (which) {
                                case 1:
                                    area.setAreaRect(area.getAreaRect() * 1e-6);
                                    break;
                                case 2:
                                    area.setAreaRect(area.getAreaRect() * 0.0015);
                                    break;
                                case 3:
                                    area.setAreaRect(area.getAreaRect() * 0.0001);
                                    break;
                            }
                            Log.v("fov", String.valueOf(fov));
                            String contentFormat = ""
//                                    + "图片宽度（按像素记）：%s \n"
//                                    + "图片高度（按像素记）：%s \n"
//                                    + "选中点 ： %s \n"
//                                    + "高度：%s \n"
                                    + "当前选中区域面积为 %.2f%s";

                            new AlertDialog.Builder(Main2Activity.this)
                                    .setTitle("提示")
                                    .setMessage(String.format(Locale.CHINA, contentFormat,
//                                            area.getImageWidth(),
//                                            area.getImageHeight(),
//                                            Utils.toString(area.getPosList()),
//                                            h,
                                            area.getAreaRect(),
                                            UNITS[which]))
                                    .setPositiveButton("确定", null)
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();

                            Toast.makeText(Main2Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }
                }).show();

    }

    public void onClick3(View view) {
        largeImageView.removeLastPoint();
    }

    public void onClick2(View view) {
        largeImageView.clearAllPoint();
    }

    public void cropImage(View view) {

        ImgUtils.saveImageToGallery(this, convertViewToBitmap(largeImageView));
        // it for crop ，
//        Intent intent = new Intent(Main2Activity.this, CropImageActivity.class);
//        intent.putExtra("path", path);
//        intent.putExtra("point", largeImageView.getAllPoint());
//        startActivity(intent);
    }

    public void onClick4(View view) {
        //选择图片

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .previewImage(true)
                .isCamera(false)
                .maxSelectNum(1)
                .forResult(0x100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x100 && resultCode == RESULT_OK) {
            List<LocalMedia> media =
                    PictureSelector.obtainMultipleResult(data);

            if (media != null && media.size() > 0) {
                LocalMedia media1 = media.get(0);
                path = media1.getPath();
                try {
                    largeImageView.setImage(new InputStreamBitmapDecoderFactory(new FileInputStream(new File(path))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.support) {
                draw.closeDrawers();
                Intent intent = new Intent(Main2Activity.this, SupportActivity.class);
                startActivity(intent);
            } else if (id == R.id.about_app) {
                draw.closeDrawers();
                Intent intent = new Intent(Main2Activity.this, AboutActivity.class);
                startActivity(intent);
//
//                new SweetAlertDialog(Main2Activity.this)
//                        .setTitleText("关于")
//                        .setContentText("云航测授权给xx使用，x、xxx、 xxx、 xx、xx、 xx、xxx、xx、xxx、xxxxxxx、xxx、、等。。。")
//                        .setConfirmText(getString(R.string.yes))
//                        .show();
            } else if (id == R.id.close_app) {
                closeApp();
            } else if (id == R.id.un_register) {
                unRegister();
            } else if (id == R.id.update_app) {
                draw.closeDrawers();
                checkVersion();
            }
        }
    };

    private void closeApp() {
       pDialog= LicenseUtil. showCustomerDialog(Main2Activity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.are_you_sure))
                .setContentText(getString(R.string.exist_app))
                .setConfirmText(getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Main2Activity.this.finish();
                    }
                })
                .setCancelText(getString(R.string.no));
        pDialog  .show();
    }

    private void unRegister() {
        pDialog =  LicenseUtil. showCustomerDialog(Main2Activity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.are_you_sure))
                .setContentText(getString(R.string.unregister_exist))
                .setConfirmText(getString(R.string.yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        SpUtils.getInstance().put(LicenseConst.KEY_DEVICE, "");
                        SpUtils.getInstance().put(LicenseConst.KEY_LICENSE, "");
                        Main2Activity.this.finish();
                    }
                })
                .setCancelText(getString(R.string.no));
        pDialog.show();
    }

    private void checkVersion() {
        pDialog =  LicenseUtil.showCustomerDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(getString(R.string.version_loading_version));
        pDialog.setCancelable(false);
        pDialog.show();
        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl(RetrofitUtil.APP_VERSION)
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        Gson gson = new Gson();
                        pDialog.dismiss();
                        VersionResponse versionData = gson.fromJson(result, VersionResponse.class);
                        if (versionData.getData() != null && versionData.getData().getVersionCode() != AppUtils.getAppVersionCode()) {
                            return crateUIData(versionData.getData().getApkUrl(), getString(R.string.new_version) + versionData.getData().getVersionName(), versionData.getData().getContext());
                        } else {
                            if (versionData.getData() == null) {
                                Toast.makeText(Main2Activity.this, versionData.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Main2Activity.this, getString(R.string.last_version_hint), Toast.LENGTH_SHORT).show();
                            }
                            return null;
                        }
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        pDialog.dismiss();
                        Toast.makeText(Main2Activity.this, R.string.version_loading_fail, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setForceUpdateListener(new ForceUpdateListener() {
            @Override
            public void onShouldForceUpdate() {
                Toast.makeText(Main2Activity.this, "请更新到最新版本", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
            }
        });
        builder.setCustomVersionDialogListener(new MyCustomVersionDialogListener());
        builder.setForceRedownload(true);
        builder.excuteMission(this);
    }


    private UIData crateUIData(String apkUrl, String title, String context) {
        UIData uiData = UIData.create();
        uiData.setTitle(title);
        uiData.setDownloadUrl(apkUrl);
        uiData.setContent(context);
        return uiData;
    }

    public Bitmap convertViewToBitmap(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;

    }

    @Override
    protected void onDestroy() {
        LicenseUtil.dismissDialog();
        super.onDestroy();
    }
}
