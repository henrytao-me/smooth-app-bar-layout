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
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrytao on 9/22/15.
 */
@CoordinatorLayout.DefaultBehavior(SmoothAppBarLayout.Behavior.class)
public class SmoothAppBarLayout extends AppBarLayout {

  private static void log(String s, Object... args) {
    Log.i("info", String.format(s, args));
  }

  protected final List<WeakReference<OnOffsetChangedListener>> mListeners;

  protected boolean mHaveChildWithInterpolator;

  public SmoothAppBarLayout(Context context) {
    this(context, null);
  }

  public SmoothAppBarLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mListeners = new ArrayList();
  }

  @Override
  public void addOnOffsetChangedListener(OnOffsetChangedListener listener) {
    super.addOnOffsetChangedListener(listener);
    int i = 0;
    for (int z = this.mListeners.size(); i < z; ++i) {
      WeakReference ref = (WeakReference) this.mListeners.get(i);
      if (ref != null && ref.get() == listener) {
        return;
      }
    }
    this.mListeners.add(new WeakReference(listener));
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    int i = 0;
    for (int z = this.getChildCount(); i < z; ++i) {
      View child = this.getChildAt(i);
      AppBarLayout.LayoutParams childLp = (AppBarLayout.LayoutParams) child.getLayoutParams();
      Interpolator interpolator = childLp.getScrollInterpolator();
      if (interpolator != null) {
        mHaveChildWithInterpolator = true;
        break;
      }
    }
  }

  public static class Behavior extends AppBarLayout.Behavior {

    protected int mCurrentScrollOffset;

    protected int mCurrentTranslationOffset;

    protected boolean mIsOnNestedFling;

    protected boolean mIsOnNestedPreScroll;

    protected View mScrollingTarget;

    public Behavior() {
    }

    public Behavior(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY,
        boolean consumed) {
      log("custom onNestedFling | %f | %f | %b", velocityX, velocityY, consumed);
      mIsOnNestedFling = true;
      mIsOnNestedPreScroll = false;
      return true;
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX,
        float velocityY) {
      log("custom onNestedPreFling | %f | %f", velocityX, velocityY);
      return false;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
      init(coordinatorLayout, child, target);
      mIsOnNestedFling = false;
      if (mIsOnNestedPreScroll) {
        log("custom onNestedPreScroll | %d | %d", dx, dy);
        scroll(coordinatorLayout, child, target, dy);
      }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed,
        int dxUnconsumed, int dyUnconsumed) {
      if (dyUnconsumed < 0) {
        mIsOnNestedPreScroll = true;
      } else {
        log("custom onNestedScroll | %d | %d | %d | %d", dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        mIsOnNestedPreScroll = false;
        scroll(coordinatorLayout, child, target, dyConsumed);
      }
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target,
        int nestedScrollAxes) {
      log("custom onNestedScrollAccepted | %d", nestedScrollAxes);
      mIsOnNestedPreScroll = false;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target,
        int nestedScrollAxes) {
      log("custom onStartNestedScroll | %d", nestedScrollAxes);
      return true;
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target) {
      log("custom onStopNestedScroll");
    }

    protected void dispatchOffsetUpdates(AppBarLayout layout, int translationOffset) {
      if (layout instanceof SmoothAppBarLayout) {
        List listeners = ((SmoothAppBarLayout) layout).mListeners;
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

    protected int getMaxOffset(AppBarLayout layout) {
      return 0;
    }

    protected int getMinOffset(AppBarLayout layout) {
      int minOffset = layout.getMeasuredHeight();
      int i = 0;
      for (int z = layout.getChildCount(); i < z; ++i) {
        View child = layout.getChildAt(i);
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        if (layoutParams instanceof AppBarLayout.LayoutParams) {
          AppBarLayout.LayoutParams childLp = (AppBarLayout.LayoutParams) layoutParams;
          int flags = childLp.getScrollFlags();
          if ((flags & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
            minOffset -= ViewCompat.getMinimumHeight(child);
            break;
          }
        }
      }
      return -minOffset;
    }

    protected boolean init(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final View target) {
      if (mScrollingTarget == null && target != null) {
        mScrollingTarget = target;
        if (target instanceof RecyclerView) {
          ((RecyclerView) mScrollingTarget).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              log("custom mScrollingTarget RecyclerView | %d", dy);
              Behavior.this.onScrollChanged(coordinatorLayout, child, target, dy);
            }
          });
        } else if (target instanceof ViewPager) {
          ((ViewPager) target).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              log("custom mScrollingTarget ViewPager | %d", positionOffsetPixels - mCurrentScrollOffset);
              Behavior.this.onScrollChanged(coordinatorLayout, child, target, positionOffsetPixels - mCurrentScrollOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }
          });
        } else {
          target.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
              log("custom mScrollingTarget ViewTreeObserver | %d", target.getScrollY() - mCurrentScrollOffset);
              Behavior.this.onScrollChanged(coordinatorLayout, child, target, target.getScrollY() - mCurrentScrollOffset);
            }
          });
        }
        return false;
      }
      return true;
    }

    protected boolean isScrollFlagEnabled(AppBarLayout layout, int flag) {
      int i = 0;
      for (int z = layout.getChildCount(); i < z; ++i) {
        View child = layout.getChildAt(i);
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        if (layoutParams instanceof AppBarLayout.LayoutParams) {
          AppBarLayout.LayoutParams childLp = (AppBarLayout.LayoutParams) layoutParams;
          int flags = childLp.getScrollFlags();
          if ((flags & flag) != 0) {
            return true;
          }
        }
      }
      return false;
    }

    protected void onScrollChanged(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dy) {
      if (mIsOnNestedFling) {
        log("custom onScrollChanged | %d", dy);
        scroll(coordinatorLayout, child, target, dy);
      }
    }

    protected void scroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dy) {
      if (dy != 0) {
        int minY = getMinOffset(child);
        int maxY = getMaxOffset(child);
        mCurrentScrollOffset = Math.max(mCurrentScrollOffset + dy, 0);
        mCurrentTranslationOffset = Math.min(Math.max(-mCurrentScrollOffset, minY), maxY);
        setTopAndBottomOffset(mCurrentTranslationOffset);
        if (child instanceof SmoothAppBarLayout && ((SmoothAppBarLayout) child).mHaveChildWithInterpolator) {
          coordinatorLayout.dispatchDependentViewsChanged(child);
        }
        dispatchOffsetUpdates(child, mCurrentTranslationOffset);
        log("custom scroll | %d | %d | %d | %d | %d", dy, mCurrentScrollOffset, mCurrentTranslationOffset, minY, maxY);
      }
    }
  }
}
