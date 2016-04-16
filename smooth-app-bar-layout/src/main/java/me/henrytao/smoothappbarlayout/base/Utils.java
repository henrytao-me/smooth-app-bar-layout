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

  public static void intPreScroll(final SmoothAppBarLayout smoothAppBarLayout, final View target, final int offset) {
    target.addOnLayoutChangeListener(new OnPreScrollListener(smoothAppBarLayout, target, offset));
  }

  public static boolean isScrollToTop(View target) {
    if (target instanceof NestedScrollView) {
      return target.getScrollY() == 0;
    } else if (target instanceof RecyclerView) {
      return ((RecyclerView) target).getLayoutManager().findViewByPosition(ObservableRecyclerView.HEADER_VIEW_POSITION) != null
          && ((RecyclerView) target).computeVerticalScrollOffset() == 0;
    }
    return true;
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

  public static boolean syncOffset(SmoothAppBarLayout smoothAppBarLayout, View target, int verticalOffset, View scroll) {
    boolean isSelected = target == scroll;
    if (scroll instanceof NestedScrollView) {
      NestedScrollView nestedScrollView = (NestedScrollView) scroll;
      if (nestedScrollView.getScrollY() < verticalOffset || (!isSelected && isScrollToTop(target))) {
        nestedScrollView.scrollTo(0, verticalOffset);
      }
      if (isSelected && (nestedScrollView.getScrollY() < verticalOffset || verticalOffset == 0)) {
        nestedScrollView.scrollTo(0, 0);
        smoothAppBarLayout.syncOffset(0);
      }
    } else if (scroll instanceof RecyclerView) {
      RecyclerView recyclerView = (RecyclerView) scroll;
      boolean isAccuracy = recyclerView.getLayoutManager().findViewByPosition(ObservableRecyclerView.HEADER_VIEW_POSITION) != null;
      if (isAccuracy && recyclerView.computeVerticalScrollOffset() < verticalOffset) {
        recyclerView.scrollBy(0, verticalOffset - recyclerView.computeVerticalScrollOffset());
      } else if (!isSelected && isScrollToTop(target)) {
        recyclerView.scrollToPosition(ObservableRecyclerView.HEADER_VIEW_POSITION);
      }
      if (isAccuracy && isSelected && (recyclerView.computeVerticalScrollOffset() < verticalOffset || verticalOffset == 0)) {
        recyclerView.scrollToPosition(ObservableRecyclerView.HEADER_VIEW_POSITION);
        smoothAppBarLayout.syncOffset(0);
      }
    }
    return true;
  }

  private static class OnPreScrollListener implements View.OnLayoutChangeListener {

    private final int mOffset;

    private final SmoothAppBarLayout vSmoothAppBarLayout;

    private final View vTarget;

    private boolean mDone;

    public OnPreScrollListener(SmoothAppBarLayout smoothAppBarLayout, View target, int offset) {
      vSmoothAppBarLayout = smoothAppBarLayout;
      vTarget = target;
      mOffset = offset;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
      int currentOffset = 0;
      if (!mDone) {
        if (v instanceof NestedScrollView) {
          currentOffset = v.getScrollY();
        } else if (v instanceof RecyclerView) {
          currentOffset = ((RecyclerView) v).computeVerticalScrollOffset();
        }
        if (currentOffset < mOffset) {
          vTarget.scrollBy(0, mOffset - currentOffset);
        } else {
          vSmoothAppBarLayout.syncOffset(mOffset);
          mDone = true;
        }
      }
    }
  }
}
