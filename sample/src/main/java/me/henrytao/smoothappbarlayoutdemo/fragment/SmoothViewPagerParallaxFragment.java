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

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.Bind;
import me.henrytao.smoothappbarlayout.PagerAdapter;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.ViewPagerRunnableAdapter;

public class SmoothViewPagerParallaxFragment extends BaseFeatureFragment {

  public static Fragment newInstance() {
    return new SmoothViewPagerParallaxFragment();
  }

  @Bind(R.id.collapsing_toolbar_layout)
  CollapsingToolbarLayout vCollapsingToolbarLayout;

  @Bind(R.id.tab_layout)
  TabLayout vTabLayout;

  @Bind(R.id.view_pager)
  ViewPager vViewPager;

  @Override
  public int getContentLayout() {
    return R.layout.fragment_smooth_view_pager_parallax;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vCollapsingToolbarLayout.setTitleEnabled(false);

    ViewPagerRunnableAdapter adapter = new ViewPagerRunnableAdapter(getChildFragmentManager());
    adapter.addFragment(DummyRecyclerViewFragment.newInstance("Cat", 100, R.layout.item_header_spacing), "Cat");
    adapter.addFragment(DummyRecyclerViewFragment.newInstance("Dog", 100, R.layout.item_header_spacing), "Dog");
    adapter.addFragment(DummyRecyclerViewFragment.newInstance("Mouse", 100, R.layout.item_header_spacing), "Mouse");
    adapter.addFragment(DummyRecyclerViewFragment.newInstance("Chicken", 5, R.layout.item_header_spacing), "Chicken");
    adapter.addFragment(DummyNestedScrollViewFragment.newInstance(getString(R.string.text_long),
        R.layout.item_header_spacing), "Duck");
    adapter.addFragment(DummyRecyclerViewFragment.newInstance("Bird", 100, R.layout.item_header_spacing), "Bird");
    adapter.addFragment(DummyNestedScrollViewFragment.newInstance(getString(R.string.text_short),
        R.layout.item_header_spacing), "Tiger");

    // PagerAdapter have to implement `me.henrytao.smoothappbarlayout.PagerAdapter` in order to make it work with `SmoothAppBarLayout`
    if (adapter instanceof PagerAdapter) {
      vViewPager.setAdapter(adapter);
    }
    vTabLayout.setupWithViewPager(vViewPager);
    vTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
  }
}
