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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.SimpleAdapter;

public class DummyRecyclerViewFragment extends Fragment implements ObservableScrollView {

  private static final String ARG_COUNT = "ARG_COUNT";

  private static final String ARG_HEADER_LAYOUT = "ARG_HEADER_LAYOUT";

  private static final String ARG_TITLE = "ARG_TITLE";

  public static DummyRecyclerViewFragment newInstance(String title, int count) {
    return DummyRecyclerViewFragment.newInstance(title, count, 0);
  }

  public static DummyRecyclerViewFragment newInstance(String title, int count, @LayoutRes int headerLayout) {
    DummyRecyclerViewFragment fragment = new DummyRecyclerViewFragment();
    Bundle bundle = new Bundle();
    bundle.putString(ARG_TITLE, title);
    bundle.putInt(ARG_COUNT, count);
    bundle.putInt(ARG_HEADER_LAYOUT, headerLayout);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Bind(android.R.id.list)
  RecyclerView vRecyclerView;

  public DummyRecyclerViewFragment() {
    // Required empty public constructor
  }

  @Override
  public View getScrollView() {
    if (isAdded()) {
      return vRecyclerView;
    }
    return null;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dummy_recycler_view, container, false);
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

    // This is RecyclerViewWrapperAdapter. Checkout at https://github.com/henrytao-me/recyclerview-multistate-section-endless-adapter
    RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(new SimpleAdapter<>(getSampleData(getArgTitle(), getArgCount()), null)) {
      @Override
      public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
      }

      @Override
      public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        int headerLayout = getArgHeaderLayout();
        if (headerLayout > 0) {
          return new HeaderHolder(layoutInflater, viewGroup, headerLayout);
        }
        return null;
      }
    };
    vRecyclerView.hasFixedSize();
    vRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    vRecyclerView.setAdapter(adapter);
  }

  protected List<String> getSampleData(String title, int count) {
    List<String> data = new ArrayList<>();
    int i = 0;
    for (int n = count; i < n; i++) {
      data.add(String.format("%s %d", title, i));
    }
    return data;
  }

  private int getArgCount() {
    Bundle bundle = getArguments();
    if (bundle != null) {
      return bundle.getInt(ARG_COUNT);
    }
    return 0;
  }

  private int getArgHeaderLayout() {
    Bundle bundle = getArguments();
    if (bundle != null) {
      return bundle.getInt(ARG_HEADER_LAYOUT);
    }
    return 0;
  }

  private String getArgTitle() {
    Bundle bundle = getArguments();
    if (bundle != null) {
      return bundle.getString(ARG_TITLE);
    }
    return "";
  }
}
