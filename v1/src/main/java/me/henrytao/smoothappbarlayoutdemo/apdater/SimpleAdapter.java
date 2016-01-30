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

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.henrytao.smoothappbarlayoutdemo.R;

/**
 * Created by henrytao on 9/27/15.
 */
public class SimpleAdapter<T> extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

  private final List<T> mData;

  private final OnItemClickListener mOnItemClickListener;

  public SimpleAdapter(List<T> data, OnItemClickListener<T> onItemClickListener) {
    mData = data;
    mOnItemClickListener = onItemClickListener;
  }

  @Override
  public int getItemCount() {
    return mData != null ? mData.size() : 0;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext()), parent, mOnItemClickListener);
  }

  public T getItem(int position) {
    return mData != null && position >= 0 && position < mData.size() ? mData.get(position) : null;
  }

  public interface OnItemClickListener<I> {

    void onItemClick(I data);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    private static View createView(LayoutInflater inflater, ViewGroup parent, @LayoutRes int layoutId) {
      return inflater.inflate(layoutId, parent, false);
    }

    @Bind(R.id.title)
    TextView vTitle;

    private Object mData;

    public ViewHolder(LayoutInflater inflater, ViewGroup parent, final OnItemClickListener onItemClickListener) {
      super(createView(inflater, parent, R.layout.item_simple));
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (onItemClickListener != null) {
            onItemClickListener.onItemClick(mData);
          }
        }
      });
    }

    public void bind(Object data) {
      mData = data;
      vTitle.setText(data.toString());
    }
  }
}
