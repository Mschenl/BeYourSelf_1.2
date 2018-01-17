package com.self.be.beyourself_12.dataInfo;

/**
 * Created by Administrator on 2017/2/18.
 */

public class PlanDailySegment {

    private String user_id,segment_name,finish_time,finish_date;
    private int _id_plan_daily,if_finish,_id;
    private  double score;

    public PlanDailySegment(int id,String user_id,int id_plan_daily,String segment_name, String finish_time, String finish_date,
                             double score, int if_finish) {
        this.user_id = user_id;
        this.segment_name = segment_name;
        this.finish_time = finish_time;
        this.finish_date = finish_date;
        _id_plan_daily = id_plan_daily;
        this.score = score;
        this.if_finish = if_finish;
        _id = id;
    }
}
