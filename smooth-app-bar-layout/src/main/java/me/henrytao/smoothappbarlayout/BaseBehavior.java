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

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.henrytao.smoothappbarlayout.widget.NestedScrollView;

/**
 * Created by henrytao on 10/4/15.
 */
public abstract class BaseBehavior extends AppBarLayout.Behavior {

  protected abstract int getCurrentScrollOffset();

  protected abstract void onInit(CoordinatorLayout coordinatorLayout, AppBarLayout child);

  protected abstract void onScrollChanged(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dy);

  protected abstract void onSyncOffset(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target);

  protected abstract void onViewPagerSelected(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, ViewPager viewPager,
      int position);

  private static void log(String s, Object... args) {
    //Log.i("info", String.format("BaseBehavior %s", String.format(s, args)));
  }

  protected List<Long> mScrollTargets = new ArrayList<>();

  private boolean mIsOnInit = false;

  private boolean mIsPullDownFromTop;

  private OnOffsetSyncedListener mOnOffsetSyncedListener;

  private ViewPager.OnPageChangeListener mOnPageChangeListener;

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
    log("onNestedFling | %f | %f | %b", velocityX, velocityY, consumed);
    return true;
  }

  @Override
  public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
    log("onNestedPreFling | %f | %f", velocityX, velocityY);
    return false;
  }

  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
    log("onNestedPreScroll | %d | %d", dx, dy);
    if (vViewPager == null) {
      vScrollTarget = getScrollTarget(target);
    }
    onScrollChanged(coordinatorLayout, child);
    if (dy < 0 && mIsPullDownFromTop) {
      onScrollChanged(coordinatorLayout, child, vScrollTarget, dy);
    }
  }

  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed,
      int dxUnconsumed, int dyUnconsumed) {
    log("onNestedScroll | %d | %d | %d | %d", dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    if (dyUnconsumed < 0) {
      mIsPullDownFromTop = true;
    } else {
      mIsPullDownFromTop = false;
    }
  }

  @Override
  public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target,
      int nestedScrollAxes) {
    log("onNestedScrollAccepted | %d", nestedScrollAxes);
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target,
      int nestedScrollAxes) {
    log("onStartNestedScroll | %d", nestedScrollAxes);
    return true;
  }

  @Override
  public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target) {
    log("onStopNestedScroll");
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
    log("scrolling | %d", offset);
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
    return target;
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
    if (mOnOffsetSyncedListener == null && child instanceof SmoothAppBarLayout) {
      mOnOffsetSyncedListener = new OnOffsetSyncedListener() {
        @Override
        public void onOffsetSynced(AppBarLayout appBarLayout) {
          BaseBehavior.this.onSyncOffset(coordinatorLayout, child);
        }
      };
      ((SmoothAppBarLayout) child).addOnOffsetSyncedListener(mOnOffsetSyncedListener);
    }

    if (mOnPageChangeListener == null && child instanceof SmoothAppBarLayout) {
      mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
          BaseBehavior.this.onViewPagerSelected(coordinatorLayout, child, position);
        }
      };
      vViewPager = ((SmoothAppBarLayout) child).getViewPager();
      if (vViewPager != null) {
        vViewPager.addOnPageChangeListener(mOnPageChangeListener);
        onViewPagerSelected(coordinatorLayout, child, vViewPager.getCurrentItem());
      }
    }

    onInit(coordinatorLayout, child);
  }

  private void initNestedScrollView(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final NestedScrollView target) {
    log("initNestedScrollView");
    target.addOnScrollListener(new NestedScrollView.OnScrollListener() {
      @Override
      public void onScrolled(android.support.v4.widget.NestedScrollView nestedScrollView, int dx, int dy) {
        if (target == BaseBehavior.this.vScrollTarget) {
          BaseBehavior.this.onScrollChanged(coordinatorLayout, child, target, dy);
        }
      }
    });
  }

  private void initRecyclerView(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final RecyclerView target) {
    log("initRecyclerView");
    target.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (target == BaseBehavior.this.vScrollTarget) {
          BaseBehavior.this.onScrollChanged(coordinatorLayout, child, target, dy);
        }
      }
    });
  }

  private void initViewTreeObserver(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final View target) {
    log("initViewTreeObserver");
    target.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
      @Override
      public void onScrollChanged() {
        if (target == BaseBehavior.this.vScrollTarget) {
          BaseBehavior.this.onScrollChanged(coordinatorLayout, child, target, target.getScrollY() - getCurrentScrollOffset());
        }
      }
    });
  }

  private void onScrollChanged(CoordinatorLayout coordinatorLayout, AppBarLayout child) {
    log("onScrollChanged | %b", vScrollTarget != null);
    if (vScrollTarget != null) {
      long tag = getViewTag(vScrollTarget, true);
      if (!mScrollTargets.contains(tag)) {
        mScrollTargets.add(tag);
        if (vScrollTarget instanceof RecyclerView) {
          initRecyclerView(coordinatorLayout, child, (RecyclerView) vScrollTarget);
        } else if (vScrollTarget instanceof NestedScrollView) {
          initNestedScrollView(coordinatorLayout, child, (NestedScrollView) vScrollTarget);
        } else {
          initViewTreeObserver(coordinatorLayout, child, vScrollTarget);
        }
      }
    }
  }

  private void onSyncOffset(CoordinatorLayout coordinatorLayout, AppBarLayout child) {
    log("onSyncOffset | %b", vScrollTarget != null);
    if (vScrollTarget != null) {
      onSyncOffset(coordinatorLayout, child, vScrollTarget);
    }
  }

  private void onViewPagerSelected(CoordinatorLayout coordinatorLayout, AppBarLayout child, int position) {
    log("onSyncOffset | %b", vViewPager.getAdapter() instanceof PagerAdapter);
    if (vViewPager.getAdapter() instanceof PagerAdapter) {
      PagerAdapter adapter = (PagerAdapter) vViewPager.getAdapter();
      vScrollTarget = getScrollTarget(adapter.getScrollView(position));
      onViewPagerSelected(coordinatorLayout, child, vScrollTarget, vViewPager, position);
    }
  }
}
