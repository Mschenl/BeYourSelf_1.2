package com.self.be.beyourself_12.dataInfo;

/**
 * Created by Administrator on 2017/2/17.
 */

public class PlanDaily {
    private String user_id,plan_name,start_time,end_time,plan_type,plan_date;
    private int plan_time,record_id;

    public PlanDaily(int record_id,String user_id,String plan_name,String start_time,String end_time,
                     String plan_date,String plan_type,int plan_time){

        this.record_id = record_id;
        this.user_id = user_id;
        this.plan_name = plan_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.plan_date = plan_date;
        this.plan_time = plan_time;
        this.plan_type = plan_type;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_date(String plan_date) {
        this.plan_date = plan_date;
    }

    public String getPlan_date() {
        return plan_date;
    }

    public void setPlan_time(int plan_time) {
        this.plan_time = plan_time;
    }

    public int getPlan_time() {
        return plan_time;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }
}
