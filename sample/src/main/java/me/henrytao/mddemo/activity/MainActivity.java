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

package me.henrytao.mddemo.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.mddemo.R;

public class MainActivity extends AppCompatActivity {

  private static final int DRAWER_CLOSE_DELAY = 240;

  @Bind(R.id.drawer_layout)
  DrawerLayout vDrawerLayout;

  @Bind(R.id.navigation_view)
  NavigationView vNavigationView;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  private Handler mDrawerHandler = new Handler();

  private ActionBarDrawerToggle mDrawerToggle;

  private int mNavItemId;

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
      return mDrawerToggle.onOptionsItemSelected(item);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(vToolbar);
    mDrawerToggle = new ActionBarDrawerToggle(this, vDrawerLayout, vToolbar, R.string.open, R.string.close);
    vDrawerLayout.setDrawerListener(mDrawerToggle);
    mDrawerToggle.syncState();

    vNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(final MenuItem menuItem) {
        return onNavigationItemClicked(menuItem);
      }
    });
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  private boolean onNavigationItemClicked(final MenuItem menuItem) {
    menuItem.setChecked(true);
    mNavItemId = menuItem.getItemId();
    vDrawerLayout.closeDrawer(GravityCompat.START);
    mDrawerHandler.postDelayed(new Runnable() {
      @Override
      public void run() {
        onNavigationItemSelected(menuItem);
      }
    }, DRAWER_CLOSE_DELAY);
    return true;
  }

  private void onNavigationItemSelected(MenuItem menuItem) {

  }
}
