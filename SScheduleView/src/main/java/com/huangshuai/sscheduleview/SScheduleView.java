package com.huangshuai.sscheduleview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import static com.huangshuai.sscheduleview.SScheduleViewUtils.getDaysListForWeek;

/**
 * Created by huangshuai on 2017/3/23.
 */

public class SScheduleView extends RelativeLayout {
    // 其他列宽度 与 第一列宽度 比
    private int sideColWidthRatio = 2;
    // 顶部日期栏 View 背景色
    private int headViewBackgroundColor;
    // 侧边时间栏 View 背景色
    private int sideViewBackgroundColor;

    private List<SScheduleViewModelInterface> courseDataList;
    public SScheduleViewCallBack callBack;

    // 一共显示的天数
    private int showDaysNum = 5;
    // 一共显示的课程节数
    private int showJiesNum = 11;
    /// 当前显示的周数
    private int showWeekNum = 1;
    /// 开学日期
    private Date termStartDate = new Date();
    // 第一行是否显示当前周数
    private boolean isShowWeekNum = true;

    // 非第一列,其他每一列的宽度
    private int firstColumnWidth;
    // 非第一列 每一列的宽度
    private int notFirstEveryColumnsWidth;
    // 第一行的高度
    private int firstRowHeight;
    // 非第一行,其他每一行的高度
    private int notFirstEveryRowHeight;

    // 第一行显示资源
    private String[] titleNames = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    // 第一行显示日期,最后一位是当前月份
    private String[] datesOfMonth;

    /// 顶部 View
    private FrameLayout headView;
    private static final int headViewID = 600;
    /// 侧边课程节数 View
    private LinearLayout sideView;
    /// 课程内容 View
    private FrameLayout contentView;

    private int[] US_DAYS_NUMS = {7, 1, 2, 3, 4, 5, 6}; //和中国星期对应上
    private List<View> myCacheViews = new ArrayList<View>(); //保存View 方便Remove
    private static final int FIRST_TV_ID = 555;
    private static final int FIRST_ROW_TV_QZ = 3;

    private TextView firstTV; // 第一个TV
    // 当前月份
    private String preMonth;
    private int todayNum;// 今日周几
    private int twoW;
    private int oneW;

    /**
     * 设置一周显示的天数
     *
     * @param num 一周显示的天数
     */
    public void setShowTotalDay(int num) {
        this.showDaysNum = num;
        resetLayout();
    }

    /**
     * 设置显示周数
     *
     * @param week 第几周
     */
    public void setShowWeek(int week) {
        this.showWeekNum = week;
        updateHeadView();
    }

    /**
     * 设置是否显示当前周数，因为有的ViewController在NavigationBar显示当前周数
     *
     * @param isShow 是否显示
     */
    public void setWeekIsShow(boolean isShow) {
        this.isShowWeekNum = isShow;
        updateHeadView();
    }

