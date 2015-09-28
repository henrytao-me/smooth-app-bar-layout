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
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.config.Constants.Feature;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdAvatarFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdDefaultFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdExitUntilCollapsedFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.GsdParallaxFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothAvatarFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothDefaultFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothExitUntilCollapsedFragment;
import me.henrytao.smoothappbarlayoutdemo.fragment.SmoothParallaxFragment;

public class FeatureActivity extends AppCompatActivity {

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
    } else if (Feature.GSD_PARALLAX.equal(feature)) {
      fragment = GsdParallaxFragment.newInstance();
    } else if (Feature.SMOOTH_PARALLAX.equal(feature)) {
      fragment = SmoothParallaxFragment.newInstance();
    } else if (Feature.GSD_AVATAR.equal(feature)) {
      fragment = GsdAvatarFragment.newInstance();
    } else if (Feature.SMOOTH_AVATAR.equal(feature)) {
      fragment = SmoothAvatarFragment.newInstance();
    }
    return fragment;
  }
}
