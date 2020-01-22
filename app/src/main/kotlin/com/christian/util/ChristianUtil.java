package com.christian.util;
/*
 * Copyright 2015 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.text.Html;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.christian.R;
import com.christian.nav.NavActivity;
import com.vincent.blurdialog.BlurDialog;
import com.vincent.blurdialog.listener.OnItemClick;
import com.vincent.blurdialog.listener.OnNegativeClick;
import com.vincent.blurdialog.listener.OnPositiveClick;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ren.qinc.markdowneditors.base.BaseApplication;
import ren.qinc.markdowneditors.view.EditorActivity;
import ren.qinc.markdowneditors.view.MainActivity;

import static com.vincent.blurdialog.BlurDialog.TYPE_SINGLE_SELECT;

public class ChristianUtil {

    public static final String DOCUMENT_GOSPEL_PATH = "DOCUMENT_GOSPEL_PATH";
    private static BlurDialog dialog;


    // 反射强势访问修改final变量
    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("accessFlags");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            field.set(BaseApplication.context.getSystemService(Context.USER_SERVICE), newValue);
        }
//        field.set(null, newValue);
    }

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }


    public static String getDateAndCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

//You can change "yyyyMMdd_HHmmss as per your requirement

        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
    }

    public static BlurDialog showWaitingDialog(EditorActivity editorActivity) {
        BlurDialog dialog = new BlurDialog.Builder()
                .isCancelable(true)
                .isOutsideCancelable(true)
                .message("Please wait...")
                .type(BlurDialog.TYPE_WAIT)
                .build(editorActivity);
        dialog.show();
        return dialog;
    }


    public static BlurDialog showListDialog(@NonNull Activity activity, String tag) {
        final List<CharSequence> list = new ArrayList<>();
        list.clear();
        if (tag.equals("activity_gospel")) {
            list.add(Html.fromHtml(activity.getString(R.string.share)));
            list.add(Html.fromHtml(activity.getString(R.string.favorite)));
        } else {
            list.add(Html.fromHtml(activity.getString(R.string.translate)));
            list.add(Html.fromHtml(activity.getString(R.string.read)));
        }

        dialog = new BlurDialog.Builder()
                .isCancelable(true)
                .isOutsideCancelable(true)
                .message("Messi the best football player")
                .singleList(list)
                .itemClick(new OnItemClick() {
                    @Override
                    public void onClick(CharSequence item) {
                        if (item.equals(list.get(0))) {
                        }

                        if (item.equals(list.get(1))) {
                        }

                        if (item.equals(list.get(2))) {
                        }

                        if (item.equals(list.get(3))) {
                        }
                    }
                })
                .negativeClick(new OnNegativeClick() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                })
                .type(TYPE_SINGLE_SELECT)
                .build(activity);
        dialog.show();
        return dialog;
    }
}
