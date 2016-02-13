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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayout.base.ObservableFragment;
import me.henrytao.smoothappbarlayout.base.Utils;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.DynamicAdapter;

public class PagerWithHeaderFragment extends Fragment implements ObservableFragment {

  protected static final String ARG_IS_RECYCLER_VIEW = "ARG_IS_RECYCLER_VIEW";

  protected static final String ARG_IS_SHORT = "ARG_IS_SHORT";

  public static Fragment newInstance(boolean isRecyclerView, boolean isShort) {
    PagerWithHeaderFragment fragment = new PagerWithHeaderFragment();
    Bundle bundle = new Bundle();
    bundle.putBoolean(ARG_IS_RECYCLER_VIEW, isRecyclerView);
    bundle.putBoolean(ARG_IS_SHORT, isShort);
    fragment.setArguments(bundle);
    return fragment;
  }

  protected DynamicAdapter<String> mAdapter;

  @Bind(R.id.nested_scroll_view)
  NestedScrollView vNestedScrollView;

  @Bind(R.id.recycler_view)
  RecyclerView vRecyclerView;

  @Bind(R.id.text)
  TextView vTextView;

  private boolean mIsRecyclerView;

  private boolean mIsShort;

  public PagerWithHeaderFragment() {
  }

  @Override
  public View getScrollTarget() {
    return mIsRecyclerView ? vRecyclerView : vNestedScrollView;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_pager_with_header, container, false);
    ButterKnife.bind(this, view);
    mIsRecyclerView = getArguments().getBoolean(ARG_IS_RECYCLER_VIEW);
    mIsShort = getArguments().getBoolean(ARG_IS_SHORT);
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override
  public boolean onOffsetChanged(SmoothAppBarLayout smoothAppBarLayout, View target, int verticalOffset) {
    return Utils.syncOffset(smoothAppBarLayout, target, verticalOffset, getScrollTarget());
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    vNestedScrollView.setVisibility(mIsRecyclerView ? View.GONE : View.VISIBLE);
    vRecyclerView.setVisibility(mIsRecyclerView ? View.VISIBLE : View.GONE);

    if (mIsRecyclerView) {
      mAdapter = new DynamicAdapter<>(getSampleData());
      RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(mAdapter) {
        @Override
        public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
          return null;
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
          return new HeaderHolder(layoutInflater, viewGroup, R.layout.item_pager_header_spacing);
        }
      };

      vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      vRecyclerView.setAdapter(adapter);
    } else {
      vTextView.setText(mIsShort ? R.string.text_short : R.string.text_long);
    }
  }

  protected List<String> getSampleData() {
    return me.henrytao.smoothappbarlayoutdemo.util.Utils.getSampleData(mIsShort ? 4 : 40);
  }
}
