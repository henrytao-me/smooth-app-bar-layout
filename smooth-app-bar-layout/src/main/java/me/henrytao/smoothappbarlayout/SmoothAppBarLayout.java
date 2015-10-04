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
import android.content.res.TypedArray;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

  protected final List<WeakReference<OnOffsetChangedListener>> mOffsetChangedListeners = new ArrayList<>();

  protected final List<WeakReference<OnOffsetSyncedListener>> mOffsetSyncedListeners = new ArrayList<>();

  protected Handler mHandler;

  protected boolean mHaveChildWithInterpolator;

  protected int mViewPagerId;

  protected ViewPager vViewPager;

  public SmoothAppBarLayout(Context context) {
    super(context);
    init(null);
  }

  public SmoothAppBarLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
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
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    initViews();
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

  public ViewPager getViewPager() {
    return vViewPager;
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

  public void syncOffsetDelayed() {
    syncOffsetDelayed(FAKE_DELAY);
  }

  protected void init(AttributeSet attrs) {
    TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SmoothAppBarLayout, 0, 0);
    try {
      mViewPagerId = a.getResourceId(R.styleable.SmoothAppBarLayout_sabl_view_pager_id, 0);
    } finally {
      a.recycle();
    }
  }

  protected void initViews() {
    if (mViewPagerId > 0) {
      vViewPager = (ViewPager) getRootView().findViewById(mViewPagerId);
    } else {
      int i = 0;
      ViewGroup parent = (ViewGroup) getParent();
      View child;
      for (int z = parent.getChildCount(); i < z; i++) {
        child = parent.getChildAt(i);
        if (child instanceof ViewPager) {
          vViewPager = (ViewPager) child;
          break;
        }
      }
    }
  }

  public interface OnOffsetSyncedListener {

    void onOffsetSynced(AppBarLayout appBarLayout);
  }

  public static class Behavior extends AppBarLayout.Behavior {

    protected int mCurrentScrollOffset;

    protected int mCurrentTranslationOffset;

    protected OnOffsetSyncedListener mOnOffsetSyncedListener;

    protected ViewPager.OnPageChangeListener mOnPageChangeListener;

    protected int mQuickReturnOffset;

    protected ScrollFlagView mScrollFlagView;

    protected List<Long> mTargetViews = new ArrayList<>();

    protected Map<Integer, Integer> mViewPagerOffset = new HashMap<>();

    protected ViewPager vViewPager;

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
      if (mScrollFlagView != null) {
        if (mScrollFlagView.isFlagScrollEnabled()) {
          minOffset = mScrollFlagView.getView().getMeasuredHeight();
        }
        if (mScrollFlagView.isFlagExitUntilCollapsedEnabled()) {
          minOffset -= ViewCompat.getMinimumHeight(mScrollFlagView.getView());
        }
      }
      return -minOffset;
    }

    protected long getViewTag(View target, boolean createIfNotExist) {
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
            Behavior.this.onViewPagerSelected(coordinatorLayout, child, target, position);
          }
        };
        vViewPager = ((SmoothAppBarLayout) child).getViewPager();
        if (vViewPager != null) {
          vViewPager.addOnPageChangeListener(mOnPageChangeListener);
        }
      }

      if (mScrollFlagView == null) {
        mScrollFlagView = new ScrollFlagView(child);
      }

      long tag = getViewTag(target, true);
      if (!mTargetViews.contains(tag)) {
        mTargetViews.add(tag);
        log("test custom vScrollTarget init | %d | %d", tag, mTargetViews.size());
        if (target instanceof RecyclerView) {
          ((RecyclerView) target).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              log("test custom vScrollTarget RecyclerView | %d | %d", dy, mCurrentScrollOffset);
              Behavior.this.onScrollTargetChanged(coordinatorLayout, child, target, dy);
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

    protected void onPropagateViewPagerScrollState(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dy,
        int offset) {
      if (vViewPager != null) {
        int newOffset;
        View scrollView;
        if (vViewPager.getAdapter() instanceof PagerAdapter) {
          PagerAdapter adapter = (PagerAdapter) vViewPager.getAdapter();
          int i = 0;
          for (int n = vViewPager.getAdapter().getCount(); i < n; i++) {
            scrollView = adapter.getScrollView(i);

            newOffset = Math.abs(offset);
            if (scrollView instanceof RecyclerView) {
              newOffset = Math.max(((RecyclerView) scrollView).computeVerticalScrollOffset(), newOffset);
            }
            mViewPagerOffset.put(i, newOffset);

            if (scrollView != target) {
              if (scrollView instanceof RecyclerView) {
                dy = newOffset - ((RecyclerView) scrollView).computeVerticalScrollOffset();
                scrollView.scrollBy(0, dy);
              }
            }

            log("custom onPropagateViewPagerScrollState | %d | %d | %d", i, dy, newOffset);
          }
        }
      }
    }

    protected void onScrollChanged(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dy) {
      if (dy != 0) {
        int minTranslationOffset = getMinOffset(child);
        int maxTranslationOffset = getMaxOffset(child);
        mCurrentScrollOffset = Math.max(mCurrentScrollOffset + dy, 0);
        int translationOffset = Math.min(Math.max(-mCurrentScrollOffset, minTranslationOffset), maxTranslationOffset);

        if (mScrollFlagView != null && mScrollFlagView.isQuickReturnEnabled()) {
          if (translationOffset == 0) {
            mQuickReturnOffset = 0;
          }
          if (translationOffset == minTranslationOffset) {
            if (dy < 0) {
              mQuickReturnOffset = Math.max(mQuickReturnOffset + dy, -ViewCompat.getMinimumHeight(mScrollFlagView.getView()));
            } else {
              mQuickReturnOffset = Math.min(mQuickReturnOffset + dy, 0);
            }
            translationOffset -= mQuickReturnOffset;
          } else {
            translationOffset = Math.min(Math.max(translationOffset - mQuickReturnOffset, minTranslationOffset), maxTranslationOffset);
          }
        }

        log("custom onScrollChanged | %d | %d | %d | %d | %d | %d",
            dy, mCurrentScrollOffset, mQuickReturnOffset, translationOffset, minTranslationOffset, maxTranslationOffset);
        scrolling(coordinatorLayout, child, target, translationOffset);
        onPropagateViewPagerScrollState(coordinatorLayout, child, target, dy, translationOffset);
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

    protected void onViewPagerSelected(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int position) {
      if (vViewPager != null && vViewPager.getAdapter() instanceof PagerAdapter) {
        int newOffset = 0;
        int currentOffset = 0;
        int dy = 0;
        View scrollView = ((PagerAdapter) vViewPager.getAdapter()).getScrollView(position);
        if (scrollView instanceof RecyclerView) {
          currentOffset = ((RecyclerView) scrollView).computeVerticalScrollOffset();
          newOffset = Math.max(currentOffset, mViewPagerOffset.get(position));
          dy = newOffset - currentOffset;
          scrollView.scrollBy(0, dy);
        }
        mCurrentScrollOffset = Math.abs(mCurrentTranslationOffset);
        log("custom onViewPagerSelected | %d | %d | %d | %d", position, dy, newOffset, mCurrentScrollOffset);
      }
    }

    protected void scrolling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int offset) {
      log("custom scrolling | %d", offset);
      mCurrentTranslationOffset = offset;
      setTopAndBottomOffset(offset);
      if (child instanceof SmoothAppBarLayout && ((SmoothAppBarLayout) child).mHaveChildWithInterpolator) {
        coordinatorLayout.dispatchDependentViewsChanged(child);
      }
      dispatchOffsetUpdates(child, offset);
    }
  }

  public static class ScrollFlagView {

    private int mFlags;

    private View vView;

    public ScrollFlagView(AppBarLayout layout) {
      if (layout != null) {
        int i = 0;
        for (int z = layout.getChildCount(); i < z; ++i) {
          View child = layout.getChildAt(i);
          ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
          if (layoutParams instanceof AppBarLayout.LayoutParams) {
            AppBarLayout.LayoutParams childLp = (AppBarLayout.LayoutParams) layoutParams;
            int flags = childLp.getScrollFlags();
            if ((flags & LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
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
      if (vView != null && (mFlags & LayoutParams.SCROLL_FLAG_ENTER_ALWAYS) != 0) {
        return true;
      }
      return false;
    }

    public boolean isFlagEnterAlwaysEnabled() {
      if (vView != null && (mFlags & LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED) != 0) {
        return true;
      }
      return false;
    }

    public boolean isFlagExitUntilCollapsedEnabled() {
      if (vView != null && (mFlags & LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED) != 0) {
        return true;
      }
      return false;
    }

    public boolean isFlagScrollEnabled() {
      if (vView != null && (mFlags & LayoutParams.SCROLL_FLAG_SCROLL) != 0) {
        return true;
      }
      return false;
    }

    public boolean isQuickReturnEnabled() {
      return isFlagEnterAlwaysEnabled() && isFlagEnterAlwaysCollapsedEnabled();
    }
  }
}
