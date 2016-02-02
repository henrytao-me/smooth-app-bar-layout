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

package me.henrytao.smoothappbarlayout.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrytao on 2/2/16.
 */
public class ListView extends android.widget.ListView {

  protected List<OnScrollChangeListener> mOnScrollListeners;

  public ListView(Context context) {
    super(context);
  }

  public ListView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public ListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
    if (mOnScrollListeners != null) {
      int i = 0;
      for (int n = mOnScrollListeners.size(); i < n; i++) {
        //mOnScrollListeners.get(i).onScrollChange(this, l, t, oldl, oldt);
      }
    }
  }

  public void addOnScrollListener(OnScrollChangeListener onScrollListener) {
    if (mOnScrollListeners == null) {
      mOnScrollListeners = new ArrayList<>();
    }
    mOnScrollListeners.add(onScrollListener);
  }
}
