![](https://raw.githubusercontent.com/sunxiaolei/loop-timepicker-view/master/app/device.gif)

##Installation
[![](https://jitpack.io/v/sunxiaolei/loop-timepicker-view.svg)](https://jitpack.io/#sunxiaolei/loop-timepicker-view)

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
	compile 'com.github.sunxiaolei:loop-timepicker-view:v1.0.0'
}
```

##Getting Started
```xml
<sunxl8.timepicker.LoopTimePickerView
	android:id="@+id/view"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"/>
```

```java
view.setViewMode(LoopTimePickerView.VIEW_MODE_DATE_ONLY);
view.setDateTitle("标题");
view.setDate(2000, 3, 3);
view.setViewMode(LoopTimePickerView.VIEW_MODE_ALL);
view.setStartTime(2000, 3, 3, 6, 50);
view.setEndTime(2000, 3, 3, 9, 30);
```