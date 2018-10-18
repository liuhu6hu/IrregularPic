package com.product.yunhangce.listener;

import android.graphics.Bitmap;

public interface CropImageListener {

    void onStart(String path);

    void onGenerateResult(Bitmap mLineBitmap, String path);

}
