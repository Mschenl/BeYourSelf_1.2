package com.self.be.beyourself_12.homeshow;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;

import com.self.be.beyourself_12.time.NowTime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/20.
 */

public class MakePlan extends Activity{
    private SQLiteDatabase db;
    private TextView tv_show_date;
    private String choose_date="";
    private ArrayList<HashMap<String,Object>> result;
    private ListView listView;
    private String from[] = {"plan_name"};
    private int to[] = {R.id.plan_name_item_task};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_database();
        setContentView(R.layout.make_plan);
        make_view();
    }
    private void make_view(){
        tv_show_date = (TextView) findViewById(R.id.tv_show_choose_time_makeplan);
        choose_date = new NowTime().getNowDate();
        tv_show_date.setText(choose_date+"计划");
        listView = (ListView) findViewById(R.id.show_plan_lv_make_plan);
        query_show();
    }
    public void OnClick_MakePlan(View view){
        switch (view.getId()){
            case R.id.btn_choose_date_makeplan:
                init_calender_view();
                break;
            case R.id.return_btn_make_plan:
                finish();
                break;
        }
    }
    private String temp_date;
    private void init_calender_view(){
        LayoutInflater layoutInflater =  getLayoutInflater();
        final View calender = layoutInflater.inflate(R.layout.calender_choose,null);

        final PopupWindow popupWindow = new PopupWindow(calender, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,true);
        popupWindow.showAsDropDown(tv_show_date,0,0);
        CalendarView calendarView = (CalendarView) calender.findViewById(R.id.calender_view);
        temp_date = choose_date;
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                temp_date = year+"/"+(month+1)+"/"+dayOfMonth;
            }

        });
        Button btn_finsh = (Button) calender.findViewById(R.id.btn_finish_choose_date);
        btn_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!choose_date.isEmpty()){
                    Toast.makeText(MakePlan.this, "选择所在时间为"+choose_date, Toast.LENGTH_SHORT).show();
                    choose_date = temp_date;
                    tv_show_date.setText(choose_date);
                    query_show();
                    popupWindow.dismiss();
                }

            }
        });
        Button btn_return = (Button) calender.findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    private void query_show(){


        String where = "plan_date = ?";
        String whereArg[] = {choose_date};
        Cursor cursor = db.query("plan_daily",null,where,whereArg,null,null,null);
        result = new ArrayList<HashMap<String,Object>>();
        int index = cursor.getColumnIndex("plan_name");
        while(cursor.moveToNext()){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("plan_name",cursor.getString(index));
            map.put("_id",cursor.getInt(0));
            result.add(map);
        }
        cursor.close();

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,result,R.layout.item_task,from,to);

        listView.setAdapter(simpleAdapter);
    }
    private void init_database(){
        db = MyDataBase.db;
    }
}
