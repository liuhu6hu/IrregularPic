package com.product.yunhangce.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LicenseUtil {
    private static SweetAlertDialog dialog;

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
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



}
