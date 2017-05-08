package com.huangshuai.sscheduleview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.huangshuai.sscheduleview.Model.AHCourseModel;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends AppCompatActivity implements SScheduleViewCallBack {

    private SScheduleView scheduleView;

    // 显示几天课表
    private int showDaysNum = 5;
    // 当前显示的第几周课表
    private int showWeekNum = 10;
    // 课表数据
    private List<SScheduleViewModelInterface> thisWeekCourseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        this.setTitle("第" + showWeekNum + "周");
        // init navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        scheduleView = (SScheduleView) findViewById(R.id.ah_home_ahschedule);
        scheduleView.setTermStartDate("2017-02-13");
        scheduleView.setShowWeek(showWeekNum);
        scheduleView.callBack = this;

        loadCourseData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.community_menu_addcourse:
                scheduleView.setWeekIsShow(true);
                return true;
            case R.id.community_menu_show_weekenday:
                showDaysNum = showDaysNum == 5 ? 7 : 5;
                scheduleView.setShowTotalDay(showDaysNum);
                return true;
            case R.id.community_menu_refresh:
                scheduleView.updateCourseViews();
                return true;
        }
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    private void loadCourseData() {
        thisWeekCourseList.add(new AHCourseModel(1, 1, 2, "数据结构", "2号楼2030104"));
        thisWeekCourseList.add(new AHCourseModel(2, 1, 3, "操作系统", "2号楼2030104"));
        thisWeekCourseList.add(new AHCourseModel(1, 5, 4, "计算机网络", "2号楼2030104"));
        thisWeekCourseList.add(new AHCourseModel(4, 1, 6, "计算机组成原理", "2号楼2030104"));

        scheduleView.updateCourseViews(thisWeekCourseList);
    }

    public void onCourseItemClick(SScheduleViewModelInterface courseModel) {
        showDialog(courseModel.getCourseName() + "\n" + courseModel.getClassRoom());
    }

    public void swipeGestureRight() {
        showWeekNum = showWeekNum - 1;
        scheduleView.setShowWeek(showWeekNum);
        this.setTitle("第" + showWeekNum + "周");
    }

    public void swipeGestureLeft() {
        showWeekNum = showWeekNum + 1;
        scheduleView.setShowWeek(showWeekNum);
        this.setTitle("第" + showWeekNum + "周");
    }

    private void showDialog(String message) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("课程详情");
        builder.setMessage(message);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.show();
    }
}
