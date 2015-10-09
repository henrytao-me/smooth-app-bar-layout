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

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.henrytao.smoothappbarlayout.PagerAdapter;

/**
 * Created by henrytao on 10/3/15.
 */
public class ViewPagerRunnableAdapter extends BaseViewPagerAdapter implements PagerAdapter.OnSyncOffsetRunnable {

  public ViewPagerRunnableAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public void onSyncOffset(int position, final int offset, final Runnable callback) {
    final View scrollView = getScrollView(position);
    if (scrollView instanceof RecyclerView) {
      scrollView.postDelayed(new Runnable() {
        @Override
        public void run() {
          if (offset == 0) {
            ((RecyclerView) scrollView).scrollToPosition(0);
          } else {
            scrollView.scrollBy(0, offset - ((RecyclerView) scrollView).computeVerticalScrollOffset());
          }
          if (callback != null) {
            callback.run();
          }
        }
      }, FAKE_DELAY);
    }
  }
}
