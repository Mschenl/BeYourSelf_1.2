package com.self.be.beyourself_12.time;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/2/18.
 */

public class NowTime {
    private Calendar taday;

    public NowTime(){
        taday = Calendar.getInstance();
    }
    public String getNowDate(){
        String taday_date = taday.get(Calendar.YEAR)+"/"+(taday.get(Calendar.MONTH)+1)+"/"+taday.get(Calendar.DAY_OF_MONTH);
        return taday_date;
    }
    public String getNowTime(){
        String taday_time = taday.get(Calendar.HOUR)+":"+taday.get(Calendar.MINUTE);
        return taday_time;
    }
}
