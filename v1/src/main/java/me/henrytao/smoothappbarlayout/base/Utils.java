/*
 * Copyright 2016 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.henrytao.smoothappbarlayout.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;

import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;

/**
 * Created by henrytao on 2/1/16.
 */
public class Utils {

  public static int getActionBarSize(Context context) {
    TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
    int size = (int) styledAttributes.getDimension(0, 0);
    styledAttributes.recycle();
    return size;
  }

  public static int getStatusBarSize(Context context) {
    int statusBarSize = 0;
    if (context != null) {
      int id = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
      if (id > 0) {
        statusBarSize = context.getResources().getDimensionPixelSize(id);
      }
    }
    return statusBarSize;
  }

  public static void log(String s, Object... args) {
    if (SmoothAppBarLayout.DEBUG) {
      Log.d("SmoothAppBarLayout", String.format(s, args));
    }
  }
}
