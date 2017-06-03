package com.christian.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtils {

    /**
     * 之前显示的内容
     */
    private static String oldMsg;
    /**
     * Toast对象
     */
    private static Snackbar snackbar = null;
    /**
     * 第一次时间
     */
    private static long oneTime = 0;
    /**
     * 第二次时间
     */
    private static long twoTime = 0;

    /**
     * 显示Toast
     *
     */
    public static void showSnackbar(View view, String message) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Snackbar.LENGTH_SHORT) {
                    snackbar.show();
                }
            } else {
                oldMsg = message;
                snackbar.setText(message);
                snackbar.show();
            }
        }
        oneTime = twoTime;
    }

    public static void dismissSnackbar() {
        if (snackbar != null)
            snackbar.dismiss();
    }
}