/*
 * Copyright 2016 "Henry Tao <hi@henrytao.me>"
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

package me.henrytao.smoothappbarlayoutdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.base.ObservableFragment;
import me.henrytao.smoothappbarlayout.base.Utils;
import me.henrytao.smoothappbarlayoutdemo.R;

public class PagerWithHeaderFragment extends Fragment implements ObservableFragment {

  public static Fragment newInstance() {
    PagerWithHeaderFragment fragment = new PagerWithHeaderFragment();
    return fragment;
  }

  @Bind(android.R.id.list)
  NestedScrollView vNestedScrollView;

  public PagerWithHeaderFragment() {
  }

  @Override
  public View getScrollTarget() {
    return vNestedScrollView;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_pager_with_header, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public void onOffsetChanged(SmoothAppBarLayout smoothAppBarLayout, int verticalOffset, boolean isSelected, boolean isTop) {
    Utils.syncOffset(smoothAppBarLayout, verticalOffset, isSelected, isTop, vNestedScrollView);
    //if (vNestedScrollView != null && (vNestedScrollView.getScrollY() < verticalOffset || (isTop && !isSelected))) {
    //  vNestedScrollView.scrollTo(0, verticalOffset);
    //}
  }
}
