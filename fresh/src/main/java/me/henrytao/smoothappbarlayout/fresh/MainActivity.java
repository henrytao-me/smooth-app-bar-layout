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

package me.henrytao.smoothappbarlayout.fresh;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayout.PagerAdapter;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.collapsing_toolbar_layout)
  CollapsingToolbarLayout vCollapsingToolbarLayout;

  @Bind(R.id.tab_layout)
  TabLayout vTabLayout;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  @Bind(R.id.view_pager)
  ViewPager vViewPager;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(vToolbar);
    vToolbar.setTitle("");
    vCollapsingToolbarLayout.setTitleEnabled(false);

    ViewPagerRunnableAdapter adapter = new ViewPagerRunnableAdapter(getSupportFragmentManager());

    adapter.addFragment(DummyNestedScrollViewFragment.newInstance(getString(R.string.text_long)), "Cat");
    adapter.addFragment(DummyNestedScrollViewFragment.newInstance(getString(R.string.text_long)), "Dog");
    adapter.addFragment(DummyNestedScrollViewFragment.newInstance(getString(R.string.text_long)), "Mouse");
    adapter.addFragment(DummyNestedScrollViewFragment.newInstance(getString(R.string.text_long)), "Chicken");
    adapter.addFragment(DummyNestedScrollViewFragment.newInstance(getString(R.string.text_long)), "Bird");
    adapter.addFragment(DummyNestedScrollViewFragment.newInstance(getString(R.string.text_long)), "Duck");

    if (adapter instanceof PagerAdapter) {
      vViewPager.setAdapter(adapter);
    }
    vTabLayout.setupWithViewPager(vViewPager);
    vTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
  }
}
