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

package me.henrytao.smoothappbarlayoutdemo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.ViewPagerAdapter;
import me.henrytao.smoothappbarlayoutdemo.fragment.PagerWithHeaderFragment;

public class SmoothViewPagerActivity extends BaseActivity {

  @Bind(R.id.tab_layout)
  TabLayout vTabLayout;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  @Bind(R.id.view_pager)
  ViewPager vViewPager;

  private ViewPagerAdapter mViewPagerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_smooth_view_pager);
    ButterKnife.bind(this);

    setSupportActionBar(vToolbar);
    vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    mViewPagerAdapter.addFragment("Cat", PagerWithHeaderFragment.newInstance());
    mViewPagerAdapter.addFragment("Dog", PagerWithHeaderFragment.newInstance());
    mViewPagerAdapter.addFragment("Mouse", PagerWithHeaderFragment.newInstance());
    mViewPagerAdapter.addFragment("Bird", PagerWithHeaderFragment.newInstance());
    mViewPagerAdapter.addFragment("Chicken", PagerWithHeaderFragment.newInstance());
    mViewPagerAdapter.addFragment("Tiger", PagerWithHeaderFragment.newInstance());
    mViewPagerAdapter.addFragment("Elephant", PagerWithHeaderFragment.newInstance());

    vViewPager.setAdapter(mViewPagerAdapter);
    vViewPager.setOffscreenPageLimit(vViewPager.getAdapter().getCount());

    vTabLayout.setupWithViewPager(vViewPager);
    vTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    vTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
  }
}
