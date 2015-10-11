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
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayoutdemo.R;

public class SmoothViewPagerDrawerLayoutActivity extends BaseActivity {

  public static Intent newIntent(Context context) {
    return new Intent(context, SmoothViewPagerDrawerLayoutActivity.class);
  }

  @Bind(R.id.drawer_layout)
  DrawerLayout vDrawerLayout;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  private ActionBarDrawerToggle mActionBarDrawerToggle;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_view_pager_drawer_layout, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_close:
        onBackPressed();
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
    setContentView(R.layout.activity_smooth_view_pager_drawer_layout);
    ButterKnife.bind(this);
    setSupportActionBar(vToolbar);

    boolean isFitsSystemWindows = ViewCompat.getFitsSystemWindows(vDrawerLayout);
    if (isFitsSystemWindows) {
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    mActionBarDrawerToggle = new ActionBarDrawerToggle(this, vDrawerLayout, vToolbar, R.string.open, R.string.close);
    vDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    mActionBarDrawerToggle.syncState();
  }
}
