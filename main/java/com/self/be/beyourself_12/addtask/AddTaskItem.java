package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Administrator on 2017/1/19.
 */

public class AddTaskItem extends Activity {
    private SQLiteDatabase db;
    private static final int DIALOG_TIMER = 1;
    private static final int DIALOG_DATER = 2;
    private java.util.Calendar mc = null;
    private EditText editview_date [];
    private EditText editview_time [];
    private EditText edit_plan_name;

    private String start_time,start_date,end_time,end_date,plan_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_DateBase();
        init_view();
    }
    private void init_view(){
        start_time = start_date = end_time = end_date = plan_name= null;
        setContentView(R.layout.add_task_item);

        mc = java.util.Calendar.getInstance();
        int edit_list_date [] = {R.id.start_date,R.id.end_date};
        int edit_list_time [] = {R.id.start_time,R.id.end_time};

        editview_date = new EditText[2];
        editview_time = new EditText[2];

        editview_date[0] = (EditText) findViewById(edit_list_date[0]);
        editview_time[0] = (EditText) findViewById(edit_list_time[0]);
        editview_date[1] = (EditText) findViewById(edit_list_date[1]);
        editview_time[1] = (EditText) findViewById(edit_list_time[1]);

        edit_plan_name = (EditText) findViewById(R.id.plan_name_add_task_item);

        get_Picker();

    }
    private void get_Picker(){


        editview_date[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddTaskItem.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        editview_date[0].setText(year+"-"+month+"-"+dayOfMonth);
                        start_date = format_date(year)+"-"+format_date(month)+"-"+format_date(dayOfMonth);

                    }
                },mc.get(java.util.Calendar.YEAR),mc.get(java.util.Calendar.MONTH),
                        mc.get(java.util.Calendar.DAY_OF_MONTH)).show();

            }
        });

        editview_date[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddTaskItem.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editview_date[1].setText(year+"-"+month+"-"+dayOfMonth);
                        end_date = format_date(year)+"-"+format_date(month)+"-"+format_date(dayOfMonth);
                    }
                },mc.get(java.util.Calendar.YEAR),mc.get(java.util.Calendar.MONTH),
                        mc.get(java.util.Calendar.DAY_OF_MONTH)).show();

            }
        });



        editview_time[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(AddTaskItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editview_time[0].setText(hourOfDay+":"+minute);
                        start_time = format_date(hourOfDay)+":"+format_date(minute)+":00";
                    }
                },mc.get(java.util.Calendar.HOUR_OF_DAY), java.util.Calendar.MINUTE,true).show();

            }
        });

        editview_time[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(AddTaskItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editview_time[1].setText(hourOfDay+":"+minute);
                        end_time = format_date(hourOfDay)+":"+format_date(minute)+":00";
                    }
                },mc.get(java.util.Calendar.HOUR_OF_DAY), java.util.Calendar.MINUTE,true).show();

            }
        });


    }
    private String format_date(int num){
        if(num<10){
            return "0"+num;
        }
        return num+"";
    }
    public void Finish_add_task(View view){
        //向数据库中添加一条计划数据
        plan_name = edit_plan_name.getText().toString();

        if(plan_name.isEmpty()||start_time.isEmpty()||start_date.isEmpty()||end_date.isEmpty()||end_time.isEmpty()){
            Toast.makeText(this,"请将信息务必填写完整！",Toast.LENGTH_SHORT).show();
            return;
        }
        String start = start_date+" "+start_time;
        String end = end_date + " " +end_time;

        Date begin_date = getDate(start);
        Date final_date = getDate(end);
        if(final_date.before(begin_date)){
            Toast.makeText(this,"  起始时间应先于终止时间！",Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put("plan_name",plan_name);
        values.put("start_time",begin_date.getTime());
        values.put("end_time",final_date.getTime());

        long insert = db.insert("plan",null,values);

        if(insert > 0){
            Toast.makeText(this,"任务添加成功！",Toast.LENGTH_SHORT).show();
            finish();

        }
        else{
            Toast.makeText(this,"抱歉,任务添加失败！请重试 ",Toast.LENGTH_SHORT).show();
        }


    }

    private java.util.Date getDate(String str) {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

            java.util.Date date = formatter.parse(str);

            return date;
        } catch (Exception e) {

        }
        return null;


    }
    private void init_DateBase(){

        db = MyDataBase.db;
    }
    public void Clear_Edit_date(View view){
        edit_plan_name.setText("");
        for(int i = 0 ;i <editview_time.length;i++){
            editview_time[i].setText("");
            editview_date[i].setText("");
        }
    }

}
