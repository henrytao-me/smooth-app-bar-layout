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

package me.henrytao.smoothappbarlayout;

import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by henrytao on 10/4/15.
 */
public class ScrollFlag {

  private int mFlags;

  private View vView;

  public ScrollFlag(AppBarLayout layout) {
    if (layout != null) {
      int i = 0;
      for (int z = layout.getChildCount(); i < z; ++i) {
        View child = layout.getChildAt(i);
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        if (layoutParams instanceof AppBarLayout.LayoutParams) {
          AppBarLayout.LayoutParams childLp = (AppBarLayout.LayoutParams) layoutParams;
          int flags = childLp.getScrollFlags();
          if ((flags & AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
            vView = child;
            mFlags = flags;
            break;
          }
        }
      }
    }
  }

  public View getView() {
    return vView;
  }

  public boolean isFlagEnterAlwaysCollapsedEnabled() {
    if (vView != null && (mFlags & AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS) != 0) {
      return true;
    }
    return false;
  }

  public boolean isFlagEnterAlwaysEnabled() {
    if (vView != null && (mFlags & AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED) != 0) {
      return true;
    }
    return false;
  }

  public boolean isFlagExitUntilCollapsedEnabled() {
    if (vView != null && (mFlags & AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
      return true;
    }
    return false;
  }

  public boolean isFlagScrollEnabled() {
    if (vView != null && (mFlags & AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
      return true;
    }
    return false;
  }

  public boolean isQuickReturnEnabled() {
    return isFlagEnterAlwaysEnabled() && isFlagEnterAlwaysCollapsedEnabled();
  }
}
