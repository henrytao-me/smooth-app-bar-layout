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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.SimpleAdapter;


public class MainActivity extends AppCompatActivity implements SimpleAdapter.OnItemClickListener<MainActivity.Feature> {

  private static final String GSD_DEFAULT = "GSD_DEFAULT";

  private static final String GSD_EXIT_UNTIL_COLLAPSED = "GSD_EXIT_UNTIL_COLLAPSED";

  private static final int NUM_OF_COLUMNS = 2;

  private static final String SM_DEFAULT = "SM_DEFAULT";

  private static final String SM_EXIT_UNTIL_COLLAPSED = "SM_EXIT_UNTIL_COLLAPSED";

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
    if (data == null) {
      return;
    }
    switch (data.mKey) {
      case GSD_DEFAULT:
        break;
      case SM_DEFAULT:
        break;
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_info:
        setTitle(getString(R.string.text_info));
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
    vRecyclerView.hasFixedSize();
    vRecyclerView.setLayoutManager(new GridLayoutManager(this, NUM_OF_COLUMNS));
    vRecyclerView.setAdapter(mAdapter);
  }

  protected List<Feature> getFeatures() {
    List<Feature> features = new ArrayList<>();
    features.add(new Feature(GSD_DEFAULT, "Default"));
    features.add(new Feature(SM_DEFAULT, "Smooth Default"));
    features.add(new Feature(GSD_EXIT_UNTIL_COLLAPSED, "ExitUntilCollapsed"));
    features.add(new Feature(SM_EXIT_UNTIL_COLLAPSED, "Smooth ExitUntilCollapsed"));
    return features;
  }

  public static class Feature {

    private String mKey;

    private String mValue;

    public Feature(String key, String value) {
      mKey = key;
      mValue = value;
    }

    @Override
    public String toString() {
      return mValue;
    }
  }
}
