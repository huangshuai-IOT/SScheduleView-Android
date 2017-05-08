# SScheduleView

![Version 0.1.0](https://img.shields.io/badge/Version-0.1.0-brightgreen.svg?style=flat)
[![License](https://img.shields.io/cocoapods/l/SScheduleView.svg?style=flat)](http://cocoapods.org/pods/SScheduleView)
![Platform Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=flat)
[![Weibo](https://img.shields.io/badge/%e5%be%ae%e5%8d%9a-%40%e9%bb%84%e5%b8%85IOT-yellow.svg?style=flat)](http://weibo.com/2189929640)

School ScheduleView for Android, pure code.

[中文文档](https://github.com/huangshuai-IOT/SScheduleView-Android/blob/master/README.zh.md)

## Features

- Show weekdays date based on course week
- Support left and right gestures to slide
- Dynamically set the weekend course display
- Dynamic changes show course

![Demo](https://github.com/huangshuai-IOT/SScheduleView-Android/blob/master/demo.gif)
## Requirements

- Android 17

## Installation
### Step 1. Add the JitPack repository to your build file
#### gradle

```ruby
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
#### maven

```ruby
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

### Step 2. Add the dependency
#### gradle

```ruby
dependencies {
	        compile 'com.github.huangshuai-IOT:SScheduleView-Android:0.1.0'
}
```

#### maven

```ruby
<dependency>
	    <groupId>com.github.huangshuai-IOT</groupId>
	    <artifactId>SScheduleView-Android</artifactId>
	    <version>0.1.0</version>
	</dependency>
```

### Demo

run  at `demo` project before run the demo.

## Usage （Support xml and code）
### xml usage

In xml can be directly introduced.

```XML
<com.huangshuai.sscheduleview.SScheduleView
        android:id="@+id/ah_home_ahschedule"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

### Code implementation


### Bind Displays the schedule data

The application's data type should implement the `SScheduleViewModelInterface` protocol

```Java
public interface SScheduleViewModelInterface {
    // 周几的课 从1开始
    int getDay();

    // 第几节课 从1开始
    int getJieci();

    //一节课的持续时间
    int getSpan();

    String getCourseName();

    String getClassRoom();

    // 课程的自定义颜色，若nil则采用随机颜色
    int getBackColor();
}
```

After that, call the update data function to display the lesson data

```Java 
private List<SScheduleViewModelInterface> thisWeekCourseList = new ArrayList<>();
scheduleView.updateCourseViews(thisWeekCourseList);
```

### Listening to player state changes

See more detail from the Example project

#### Delegate

```Java
public interface SScheduleViewCallBack {
    void onCourseItemClick(SScheduleViewModelInterface var1);

    void swipeGestureRight();

    void swipeGestureLeft();
}
```

## Customize SScheduleView

```Java
    public void setShowTotalDay(int num);

    public void setShowWeek(int week);

    public void setWeekIsShow(boolean isShow);

    public void setTermStartDate(String dateString);

    public void clearOldCourseView();
```

## Contact me

- Weibo: [黄帅IOT](http://weibo.com/u/2189929640)
- Blog: https://huangshuai-iot.github.io/
- Email: shuai.huang.iot@foxmail.com

## Contributors

You are welcome to fork and submit pull requests.

## License
SScheduleView is available under the MIT license. See the LICENSE file for more info.

