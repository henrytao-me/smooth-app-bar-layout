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

package me.henrytao.smoothappbarlayout.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrytao on 10/1/15.
 */
public class NestedScrollView extends android.support.v4.widget.NestedScrollView {

  protected List<OnScrollListener> mOnScrollListeners;

  public NestedScrollView(Context context) {
    super(context);
  }

  public NestedScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public NestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
    if (mOnScrollListeners != null) {
      int i = 0;
      for (int n = mOnScrollListeners.size(); i < n; i++) {
        mOnScrollListeners.get(i).onScrolled(this, l - oldl, t - oldt);
      }
    }
  }

  public void addOnScrollListener(OnScrollListener onScrollListener) {
    if (mOnScrollListeners == null) {
      mOnScrollListeners = new ArrayList<>();
    }
    mOnScrollListeners.add(onScrollListener);
  }

  public interface OnScrollListener {

    void onScrolled(android.support.v4.widget.NestedScrollView nestedScrollView, int dx, int dy);
  }
}
