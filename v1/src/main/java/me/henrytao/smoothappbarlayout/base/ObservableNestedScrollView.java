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

import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewTreeObserver;

import me.henrytao.smoothappbarlayoutdemo.R;

/**
 * Created by henrytao on 2/3/16.
 */
public class ObservableNestedScrollView implements Observer, NestedScrollView.OnScrollChangeListener {

  public static ObservableNestedScrollView newInstance(@NonNull NestedScrollView nestedScrollView, boolean overrideOnScrollListener,
      OnScrollListener onScrollListener) {
    ObservableNestedScrollView observable = new ObservableNestedScrollView(nestedScrollView, overrideOnScrollListener);
    observable.setOnScrollListener(onScrollListener);
    return observable;
  }

  private NestedScrollView mNestedScrollView;

  private OnScrollListener mOnScrollListener;

  private boolean mOverrideOnScrollListener;

  public ObservableNestedScrollView(@NonNull NestedScrollView nestedScrollView, boolean overrideOnScrollListener) {
    mNestedScrollView = nestedScrollView;
    mOverrideOnScrollListener = overrideOnScrollListener;
    if (mNestedScrollView.getTag(R.id.tag_observable_view) == null) {
      mNestedScrollView.setTag(R.id.tag_observable_view, true);
      init();
    }
  }

  @Override
  public View getView() {
    return mNestedScrollView;
  }

  @Override
  public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
    if (mOnScrollListener != null) {
      mOnScrollListener.onScrollChanged(mNestedScrollView,
          scrollX,
          scrollY,
          scrollX - oldScrollX,
          scrollY - oldScrollY,
          true);
    }
  }

  @Override
  public void setOnScrollListener(OnScrollListener onScrollListener) {
    mOnScrollListener = onScrollListener;
  }

  private void init() {
    if (mNestedScrollView instanceof me.henrytao.smoothappbarlayout.widget.NestedScrollView) {
      ((me.henrytao.smoothappbarlayout.widget.NestedScrollView) mNestedScrollView).addOnScrollListener(this);
    } else {
      if (mOverrideOnScrollListener) {
        mNestedScrollView.setOnScrollChangeListener(this);
      } else {
        mNestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
          @Override
          public void onScrollChanged() {
            if (mOnScrollListener != null) {
              int currentScrollX = mNestedScrollView.getScrollX();
              int currentScrollY = mNestedScrollView.getScrollY();
              mOnScrollListener.onScrollChanged(mNestedScrollView,
                  currentScrollX,
                  currentScrollY,
                  currentScrollX - Utils.parseInt(mNestedScrollView.getTag(R.id.tag_observable_view_last_scroll_x)),
                  currentScrollY - Utils.parseInt(mNestedScrollView.getTag(R.id.tag_observable_view_last_scroll_y)),
                  true);
              mNestedScrollView.setTag(R.id.tag_observable_view_last_scroll_x, currentScrollX);
              mNestedScrollView.setTag(R.id.tag_observable_view_last_scroll_y, currentScrollY);
            }
          }
        });
      }
    }
  }
}
