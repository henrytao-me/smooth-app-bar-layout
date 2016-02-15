[ ![Download](https://api.bintray.com/packages/henrytao-me/maven/smooth-app-bar-layout/images/download.svg) ](https://bintray.com/henrytao-me/maven/smooth-app-bar-layout/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-smooth--app--bar--layout-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2565)

smooth-app-bar-layout
================

This is a smooth version of `Google Support Design AppBarLayout`. If you are using `AppBarLayout`, you will know it has an issue with fling. Check out these threads to know about that problem:
[http://stackoverflow.com/questions/30923889/flinging-with-recyclerview-appbarlayout](http://stackoverflow.com/questions/30923889/flinging-with-recyclerview-appbarlayout)
[https://code.google.com/p/android/issues/detail?id=177729...](https://code.google.com/p/android/issues/detail?id=177729&q=appbarlayout&colspec=ID%20Type%20Status%20Owner%20Summary%20Stars)

If you find that it still doesn't meet your need, don't hesitate to send me a request. I love to work with you to solve problems. [Send a request HERE](https://github.com/henrytao-me/smooth-app-bar-layout/issues)


## Installation

``` groovy
compile "me.henrytao:smooth-app-bar-layout:<latest-version>"
```

`smooth-app-bar-layout` is deployed to `jCenter`. Make sure you have `jcenter()` in your project gradle.


## Tested environments

- `"com.android.support:design:23.1.1"`
- `"com.android.support:appcompat-v7:23.1.1"`


## Demo

[![Get it on Google Play](https://raw.githubusercontent.com/henrytao-me/smooth-app-bar-layout/master/screenshots/google-play.png)](https://play.google.com/store/apps/details?id=me.henrytao.smoothappbarlayout)

Please note that the app on the Play store is not always the latest version.


## Concepts

 ![concepts](./screenshots/concept.final.jpg)


## Features

- Scroll
- EnterAlways
- EnterAlwaysCollapsed
- ExitUntilCollapsed
- QuickReturn
- Custom NestedScrollView (NEW)
- ViewPager Scroll
- ViewPager ExitUntilCollapsed (NEW)
- ViewPager QuickReturn (NEW)
- Support SwipeRefreshLayout


Checkout these demo videos:

- Google Design Support AppBarLayout: [https://youtu.be/oaN2UTZIRUk](https://youtu.be/oaN2UTZIRUk)
- SmoothAppBarLayout: [https://youtu.be/la6ixCL9RKs](https://youtu.be/la6ixCL9RKs)


 ![screenshots](./screenshots/screenshots.jpg)


## Important Notes

- Remember to set `android:id` for `me.henrytao.smoothappbarlayout.SmoothAppBarLayout` in layout file so that it can call `onSaveInstanceState` and `onRestoreInstanceState` correctly. Otherwise, it won't work correctly with `onOrientationChanged`.
- Remember to set `android:minHeight` for `me.henrytao.smoothappbarlayout.SmoothAppBarLayout` in layout file if you use `ViewPager`.
- `clipToPadding` in `RecyclerView` won't work. You have to set HeaderHolder in apdater and it has to be placed at index 0.

 
## Usage

Super easy! Just need to do 3 steps:

- Change `android.support.design.widget.AppBarLayout` to `me.henrytao.smoothappbarlayout.SmoothAppBarLayout` and set `android:id`
- Remove `app:layout_behavior="@string/appbar_scrolling_view_behavior"`.
- Add header to your NestedScrollView or RecyclerView.

#### Original AppBarLayout from Google Support Design

``` xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:layout_width="match_parent"
  android:layout_height="match_parent">

  <android.support.v7.widget.RecyclerView
    android:id="@android:id/list"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/appbar_scrolling_view_behavior" />

  <android.support.design.widget.AppBarLayout
    android:layout_width="match_parent"
    android:layout_height="@dimen/app_bar_height">

    <android.support.design.widget.CollapsingToolbarLayout
      android:id="@+id/collapsing_toolbar_layout"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:layout_scrollFlags="scroll|exitUntilCollapsed">

      <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        app:layout_collapseMode="pin"
        app:navigationIcon="@drawable/ic_toolbar_arrow_back"
        style="@style/AppStyle.MdToolbar" />
    </android.support.design.widget.CollapsingToolbarLayout>
  </android.support.design.widget.AppBarLayout>
</android.support.design.widget.CoordinatorLayout>
```

#### SmoothAppBarLayout 

``` xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:layout_width="match_parent"
  android:layout_height="match_parent">

  <android.support.v7.widget.RecyclerView
    android:id="@android:id/list"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />

  <me.henrytao.smoothappbarlayout.SmoothAppBarLayout
    android:id="@+id/smooth_app_bar_layout"
    android:layout_width="match_parent"
    android:layout_height="@dimen/app_bar_height">

    <android.support.design.widget.CollapsingToolbarLayout
      android:id="@+id/collapsing_toolbar_layout"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:layout_scrollFlags="scroll|exitUntilCollapsed">

      <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        app:layout_collapseMode="pin"
        app:navigationIcon="@drawable/ic_toolbar_arrow_back"
        style="@style/AppStyle.MdToolbar" />
    </android.support.design.widget.CollapsingToolbarLayout>
  </me.henrytao.smoothappbarlayout.SmoothAppBarLayout>
</android.support.design.widget.CoordinatorLayout>
```

#### SmoothCollapsingToolbarLayout example

``` xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:layout_width="match_parent"
  android:layout_height="match_parent">

  <android.support.v7.widget.RecyclerView
    android:id="@android:id/list"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />

  <me.henrytao.smoothappbarlayout.SmoothAppBarLayout
    android:id="@+id/smooth_app_bar_layout"
    android:layout_width="match_parent"
    android:layout_height="@dimen/app_bar_height">

    <android.support.design.widget.CollapsingToolbarLayout
      android:id="@+id/collapsing_toolbar_layout"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      app:layout_scrollFlags="scroll|exitUntilCollapsed">

      <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        app:layout_collapseMode="pin"
        app:navigationIcon="@drawable/ic_toolbar_arrow_back"
        style="@style/AppStyle.MdToolbar" />

      <me.henrytao.smoothappbarlayout.SmoothCollapsingToolbarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="left|center_vertical"
        android:minHeight="?attr/actionBarSize"
        app:sctl_avatar_id="@+id/avatar"
        app:sctl_collapsed_avatarSize="?attr/mdIcon_sm"
        app:sctl_collapsed_offsetX="?attr/actionBarSize"
        app:sctl_collapsed_offsetY="0dp"
        app:sctl_collapsed_subtitleTextSize="14dp"
        app:sctl_collapsed_titleTextSize="16dp"
        app:sctl_expanded_avatarSize="?attr/mdIcon_lg"
        app:sctl_expanded_offsetX="?attr/mdLayout_spacing_md"
        app:sctl_expanded_offsetY="?attr/mdLayout_spacing_md"
        app:sctl_expanded_subtitleTextSize="16dp"
        app:sctl_expanded_titleTextSize="34dp"
        app:sctl_subtitle_id="@+id/subtitle"
        app:sctl_title_id="@+id/title">

        <ImageView
          android:id="@+id/avatar"
          android:layout_width="?attr/mdIcon_sm"
          android:layout_height="?attr/mdIcon_sm"
          android:layout_gravity="center_vertical"
          android:src="@drawable/ic_blank_circle" />

        <LinearLayout
          android:layout_width="wrap_content"
          android:layout_height="wrap_content"
          android:layout_gravity="center_vertical"
          android:layout_marginLeft="?attr/mdLayout_spacing_md"
          android:orientation="vertical">

          <TextView
            android:id="@+id/title"
            android:text="Title"
            style="@style/MdText.Title" />

          <TextView
            android:id="@+id/subtitle"
            android:text="Subtitle"
            style="@style/MdText.Body1" />
        </LinearLayout>
      </me.henrytao.smoothappbarlayout.SmoothCollapsingToolbarLayout>
    </android.support.design.widget.CollapsingToolbarLayout>
  </me.henrytao.smoothappbarlayout.SmoothAppBarLayout>
</android.support.design.widget.CoordinatorLayout>
```


## Contributing

Any contributions are welcome!  
Please check the [CONTRIBUTING](CONTRIBUTING.md) guideline before submitting a new issue. Wanna send PR? [Click HERE](https://github.com/henrytao-me/smooth-app-bar-layout/pulls)


## Donation

Let's buy me some coffee :bow:

[![](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=hungtaoquang%40gmail%2ecom&lc=VN&item_name=Henry%20Tao&item_number=smooth%2dapp%2dbar%2dlayout&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)


## License

    Copyright 2015 "Henry Tao <hi@henrytao.me>"

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

