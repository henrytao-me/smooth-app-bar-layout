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

package me.henrytao.smoothappbarlayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.henrytao.smoothappbarlayout.base.ObservableNestedScrollView;
import me.henrytao.smoothappbarlayout.base.ObservableRecyclerView;
import me.henrytao.smoothappbarlayout.base.OnScrollListener;
import me.henrytao.smoothappbarlayout.base.ScrollTargetCallback;
import me.henrytao.smoothappbarlayout.base.Utils;

/**
 * Created by henrytao on 2/1/16.
 */
public abstract class BaseBehavior extends AppBarLayout.Behavior {

  protected abstract void onInit(CoordinatorLayout coordinatorLayout, AppBarLayout child);

  protected abstract void onScrollChanged(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
      int y, int dy, boolean accuracy);

  private DragCallback mDragCallbackListener;

  private boolean mIsOnInit = false;

  private boolean mIsPullDownFromTop;

  private boolean mOverrideOnScrollListener;

  private ScrollTargetCallback mScrollTargetCallback;

  private List<Long> mScrollTargets = new ArrayList<>();

  private View vScrollTarget;

  private ViewPager vViewPager;

  public BaseBehavior() {
  }

  public BaseBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onMeasureChild(CoordinatorLayout coordinatorLayout, AppBarLayout child, int parentWidthMeasureSpec, int widthUsed,
      int parentHeightMeasureSpec, int heightUsed) {
    if (!mIsOnInit && coordinatorLayout != null && child != null) {
      mIsOnInit = true;
      init(coordinatorLayout, child);
    }
    return super.onMeasureChild(coordinatorLayout, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
  }

  @Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
      float velocityX, float velocityY, boolean consumed) {
    Utils.log("onNestedFling | %f | %f | %b", velocityX, velocityY, consumed);
    return true;
  }

  @Override
  public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
    Utils.log("onNestedPreFling | %f | %f", velocityX, velocityY);
    return false;
  }

  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
    Utils.log("onNestedPreScroll | %d | %d", dx, dy);

    vScrollTarget = getScrollTarget(target);
    //if (vViewPager == null) {
    //  vScrollTarget = getScrollTarget(target);
    //}
    initScrollTarget(coordinatorLayout, child);

    if (dy < 0 && mIsPullDownFromTop) {
      onScrollChanged(coordinatorLayout, child, vScrollTarget, 0, dy, true);
    }
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed,
      int dxUnconsumed, int dyUnconsumed) {
    Utils.log("onNestedScroll | %d | %d | %d | %d", dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    if (dyUnconsumed < 0) {
      mIsPullDownFromTop = true;
    } else {
      mIsPullDownFromTop = false;
    }
  }

  @Override
  public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target,
      int nestedScrollAxes) {
    Utils.log("onNestedScrollAccepted | %d", nestedScrollAxes);
    onNestedPreScroll(coordinatorLayout, child, target, 0, 0, null);
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target,
      int nestedScrollAxes) {
    Utils.log("onStartNestedScroll | %d", nestedScrollAxes);
    return true;
  }

  @Override
  public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target) {
    Utils.log("onStopNestedScroll");
  }

  @Override
  public void setDragCallback(DragCallback callback) {
    super.setDragCallback(callback);
    mDragCallbackListener = callback;
  }

  public void setOverrideOnScrollListener(boolean overrideOnScrollListener) {
    mOverrideOnScrollListener = overrideOnScrollListener;
  }

  public void setScrollTargetCallback(ScrollTargetCallback scrollTargetCallback) {
    mScrollTargetCallback = scrollTargetCallback;
  }

  protected void dispatchOffsetUpdates(AppBarLayout layout, int translationOffset) {
    if (layout instanceof SmoothAppBarLayout) {
      List listeners = ((SmoothAppBarLayout) layout).mOffsetChangedListeners;
      int i = 0;
      for (int z = listeners.size(); i < z; ++i) {
        WeakReference ref = (WeakReference) listeners.get(i);
        AppBarLayout.OnOffsetChangedListener listener = ref != null ? (AppBarLayout.OnOffsetChangedListener) ref.get() : null;
        if (listener != null) {
          listener.onOffsetChanged(layout, translationOffset);
        }
      }
    }
  }

  protected void scrolling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int offset) {
    Utils.log("scrolling | %d", offset);
    setTopAndBottomOffset(offset);
    if (child instanceof SmoothAppBarLayout && ((SmoothAppBarLayout) child).mHaveChildWithInterpolator) {
      coordinatorLayout.dispatchDependentViewsChanged(child);
    }
    dispatchOffsetUpdates(child, offset);
  }

  private View getScrollTarget(View target) {
    if (target instanceof SwipeRefreshLayout && ((SwipeRefreshLayout) target).getChildCount() > 0) {
      return ((SwipeRefreshLayout) target).getChildAt(0);
    }
    return mScrollTargetCallback != null ? mScrollTargetCallback.callback(target) : target;
  }

  private long getViewTag(View target, boolean createIfNotExist) {
    if (target == null) {
      return 0;
    }
    Object tag = target.getTag(R.id.tag_view_target);
    if (tag == null) {
      if (!createIfNotExist) {
        return 0;
      }
      tag = System.currentTimeMillis();
      target.setTag(R.id.tag_view_target, tag);
    }
    return (long) tag;
  }

  private void init(final CoordinatorLayout coordinatorLayout, final AppBarLayout child) {
    // disable dragCallback by default
    if (mDragCallbackListener == null) {
      mDragCallbackListener = new DragCallback() {

        @Override
        public boolean canDrag(AppBarLayout appBarLayout) {
          return false;
        }
      };
      setDragCallback(mDragCallbackListener);
    }

    if (child instanceof SmoothAppBarLayout) {
      vViewPager = ((SmoothAppBarLayout) child).getViewPager();
    }

    // dispatch init event
    Utils.log("onInit");
    onInit(coordinatorLayout, child);
  }

  private void initScrollTarget(final CoordinatorLayout coordinatorLayout, final AppBarLayout child) {
    Utils.log("initScrollTarget | %b", vScrollTarget != null);
    if (vScrollTarget != null) {
      long tag = getViewTag(vScrollTarget, true);
      if (!mScrollTargets.contains(tag)) {
        mScrollTargets.add(tag);
        OnScrollListener listener = new OnScrollListener() {

          @Override
          public void onScrollChanged(View view, int x, int y, int dx, int dy, boolean accuracy) {
            if (view == vScrollTarget) {
              BaseBehavior.this.onScrollChanged(coordinatorLayout, child, view, y, dy, accuracy);
            }
          }
        };
        if (vScrollTarget instanceof NestedScrollView) {
          ObservableNestedScrollView.newInstance((NestedScrollView) vScrollTarget, mOverrideOnScrollListener, listener);
        } else if (vScrollTarget instanceof RecyclerView) {
          ObservableRecyclerView.newInstance((RecyclerView) vScrollTarget, listener);
        }
      }
    }
  }
}
