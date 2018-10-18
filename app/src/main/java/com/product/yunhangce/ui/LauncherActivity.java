package com.product.yunhangce.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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
import com.product.yunhangce.http.CommonResponse;
import com.product.yunhangce.http.VersionResponse;
import com.product.yunhangce.listener.MyCustomVersionDialogListener;
import com.product.yunhangce.premission.MyPermissionCallback;
import com.product.yunhangce.util.AppUtils;
import com.product.yunhangce.util.LicenseConst;
import com.product.yunhangce.util.LicenseUtil;
import com.product.yunhangce.util.NetworkUtils;
import com.product.yunhangce.util.SpUtils;
import com.product.yunhangce.http.RetrofitUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionItem;
import retrofit2.Call;
import retrofit2.Callback;

public class LauncherActivity extends AppCompatActivity {

    private SweetAlertDialog.OnSweetClickListener listener = new SweetAlertDialog.OnSweetClickListener() {
        @Override
        public void onClick(SweetAlertDialog dialog) {
            NetworkUtils.openWirelessSettings();
        }
    };
    private TextView licenseDate;
    private boolean licenseOk;
    private boolean waitOk;
    private DownloadBuilder builder;
    private boolean cancelCheckVersion;
    private MyCustomVersionDialogListener dialog;
    private boolean isRunning =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        licenseDate = (TextView) findViewById(R.id.license_date);

    }

    @Override
    protected void onResume() {
        super.onResume();
        permission();
    }

    /**
     * check license is enable
     */
    private void initInfo() {
        if (LicenseUtil.licenseOk()) {
            String deviceId = SpUtils.getInstance().getString(LicenseConst.KEY_DEVICE);
            String license = SpUtils.getInstance().getString(LicenseConst.KEY_LICENSE);
            RetrofitUtil.getInstance().info(deviceId, license, new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode() == 200) {
                            licenseOk = true;
                            MyApplication.setCommonDate(response.body().getData());
                            startMainActivity();
                        } else {
                            LicenseUtil.showLicenseDialogActivity(LauncherActivity.this, response.body().getMessage());
                        }
                    } else {
                        LicenseUtil.showSimpleLicenseDialog(LauncherActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    LicenseUtil.showSimpleLicenseDialog(LauncherActivity.this);
                }
            });
        }
    }

    /**
     * check permissions is ok
     */
    private void permission() {
        ArrayList<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_contacts));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储空间", R.drawable.permission_ic_storage));
        HiPermission.create(this)
                .title("亲爱的用户")
                .msg("为了能够正常使用，请开启这些权限吧！")
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))//图标的颜色
                .permissions(permissionItems)
                .checkMutiPermission(new MyPermissionCallback() {
                    @Override
                    public void onClose() {
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        if (NetworkUtils.isConnected()) {
                            checkVersion();
                        } else {
                            LicenseUtil.showSimpleDialog(LauncherActivity.this, "网络连接失败请，去打开网络", listener);
                        }
                    }
                });
    }

    public void lunch() {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!isRunning){
                            return;
                        }
                        if (LicenseUtil.licenseOk()) {
                            waitOk = true;
                            startMainActivity();
                        } else {
                            LicenseUtil.showSimpleLicenseDialog(LauncherActivity.this);

                        }
                    }
                }, 2000);
    }


    public void startMainActivity() {

        if (licenseOk && waitOk) {
            startActivity(new Intent(LauncherActivity.this, Main2Activity.class));
            finish();
        }
    }

    private void startInitData() {

        if (NetworkUtils.isConnected()) {
            initInfo();
            lunch();
        } else {
            LicenseUtil.showSimpleDialog(LauncherActivity.this, "网络连接失败请，去打开网络", listener);
        }
    }

    private void checkVersion() {
        if (cancelCheckVersion) {
            startInitData();
            return;
        }
        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl(RetrofitUtil.APP_VERSION)
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        Gson gson = new Gson();
                        VersionResponse versionData = gson.fromJson(result, VersionResponse.class);
                        if (versionData.getData() != null && versionData.getData().getVersionCode() != AppUtils.getAppVersionCode()) {
                            return crateUIData(versionData.getData().getApkUrl(), getString(R.string.new_version) + versionData.getData().getVersionName(), versionData.getData().getContext());
                        } else {
                            if (versionData.getData() == null) {
                                Toast.makeText(LauncherActivity.this, versionData.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            startInitData();
                            return null;
                        }
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {
                        startInitData();
                    }
                });
        builder.setForceUpdateListener(new ForceUpdateListener() {
            @Override
            public void onShouldForceUpdate() {
                cancelCheckVersion = true;
                Toast.makeText(LauncherActivity.this, "请更新到最新版本", Toast.LENGTH_SHORT).show();
            }
        });

//        builder.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel() {
//                if (NetworkUtils.isConnected()) {
//                    checkVersion();
//                } else {
//                    LicenseUtil.showSimpleDialog(LauncherActivity.this, "网络连接失败请，去打开网络", listener);
//                }
//            }
//        });
        dialog=new MyCustomVersionDialogListener();
        builder.setCustomVersionDialogListener(dialog);
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

    @Override
    protected void onDestroy() {
        isRunning=false;
        LicenseUtil.dismissDialog();
        super.onDestroy();
    }
}
