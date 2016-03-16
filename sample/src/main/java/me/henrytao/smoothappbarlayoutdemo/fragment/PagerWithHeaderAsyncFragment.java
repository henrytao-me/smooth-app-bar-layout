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

package me.henrytao.smoothappbarlayoutdemo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.base.Utils;
import me.henrytao.smoothappbarlayoutdemo.R;

public class PagerWithHeaderAsyncFragment extends PagerWithHeaderFragment {

  public static Fragment newInstance(boolean isShort) {
    PagerWithHeaderAsyncFragment fragment = new PagerWithHeaderAsyncFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean(ARG_IS_RECYCLER_VIEW, true);
    bundle.putBoolean(ARG_IS_SHORT, isShort);
    bundle.putInt(ARG_HEADER_LAYOUT, R.layout.item_pager_header_spacing);
    fragment.setArguments(bundle);
    return fragment;
  }

  private Handler mHandler;

  private Runnable mRunnable;

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (mRunnable != null) {
      mHandler.removeCallbacks(mRunnable);
    }
    mRunnable = null;
    mHandler = null;
  }

  @Override
  public boolean onOffsetChanged(final SmoothAppBarLayout smoothAppBarLayout, final View target, final int verticalOffset) {
    if (mHandler != null && getScrollTarget() == target) {
      if (mRunnable != null) {
        mHandler.removeCallbacks(mRunnable);
      }
      mAdapter.clear();
      vRecyclerView.getAdapter().notifyDataSetChanged();
      vRecyclerView.setNestedScrollingEnabled(false);
      mRunnable = new Runnable() {
        @Override
        public void run() {
          mAdapter.addItems(getSampleData());
          vRecyclerView.getAdapter().notifyDataSetChanged();
          vRecyclerView.setNestedScrollingEnabled(true);
          Utils.syncOffset(smoothAppBarLayout, target, verticalOffset, getScrollTarget());
        }
      };
      mHandler.postDelayed(mRunnable, 2000);
    }
    return false;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mHandler = new Handler();
  }
}
