package com.huangshuai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangshuai.sscheduleview.R;
import com.huangshuai.sscheduleview.SScheduleTheme;
import com.huangshuai.sscheduleview.SScheduleViewModelInterface;

import java.util.Random;

/**
 * Created by tangyijian on 2017/5/9.
 */

public class CustomSSViewAdapter {
    private Context mContext;
    // 显示几天课表
    private int showDaysNum ;
    // 一共显示的课程节数
    private int showJiesNum;

    public CustomSSViewAdapter(Context context){
        mContext=context;
        showDaysNum=5;
        showJiesNum=11;
    }

    /**
     * 第一行第一列空白格子
     * @return
     */
    public TextView getHeadView(){
        TextView textView=new TextView(mContext);
        textView.setBackgroundColor(mContext.getResources().getColor(R.color.BlankAreaColor));
        textView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        return textView;
    }

    /**
     * 显示天数
     * @return
     */
    public int getShowDaysNum(){
        return showDaysNum;
    }

    public void setShowDaysNum(int showDaysNum) {
        this.showDaysNum = showDaysNum;
    }
    //    public int getSideColWidthRatio(){
//        return 2;
//    }

    /**
     * 一天课时
     * @return
     */
    public int getShowJiesNum(){
        return showJiesNum;
    }

    public void setShowJiesNum(int showJiesNum) {
        this.showJiesNum = showJiesNum;
    }

    /**
     * 第一行日期
     * @param weekDayName
     * @param dayName
     * @param firstRowHeight
     * @param isToday
     * @return
     */
    public View getWeekTitleView(String weekDayName,String dayName,int firstRowHeight,boolean isToday) {
        LinearLayout headViewCell = new LinearLayout(mContext);
        headViewCell.setBackgroundResource(R.drawable.head_view_back);
        headViewCell.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams tvLLP = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                firstRowHeight / 2);

        TextView dayTV = new TextView(mContext);
        dayTV.setText(weekDayName);
        dayTV.setLayoutParams(tvLLP);
        dayTV.setGravity(Gravity.CENTER);
        dayTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        headViewCell.addView(dayTV);

        TextView dateTV = new TextView(mContext);
        dateTV.setLayoutParams(tvLLP);
        dateTV.setText(dayName);
        dateTV.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        dateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        headViewCell.addView(dateTV);

        if (isToday) {
            headViewCell.setBackgroundColor(0x77069ee9);
            dateTV.setTextColor(Color.WHITE);
            dayTV.setTextColor(Color.WHITE);
        }
        return headViewCell;
    }

    /**
     * 第一列课时
     * @param periodName 具体课时
     * @return
     */
    public View getPeriodView(String periodName) {
        TextView jieciTV = new TextView(mContext);
        jieciTV.setBackgroundResource(R.drawable.side_view_back);
        jieciTV.setText(periodName);
        jieciTV.setGravity(Gravity.CENTER);
        jieciTV.setTextColor(Color.GRAY);
        return jieciTV;
    }

    /**
     * 每个课程格子的背景
     * @return
     */
    public View getCourseBackView() {
        FrameLayout courseBackView = new FrameLayout(mContext);
        courseBackView.setBackgroundResource(R.drawable.course_back);
        return courseBackView;
    }

    /**
     * 有课程的格子
     * @return
     */
    public TextView getCourseView(SScheduleViewModelInterface data) {
        TextView couseInfoTV = new TextView(mContext);
        couseInfoTV.setText(data.getCourseName() + "\n" + data.getClassRoom());
        couseInfoTV.setTextColor(Color.WHITE);
        couseInfoTV.setGravity(Gravity.CENTER);
//        couseInfoTV.setPadding(oneW, oneW, oneW, oneW);
        couseInfoTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        couseInfoTV.setEllipsize(TextUtils.TruncateAt.END);
        couseInfoTV.setLines(7);

        // 设置背景颜色
        int bg = new Random().nextInt(SScheduleTheme.COURSE_BG.length - 1);
        couseInfoTV.setBackgroundResource(SScheduleTheme.COURSE_BG[bg]);
        if (data.getBackColor() != 0) {
            GradientDrawable myGrad = (GradientDrawable) couseInfoTV.getBackground();
            myGrad.setColor(data.getBackColor());
        }

        return couseInfoTV;
    }
}
