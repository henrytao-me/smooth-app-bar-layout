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

package me.henrytao.smoothappbarlayoutdemo.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayoutdemo.R;

public class DummyNestedScrollViewFragment extends Fragment implements ObservableScrollView {

  private static final String ARG_HEADER_LAYOUT = "ARG_HEADER_LAYOUT";

  private static final String ARG_TEXT = "ARG_TEXT";

  public static DummyNestedScrollViewFragment newInstance(String text) {
    return DummyNestedScrollViewFragment.newInstance(text, 0);
  }

  public static DummyNestedScrollViewFragment newInstance(String text, @LayoutRes int headerLayout) {
    DummyNestedScrollViewFragment fragment = new DummyNestedScrollViewFragment();
    Bundle bundle = new Bundle();
    bundle.putString(ARG_TEXT, text);
    bundle.putInt(ARG_HEADER_LAYOUT, headerLayout);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Bind(R.id.frame_layout)
  FrameLayout vFrameLayout;

  @Bind(android.R.id.list)
  NestedScrollView vNestedScrollView;

  @Bind(R.id.text)
  TextView vText;

  public DummyNestedScrollViewFragment() {
    // Required empty public constructor
  }

  @Override
  public View getScrollView() {
    if (isAdded()) {
      return vNestedScrollView;
    }
    return null;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dummy_nested_scroll_view, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    vText.setText(getArgText());
    int headerLayout = getArgHeaderLayout();
    if (headerLayout > 0) {
      vFrameLayout.removeAllViews();
      vFrameLayout.addView(LayoutInflater.from(getContext()).inflate(headerLayout, null));
    }
  }

  private int getArgHeaderLayout() {
    Bundle bundle = getArguments();
    if (bundle != null) {
      return bundle.getInt(ARG_HEADER_LAYOUT);
    }
    return 0;
  }

  private String getArgText() {
    Bundle bundle = getArguments();
    if (bundle != null) {
      return bundle.getString(ARG_TEXT);
    }
    return "";
  }
}
