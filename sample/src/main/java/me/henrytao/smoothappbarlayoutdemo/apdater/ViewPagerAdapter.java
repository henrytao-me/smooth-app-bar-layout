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

package me.henrytao.smoothappbarlayoutdemo.apdater;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.henrytao.smoothappbarlayout.PagerAdapter;

/**
 * Created by henrytao on 10/3/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter implements PagerAdapter {

  private final List<Fragment> mFragments = new ArrayList<>();

  private final List<Integer> mScrollViewIds = new ArrayList<>();

  private final Map<Integer, View> mScrollViews = new HashMap<>();

  private final List<String> mTitles = new ArrayList<>();

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public int getCount() {
    return mFragments.size();
  }

  @Override
  public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mTitles.get(position);
  }

  @Override
  public View getScrollView(int position) {
    View view = getItem(position).getView();
    if (view != null) {
      mScrollViews.put(position, view.findViewById(mScrollViewIds.get(position)));
    }
    return mScrollViews.get(position);
  }

  public void addFragment(Fragment fragment, String title) {
    addFragment(fragment, title, 0);
  }

  public void addFragment(Fragment fragment, String title, @IdRes int scrollViewId) {
    mFragments.add(fragment);
    mTitles.add(title);
    mScrollViewIds.add(scrollViewId);
  }
}
