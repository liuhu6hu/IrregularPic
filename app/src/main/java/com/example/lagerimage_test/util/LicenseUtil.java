package com.example.lagerimage_test.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.lagerimage_test.ui.RegisterLicenseActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LicenseUtil {
    private static SweetAlertDialog dialog;

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static boolean licenseOk() {
        String deviceId = SpUtils.getInstance().getString(LicenseConst.KEY_DEVICE);
        String license = SpUtils.getInstance().getString(LicenseConst.KEY_LICENSE);
        if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(license)) {
            return false;
        }
        return true;
    }

    /**
     * show Dialog for start register activity
     * if  click ok  , {@showLicenseDialogActivity(Context context, Sting msg)}
     *
     * @param context
     */
    public static SweetAlertDialog showSimpleLicenseDialog(Activity context) {
        return showLicenseDialogActivity(context, "应用需要授权");
    }

    /**
     * show Dialog for start register activity
     *
     * @param context
     */
    public static SweetAlertDialog showSimpleDialog(final Activity context, String msg, SweetAlertDialog.OnSweetClickListener listener) {
        dismissDialog();
        dialog = new SweetAlertDialog(context)
                .setTitleText("提示")
                .setContentText(msg)
                .setConfirmClickListener(listener)
                .setConfirmText("确定");
        dialog.show();
        return dialog;
    }

    /**
     * show Dialog for start register activity
     *
     * @param context
     */
    public static SweetAlertDialog showCustomerDialog(final Activity context, int alertType) {
        dismissDialog();
        dialog = new SweetAlertDialog(context, alertType);
        dialog.show();
        return dialog;
    }

    /**
     * show Dialog for start register activity
     *
     * @param context
     */
    public static SweetAlertDialog showCustomerDialog(final Activity context) {
        dismissDialog();
        dialog = new SweetAlertDialog(context);
        dialog.show();
        return dialog;
    }

    /**
     * show Dialog for start register activity
     * if  click ok  , {@startRegister(Context context)}
     *
     * @param context
     */
    public static SweetAlertDialog showLicenseDialogActivity(final Activity context, String msg) {
        SweetAlertDialog.OnSweetClickListener listener = new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                startRegister(context);
                context.finish();
            }

        };
        return showSimpleDialog(context, msg, listener);
    }

    /**
     * start RegisterLicenseActivity to register license
     *
     * @param context
     */
    public static void startRegister(Context context) {
        Intent intent = new Intent(context, RegisterLicenseActivity.class);
        context.startActivity(intent);
    }
}
