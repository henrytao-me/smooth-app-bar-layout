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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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

  public static int parseInt(Object value) {
    if (value == null) {
      return 0;
    }
    return Integer.valueOf(value.toString());
  }

  public static void syncOffset(SmoothAppBarLayout smoothAppBarLayout, int verticalOffset, boolean isSelected, boolean isTop, View target) {
    if (target instanceof NestedScrollView) {
      if (isSelected && target.getScrollY() == 0) {
        smoothAppBarLayout.syncOffset(0);
      } else if (target.getScrollY() < verticalOffset || (isTop && !isSelected)) {
        target.scrollTo(0, verticalOffset);
      }
    } else if (target instanceof RecyclerView) {
      RecyclerView recyclerView = (RecyclerView) target;
      boolean isAccuracy = recyclerView.getLayoutManager().findViewByPosition(ObservableRecyclerView.HEADER_VIEW_POSITION) != null;
      int currentScrollY = recyclerView.computeVerticalScrollOffset();
      if (isAccuracy) {
        if (isSelected && currentScrollY == 0) {
          smoothAppBarLayout.syncOffset(0);
        } else if (currentScrollY < verticalOffset || (isTop && !isSelected)) {
          recyclerView.scrollTo(0, currentScrollY);
        }
      }
    }
  }
}
