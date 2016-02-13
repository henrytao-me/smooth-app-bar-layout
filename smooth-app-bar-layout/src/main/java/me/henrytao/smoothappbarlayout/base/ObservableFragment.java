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

import android.view.View;

import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;

/**
 * Created by henrytao on 2/11/16.
 */
public interface ObservableFragment {

  /**
   * Get view that handles scrollEvent
   *
   * @return NestedScrollView or RecyclerView
   */
  View getScrollTarget();


  /**
   * Listener for offset changed event
   *
   * @param target         The view that is currently selected by ViewPager
   * @param verticalOffset Current vertical offset of SmoothAppBarLayout. It has the same value with smoothAppBarLayout.getCurrentOffset()
   * @return False if scroll hasn't been initiated or is waiting for async loading. If false, it will stop propagating ViewPager
   */
  boolean onOffsetChanged(SmoothAppBarLayout smoothAppBarLayout, View target, int verticalOffset);
}
