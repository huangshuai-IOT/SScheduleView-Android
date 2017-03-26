package com.huangshuai.sscheduleview;

/**
 * Created by huangshuai on 2017/3/26.
 */

public interface SScheduleViewCallBack {
    void onCourseItemClick(SScheduleViewModelInterface courseModel);
    void swipeGestureRight();
    void swipeGestureLeft();
}
