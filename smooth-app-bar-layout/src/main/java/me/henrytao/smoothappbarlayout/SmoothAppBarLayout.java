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
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by henrytao on 9/22/15.
 */
@CoordinatorLayout.DefaultBehavior(SmoothAppBarLayout.Behavior.class)
public class SmoothAppBarLayout extends AppBarLayout {

  protected static final int FAKE_DELAY = 200;

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

  public static class Behavior extends BaseBehavior {

    private static void log(String s, Object... args) {
      Log.i("info", String.format("Behavior %s", String.format(s, args)));
    }

    protected int mCurrentScrollOffset;

    protected int mQuickReturnOffset;

    protected ScrollFlag mScrollFlag;

    public Behavior() {
    }

    public Behavior(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    @Override
    protected int getCurrentScrollOffset() {
      return mCurrentScrollOffset;
    }

    @Override
    protected void onInit(CoordinatorLayout coordinatorLayout, AppBarLayout child) {
      if (mScrollFlag == null) {
        mScrollFlag = new ScrollFlag(child);
      }
    }

    @Override
    protected void onScrollChanged(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dy) {
      if (dy != 0) {
        int minTranslationOffset = getMinOffset(child);
        int maxTranslationOffset = getMaxOffset(child);
        mCurrentScrollOffset = Math.max(mCurrentScrollOffset + dy, 0);
        int translationOffset = Math.min(Math.max(-mCurrentScrollOffset, minTranslationOffset), maxTranslationOffset);

        if (mScrollFlag != null && mScrollFlag.isQuickReturnEnabled()) {
          if (translationOffset == 0) {
            mQuickReturnOffset = 0;
          }
          if (translationOffset == minTranslationOffset) {
            if (dy < 0) {
              mQuickReturnOffset = Math.max(mQuickReturnOffset + dy, -ViewCompat.getMinimumHeight(mScrollFlag.getView()));
            } else {
              mQuickReturnOffset = Math.min(mQuickReturnOffset + dy, 0);
            }
            translationOffset -= mQuickReturnOffset;
          } else {
            translationOffset = Math.min(Math.max(translationOffset - mQuickReturnOffset, minTranslationOffset), maxTranslationOffset);
          }
        }

        log("onScrollChanged | %d | %d | %d | %d | %d | %d",
            dy, mCurrentScrollOffset, mQuickReturnOffset, translationOffset, minTranslationOffset, maxTranslationOffset);
        scrolling(coordinatorLayout, child, target, translationOffset);
        onPropagateViewPagerScrollState(coordinatorLayout, child, target, dy, translationOffset);
      }
    }

    @Override
    protected void onSyncOffset(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target) {
      int newOffset = mCurrentScrollOffset;
      if (target instanceof RecyclerView) {
        newOffset = ((RecyclerView) target).computeVerticalScrollOffset();
      }
      log("custom onSyncOffset | %d | %d", mCurrentScrollOffset, newOffset);
      onScrollChanged(coordinatorLayout, child, target, newOffset - mCurrentScrollOffset);
    }

    @Override
    protected void onViewPagerSelected(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, ViewPager viewPager,
        int position) {
      //if (vViewPager != null && vViewPager.getAdapter() instanceof PagerAdapter) {
      //  int newOffset = 0;
      //  int currentOffset = 0;
      //  int dy = 0;
      //  View scrollView = ((PagerAdapter) vViewPager.getAdapter()).getScrollView(position);
      //  if (scrollView instanceof RecyclerView) {
      //    currentOffset = ((RecyclerView) scrollView).computeVerticalScrollOffset();
      //    newOffset = Math.max(currentOffset, mViewPagerOffset.get(position));
      //    dy = newOffset - currentOffset;
      //    scrollView.scrollBy(0, dy);
      //  }
      //  mCurrentScrollOffset = Math.abs(mCurrentTranslationOffset);
      //  log("custom onViewPagerSelected | %d | %d | %d | %d", position, dy, newOffset, mCurrentScrollOffset);
      //}
    }

    protected int getMaxOffset(AppBarLayout layout) {
      return 0;
    }

    protected int getMinOffset(AppBarLayout layout) {
      int minOffset = layout.getMeasuredHeight();
      if (mScrollFlag != null) {
        if (mScrollFlag.isFlagScrollEnabled()) {
          minOffset = mScrollFlag.getView().getMeasuredHeight();
        }
        if (mScrollFlag.isFlagExitUntilCollapsedEnabled()) {
          minOffset -= ViewCompat.getMinimumHeight(mScrollFlag.getView());
        }
      }
      return -minOffset;
    }

    protected void onPropagateViewPagerScrollState(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
        int dy, int offset) {
      //if (vViewPager != null) {
      //  int newOffset;
      //  View scrollView;
      //  if (vViewPager.getAdapter() instanceof PagerAdapter) {
      //    PagerAdapter adapter = (PagerAdapter) vViewPager.getAdapter();
      //    int i = 0;
      //    for (int n = vViewPager.getAdapter().getCount(); i < n; i++) {
      //      scrollView = adapter.getScrollView(i);
      //
      //      newOffset = Math.abs(offset);
      //      if (scrollView instanceof RecyclerView) {
      //        newOffset = Math.max(((RecyclerView) scrollView).computeVerticalScrollOffset(), newOffset);
      //      }
      //      mViewPagerOffset.put(i, newOffset);
      //
      //      if (scrollView != target) {
      //        if (scrollView instanceof RecyclerView) {
      //          dy = newOffset - ((RecyclerView) scrollView).computeVerticalScrollOffset();
      //          scrollView.scrollBy(0, dy);
      //        }
      //      }
      //
      //      log("custom onPropagateViewPagerScrollState | %d | %d | %d", i, dy, newOffset);
      //    }
      //  }
      //}
    }
  }
}
