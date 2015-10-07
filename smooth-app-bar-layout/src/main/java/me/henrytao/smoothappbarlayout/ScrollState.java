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

package me.henrytao.smoothappbarlayout;

/**
 * Created by henrytao on 10/5/15.
 */
public class ScrollState {

  private int mIdealOffset;

  private int mOffset = 0;

  private State mState = State.DEFAULT;

  public ScrollState() {
  }

  public int getOffset() {
    if (mState == State.DEFAULT) {
      return mIdealOffset;
    }
    return Math.max(mOffset, mIdealOffset);
  }

  public void setOffset(int offset, int idealOffset) {
    mOffset = mState == State.SCROLLED ? Math.max(mOffset, offset) : offset;
    mIdealOffset = idealOffset;
  }

  public void setState(State state) {
    mState = state;
    if (mState == State.DEFAULT) {
      mOffset = 0;
    }
  }

  public enum State {
    DEFAULT, SCROLLED
  }
}
