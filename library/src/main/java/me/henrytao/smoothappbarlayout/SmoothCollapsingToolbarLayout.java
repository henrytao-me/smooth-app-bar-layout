/*
 * Copyright (C) 2015 MySQUAR. All rights reserved.
 *
 * This software is the confidential and proprietary information of MySQUAR or one of its
 * subsidiaries. You shall not disclose this confidential information and shall use it only in
 * accordance with the terms of the license agreement or other applicable agreement you entered into
 * with MySQUAR.
 *
 * MySQUAR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. MySQUAR SHALL NOT BE LIABLE FOR ANY LOSSES
 * OR DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR
 * ITS DERIVATIVES.
 */

package me.henrytao.smoothappbarlayout;

import com.squar.mychat.app.util.Ln;
import com.squar.mychat.dev.debug.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by henrytao on 9/24/15.
 */
public class SmoothCollapsingToolbarLayout extends LinearLayout {

  private static void log(Object s1, Object... args) {
    Ln.i(s1, args);
  }

  private int mAvatarId;

  private float mCollapsedAvatarSize;

  private float mCollapsedOffsetX;

  private float mCollapsedOffsetY;

  private float mCollapsedSubTitleTextSize;

  private float mCollapsedTitleTextSize;

  private float mExpandedAvatarSize;

  private float mExpandedOffsetX;

  private float mExpandedOffsetY;

  private float mExpandedSubtitleTextSize;

  private float mExpandedTitleTextSize;

  private AppBarLayout.OnOffsetChangedListener mOnAppBarLayoutOffsetChangedListener;

  private OnOffsetChangedListener mOnOffsetChangedListener;

  private int mSubtitleId;

  private int mTitleId;

  private AppBarLayout vAppBarLayout;

  private View vAvatar;

  private TextView vSubtitle;

  private TextView vTitle;

  private Toolbar vToolbar;

  public SmoothCollapsingToolbarLayout(Context context) {
    super(context);
    init(null);
  }

  public SmoothCollapsingToolbarLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public SmoothCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SmoothCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(attrs);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    initViews();
    if (!isInEditMode()) {
      mOnAppBarLayoutOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
          SmoothCollapsingToolbarLayout.this.onOffsetChanged(appBarLayout, i);
        }
      };
      getAppBarLayout().addOnOffsetChangedListener(mOnAppBarLayoutOffsetChangedListener);
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    if (mOnAppBarLayoutOffsetChangedListener != null) {
      getAppBarLayout().removeOnOffsetChangedListener(mOnAppBarLayoutOffsetChangedListener);
    }
    super.onDetachedFromWindow();
  }

  public void setOnOffsetChangedListener(OnOffsetChangedListener onOffsetChangedListener) {
    mOnOffsetChangedListener = onOffsetChangedListener;
  }

  protected AppBarLayout getAppBarLayout() {
    if (vAppBarLayout == null) {
      if (getParent() instanceof CollapsingToolbarLayout && getParent().getParent() instanceof AppBarLayout) {
        vAppBarLayout = (AppBarLayout) getParent().getParent();
      } else {
        throw new IllegalStateException("Must be inside a CollapsingToolbarLayout and AppBarLayout");
      }
    }
    return vAppBarLayout;
  }

  protected Toolbar getToolbar() {
    if (vToolbar == null) {
      int i = 0;
      ViewGroup parent = (ViewGroup) getParent();
      for (int z = parent.getChildCount(); i < z; i++) {
        if (parent.getChildAt(i) instanceof Toolbar) {
          vToolbar = (Toolbar) parent.getChildAt(i);
        }
      }
      if (vToolbar == null) {
        throw new IllegalStateException("Must have Toolbar");
      }
    }
    return vToolbar;
  }

  protected float getTranslationOffset(float expandedOffset, float collapsedOffset, float ratio) {
    return expandedOffset + ratio * (collapsedOffset - expandedOffset);
  }

  protected void init(AttributeSet attrs) {
    setOrientation(HORIZONTAL);
    TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SmoothCollapsingToolbarLayout, 0, 0);
    try {
      mCollapsedOffsetX = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_offsetX, 0);
      mCollapsedOffsetY = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_offsetY, 0);
      mCollapsedAvatarSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_avatarSize, -1);
      mCollapsedTitleTextSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_titleTextSize, -1);
      mCollapsedSubTitleTextSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_collapsed_subtitleTextSize, -1);

      mExpandedOffsetX = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_offsetX, 0);
      mExpandedOffsetY = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_offsetY, 0);
      mExpandedAvatarSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_avatarSize, -1);
      mExpandedTitleTextSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_titleTextSize, -1);
      mExpandedSubtitleTextSize = a.getDimension(R.styleable.SmoothCollapsingToolbarLayout_sctl_expanded_subtitleTextSize, -1);

      mAvatarId = a.getResourceId(R.styleable.SmoothCollapsingToolbarLayout_sctl_avatar_id, 0);
      mTitleId = a.getResourceId(R.styleable.SmoothCollapsingToolbarLayout_sctl_title_id, 0);
      mSubtitleId = a.getResourceId(R.styleable.SmoothCollapsingToolbarLayout_sctl_subtitle_id, 0);
    } finally {
      a.recycle();
    }
  }

  protected void initViews() {
    updateViews(0);
    View view;
    if (mAvatarId > 0) {
      vAvatar = findViewById(mAvatarId);
    }
    if (mTitleId > 0) {
      view = findViewById(mTitleId);
      vTitle = view == null ? null : (TextView) view;
    }
    if (mSubtitleId > 0) {
      view = findViewById(mSubtitleId);
      vSubtitle = view == null ? null : (TextView) view;
    }
  }

  protected boolean isAvatarSizeEnabled() {
    return vAvatar != null && mCollapsedAvatarSize > 0 && mExpandedAvatarSize > 0;
  }

  protected boolean isSubtitleTextSizeEnabled() {
    return vSubtitle != null && mCollapsedSubTitleTextSize > 0 && mExpandedSubtitleTextSize > 0;
  }

  protected boolean isTitleTextSizeEnabled() {
    return vTitle != null && mCollapsedTitleTextSize > 0 && mExpandedTitleTextSize > 0;
  }

  protected void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    int maxOffset = getAppBarLayout().getMeasuredHeight() - getToolbar().getMeasuredHeight();
    float ratio = Math.min(Math.abs(verticalOffset) * 1.0f / maxOffset, 1.0f);
    SmoothCollapsingToolbarLayout.this.updateViews(ratio);
    log("test onOffsetChanged collapsing | %d | %f", verticalOffset, ratio);
  }

  protected void updateViews(float ratio) {
    int startOffsetX = 0;
    int startOffsetY = getAppBarLayout().getMeasuredHeight() - getMeasuredHeight();
    ViewCompat.setTranslationX(this, startOffsetX + getTranslationOffset(mExpandedOffsetX, mCollapsedOffsetX, ratio));
    ViewCompat.setTranslationY(this, startOffsetY - getTranslationOffset(mExpandedOffsetY, mCollapsedOffsetY, ratio));
    if (isAvatarSizeEnabled()) {
      ViewGroup.LayoutParams params = vAvatar.getLayoutParams();
      params.height = params.width = (int) getTranslationOffset(mExpandedAvatarSize, mCollapsedAvatarSize, ratio);
    }
    if (isTitleTextSizeEnabled()) {
      vTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTranslationOffset(mExpandedTitleTextSize, mCollapsedTitleTextSize, ratio));
    }
    if (isSubtitleTextSizeEnabled()) {
      vSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTranslationOffset(mExpandedSubtitleTextSize, mCollapsedSubTitleTextSize, ratio));
    }
    if (mOnOffsetChangedListener != null) {
      mOnOffsetChangedListener.onOffsetChanged(ratio);
    }
    log("test updateViews | %d | %f", (int) (mExpandedAvatarSize + ratio * (mCollapsedAvatarSize - mExpandedAvatarSize)), ratio);
  }

  public interface OnOffsetChangedListener {

    void onOffsetChanged(float ratio);
  }
}
