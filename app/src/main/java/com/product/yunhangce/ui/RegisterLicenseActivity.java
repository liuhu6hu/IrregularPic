package com.product.yunhangce.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.product.yunhangce.MyApplication;
import com.product.yunhangce.R;
import com.product.yunhangce.http.CommonResponse;
import com.product.yunhangce.premission.MyPermissionCallback;
import com.product.yunhangce.util.LicenseConst;
import com.product.yunhangce.util.LicenseUtil;
import com.product.yunhangce.util.PhoneUtils;
import com.product.yunhangce.util.SpUtils;
import com.product.yunhangce.http.Response;
import com.product.yunhangce.http.RetrofitUtil;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionItem;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterLicenseActivity extends AppCompatActivity {

    private TextInputLayout tilPhone;
    private String deviceId;
    private String registerCode;
    private ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_license);
        initView();
    }

    private void initView() {
        tilPhone = (TextInputLayout) findViewById(R.id.til_phone);
    }

    long backTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - backTime > 2000) {
            backTime = currentTime;
            showToast("应用需要授权才可以使用");
        } else {
            finish();
        }

    }

    public void onClickRegister(View view) {

        ArrayList<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_contacts));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "存储空间", R.drawable.permission_ic_storage));
        HiPermission.create(this)
                .title("尊敬的用户")
                .msg("为了能够正常使用，请开启这些权限吧！")
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))//图标的颜色
                .permissions(permissionItems)
                .checkMutiPermission(new MyPermissionCallback() {
                    @Override
                    public void onClose() {
                        showToast("需要打开权限才可以使用本程序");
                    }

                    @Override
                    public void onFinish() {
                        registerDevice();
                    }
                });
    }

    private void registerDevice() {
        proDialog = android.app.ProgressDialog.show(this, "", "加载中...");
        proDialog.setCancelable(false);
        proDialog.show();
        deviceId = PhoneUtils.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = PhoneUtils.getIMEI();
        }
        if (TextUtils.isEmpty(deviceId)) {
            Toast.makeText(this, "不能获取到设备标识请打开权限", Toast.LENGTH_SHORT).show();
            return;
        }
        registerCode = tilPhone.getEditText().getText().toString();

        if (TextUtils.isEmpty(registerCode)) {
            showToast("请输入授权码");
            return;

        }
        RetrofitUtil.getInstance().registerLicense(deviceId, registerCode, new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                if (response.isSuccessful()) {
                    if (response.body().getCode() == 200) {
                        successRegister();
                        initData();
                    } else {
                        proDialog.dismiss();
                        showToast(response.body().getMessage());
                    }
                } else {
                    showToast("访问失败,请检查网络连接");
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                proDialog.dismiss();
                showToast("访问失败,请检查网络连接");
            }
        });
    }

    private static Toast toast;

    public void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(this,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }


    /**
     * save device id and license
     */
    private void successRegister() {
        SpUtils.getInstance().put(LicenseConst.KEY_DEVICE, deviceId);
        SpUtils.getInstance().put(LicenseConst.KEY_LICENSE, registerCode);
    }

    /**
     * check license is enable
     */
    private void initData() {
        if (LicenseUtil.licenseOk()) {
            String deviceId = SpUtils.getInstance().getString(LicenseConst.KEY_DEVICE);
            String license = SpUtils.getInstance().getString(LicenseConst.KEY_LICENSE);
            RetrofitUtil.getInstance().info(deviceId, license, new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                    proDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getCode() == 200) {
                            MyApplication.setCommonDate(response.body().getData());
                            startActivity(new Intent(RegisterLicenseActivity.this, Main2Activity.class));
                            finish();
                        } else {

                            LicenseUtil.showLicenseDialogActivity(RegisterLicenseActivity.this, response.body().getMessage());
                        }
                    } else {
                        LicenseUtil.showSimpleLicenseDialog(RegisterLicenseActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    if(proDialog!=null&&proDialog.isShowing()){
                        proDialog.dismiss();
                    }
                    LicenseUtil.showSimpleDialog(RegisterLicenseActivity.this, "初始化数据失败,请重新打开程序", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        LicenseUtil.dismissDialog();
        super.onDestroy();
    }
}
