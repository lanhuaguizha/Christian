/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.christian.search.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * General purpose utility methods for the Dialer.
 */
public class DialerUtils {
    public static void showInputMethod(View view) {
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    public static void hideInputMethod(View view) {
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