    /**
     * 设置当前学期开学时间 "yyyy-MM-dd"
     *
     * @param dateString such as “2017-02-13”
     */
    public void setTermStartDate(String dateString) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        try {
            formater.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            this.termStartDate = formater.parse(dateString);

            long from = termStartDate.getTime();
            long now = new Date().getTime();
            int week = (int) (((now - from) / (7 * 24 * 3600 * 1000)) + 1);
            if (week < 1) {
                week = 0;
            }
            showWeekNum = week;
            updateHeadView();
        } catch (ParseException e) {
            Log.e("调试信息", e.getLocalizedMessage());
        }
    }

    /**
     * 清空课表数据，可用于账户退出登录后的操作
     */
    public void clearOldCourseView() {
        if (myCacheViews == null || myCacheViews.isEmpty())
            return;

        for (int i = myCacheViews.size() - 1; i >= 0; i--) {
            contentView.removeView(myCacheViews.get(i));
            myCacheViews.remove(i);
        }
    }

    /**
     * 显示课程数据
     *
     * @param courseData 课程数据
     */
    public void updateCourseViews(List<SScheduleViewModelInterface> courseData) {
        this.courseDataList = courseData;
        clearOldCourseView();
        updateCourseViews();
    }

    public SScheduleView(Context context) {
        this(context, null);
    }

    public SScheduleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SScheduleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initUI();
    }

    // 重绘整个UI
    private void resetLayout() {
        removeAllViews();
        init(getContext());
        initUI();
        updateCourseViews();
    }

    private void init(Context context) {
        twoW = SScheduleViewUtils.dip2px(context, 2);
        oneW = SScheduleViewUtils.dip2px(context, 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        todayNum = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        datesOfMonth = SScheduleViewUtils.getOneWeekDatesOfMonth(showDaysNum, System.currentTimeMillis());
        preMonth = datesOfMonth[showDaysNum];
    }

    private void initUI() {
        initUISize();
        drawFirstRow();
        drawOtherRows();
        addContentViewGesture();
    }

    /**
     * 设置UI的各个Size
     */
    private void initUISize() {
        int screenWidth = SScheduleViewUtils.getScreenWidth(getContext());
        int screenHeight = SScheduleViewUtils.getScreenHeight(getContext());

        /** 设置每一列的宽度 **/
        firstColumnWidth = screenWidth / (sideColWidthRatio * showDaysNum + 1);
        notFirstEveryColumnsWidth = firstColumnWidth * sideColWidthRatio;

        /** 设置每一行的高度 **/
        firstRowHeight = SScheduleViewUtils.dip2px(getContext(), 40);
        notFirstEveryRowHeight = (screenHeight - SScheduleViewUtils.dip2px(getContext(), 200)) / showJiesNum + SScheduleViewUtils.dip2px(getContext(), 5);
    }

    /**
     * 绘制第一行
     */
    @SuppressWarnings("ResourceType")
    private void drawFirstRow() {
        headView = new FrameLayout(getContext());
        headView.setId(headViewID);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, firstRowHeight);
        headView.setLayoutParams(rlp);
        addView(headView);

        datesOfMonth = getDaysListForWeek(termStartDate, showWeekNum);

        drawFirstRowFirstColCell();
        drawFirstRowOtherColCell();
    }

    /**
     * 绘制第一行第一列
     */
    private void drawFirstRowFirstColCell() {
        firstTV = new TextView(getContext());
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(firstColumnWidth, firstRowHeight);
        firstTV.setBackgroundColor(getResources().getColor(R.color.BlankAreaColor));
        firstTV.setText(datesOfMonth[7]);
        firstTV.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        firstTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        firstTV.setPadding(oneW, twoW, oneW, twoW);
        firstTV.setLayoutParams(rlp);
        headView.addView(firstTV);
    }

    /**
     * 绘制第一行其他列
     */
    private void drawFirstRowOtherColCell() {

        for (int i = 0; i < showDaysNum; i++) {

            LinearLayout headViewCell = new LinearLayout(getContext());
            headViewCell.setBackgroundResource(R.drawable.head_view_back);
            headViewCell.setOrientation(LinearLayout.VERTICAL);

            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(notFirstEveryColumnsWidth,
                    firstRowHeight);
            flp.setMargins(firstColumnWidth + i * notFirstEveryColumnsWidth, 0, 0, 0);

            headViewCell.setLayoutParams(flp);


            LinearLayout.LayoutParams tvLLP = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    firstRowHeight / 2);

            TextView dayTV = new TextView(getContext());
            dayTV.setText(titleNames[i]);
            dayTV.setLayoutParams(tvLLP);
            dayTV.setGravity(Gravity.CENTER);
//            dayTV.setPadding(twoW, twoW, twoW, twoW);
            dayTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            headViewCell.addView(dayTV);

            TextView dateTV = new TextView(getContext());
            dateTV.setLayoutParams(tvLLP);
            dateTV.setText(datesOfMonth[i]);
            dateTV.setGravity(Gravity.CENTER | Gravity.BOTTOM);
//            dateTV.setPadding(twoW, 0, twoW, twoW * 2);
            dateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            headViewCell.addView(dateTV);

            if (US_DAYS_NUMS[todayNum] - 1 == i) {
                headViewCell.setBackgroundColor(0x77069ee9);
                dateTV.setTextColor(Color.WHITE);
                dayTV.setTextColor(Color.WHITE);
            }

            headView.addView(headViewCell);
        }
    }

    /**
     * 绘制其他行
     * 整个下面是一个ScrollView
     */
    private void drawOtherRows() {
        ScrollView scrollView = new ScrollView(getContext());
        LayoutParams scrollViewLP = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollViewLP.addRule(RelativeLayout.BELOW, headView.getId());
        scrollView.setLayoutParams(scrollViewLP);
        scrollView.setVerticalScrollBarEnabled(false);

        LinearLayout scrollContentView = new LinearLayout(getContext());
        ViewGroup.LayoutParams scrollContentViewLP = new ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        scrollContentView.setLayoutParams(scrollContentViewLP);

        scrollView.addView(scrollContentView);
        addView(scrollView);

        drawOtherRowFirstCol(scrollContentView);
        drawOtherRowOtherCol(scrollContentView);
    }

    /**
     * 绘制其他行的第一列，即课程节数
     *
     * @param scrollContentView
     */
    private void drawOtherRowFirstCol(LinearLayout scrollContentView) {
        sideView = new LinearLayout(getContext());
        LinearLayout.LayoutParams sideViewLP = new LinearLayout.LayoutParams(firstColumnWidth, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        sideView.setLayoutParams(sideViewLP);
        sideView.setOrientation(LinearLayout.VERTICAL);
        scrollContentView.addView(sideView);

        for (int i = 0; i < showJiesNum; i++) {
            TextView jieciTV = new TextView(getContext());
            LinearLayout.LayoutParams jieciTVLP = new LinearLayout.LayoutParams(firstColumnWidth, notFirstEveryRowHeight);
            jieciTV.setLayoutParams(jieciTVLP);
            jieciTV.setBackgroundResource(R.drawable.side_view_back);
            jieciTV.setText("" + (i + 1));
            jieciTV.setGravity(Gravity.CENTER);
            jieciTV.setTextColor(Color.GRAY);
            sideView.addView(jieciTV);
        }
    }

    /**
     * 绘制其他行其他列，即每个课程背景格子
     */
    private void drawOtherRowOtherCol(LinearLayout scrollContentView) {
        contentView = new FrameLayout(getContext());
        contentView.setClickable(true);

        LinearLayout.LayoutParams contentViewLP = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(contentViewLP);
        scrollContentView.addView(contentView);

        for (int i = 0; i < showDaysNum * showJiesNum; i++) {
            final int row = i / showDaysNum;
            final int col = i % showDaysNum;
            // 课程背景格子
            FrameLayout courseBackView = new FrameLayout(getContext());
            FrameLayout.LayoutParams courseBackViewLP = new FrameLayout.LayoutParams(notFirstEveryColumnsWidth,
                    notFirstEveryRowHeight);
            courseBackView.setBackgroundResource(R.drawable.course_back);
            courseBackViewLP.setMargins(col * notFirstEveryColumnsWidth, row * notFirstEveryRowHeight, 0, 0);
            courseBackView.setLayoutParams(courseBackViewLP);
            contentView.addView(courseBackView);
        }
    }

    /**
     * 更新课表View
     */
    public void updateCourseViews() {

        for (final SScheduleViewModelInterface data : courseDataList) {
            FrameLayout courseBackView = new FrameLayout(getContext());

            FrameLayout.LayoutParams courseBackViewFLP = new FrameLayout.LayoutParams(notFirstEveryColumnsWidth,
                    notFirstEveryRowHeight * data.getSpan());
            courseBackViewFLP.setMargins((data.getDay() - 1) * notFirstEveryColumnsWidth, (data.getJieci() - 1) * notFirstEveryRowHeight, 0, 0);
            courseBackView.setLayoutParams(courseBackViewFLP);
            courseBackView.setPadding(twoW, twoW, twoW, twoW);

            TextView couseInfoTV = new TextView(getContext());
            couseInfoTV.setText(data.getCourseName() + "\n" + data.getClassRoom());
            couseInfoTV.setTextColor(Color.WHITE);
            couseInfoTV.setGravity(Gravity.CENTER);
            couseInfoTV.setPadding(oneW, oneW, oneW, oneW);
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

            courseBackViewFLP = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            couseInfoTV.setLayoutParams(courseBackViewFLP);
            couseInfoTV.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.onCourseItemClick(data);
                }
            });

            courseBackView.addView(couseInfoTV);
            myCacheViews.add(courseBackView);
            contentView.addView(courseBackView);
        }
    }

    private void addContentViewGesture() {
        final GestureDetector gdt = new GestureDetector(new GestureListener());
        contentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gdt.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            final float FLIP_DISTANCE = 50;

            if (e1.getX() - e2.getX() > FLIP_DISTANCE) {
                // 向左滑
                showWeekNum = showWeekNum + 1;
                setShowWeek(showWeekNum);
                callBack.swipeGestureLeft();
                return true;
            }
            if (e2.getX() - e1.getX() > FLIP_DISTANCE) {
                // 向右滑
                if (showWeekNum > 1) {
                    showWeekNum = showWeekNum - 1;
                    setShowWeek(showWeekNum);
                    callBack.swipeGestureRight();
                }
                return true;
            }
            if (e1.getY() - e2.getY() > FLIP_DISTANCE) {
                Log.i("MYTAG", "向上滑...");
                return true;
            }
            if (e2.getY() - e1.getY() > FLIP_DISTANCE) {
                Log.i("MYTAG", "向下滑...");
                return true;
            }

            return false;
        }
    }

    private void updateHeadView() {
        removeView(headView);
        drawFirstRow();
    }
}
