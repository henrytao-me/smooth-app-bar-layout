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

import android.content.Intent;
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
  public void onItemClick(Feature feature) {
    if (feature == null || feature.mClass == null || TextUtils.isEmpty(feature.mTitle)) {
      return;
    }
    startActivity(new Intent(this, feature.mClass));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_info:
        startActivity(InfoActivity.newIntent(this));
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
    features.add(new Feature(GsdScrollActivity.class, "scroll"));
    features.add(new Feature(SmoothScrollActivity.class, "scroll"));
    features.add(new Feature(GsdScrollEnterAlwaysActivity.class, "scroll | enterAlways"));
    features.add(new Feature(SmoothScrollEnterAlwaysActivity.class, "scroll | enterAlways"));
    features.add(new Feature(GsdScrollEnterAlwaysCollapsedActivity.class, "scroll | enterAlwaysCollapsed"));
    features.add(new Feature(SmoothScrollEnterAlwaysCollapsedActivity.class, "scroll | enterAlwaysCollapsed"));
    features.add(new Feature(GsdScrollExitUntilCollapsedActivity.class, "scroll | exitUntilCollapsed"));
    features.add(new Feature(SmoothScrollExitUntilCollapsedActivity.class, "scroll | exitUntilCollapsed"));
    features.add(new Feature(GsdScrollSnapActivity.class, "scroll | snap"));
    features.add(new Feature(null, null));
    features.add(new Feature(GsdQuickReturnActivity.class, "quickReturn"));
    features.add(new Feature(SmoothQuickReturnActivity.class, "quickReturn"));
    features.add(new Feature(null, null));
    features.add(new Feature(SmoothNestedScrollViewActivity.class, "nestedScrollView"));
    features.add(new Feature(null, null));
    features.add(new Feature(SmoothCustomNestedScrollViewActivity.class, "Custom nestedScrollView"));
    features.add(new Feature(null, null));
    features.add(new Feature(SmoothListViewActivity.class, "listView"));
    return features;
  }

  public static class Feature {

    private Class<?> mClass;

    private String mTitle;

    public Feature(Class<?> aClass, String value) {
      mClass = aClass;
      mTitle = value;
    }

    @Override
    public String toString() {
      return mTitle;
    }
  }
}
