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

package me.henrytao.smoothappbarlayoutdemo.config;

import android.text.TextUtils;

/**
 * Created by henrytao on 9/27/15.
 */
public class Constants {

  public enum Feature {
    GSD_DEFAULT("GSD_DEFAULT"), SMOOTH_DEFAULT("SMOOTH_DEFAULT"),
    GSD_EXIT_UNTIL_COLLAPSED("GSD_EXIT_UNTIL_COLLAPSED"), SMOOTH_EXIT_UNTIL_COLLAPSED("SMOOTH_EXIT_UNTIL_COLLAPSED"),
    GSD_PARALLAX("GSD_PARALLAX"), SMOOTH_PARALLAX("SMOOTH_PARALLAX"),
    GSD_AVATAR("GSD_AVATAR"), SMOOTH_AVATAR("SMOOTH_AVATAR"),
    GSD_ENTER_ALWAYS("GSD_ENTER_ALWAYS"), SMOOTH_ENTER_ALWAYS("SMOOTH_ENTER_ALWAYS"),
    GSD_ENTER_ALWAYS_COLLAPSED("GSD_ENTER_ALWAYS_COLLAPSED"), SMOOTH_ENTER_ALWAYS_COLLAPSED("SMOOTH_ENTER_ALWAYS_COLLAPSED"),
    GSD_NESTED_SCROLL_VIEW_PARALLAX("GSD_NESTED_SCROLL_VIEW_PARALLAX"), SMOOTH_NESTED_SCROLL_VIEW_PARALLAX(
        "SMOOTH_NESTED_SCROLL_VIEW_PARALLAX"),
    GSD_NESTED_SCROLL_VIEW_PARALLAX_2("GSD_NESTED_SCROLL_VIEW_PARALLAX_2"), SMOOTH_NESTED_SCROLL_VIEW_PARALLAX_2(
        "SMOOTH_NESTED_SCROLL_VIEW_PARALLAX_2"),
    GSD_VIEW_PAGER("GSD_VIEW_PAGER"), SMOOTH_VIEW_PAGER("SMOOTH_VIEW_PAGER");

    private String mName;

    Feature(String name) {
      mName = name;
    }

    @Override
    public String toString() {
      return mName;
    }

    public boolean equal(String name) {
      return TextUtils.equals(name, mName);
    }
  }
}
