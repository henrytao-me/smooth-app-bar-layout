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

import com.yalantis.phoenix.PullToRefreshView;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.SimpleAdapter;

public class SmoothCustomPullToRefreshFragment extends BaseFeatureFragment implements PullToRefreshView.OnRefreshListener {

  public static Fragment newInstance() {
    return new SmoothCustomPullToRefreshFragment();
  }

  @Bind(R.id.pull_to_refresh)
  PullToRefreshView vPullToRefresh;

  @Bind(android.R.id.list)
  RecyclerView vRecyclerView;

  @Override
  public int getContentLayout() {
    return R.layout.fragment_smooth_custom_pull_to_refresh;
  }

  @Override
  public void onRefresh() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (isAdded() && vPullToRefresh != null) {
          vPullToRefresh.setRefreshing(false);
        }
      }
    }, 2000);
  }

  @Override
  public void onViewCreated(final View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // This is RecyclerViewWrapperAdapter. Checkout at https://github.com/henrytao-me/recyclerview-multistate-section-endless-adapter
    RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(new SimpleAdapter<>(getSampleData(), null)) {
      @Override
      public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
      }

      @Override
      public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return new HeaderHolder(layoutInflater, viewGroup, R.layout.item_header_spacing);
      }
    };
    vRecyclerView.hasFixedSize();
    vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    vRecyclerView.setAdapter(adapter);

    vPullToRefresh.setRefreshViewPadding(0, getContext().getResources().getDimensionPixelOffset(R.dimen.app_bar_height), 0, 0);
    vPullToRefresh.setOnRefreshListener(this);
  }
}
