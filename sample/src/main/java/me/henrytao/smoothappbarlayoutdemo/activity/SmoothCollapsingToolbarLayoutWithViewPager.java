/*
 * Copyright 2017 "Henry Tao <hi@henrytao.me>"
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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.ViewPagerAdapter;
import me.henrytao.smoothappbarlayoutdemo.fragment.PagerWithHeaderFragment;

/**
 * Created by henrytao on 3/12/17.
 */

public class SmoothCollapsingToolbarLayoutWithViewPager extends BaseActivity {

  @Bind(R.id.chelsea_logo)
  ImageView vChelseaLogo;

  @Bind(R.id.chelsea_title)
  TextView vChelseaTitle;

  @Bind(R.id.smooth_app_bar_layout)
  SmoothAppBarLayout vSmoothAppBarLayout;

  @Bind(R.id.tab_layout)
  TabLayout vTabLayout;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  @Bind(R.id.view_pager)
  ViewPager vViewPager;

  private int mActionBarSize;

  private int mLogoCollapsedSize;

  private int mTitleCollapsedSize;

  private int mTitleExpandedSize;

  private ViewPagerAdapter mViewPagerAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_smooth_collapsing_toolbar_layout_with_view_pager);
    ButterKnife.bind(this);

    setSupportActionBar(vToolbar);
    getSupportActionBar().setTitle("");
    vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    mViewPagerAdapter.onRestoreInstanceState(savedInstanceState);
    mViewPagerAdapter
        .addFragment("Cat", PagerWithHeaderFragment.newInstance(false, false, R.layout.item_header_view_pager_parallax_spacing));
    mViewPagerAdapter
        .addFragment("Dog", PagerWithHeaderFragment.newInstance(true, false, R.layout.item_header_view_pager_parallax_spacing));
    mViewPagerAdapter
        .addFragment("Mouse", PagerWithHeaderFragment.newInstance(true, true, R.layout.item_header_view_pager_parallax_spacing));
    mViewPagerAdapter
        .addFragment("Bird", PagerWithHeaderFragment.newInstance(false, true, R.layout.item_header_view_pager_parallax_spacing));
    mViewPagerAdapter
        .addFragment("Chicken", PagerWithHeaderFragment.newInstance(false, false, R.layout.item_header_view_pager_parallax_spacing));
    mViewPagerAdapter
        .addFragment("Tiger", PagerWithHeaderFragment.newInstance(false, false, R.layout.item_header_view_pager_parallax_spacing));
    mViewPagerAdapter
        .addFragment("Elephant", PagerWithHeaderFragment.newInstance(false, false, R.layout.item_header_view_pager_parallax_spacing));

    vViewPager.setAdapter(mViewPagerAdapter);
    vViewPager.setOffscreenPageLimit(vViewPager.getAdapter().getCount());

    vTabLayout.setupWithViewPager(vViewPager);
    vTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    vTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

    mLogoCollapsedSize = getResources().getDimensionPixelSize(R.dimen.mdActionBarSize);
    mTitleExpandedSize = getResources().getDimensionPixelSize(R.dimen.chelsea_title_expanded_size);
    mTitleCollapsedSize = getResources().getDimensionPixelSize(R.dimen.chelsea_title_collapsed_size);
    mActionBarSize = getResources().getDimensionPixelOffset(R.dimen.mdActionBarSize);

    vSmoothAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int offset = Math.abs(verticalOffset);
        int maxOffset = appBarLayout.getHeight() - ViewCompat.getMinimumHeight(appBarLayout);
        float scale = offset * 1.0f / maxOffset;
        int logoSize = vChelseaLogo.getWidth();
        int titleHeight = vChelseaTitle.getHeight();
        int logoTX = 0, logoTY = 0, titleTX = 0, titleTY = 0;
        float logoScale = 1f, titleScale = 1f;

        logoScale = (logoSize - scale * (logoSize - mLogoCollapsedSize)) / logoSize;
        titleScale = (mTitleExpandedSize - scale * (mTitleExpandedSize - mTitleCollapsedSize)) / mTitleExpandedSize;

        logoTX = translate(0, -(((appBarLayout.getWidth() - logoSize) / 2) - mActionBarSize), scale,
            (int) ((mLogoCollapsedSize - logoSize) * (1 - logoScale)));
        logoTY = translate(-titleHeight, 0, scale, (int) ((logoSize - mLogoCollapsedSize) * (1 - logoScale)));

        vChelseaLogo.setScaleX(logoScale);
        vChelseaLogo.setScaleY(logoScale);
        vChelseaLogo.setTranslationX(logoTX);
        vChelseaLogo.setTranslationY(logoTY);
        vChelseaTitle.setScaleX(titleScale);
        vChelseaTitle.setScaleY(titleScale);
        vChelseaTitle.setTranslationX(titleTX);
        vChelseaTitle.setTranslationY(titleTY);
      }
    });
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    mViewPagerAdapter.onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  private int translate(int start, int end, float scale, int adjust) {
    return (int) (start + (end - start) * scale + adjust);
  }
}
