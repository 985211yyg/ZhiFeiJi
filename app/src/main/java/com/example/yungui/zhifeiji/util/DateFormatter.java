package com.example.yungui.zhifeiji.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yungui on 2017/2/10.
 */

public class DateFormatter {
    /*
    将long类型的date转换为string类型
     */
    public String ZhiHuDailyFormat(long date) {
        String Fdate;
        Date d = new Date(date + 24 * 60 * 60 * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Fdate = dateFormat.format(d);
        return Fdate;
    }

    public String DouBanFormat(long date) {
        String Fdate;
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d");
        Fdate= dateFormat.format(d);
        return Fdate;
    }
}
