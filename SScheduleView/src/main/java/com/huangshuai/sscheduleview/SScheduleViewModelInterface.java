package com.huangshuai.sscheduleview;

import android.graphics.Color;

/**
 * Created by huangshuai on 2017/3/23.
 */

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
