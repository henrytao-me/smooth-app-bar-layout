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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.SmoothCollapsingToolbarLayout;
import me.henrytao.smoothappbarlayout.utils.ResourceUtils;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.SimpleAdapter;

public class GooglePlayActivity extends BaseActivity {

  private static final int ITEM_COUNT = 100;

  public static Intent newIntent(Context context) {
    return new Intent(context, GooglePlayActivity.class);
  }

  @Bind(R.id.collapsing_toolbar_layout)
  CollapsingToolbarLayout vCollapsingToolbarLayout;

  @Bind(android.R.id.list)
  RecyclerView vRecyclerView;

  @Bind(R.id.smooth_app_bar_layout)
  SmoothAppBarLayout vSmoothAppBarLayout;

  @Bind(R.id.smooth_collapsing_toolbar_layout)
  SmoothCollapsingToolbarLayout vSmoothCollapsingToolbarLayout;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  private int mActionBarSize;

  private int mLastOffset;

  private int mSmoothAppBarLayoutSize;

  private int mStatusBarSize;

  private CharSequence mTitle;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_google_play, menu);
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
    setContentView(R.layout.activity_google_play);
    ButterKnife.bind(this);
    setSupportActionBar(vToolbar);
    vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    boolean isFitsSystemWindows = ViewCompat.getFitsSystemWindows(vSmoothAppBarLayout);
    if (isFitsSystemWindows) {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    vCollapsingToolbarLayout.setTitleEnabled(false);

    vRecyclerView.hasFixedSize();
    vRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    vRecyclerView.setAdapter(new SimpleAdapter<>(getSampleData(), null));

    // use this SmoothCollapsingToolbarLayout for customizing layout when AppBar offset changed
    mActionBarSize = ResourceUtils.getStatusBarSize(this);
    mStatusBarSize = isFitsSystemWindows ? ResourceUtils.getStatusBarSize(this) : 0;
    mSmoothAppBarLayoutSize = getResources().getDimensionPixelSize(R.dimen.google_play_cover);
    mTitle = getTitle();
    vSmoothCollapsingToolbarLayout.setOnOffsetChangedListener(new SmoothCollapsingToolbarLayout.OnOffsetChangedListener() {
      @Override
      public void onOffsetChanged(float ratio) {
        int offset = (int) (ratio * mSmoothAppBarLayoutSize);
        float dy = offset - mLastOffset;
        boolean isTitleShowed = false;
        if (dy <= 0 && offset > 0 && (offset >= (mSmoothAppBarLayoutSize - mActionBarSize - mStatusBarSize))) {
          isTitleShowed = true;
        }
        vToolbar.setTitle(isTitleShowed ? mTitle : "");
        mLastOffset = offset;
      }
    });
  }

  private List<String> getSampleData() {
    List<String> data = new ArrayList<>();
    int i = 0;
    for (int n = ITEM_COUNT; i < n; i++) {
      data.add(String.format("Line %d", i));
    }
    return data;
  }
}
