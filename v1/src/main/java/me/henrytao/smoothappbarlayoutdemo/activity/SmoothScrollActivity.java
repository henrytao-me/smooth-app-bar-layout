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

package me.henrytao.smoothappbarlayoutdemo.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayoutdemo.R;
import me.henrytao.smoothappbarlayoutdemo.apdater.DynamicAdapter;
import me.henrytao.smoothappbarlayoutdemo.util.Utils;

public class SmoothScrollActivity extends BaseActivity {

  @Bind(android.R.id.list)
  RecyclerView vRecyclerView;

  @Bind(R.id.toolbar)
  Toolbar vToolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_smooth_scroll);
    ButterKnife.bind(this);

    setSupportActionBar(vToolbar);
    vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    vRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    vRecyclerView.setAdapter(new DynamicAdapter(Utils.getSampleData()));

    vRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        Utils.log("onScrollStateChanged %d", newState);
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int childCount = vRecyclerView.getChildCount();
        int firstVisiblePosition = 0, firstCompletelyVisiblePosition = 0, lastVisiblePosition = 0, lastCompletelyVisiblePosition = 0;
        int verticalScrollOffset = 0;
        if (vRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
          LinearLayoutManager layoutManager = (LinearLayoutManager) vRecyclerView.getLayoutManager();
          firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
          firstCompletelyVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
          lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
          lastCompletelyVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        }

        View view = vRecyclerView.getLayoutManager().findViewByPosition(0);
        Utils.log("onScrolled %d | %d | %d |-| %d | %d | %d | %d |-| %d |-| %b | %d | %d",
            dx, dy, childCount,
            firstVisiblePosition, firstCompletelyVisiblePosition, lastCompletelyVisiblePosition, lastVisiblePosition,
            verticalScrollOffset,
            view != null, vRecyclerView.computeVerticalScrollOffset(), view != null ? view.getMeasuredHeight() : 0);
        super.onScrolled(recyclerView, dx, dy);
      }
    });

    vRecyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
        super.onChanged();
        Utils.log("registerAdapterDataObserver | onChanged");
      }

      @Override
      public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        Utils.log("registerAdapterDataObserver | onItemRangeChanged | %d | %d", positionStart, itemCount);
      }

      @Override
      public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        Utils.log("registerAdapterDataObserver | onItemRangeChanged2 | %d | %d | %s", positionStart, itemCount,
            payload != null ? payload.toString() : "");
      }

      @Override
      public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        Utils.log("registerAdapterDataObserver | onItemRangeInserted | %d | %d", positionStart, itemCount);
      }

      @Override
      public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        Utils.log("registerAdapterDataObserver | onItemRangeMoved | %d | %d | %d", fromPosition, toPosition, itemCount);
      }

      @Override
      public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        Utils.log("registerAdapterDataObserver | onItemRangeRemoved | %d | %d", positionStart, itemCount);
      }
    });
  }
}
