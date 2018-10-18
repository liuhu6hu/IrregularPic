package com.product.yunhangce.listener;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.product.yunhangce.R;
import com.product.yunhangce.ui.BaseDialog;

public class MyCustomVersionDialogListener implements CustomVersionDialogListener {
    @Override
    public Dialog getCustomVersionDialog(Context context, UIData versionBundle) {
        BaseDialog baseDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_two_layout);
        TextView textView = baseDialog.findViewById(R.id.tv_msg);
        textView.setText(versionBundle.getContent());
        TextView tvTitle = baseDialog.findViewById(R.id.tv_title);
        textView.setText(versionBundle.getContent());
        tvTitle.setText(versionBundle.getTitle());
        baseDialog.setCanceledOnTouchOutside(false);
        return baseDialog;
    }
}
