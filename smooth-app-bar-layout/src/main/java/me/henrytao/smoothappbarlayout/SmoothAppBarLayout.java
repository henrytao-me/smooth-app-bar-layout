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
import android.os.Handler;
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
import java.util.Iterator;
import java.util.List;

import me.henrytao.smoothappbarlayout.widget.NestedScrollView;

/**
 * Created by henrytao on 9/22/15.
 */
@CoordinatorLayout.DefaultBehavior(SmoothAppBarLayout.Behavior.class)
public class SmoothAppBarLayout extends AppBarLayout {

  protected static final int FAKE_DELAY = 200;

  private static void log(String s, Object... args) {
    Log.i("info", String.format(s, args));
  }

  protected final List<WeakReference<OnOffsetChangedListener>> mOffsetChangedListeners;

  protected final List<WeakReference<OnOffsetSyncedListener>> mOffsetSyncedListeners;

  protected Handler mHandler;

  protected boolean mHaveChildWithInterpolator;

  public SmoothAppBarLayout(Context context) {
    this(context, null);
  }

  public SmoothAppBarLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mOffsetChangedListeners = new ArrayList<>();
    this.mOffsetSyncedListeners = new ArrayList<>();
  }

  @Override
  public void addOnOffsetChangedListener(OnOffsetChangedListener listener) {
    super.addOnOffsetChangedListener(listener);
    int i = 0;
    for (int z = this.mOffsetChangedListeners.size(); i < z; ++i) {
      WeakReference ref = (WeakReference) this.mOffsetChangedListeners.get(i);
      if (ref != null && ref.get() == listener) {
        return;
      }
    }
    this.mOffsetChangedListeners.add(new WeakReference(listener));
  }

  @Override
  public void removeOnOffsetChangedListener(OnOffsetChangedListener listener) {
    super.removeOnOffsetChangedListener(listener);
    Iterator i = mOffsetChangedListeners.iterator();
    while (true) {
      OnOffsetChangedListener item;
      do {
        if (!i.hasNext()) {
          return;
        }
        WeakReference ref = (WeakReference) i.next();
        item = (OnOffsetChangedListener) ref.get();
      } while (item != listener && item != null);
      i.remove();
    }
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

  public void addOnOffsetSyncedListener(OnOffsetSyncedListener listener) {
    int i = 0;
    for (int z = this.mOffsetSyncedListeners.size(); i < z; ++i) {
      WeakReference ref = (WeakReference) this.mOffsetSyncedListeners.get(i);
      if (ref != null && ref.get() == listener) {
        return;
      }
    }
    this.mOffsetSyncedListeners.add(new WeakReference(listener));
  }

  public void removeOnOffsetSyncedListener(OnOffsetSyncedListener listener) {
    Iterator i = mOffsetSyncedListeners.iterator();
    while (true) {
      OnOffsetSyncedListener item;
      do {
        if (!i.hasNext()) {
          return;
        }
        WeakReference ref = (WeakReference) i.next();
        item = (OnOffsetSyncedListener) ref.get();
      } while (item != listener && item != null);
      i.remove();
    }
  }

  public void syncOffset() {
    int i = 0;
    for (int z = mOffsetSyncedListeners.size(); i < z; ++i) {
      WeakReference ref = (WeakReference) mOffsetSyncedListeners.get(i);
      OnOffsetSyncedListener listener = ref != null ? (OnOffsetSyncedListener) ref.get() : null;
      if (listener != null) {
        listener.onOffsetSynced(this);
      }
    }
  }

  public void syncOffsetDelayed() {
    syncOffsetDelayed(FAKE_DELAY);
  }

  public void syncOffsetDelayed(int delayMillis) {
    if (mHandler == null) {
      mHandler = new Handler();
    }
    mHandler.postDelayed(new Runnable() {
      @Override
      public void run() {
        SmoothAppBarLayout.this.syncOffset();
      }
    }, delayMillis);
  }

  public interface OnOffsetSyncedListener {

    void onOffsetSynced(AppBarLayout appBarLayout);
  }

  public static class Behavior extends AppBarLayout.Behavior {

    protected int mCurrentScrollOffset;

    protected int mCurrentTranslationOffset;

    protected OnOffsetSyncedListener mOnOffsetSyncedListener;

    protected int mQuickReturnOffset;

    protected View vQuickReturnView;

    protected View vScrollTarget;

    public Behavior() {
    }

    public Behavior(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    @Override
    public boolean onNestedFling(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final View target,
        float velocityX, float velocityY, boolean consumed) {
      log("custom onNestedFling | %f | %f | %b", velocityX, velocityY, consumed);
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
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed,
        int dxUnconsumed, int dyUnconsumed) {
      log("custom onNestedScroll | %d | %d | %d | %d", dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target,
        int nestedScrollAxes) {
      log("custom onNestedScrollAccepted | %d", nestedScrollAxes);
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
        List listeners = ((SmoothAppBarLayout) layout).mOffsetChangedListeners;
        int i = 0;
        for (int z = listeners.size(); i < z; ++i) {
          WeakReference ref = (WeakReference) listeners.get(i);
          OnOffsetChangedListener listener = ref != null ? (OnOffsetChangedListener) ref.get() : null;
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

    protected View getQuickReturnView(AppBarLayout layout, boolean force) {
      if (vQuickReturnView == null || force) {
        int i = 0;
        for (int z = layout.getChildCount(); i < z; ++i) {
          View child = layout.getChildAt(i);
          ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
          if (layoutParams instanceof AppBarLayout.LayoutParams) {
            AppBarLayout.LayoutParams childLp = (AppBarLayout.LayoutParams) layoutParams;
            int flags = childLp.getScrollFlags();
            if ((flags & LayoutParams.SCROLL_FLAG_ENTER_ALWAYS) != 0 && (flags & LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED) != 0) {
              vQuickReturnView = child;
              break;
            }
          }
        }
      }
      return vQuickReturnView;
    }

    protected boolean init(final CoordinatorLayout coordinatorLayout, final AppBarLayout child, final View target) {
      if (mOnOffsetSyncedListener == null && child instanceof SmoothAppBarLayout) {
        mOnOffsetSyncedListener = new OnOffsetSyncedListener() {
          @Override
          public void onOffsetSynced(AppBarLayout appBarLayout) {
            Behavior.this.onSyncOffset(coordinatorLayout, child, target);
          }
        };
        ((SmoothAppBarLayout) child).addOnOffsetSyncedListener(mOnOffsetSyncedListener);
      }
      if (vScrollTarget == null && target != null) {
        vScrollTarget = target;
        if (target instanceof RecyclerView) {
          ((RecyclerView) target).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              log("test custom vScrollTarget RecyclerView | %d | %d", dy, mCurrentScrollOffset);
              Behavior.this.onScrollTargetChanged(coordinatorLayout, child, target, dy);
            }
          });
        } else if (target instanceof ViewPager) {
          ((ViewPager) target).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              log("custom vScrollTarget ViewPager | %d", positionOffsetPixels - mCurrentScrollOffset);
              Behavior.this.onScrollTargetChanged(coordinatorLayout, child, target, positionOffsetPixels - mCurrentScrollOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }
          });
        } else if (target instanceof NestedScrollView) {
          ((NestedScrollView) target).addOnScrollListener(new NestedScrollView.OnScrollListener() {
            @Override
            public void onScrolled(android.support.v4.widget.NestedScrollView nestedScrollView, int dx, int dy) {
              log("test custom vScrollTarget NestedScrollView | %d | %d", dy, mCurrentScrollOffset);
              Behavior.this.onScrollTargetChanged(coordinatorLayout, child, target, dy);
            }
          });
        } else {
          target.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
              log("custom vScrollTarget ViewTreeObserver | %d", target.getScrollY() - mCurrentScrollOffset);
              Behavior.this.onScrollTargetChanged(coordinatorLayout, child, target, target.getScrollY() - mCurrentScrollOffset);
            }
          });
        }
        return false;
      }
      return true;
    }

    protected void onScrollChanged(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dy) {
      if (dy != 0) {
        int minTranslationOffset = getMinOffset(child);
        int maxTranslationOffset = getMaxOffset(child);
        mCurrentScrollOffset = Math.max(mCurrentScrollOffset + dy, 0);
        mCurrentTranslationOffset = Math.min(Math.max(-mCurrentScrollOffset, minTranslationOffset), maxTranslationOffset);

        int offset = mCurrentTranslationOffset;
        View quickReturnView = getQuickReturnView(child, false);
        if (quickReturnView != null) {
          if (mCurrentTranslationOffset == 0) {
            mQuickReturnOffset = 0;
          }
          if (mCurrentTranslationOffset == minTranslationOffset) {
            if (dy < 0) {
              mQuickReturnOffset = Math.max(mQuickReturnOffset + dy, -ViewCompat.getMinimumHeight(quickReturnView));
            } else {
              mQuickReturnOffset = Math.min(mQuickReturnOffset + dy, 0);
            }
            offset -= mQuickReturnOffset;
          } else {
            offset = Math.min(Math.max(offset - mQuickReturnOffset, minTranslationOffset), maxTranslationOffset);
          }
        }

        log("custom onScrollChanged | %d | %d | %d | %d | %d | %d",
            dy, mCurrentScrollOffset, mQuickReturnOffset, offset, minTranslationOffset, maxTranslationOffset);
        scrolling(coordinatorLayout, child, target, offset);
      }
    }

    protected void onScrollTargetChanged(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dy) {
      log("custom onScrollTargetChanged | %d", dy);
      onScrollChanged(coordinatorLayout, child, target, dy);
    }

    protected void onSyncOffset(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target) {
      int newOffset = target instanceof RecyclerView ? ((RecyclerView) target).computeVerticalScrollOffset() : mCurrentScrollOffset;
      log("custom onSyncOffset | %d | %d", mCurrentScrollOffset, newOffset);
      onScrollChanged(coordinatorLayout, child, target, newOffset - mCurrentScrollOffset);
    }

    protected void scrolling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int offset) {
      log("custom scrolling | %d", offset);
      setTopAndBottomOffset(offset);
      if (child instanceof SmoothAppBarLayout && ((SmoothAppBarLayout) child).mHaveChildWithInterpolator) {
        coordinatorLayout.dispatchDependentViewsChanged(child);
      }
      dispatchOffsetUpdates(child, offset);
    }
  }
}
