package com.qinlei.customview2;

/**
 * Created by ql on 2017/4/20.
 */

public class TimeUtil {
    public static String formatTime(int second) {
        int minute = 0;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            minute = minute % 60;
        }
        // 转换时分秒 00:00
        String duration = (minute >= 10 ? minute : "0" + minute) + ":" + (second >= 10 ? second : "0" + second);
        return duration;
    }
}
