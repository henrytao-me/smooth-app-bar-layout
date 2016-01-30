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

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.SimpleAdapter;
import me.henrytao.smoothappbarlayoutdemo.config.Constants;


public class MainActivity extends BaseActivity implements SimpleAdapter.OnItemClickListener<MainActivity.Feature> {

  private static final int NUM_OF_COLUMNS = 2;

  @Bind(android.R.id.list)
  RecyclerView vRecyclerView;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  private SimpleAdapter<Feature> mAdapter;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public void onItemClick(Feature data) {
    if (data == null || TextUtils.isEmpty(data.mValue)) {
      return;
    }
    if (data.mKey == Constants.Feature.SMOOTH_INBOX) {
      startActivity(InboxActivity.newIntent(this));
    } else if (data.mKey == Constants.Feature.SMOOTH_GOOGLE_PLAY) {
      startActivity(GooglePlayActivity.newIntent(this));
    } else if (data.mKey == Constants.Feature.SMOOTH_VIEW_PAGER_DRAWER_LAYOUT) {
      startActivity(SmoothViewPagerDrawerLayoutActivity.newIntent(this));
    } else if (data.mKey == Constants.Feature.SMOOTH_VIEW_PAGER_PARALLAX_DRAWER_LAYOUT) {
      startActivity(SmoothViewPagerParallaxDrawerLayoutActivity.newIntent(this));
    } else {
      startActivity(FeatureActivity.newIntent(this, data.mKey, data.mValue));
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_info:
        startActivity(InfoActivity.newIntent(this));
        return true;
      case R.id.action_donate:
        showDonateDialog();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(vToolbar);

    mAdapter = new SimpleAdapter(getFeatures(), this);
    vRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_OF_COLUMNS));
    vRecyclerView.setAdapter(mAdapter);
  }

  protected List<Feature> getFeatures() {
    List<Feature> features = new ArrayList<>();
    features.add(new Feature(Constants.Feature.GSD_INBOX, ""));
    features.add(new Feature(Constants.Feature.SMOOTH_INBOX, "Inbox"));
    features.add(new Feature(Constants.Feature.GSD_GOOGLE_PLAY, ""));
    features.add(new Feature(Constants.Feature.SMOOTH_GOOGLE_PLAY, "Google Play"));
    features.add(new Feature(Constants.Feature.GSD_VIEW_PAGER_DRAWER_LAYOUT, ""));
    features.add(new Feature(Constants.Feature.SMOOTH_VIEW_PAGER_DRAWER_LAYOUT, "Smooth ViewPager DrawerLayout"));
    features.add(new Feature(Constants.Feature.GSD_VIEW_PAGER_PARALLAX_DRAWER_LAYOUT, ""));
    features.add(new Feature(Constants.Feature.SMOOTH_VIEW_PAGER_PARALLAX_DRAWER_LAYOUT, "Smooth ViewPager Parallax DrawerLayout"));
    features.add(new Feature(Constants.Feature.GSD_DEFAULT, "Default"));
    features.add(new Feature(Constants.Feature.SMOOTH_DEFAULT, "Smooth Default"));
    features.add(new Feature(Constants.Feature.GSD_EXIT_UNTIL_COLLAPSED, "ExitUntilCollapsed"));
    features.add(new Feature(Constants.Feature.SMOOTH_EXIT_UNTIL_COLLAPSED, "Smooth ExitUntilCollapsed"));
    features.add(new Feature(Constants.Feature.GSD_ENTER_ALWAYS, "Enter Always"));
    features.add(new Feature(Constants.Feature.SMOOTH_ENTER_ALWAYS, "Smooth Enter Always"));
    features.add(new Feature(Constants.Feature.GSD_ENTER_ALWAYS_COLLAPSED, "Enter Always Collapsed (Quick Return)"));
    features.add(new Feature(Constants.Feature.SMOOTH_ENTER_ALWAYS_COLLAPSED, "Smooth Enter Always Collapsed (Quick Return)"));
    features.add(new Feature(Constants.Feature.GSD_ENTER_ALWAYS_COLLAPSED_PARALLAX, "Enter Always Collapsed Parallax (Quick Return)"));
    features.add(new Feature(Constants.Feature.SMOOTH_ENTER_ALWAYS_COLLAPSED_PARALLAX,
        "Smooth Enter Always Collapsed Parallax (Quick Return)"));
    features.add(new Feature(Constants.Feature.GSD_AVATAR, ""));
    features.add(new Feature(Constants.Feature.SMOOTH_AVATAR, "Smooth Avatar"));
    features.add(new Feature(Constants.Feature.GSD_PARALLAX, "Parallax"));
    features.add(new Feature(Constants.Feature.SMOOTH_PARALLAX, "Smooth Parallax"));
    features.add(new Feature(Constants.Feature.GSD_NESTED_SCROLL_VIEW_PARALLAX, "NestedScrollView Parallax"));
    features.add(new Feature(Constants.Feature.SMOOTH_NESTED_SCROLL_VIEW_PARALLAX, "Smooth NestedScrollView Parallax"));
    features.add(new Feature(Constants.Feature.GSD_NESTED_SCROLL_VIEW_PARALLAX_2, ""));
    features.add(new Feature(Constants.Feature.SMOOTH_NESTED_SCROLL_VIEW_PARALLAX_2, "Smooth NestedScrollView Parallax 2"));
    features.add(new Feature(Constants.Feature.GSD_SWIPE_REFRESH_LAYOUT, "SwipeRefreshLayout"));
    features.add(new Feature(Constants.Feature.SMOOTH_SWIPE_REFRESH_LAYOUT, "Smooth SwipeRefreshLayout"));
    features.add(new Feature(Constants.Feature.GSD_VIEW_PAGER, "ViewPager"));
    features.add(new Feature(Constants.Feature.SMOOTH_VIEW_PAGER, "Smooth ViewPager"));
    features.add(new Feature(Constants.Feature.GSD_VIEW_PAGER_RUNNABLE, ""));
    features.add(new Feature(Constants.Feature.SMOOTH_VIEW_PAGER_RUNNABLE, "Smooth ViewPager Runnable"));
    features.add(new Feature(Constants.Feature.GSD_VIEW_PAGER_QUICK_RETURN, ""));
    features.add(new Feature(Constants.Feature.SMOOTH_VIEW_PAGER_QUICK_RETURN, "Smooth ViewPager QuickReturn"));
    features.add(new Feature(Constants.Feature.GSD_VIEW_PAGER_PARALLAX, "ViewPager Parallax"));
    features.add(new Feature(Constants.Feature.SMOOTH_VIEW_PAGER_PARALLAX, "Smooth ViewPager Parallax"));
    return features;
  }

  public static class Feature {

    private Constants.Feature mKey;

    private String mValue;

    public Feature(Constants.Feature key, String value) {
      mKey = key;
      mValue = value;
    }

    @Override
    public String toString() {
      return mValue;
    }
  }
}
