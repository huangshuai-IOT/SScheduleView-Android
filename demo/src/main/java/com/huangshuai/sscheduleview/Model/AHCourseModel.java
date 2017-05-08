package com.huangshuai.sscheduleview.Model;

import com.huangshuai.sscheduleview.SScheduleViewModelInterface;

/**
 * Created by huangshuai on 2017/5/8.
 */

public class AHCourseModel implements SScheduleViewModelInterface {

    // 周几
    private int dayNum = 0;

    // 第几节课
    private int jieNum = 0;

    // 课程跨度
    private int courseSpan = 2;

    // 课程名称
    private String courseName = "";

    // 课程地点
    private String courseClassRoom = "";

    public AHCourseModel(int dayNum, int jieNum, int courseSpan, String courseName, String courseClassRoom) {
        this.dayNum = dayNum;
        this.jieNum = jieNum;
        this.courseSpan = courseSpan;
        this.courseName = courseName;
        this.courseClassRoom = courseClassRoom;
    }

    // 周几的课 从1开始
    public int getDay() {
        return dayNum;
    }

    // 第几节课 从1开始
    public int getJieci() {
        return jieNum;
    }

    //一节课的持续时间
    public int getSpan() {
        return courseSpan;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getClassRoom() {
        return courseClassRoom;
    }

    // 课程的自定义颜色，若-1则采用随机颜色
    public int getBackColor() {
        return 0;
    }
}
