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

package me.henrytao.smoothappbarlayout.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.henrytao.smoothappbarlayout.R;

/**
 * Created by henrytao on 2/3/16.
 */
public class ObservableRecyclerView implements Observer {

  public static final int HEADER_VIEW_POSITION = 0;

  public static ObservableRecyclerView newInstance(@NonNull RecyclerView recyclerView, OnScrollListener onScrollListener) {
    ObservableRecyclerView observable = new ObservableRecyclerView(recyclerView);
    observable.setOnScrollListener(onScrollListener);
    return observable;
  }

  private OnScrollListener mOnScrollListener;

  private RecyclerView mRecyclerView;

  protected ObservableRecyclerView(@NonNull RecyclerView recyclerView) {
    mRecyclerView = recyclerView;
    if (mRecyclerView.getTag(R.id.tag_observable_view) == null) {
      mRecyclerView.setTag(R.id.tag_observable_view, true);
      init();
    }
  }

  @Override
  public View getView() {
    return mRecyclerView;
  }

  @Override
  public void setOnScrollListener(OnScrollListener onScrollListener) {
    mOnScrollListener = onScrollListener;
  }

  private void init() {
    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mOnScrollListener != null) {
          mOnScrollListener.onScrollChanged(recyclerView,
              recyclerView.computeHorizontalScrollOffset(), recyclerView.computeVerticalScrollOffset(),
              dx, dy,
              recyclerView.getLayoutManager().findViewByPosition(HEADER_VIEW_POSITION) != null);
        }
      }
    });

    mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Utils.log("ObservableRecyclerView | %d | %d | %d | %d", left, top, right, bottom);
        onAdapterChanged();
      }
    });
  }

  private void onAdapterChanged() {
    if (mOnScrollListener != null) {
      mOnScrollListener.onScrollChanged(mRecyclerView,
          mRecyclerView.computeHorizontalScrollOffset(), mRecyclerView.computeVerticalScrollOffset(),
          0, 0,
          mRecyclerView.getLayoutManager().findViewByPosition(HEADER_VIEW_POSITION) != null);
    }
  }
}
