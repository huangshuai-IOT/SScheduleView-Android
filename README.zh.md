# SScheduleView

[![](https://www.jitpack.io/v/huangshuai-IOT/SScheduleView-Android.svg)](https://www.jitpack.io/#huangshuai-IOT/SScheduleView-Android)
[![License](https://img.shields.io/cocoapods/l/SScheduleView.svg?style=flat)](http://cocoapods.org/pods/SScheduleView)
![Platform Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=flat)
[![Weibo](https://img.shields.io/badge/%e5%be%ae%e5%8d%9a-%40%e9%bb%84%e5%b8%85IOT-yellow.svg?style=flat)](http://weibo.com/2189929640)

Android 课程表控件，方便快速集成。

[English Document](https://github.com/huangshuai-IOT/SScheduleView-Android/blob/master/README.md)

## 介绍
本项目基于 RelativeLayout 绘制的课程表控件，方便快速集成。
![演示](https://github.com/huangshuai-IOT/SScheduleView-Android/blob/master/demo.gif)

## 功能
- 根据课程周显示本周日期
- 支持左右手势滑动
- 动态设置周末课程显示
- 动态修改显示课程

## 要求
- Android 17

## 安装
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
	        compile 'com.github.huangshuai-IOT:SScheduleView-Android:0.2.0'
}
```
#### maven
```ruby
<dependency>
	    <groupId>com.github.huangshuai-IOT</groupId>
	    <artifactId>SScheduleView-Android</artifactId>
	    <version>0.2.0</version>
	</dependency>
```

### Demo

运行 Demo ，请下载后运行demo即可。

## 使用 （支持 xml 和代码）
### xml 用法

在xml中直接引入即可。

```XML
<com.huangshuai.sscheduleview.SScheduleView
        android:id="@+id/ah_home_ahschedule"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

### 代码布局


### 绑定显示课表数据

应用的课程表数据类型应该实现 `SScheduleViewModelInterface` 协议

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

之后，调用更新数据函数，即可显示课表数据

```Java 
private List<SScheduleViewModelInterface> thisWeekCourseList = new ArrayList<>();
scheduleView.updateCourseViews(thisWeekCourseList);
```

### 监听状态变化

具体用法请看 Example 项目

#### 协议方式

```Java
public interface SScheduleViewCallBack {
    void onCourseItemClick(SScheduleViewModelInterface var1);

    void swipeGestureRight();

    void swipeGestureLeft();
}
```

## SScheduleView 自定义属性

```Java
    public void setShowTotalDay(int num);

    public void setShowWeek(int week);

    public void setWeekIsShow(boolean isShow);

    public void setTermStartDate(String dateString);

    public void clearOldCourseView();
```

## 联系我

- 微博: [黄帅IOT](http://weibo.com/u/2189929640)
- 博客: https://huangshuai-iot.github.io/
- 邮箱: shuai.huang.iot@foxmail.com

## 贡献者

欢迎提交 issue 和 PR，大门永远向所有人敞开。

## License

SScheduleView is available under the MIT license. See the LICENSE file for more info.


