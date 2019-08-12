package com.example.demo;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MyWindowManager {
    private static WindowManager mWindowManager;
    private static FloatSmallWindow smallWindow;
    private static WindowManager.LayoutParams layoutParams;

    public static void createWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (smallWindow == null) {
            smallWindow = new FloatSmallWindow(context);
            if (layoutParams == null) {
                layoutParams = new WindowManager.LayoutParams();
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                layoutParams.format = PixelFormat.RGBA_8888;
                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.x = screenWidth;
                layoutParams.y = screenHeight / 2;
            }
            smallWindow.setParams(layoutParams);
            windowManager.addView(smallWindow, layoutParams);
        }
    }

    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

}
