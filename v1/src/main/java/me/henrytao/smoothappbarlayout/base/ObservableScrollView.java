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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import me.henrytao.smoothappbarlayoutdemo.R;

/**
 * Created by henrytao on 2/3/16.
 */
public class ObservableScrollView implements Observer {

  public static ObservableScrollView newInstance(@NonNull ScrollView scrollView, OnScrollListener onScrollListener) {
    ObservableScrollView observable = new ObservableScrollView(scrollView);
    observable.setOnScrollListener(onScrollListener);
    return observable;
  }

  private OnScrollListener mOnScrollListener;

  private ScrollView mScrollView;

  public ObservableScrollView(@NonNull ScrollView scrollView) {
    mScrollView = scrollView;
    if (mScrollView.getTag(R.id.tag_observable_view) == null) {
      mScrollView.setTag(R.id.tag_observable_view, true);
      init();
    }
  }

  @Override
  public View getView() {
    return mScrollView;
  }

  @Override
  public void setOnScrollListener(OnScrollListener onScrollListener) {
    mOnScrollListener = onScrollListener;
  }

  private void init() {
    mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
      @Override
      public void onScrollChanged() {
        if (mOnScrollListener != null) {
          int currentScrollX = mScrollView.getScrollX();
          int currentScrollY = mScrollView.getScrollY();
          mOnScrollListener.onScrollChanged(mScrollView,
              currentScrollX,
              currentScrollY,
              currentScrollX - (Integer) mScrollView.getTag(R.id.tag_observable_view_last_scroll_x),
              currentScrollY - (Integer) mScrollView.getTag(R.id.tag_observable_view_last_scroll_y),
              true);
        }
      }
    });
  }
}
