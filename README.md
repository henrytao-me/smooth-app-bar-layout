[ ![Download](https://api.bintray.com/packages/henrytao-me/maven/recyclerview/images/download.svg) ](https://bintray.com/henrytao-me/maven/recyclerview/_latestVersion)

android-md-core
================

`AppCompat` and `Google Support Design` are really awesome but they also have some limitations. Theming android app is not an easy task, especially keeping consistent UI on both Lollipop and Non-Lollipop devices is even harder. If you've tried `CardView`, `Spinner`, `Theme`, you know what I mean. So, I propose `android-md-core` - Material bootstrap for Android development like Bootstrap or Foundation for web.

I am working hard to predefine element styles as many as I can. If you find that it still doesn't meet your need, don't hesitate to send me a request. I love to work with you to solve problems. [Send a request HERE](https://github.com/henrytao-me/android-md-core/issues)


## Installation

```
compile "me.henrytao:mdcore:<latest-version>"
```

`android-md-core` is deployed to `jCenter`. Make sure you have `jcenter()` in your project gradle.


## Demo

[![Get it on Google Play](https://developer.android.com/images/brand/en_generic_rgb_wo_45.png)](https://play.google.com/store/apps/details?id=me.henrytao.mdcore)

Please note that the app on the Play store is not always the latest version.


## Concepts
`android-md-core` is designed base on some priciples:

- **Widely support:** based on `appcompat` and `google support design` for sure.
- **Simplicity:** used `attribute-based` theme system rather than `value-based` theme system. This increases the flexibility to have multiple theme in one app. 
- **Ease to use:** predefined style for most of base elements. So that, you can use easily like `Bootstrap` or `Foundation` for web.
- **Consistency:** consistent UI for both Lollipop and Non-Lollipop devices. 
- **Modern:** keep up to date with [Google Material Design](https://www.google.com/design/spec/material-design/introduction.html).


## Features

 ![Button](./screenshots/all-in-one.jpg)

More? I are working hard to predefine more and more elements. They are coming soon.


## Usage

#### Step 1. Define your theme in `themes.xml`

Please checkout [Google Material Color Section](https://www.google.com/design/spec/style/color.html) to understand about app color. Here are my summaries: 

- Limit your selection of colors by choosing three hues from the primary palette and one accent color from the secondary palette.
- There are 4 main palettes: `primary, accent, warn and background`. Each palette has 4 main colors: `normal, dark, darker and lighter`. It will end up with 16 colors that being used across the app. 
- LightTheme is `LIGHT` background on `BLACK` text color.
- DarkTheme is `DARK` background on `WHITE` text color. 

```
<resources>

  <!-- AppTheme based on LightTheme -->
  <style name="AppTheme" parent="MdTheme">

  </style>

  <!-- AppTheme.Purple based on DarkTheme -->
  <style name="AppTheme.Purple" parent="MdTheme.Dark">
    <item name="mdColor_primaryPalette">#9C27B0</item>
    <item name="mdColor_primaryPalette_dark">#7B1FA2</item>
    <item name="mdColor_primaryPalette_darker">#6A1B9A</item>
    <item name="mdColor_primaryPalette_light">#CE93D8</item>

    <item name="mdColor_accentPalette">#8BC34A</item>
    <item name="mdColor_accentPalette_dark">#689F38</item>
    <item name="mdColor_accentPalette_darker">#558B2F</item>
    <item name="mdColor_accentPalette_light">#AED581</item>
  </style>
</resources>
```

#### Step 2. Apply predefined styles to your app

Please checkout sample section in repo. Below is how you define singleline ListItem with icon and divider. 

```
<RelativeLayout
  android:background="@drawable/md_ripple"
  android:clickable="true"
  style="@style/MdList.SingleLine.IconWithText">

  <ImageView
    android:src="@drawable/ic_blank"
    style="@style/MdList.SingleLine.IconWithText.Icon" />

  <TextView
    android:text="@string/text_title"
    style="@style/MdList.SingleLine.IconWithText.Text" />

  <View style="@style/MdDivider.AlignParentBottom" />
</RelativeLayout>
```


## Contributing

Any contributions are welcome!  
Please check the [CONTRIBUTING](CONTRIBUTING.md) guideline before submitting a new issue. Wanna send PR? [Click HERE](https://github.com/henrytao-me/android-md-core/pulls)


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

