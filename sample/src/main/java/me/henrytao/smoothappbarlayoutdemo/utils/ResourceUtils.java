/*
 * Copyright 2015 "Henry Tao <hi@henrytao.me>"
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

package me.henrytao.smoothappbarlayoutdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.DecimalFormat;

import me.henrytao.smoothappbarlayoutdemo.R;

/**
 * Created by henrytao on 5/10/15.
 */
public class ResourceUtils {

  public static void closeKeyBoard(Activity activity) {
    closeKeyBoard(activity, activity.getCurrentFocus());
  }

  public static void closeKeyBoard(Context context, View view) {
    if (view != null) {
      ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
          .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }

  public static int getActionBarSizeInPixel(Activity context) {
    return getDimensionPixelSizeFromAttribute(context, android.R.attr.actionBarSize);
  }

  public static int getColorFromAttribute(Context context, int attrId) {
    if (attrId == 0) {
      return 0;
    }
    TypedValue typedValue = new TypedValue();
    context.getTheme().resolveAttribute(attrId, typedValue, true);
    return typedValue.data;
  }

  public static int getDimensionPixelSize(Context context, int resId) {
    return context.getResources().getDimensionPixelSize(resId);
  }

  public static int getDimensionPixelSizeFromAttribute(Activity context, int attrId) {
    if (attrId == 0) {
      return 0;
    }
    TypedValue typedValue = new TypedValue();
    context.getTheme().resolveAttribute(attrId, typedValue, true);
    DisplayMetrics metrics = new DisplayMetrics();
    context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    return typedValue.complexToDimensionPixelSize(typedValue.data, metrics);
  }

  public static int getDrawableIdFromAttribute(Context context, int attrId) {
    if (attrId == 0) {
      return 0;
    }
    TypedValue typedValue = new TypedValue();
    context.getTheme().resolveAttribute(attrId, typedValue, true);
    return typedValue.resourceId;
  }

  public static float getFloatFromResource(Context context, int resId) {
    TypedValue outValue = new TypedValue();
    context.getResources().getValue(resId, outValue, true);
    return outValue.getFloat();
  }

  public static String getQuantityString(Resources res, int arrayId, int number) {
    return getQuantityString(res, arrayId, number, null);
  }

  public static String getQuantityString(Resources res, int arrayId, int number, DecimalFormat formatter) {
    try {
      String value = String.valueOf(number);
      if (formatter != null) {
        value = formatter.format(number);
      }
      String[] labels = res.getStringArray(arrayId);
      switch (number) {
        case 0:
        case 1:
          return String.format("%s %s", value, labels[0]);
        default:
          return String.format("%s %s", value, labels[1]);
      }
    } catch (Exception e) {
      return "";
    }
  }

  public static int getScreenHeight(Activity activity) {
    DisplayMetrics displaymetrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    return displaymetrics.heightPixels;
  }

  public static int getScreenWidth(Activity activity) {
    DisplayMetrics displaymetrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
    return displaymetrics.widthPixels;
  }

  public static int getSecondaryActionBarSizeInPixel(Activity context) {
    return getDimensionPixelSizeFromAttribute(context, R.attr.secondaryActionBarSize);
  }

  public static int getStatusBarHeight(Context context, boolean lollipopOnly) {
    if (lollipopOnly && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      return 0;
    }
    int result = 0;
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      result = context.getResources().getDimensionPixelSize(resourceId);
    }
    return result;
  }

  public static Uri getUriFromRawId(Context context, int rawId) {
    return Uri.parse("android.resource://" + context.getPackageName() + "/" + rawId);
  }

  public static View inflate(Context context, int layoutResId) {
    return inflate(context, layoutResId, null, false);
  }

  public static View inflate(Context context, int layoutResId, ViewGroup parent) {
    return inflate(context, layoutResId, parent, false);
  }

  public static View inflate(Context context, int layoutResId, ViewGroup root, boolean attachToRoot) {
    if (context instanceof Activity) {
      return ((Activity) context).getLayoutInflater().inflate(layoutResId, root, attachToRoot);
    }
    return null;
  }

  public static boolean isLandscapeMode(Context context) {
    if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      return true;
    }
    return false;
  }

  public static boolean isPortraitMode(Context context) {
    if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      return true;
    }
    return false;
  }

  public static void moveCursorToTheEnd(EditText editText) {
    int length = editText.getText().length();
    editText.setSelection(length, length);
  }

  public static void showKeyboard(Context context, EditText editText) {
    editText.requestFocus();
    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
  }
}
