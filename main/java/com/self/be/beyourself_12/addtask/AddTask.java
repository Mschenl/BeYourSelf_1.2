package com.self.be.beyourself_12.addtask;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.chart.GanttChart;
import com.self.be.beyourself_12.dataInfo.PersonInfo;
import com.self.be.beyourself_12.time.NowTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AddTask {
    private SQLiteDatabase db;
    private AppCompatActivity mainActivity;
    private SimpleAdapter sim_adapter;
    private List<HashMap<String,Object>> resList;
    private TextView tv_show_date;
    private String choose_date;
    private LinearLayout layout;
    private GanttChart ganttChart;

    private String [] from = {"plan_name","date_point","sign"};
    private int [] to = {R.id.plan_name_list_item_add_task,R.id.time_point_list_item_add_task
    ,R.id.sign_list_item_add_task};

    public AddTask(AppCompatActivity main){
        mainActivity = main;
    }

    public void MakeView(){
        db = MyDataBase.db;
        init_views();
        Make_GanttChart();
    }
    private void init_views(){
        layout = (LinearLayout) mainActivity.findViewById(R.id.GanttChart_layout);
        tv_show_date = (TextView) mainActivity.findViewById(R.id.tv_show_nowtime_addtask);
        choose_date = new NowTime().getNowDate();
        tv_show_date.setText(choose_date);
        Button button_add_plan = (Button) mainActivity.findViewById(R.id.bt_top_addTask);
        button_add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = PersonInfo.get_user_id();
                if(user_id.isEmpty()){
                    Toast.makeText(mainActivity,"用户还未登录！不能添加计划",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(mainActivity,CheckDailyTask.class);
                intent.putExtra("plan_date",choose_date);
                mainActivity.startActivity(intent);
            }
        });

    }
//    private String format_date(Date date){
//        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
//        String nowTime= df.format(date);      //将date数据用特定形式字符串输出
//        return nowTime;
//    }
    public void AddTask_click(View view){

        switch (view.getId()){

            case R.id.choose_date_addtask://选择日历
                make_calender();
                break;
        }
    }
    private  String temp_date;
    private void make_calender(){
        Button btn_cal = (Button) mainActivity.findViewById(R.id.choose_date_addtask);

        LayoutInflater layoutInflater =  mainActivity.getLayoutInflater();
        final View calender = layoutInflater.from(mainActivity).inflate(R.layout.calendar_addtask,null);

        final PopupWindow popupWindow = new PopupWindow(calender, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,true);
        popupWindow.showAsDropDown(btn_cal,0,0);
        CalendarView calendarView = (CalendarView) calender.findViewById(R.id.calender_view_addtask);
        temp_date = new NowTime().getNowDate();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                temp_date = year+"/"+month+"/"+dayOfMonth;
            }

        });
        Button btn_finsh = (Button) calender.findViewById(R.id.finish_choose_data_calender_addtask);
        btn_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_date = temp_date;
                tv_show_date.setText(choose_date);
                layout.removeView(ganttChart);
                Make_GanttChart();
                popupWindow.dismiss();
            }
        });
        Button btn_return = (Button) calender.findViewById(R.id.return_calender_addtask);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }
    private void Make_GanttChart(){

        ganttChart = new GanttChart(mainActivity,choose_date);
        ganttChart.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        //通知view组件重绘
        ganttChart.invalidate();
        layout.addView(ganttChart);
//        Toast.makeText(mainActivity,layout.getLayoutParams().height+"",Toast.LENGTH_SHORT).show();
    }


    //  addtask界面中的轴向图
    private void make_time_axis(){
//        String str[]  = {date.getTime()+""};
        Cursor cursor = db.query("plan",null,null,null,null,null,null);
        int index_plan_name = cursor.getColumnIndex("plan_name");
        int index_start_time = cursor.getColumnIndex("start_time");
        int index_end_time = cursor.getColumnIndex("end_time");

        DateFormat df = new SimpleDateFormat("HH:mm");
//        String nowTime= df.format(date);
        resList = new ArrayList<HashMap<String,Object>>();
        while(cursor.moveToNext()){
            HashMap<String,Object> map = new HashMap<String,Object>();

            map.put("plan_name",cursor.getString(index_plan_name));
            map.put("time_point",cursor.getLong(index_start_time));   //start
            Date start_date = new Date(cursor.getLong(index_start_time));
            map.put("date_point",df.format(start_date));
            map.put("if_start_time",true);
            map.put("sign","begin");
            resList.add(map);

            HashMap<String,Object> map1 = new HashMap<String,Object>();

            map1.put("plan_name",cursor.getString(index_plan_name));
            map1.put("time_point",cursor.getLong(index_end_time));      //end
            Date end_date =new Date(cursor.getLong(index_end_time));
            map1.put("date_point",df.format(end_date));
            map1.put("if_start_time",false);
            map1.put("sign","end");

            resList.add(map1);


        }
        Collections.sort(resList, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                return (int)((long)o1.get("time_point") - (long)o2.get("time_point"));
            }
        });


        sim_adapter = new SimpleAdapter(mainActivity,resList,R.layout.list_view_item_add_task,from,to);

//        listView.setAdapter(sim_adapter);
    }

}
