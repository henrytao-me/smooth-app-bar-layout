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

package me.henrytao.smoothappbarlayoutdemo.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrytao on 1/31/16.
 */
public class Utils {

  private static final int ITEM_COUNT = 10;

  public static List<String> getSampleData() {
    return getSampleData(ITEM_COUNT);
  }

  public static List<String> getSampleData(int count) {
    List<String> data = new ArrayList<>();
    int i = 0;
    for (int n = count; i < n; i++) {
      data.add(String.format("Line %d", i));
    }
    return data;
  }

  public static void log(String s, Object... args) {
    Log.d("SmoothAppBarLayoutDemo", String.format(s, args));
  }
}
