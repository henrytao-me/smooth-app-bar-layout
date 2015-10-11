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

package me.henrytao.smoothappbarlayoutdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.config.Constants.Feature;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdAvatarFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdDefaultFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdEnterAlwaysCollapsedFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdEnterAlwaysCollapsedParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdEnterAlwaysFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdExitUntilCollapsedFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdNestedScrollViewParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdSwipeRefreshLayoutFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdViewPagerFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdViewPagerParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothAvatarFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothDefaultFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothEnterAlwaysCollapsedFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothEnterAlwaysCollapsedParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothEnterAlwaysFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothExitUntilCollapsedFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothNestedScrollViewParallax2Fragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothNestedScrollViewParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothSwipeRefreshLayoutFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothViewPagerFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothViewPagerParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothViewPagerQuickReturnFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothViewPagerRunnableFragment;

public class FeatureActivity extends BaseActivity {

  private static final String FEATURE = "FEATURE";

  private static final String TITLE = "TITLE";

  public static Intent newIntent(Context context, Feature feature, String title) {
    Intent intent = new Intent(context, FeatureActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(FEATURE, feature.toString());
    bundle.putString(TITLE, title);
    intent.putExtras(bundle);
    return intent;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_feature, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_donate:
        showDonateDialog();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feature);

    setTitle(getIntent().getExtras().getString(TITLE));
    Fragment fragment = getFeatureFragment();
    if (fragment != null) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fragment, fragment)
          .commit();
    }
  }

  protected Fragment getFeatureFragment() {
    Fragment fragment = null;
    String feature = getIntent().getExtras().getString(FEATURE);
    if (Feature.GSD_DEFAULT.equal(feature)) {
      fragment = GsdDefaultFragment.newInstance();
    } else if (Feature.SMOOTH_DEFAULT.equal(feature)) {
      fragment = SmoothDefaultFragment.newInstance();
    } else if (Feature.GSD_EXIT_UNTIL_COLLAPSED.equal(feature)) {
      fragment = GsdExitUntilCollapsedFragment.newInstance();
    } else if (Feature.SMOOTH_EXIT_UNTIL_COLLAPSED.equal(feature)) {
      fragment = SmoothExitUntilCollapsedFragment.newInstance();
    } else if (Feature.GSD_AVATAR.equal(feature)) {
      fragment = GsdAvatarFragment.newInstance();
    } else if (Feature.GSD_ENTER_ALWAYS.equal(feature)) {
      fragment = GsdEnterAlwaysFragment.newInstance();
    } else if (Feature.SMOOTH_ENTER_ALWAYS.equal(feature)) {
      fragment = SmoothEnterAlwaysFragment.newInstance();
    } else if (Feature.GSD_ENTER_ALWAYS_COLLAPSED.equal(feature)) {
      fragment = GsdEnterAlwaysCollapsedFragment.newInstance();
    } else if (Feature.SMOOTH_ENTER_ALWAYS_COLLAPSED.equal(feature)) {
      fragment = SmoothEnterAlwaysCollapsedFragment.newInstance();
    } else if (Feature.SMOOTH_AVATAR.equal(feature)) {
      fragment = SmoothAvatarFragment.newInstance();
    } else if (Feature.GSD_PARALLAX.equal(feature)) {
      fragment = GsdParallaxFragment.newInstance();
    } else if (Feature.SMOOTH_PARALLAX.equal(feature)) {
      fragment = SmoothParallaxFragment.newInstance();
    } else if (Feature.GSD_ENTER_ALWAYS_COLLAPSED_PARALLAX.equal(feature)) {
      fragment = GsdEnterAlwaysCollapsedParallaxFragment.newInstance();
    } else if (Feature.SMOOTH_ENTER_ALWAYS_COLLAPSED_PARALLAX.equal(feature)) {
      fragment = SmoothEnterAlwaysCollapsedParallaxFragment.newInstance();
    } else if (Feature.GSD_NESTED_SCROLL_VIEW_PARALLAX.equal(feature)) {
      fragment = GsdNestedScrollViewParallaxFragment.newInstance();
    } else if (Feature.SMOOTH_NESTED_SCROLL_VIEW_PARALLAX.equal(feature)) {
      fragment = SmoothNestedScrollViewParallaxFragment.newInstance();
    } else if (Feature.SMOOTH_NESTED_SCROLL_VIEW_PARALLAX_2.equal(feature)) {
      fragment = SmoothNestedScrollViewParallax2Fragment.newInstance();
    } else if (Feature.GSD_SWIPE_REFRESH_LAYOUT.equal(feature)) {
      fragment = GsdSwipeRefreshLayoutFragment.newInstance();
    } else if (Feature.SMOOTH_SWIPE_REFRESH_LAYOUT.equal(feature)) {
      fragment = SmoothSwipeRefreshLayoutFragment.newInstance();
    } else if (Feature.GSD_VIEW_PAGER.equal(feature)) {
      fragment = GsdViewPagerFragment.newInstance();
    } else if (Feature.SMOOTH_VIEW_PAGER.equal(feature)) {
      fragment = SmoothViewPagerFragment.newInstance();
    } else if (Feature.SMOOTH_VIEW_PAGER_RUNNABLE.equal(feature)) {
      fragment = SmoothViewPagerRunnableFragment.newInstance();
    } else if (Feature.SMOOTH_VIEW_PAGER_QUICK_RETURN.equal(feature)) {
      fragment = SmoothViewPagerQuickReturnFragment.newInstance();
    } else if (Feature.GSD_VIEW_PAGER_PARALLAX.equal(feature)) {
      fragment = GsdViewPagerParallaxFragment.newInstance();
    } else if (Feature.SMOOTH_VIEW_PAGER_PARALLAX.equal(feature)) {
      fragment = SmoothViewPagerParallaxFragment.newInstance();
    }
    return fragment;
  }
}
