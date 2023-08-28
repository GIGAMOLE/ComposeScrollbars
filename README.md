[![](/media/header.png)](https://intive.com/)

![](https://jitpack.io/v/GIGAMOLE/ComposeScrollbars.svg?style=flat-square) | [Setup Guide](#setup)
| [Report new issue](https://github.com/GIGAMOLE/ComposeScrollbars/issues/new)

# ComposeScrollbars

The `ComposeScrollbars` is a feature-rich Android Compose UI library that seamlessly incorporates customisable scrollbars, including size, animations, background/knob layer style, and scroll behavior, for a seamless UX.

![](/media/demo.gif)

Features:

- **Advanced Customization:** Customize scrollbar size, orientation, gravity and visibility animation.
- **Layers Configuration:** Configure background and knob layers with style, appearance and animation.
- **Knob Scroll Behavior:** Choose from static, worm, fraction, or exact scroll behaviors for the knob.
- **Scroll States Support:** Ready for ScrollState, LazyListState, LazyGridState, LazyStaggeredGridState.
- **Custom Content Draw:** Design custom backgrounds and knobs when required.
- **Sample App:** Explore and experiment with [sample app](#sample-app).

## Sample App

| Sample 1 | Sample 2 | Sample 3 |
|-|-|-|
| <img src="/media/sample-1.gif" width="248"/> | <img src="/media/sample-2.gif" width="248"/> | <img src="/media/sample-3.gif" width="248"/> |

| Sample 4 | Sample 5 | Sample 6 |
|-|-|-|
| <img src="/media/sample-4.gif" width="248"/> | <img src="/media/sample-5.gif" width="248"/> | <img src="/media/sample-6.gif" width="248"/> |

Download or clone this repository to discover the sample app.

## Setup

Add to the root `build.gradle.kts`:

``` groovy
allprojects {
    repositories {
        ...
        maven("https://jitpack.io")
    }
}
```

Add to the package `build.gradle.kts`:

``` groovy
dependencies {
    implementation("com.github.GIGAMOLE:ComposeScrollbars:{latest-version}")
}
```

Latest version: ![](https://jitpack.io/v/GIGAMOLE/ComposeScrollbars.svg?style=flat-square).

Also, it's possible to download the latest artifact from the [releases page](https://github.com/GIGAMOLE/ComposeScrollbars/releases).

## Guide

The `ComposeScrollbars` comes with the main component: [`Scrollbars`](#scrollbars).

For more technical and detailed documentation, read the library `KDoc`.

### Scrollbars

The `Scrollbars` presents scrollbars based on the provided [`ScrollbarsState`](#scrollbarsstate).

Just place it on top of your scrollable content.

### ScrollbarsState

The `ScrollbarsState` consists of two required components: [`ScrollbarsConfig`](#scrollbarsconfig) and [`ScrollbarsScrollType`](#scrollbarsscrolltype).

To create a `ScrollbarsState`, use one of the utility functions: `rememberScrollbarsState(...)` or make it on your own.

#### ScrollbarsConfig

The `ScrollbarsConfig` setups the scrollbars layouts, styles and appearances:

|Param|Description|
|-|-|
|`orientation`|The scrollbars orientation: `Horizontal` or `Vertical`.|
|`gravity`|The scrollbars gravity: `Start` or `End`.|
|`paddingValues`|The scrollbars layers container padding values.|
|`sizeType`|The scrollbars layers container size: `Full`, `Fraction` or `Exact`.|
|`layersType`|The [`ScrollbarsLayersType`](#scrollbarslayerstype).|
|`backgroundLayerContentType`|The background [`ScrollbarsLayerContentType`](#scrollbarslayercontenttype).|
|`knobLayerContentType`|The knob [`ScrollbarsLayerContentType`](#scrollbarslayercontenttype).|
|`visibilityType`|The [`ScrollbarsVisibilityType`](#scrollbarsvisibilitytype).|

##### ScrollbarsLayersType

The `ScrollbarsLayersType` can be one of the following:

- `Wrap`: Wraps a knob layer into a background layer. The layers are centered.
- `Split`: Splits a knob and a background layer into each own configurable layer.

Each mode can set layers thickness(`Exact` or `Wrap`), padding values, and layer gravity (`Start`, `Center` or `End`).

##### ScrollbarsLayerContentType

The `ScrollbarsLayerContentType` can be one of the following:

- `None`: The empty (not visible) layer content.
- `Custom`: Provides custom layer content via the `@Composable` lambda.
- `Default`: The default layer content.

The `Default` mode can set the content layer shape, style (`Backgrond` or `Border`), color (`Idle` or `IdleActive`).

##### ScrollbarsVisibilityType

The `ScrollbarsVisibilityType` can be one of the following:

- `Static`: The static scrollbars visibility. Always visible.
- `Dynamic`: The dynamic scrollbars visibility. Includes in/out animation, fading and other.

The `Dynamic` mode can be one of the following:

- `Fade`: The dynamic visibility with only fade.
- `Slide`: The dynamic visibility with the slide, and optional fade.
- `Scale`: The dynamic visibility with the scale, and optional fade.

The `Dynamic` mode can the following UX utility params:

- `isVisibleWhenScrollNotPossible`: Indicates whether scrollbars are visible when the scroll is not possible (short content).
- `isVisibleOnTouchDown`: Indicates whether scrollbars are visible when any press/touch down event occurred.

#### ScrollbarsScrollType

The `ScrollbarsScrollType` can be one of the following:

- `Scroll`: The scrollbars for a `ScrollState` content.
- `Lazy.List`: The scrollbars for a `LazyListState` content.
- `Lazy.Grid`: The scrollbars for a `LazyGridState` content.
- `Lazy.StaggeredGrid`: The scrollbars for a `LazyStaggeredGridState` content.

The `Lazy.List` and `Lazy.Grid` supports the scrollbars for `Static` or `Dynamic` items heights.

The `Lazy.StaggeredGrid` only supports the scrollbars for `Dynamic` items heights.

Each mode can set the knob size type:

- `Auto`: The scrollbars knob with an automatic size.
- `Exact`: The scrollbars knob with an exact size.
- `Fraction`: The scrollbars knob with a fraction size.
- `Worm`: The scrollbars knob with a size, which represents current visible items as a section or with sub-interpolation. Only available for `Dynamic` item heights.

## License

MIT License. See the [LICENSE](https://github.com/GIGAMOLE/ComposeScrollbars/blob/master/LICENSE) file for more details.

## Credits

Special thanks to the [GoDaddy](https://github.com/godaddy) for the amazing [color picker library](https://github.com/godaddy/compose-color-picker).

Created at [intive](https://intive.com).  
**We spark digital excitement.**

[![](/media/credits.png)](https://intive.com/)

## Author:

[Basil Miller](https://www.linkedin.com/in/gigamole/)  
[gigamole53@gmail.com](mailto:gigamole53@gmail.com)

[![](/media/footer.png)](https://intive.com/careers)
