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

package me.henrytao.smoothappbarlayoutdemo.fragment;

import com.yalantis.phoenix.PullToRefreshView;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.Bind;
import me.henrytao.smoothappbarlayout.BaseBehavior;
import me.henrytao.smoothappbarlayout.PagerAdapter;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.ViewPagerRunnableAdapter;

public class SmoothCustomPullToRefreshViewPagerFragment extends BaseFeatureFragment {

  public static Fragment newInstance() {
    return new SmoothCustomPullToRefreshViewPagerFragment();
  }

  @Bind(R.id.collapsing_toolbar_layout)
  CollapsingToolbarLayout vCollapsingToolbarLayout;

  @Bind(R.id.smooth_app_bar_layout)
  SmoothAppBarLayout vSmoothAppBarLayout;

  @Bind(R.id.tab_layout)
  TabLayout vTabLayout;

  @Bind(R.id.view_pager)
  ViewPager vViewPager;

  @Override
  public int getContentLayout() {
    return R.layout.fragment_smooth_custom_pull_to_refresh_view_pager;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vCollapsingToolbarLayout.setTitleEnabled(false);

    ViewPagerRunnableAdapter adapter = new ViewPagerRunnableAdapter(getChildFragmentManager());
    adapter.addFragment(DummyNestedScrollViewCustomPullToRefreshFragment.newInstance(getString(R.string.text_long)), "Cat");
    adapter.addFragment(DummyNestedScrollViewCustomPullToRefreshFragment.newInstance(getString(R.string.text_long)), "Dog");
    adapter.addFragment(DummyNestedScrollViewCustomPullToRefreshFragment.newInstance(getString(R.string.text_long)), "Mouse");
    adapter.addFragment(DummyNestedScrollViewCustomPullToRefreshFragment.newInstance(getString(R.string.text_long)), "Chicken");
    adapter.addFragment(DummyNestedScrollViewCustomPullToRefreshFragment.newInstance(getString(R.string.text_long)), "Tiger");

    // PagerAdapter have to implement `me.henrytao.smoothappbarlayout.PagerAdapter` in order to make it work with `SmoothAppBarLayout`
    if (adapter instanceof PagerAdapter) {
      vViewPager.setAdapter(adapter);
    }
    vTabLayout.setupWithViewPager(vViewPager);
    vTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
  }
}
