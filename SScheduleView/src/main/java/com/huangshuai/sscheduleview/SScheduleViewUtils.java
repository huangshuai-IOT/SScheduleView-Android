package com.huangshuai.sscheduleview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by huangshuai on 2017/3/23.
 */

public class SScheduleViewUtils {
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private static float density = -1F;
    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5F);
    }

    public static float getDensity(Context context) {
        if (density <= 0F) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    /**
     * 和中国星期对应上
     */
    private static int[] US_DAYS_NUMS = {7, 1, 2, 3, 4, 5, 6};

    /**
     * 获取以今天为基准 ，星期一到星期日在这个月中是几号
     * @return
     */
    public static String[] getOneWeekDatesOfMonth(int totalDay,long millis) {
        String preMonth = "";
        Calendar toDayCal = Calendar.getInstance();
        toDayCal.setTimeInMillis(millis);
        String[] temp = new String[totalDay + 1];
        int b = US_DAYS_NUMS[toDayCal.get(Calendar.DAY_OF_WEEK) - 1];
        if (b != 7) { //7是美历的下个星期的周一，而在中国是星期日。如果不为7，则直接拿到这周的周一，如果为7则需要拿到上周的周一(美历)
            toDayCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            toDayCal.add(Calendar.WEEK_OF_MONTH, -1);//跳转到上周周日（美历的周一）
            toDayCal.set(Calendar.DAY_OF_WEEK, 2);//设置为周一（美历的周二）
        }
        int ds = 0;
        for (int i = 1; i < totalDay; i++) {
            if (i == 1) {
                ds = toDayCal.get(Calendar.DAY_OF_MONTH);
                temp[i - 1] = toDayCal.get(Calendar.DAY_OF_MONTH) + "";
                preMonth = (toDayCal.get(Calendar.MONTH) + 1) + "月";
            }
            toDayCal.add(Calendar.DATE, 1);
            if (toDayCal.get(Calendar.DAY_OF_MONTH) < ds) {
                temp[i] = (toDayCal.get(Calendar.MONTH) + 1) + "月";
                ds = toDayCal.get(Calendar.DAY_OF_MONTH);
            } else {
                temp[i] = toDayCal.get(Calendar.DAY_OF_MONTH) + "";
            }
        }
        temp[totalDay] = preMonth;

        return temp;
    }

    public static String[] getDaysListForWeek(Date startTermDate, int week) {
        String[] weekString = new String[8];
        long startDate = startTermDate.getTime();
        startDate = startDate + (long)((week - 1)) * 7 * 24 * 3600 * 1000;
        Calendar toDayCal = Calendar.getInstance();
        toDayCal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        for(int i = 0;i<7;i++) {
            long day = startDate + (i * 24 *3600 * 1000);
            toDayCal.setTimeInMillis(day);
            String string = "" + toDayCal.get(Calendar.DAY_OF_MONTH);
            weekString[i] = string;
        }

        String preMonth = (toDayCal.get(Calendar.MONTH) + 1) + "月";
        weekString[7] = preMonth;
        return weekString;
    }
}
