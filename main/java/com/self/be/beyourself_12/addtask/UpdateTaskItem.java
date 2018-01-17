package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/26.
 */

public class UpdateTaskItem extends Activity {
    private SQLiteDatabase db;
    private Button btn_finsh_update;
    private TextView tv_title;
    private int _id;
    private EditText edit_plan_name,edit_start_date,edit_start_time,edit_end_date,edit_end_time;
    private Calendar mc,start,end;
    private int syear,smonth,sday,shour,sminute;
    private int eyear,emonth,eday,ehour,eminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MyDataBase.db;
        setContentView(R.layout.add_task_item);
        init_view();
        get_Picker();
    }
    private void init_view(){
        //将title变成修改界面的标题
        tv_title = (TextView) findViewById(R.id.title_add_task_item);
        tv_title.setText("修改计划");

        //将原布局中的完成按钮变成修改界面的完成按钮
        btn_finsh_update = (Button) findViewById(R.id.finish_add_task);
        btn_finsh_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(syear == 0 || eyear == 0 || edit_plan_name.getText().length() == 0){

                    Toast.makeText(UpdateTaskItem.this,"请将信息务必填写完整！",Toast.LENGTH_SHORT).show();
                    //信息不完整就要返回
                    return ;
                }
                Calendar start_date = Calendar.getInstance();
                start_date.set(syear,smonth,sday,shour,sminute);
                Calendar end_date = Calendar.getInstance();
                end_date.set(eyear,emonth,eday,ehour,eminute);

                ContentValues values = new ContentValues();
                values.put("plan_name",edit_plan_name.getText().toString());
                values.put("start_time",start_date.getTimeInMillis());
                values.put("end_time",end_date.getTimeInMillis());
                String whereClause = "_id = ?";
                String whereArg [] = {String.valueOf(_id)};

                int update = db.update("plan",values,whereClause,whereArg);
                if (update != 0){
                    Toast.makeText(UpdateTaskItem.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        //将修改前的数据进行展示
        edit_plan_name = (EditText) findViewById(R.id.plan_name_add_task_item);

        edit_start_date = (EditText) findViewById(R.id.start_date);
        edit_start_time = (EditText) findViewById(R.id.start_time);

        edit_end_date = (EditText) findViewById(R.id.end_date);
        edit_end_time = (EditText) findViewById(R.id.end_time);


        Intent intent = getIntent();
        String [] planname = {intent.getStringExtra("plan_name")};//进行数据库查询操作
        Cursor cursor = db.query("plan",null,"plan_name = ?",planname,null,null,null);
        int start_index = cursor.getColumnIndex("start_time");
        int end_index = cursor.getColumnIndex("end_time");

        cursor.moveToFirst();
        Date start_date = new Date(cursor.getLong(start_index));
        Date end_date = new Date(cursor.getLong(end_index));
        Calendar _temp = Calendar.getInstance();
        _temp.setTime(start_date);
        syear = _temp.get(Calendar.YEAR);smonth = _temp.get(Calendar.MONTH);sday = _temp.get(Calendar.DAY_OF_MONTH);shour =  _temp.get(Calendar.HOUR_OF_DAY); sminute = _temp.get(Calendar.MINUTE);
        _temp.setTime(end_date);
        eyear = _temp.get(Calendar.YEAR);emonth =  _temp.get(Calendar.MONTH);eday = _temp.get(Calendar.DAY_OF_MONTH);ehour = _temp.get(Calendar.HOUR_OF_DAY); eminute = _temp.get(Calendar.MINUTE);;

        SimpleDateFormat form1 = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat form2 = new SimpleDateFormat("HH:mm");

        edit_start_date.setText(form1.format(start_date));
        edit_start_time.setText(form2.format(start_date));
        edit_end_date.setText(form1.format(end_date));
        edit_end_time.setText(form2.format(end_date));
        int name_index = cursor.getColumnIndex("plan_name");
        edit_plan_name.setText(cursor.getString(name_index));

        _id = cursor.getInt(0);
        //Toast.makeText(this,""+_id,Toast.LENGTH_SHORT).show();
        cursor.close();
    }
    private void get_Picker(){
        mc = Calendar.getInstance();
        edit_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(UpdateTaskItem.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edit_start_date.setText(year+"-"+month+"-"+dayOfMonth);
                        syear = year;
                        smonth = month;
                        sday = dayOfMonth;

                    }
                },mc.get(java.util.Calendar.YEAR),mc.get(java.util.Calendar.MONTH),
                        mc.get(java.util.Calendar.DAY_OF_MONTH)).show();

            }
        });

        edit_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(UpdateTaskItem.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edit_end_date.setText(year+"-"+month+"-"+dayOfMonth);
                        eyear = year;
                        emonth = month;
                        eday = dayOfMonth;
                    }
                },mc.get(java.util.Calendar.YEAR),mc.get(java.util.Calendar.MONTH),
                        mc.get(java.util.Calendar.DAY_OF_MONTH)).show();

            }
        });



        edit_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(UpdateTaskItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edit_start_time.setText(hourOfDay+":"+minute);
                        shour = hourOfDay;
                        sminute = minute;
                    }
                },mc.get(java.util.Calendar.HOUR_OF_DAY), java.util.Calendar.MINUTE,true).show();

            }
        });

        edit_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(UpdateTaskItem.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edit_end_time.setText(hourOfDay+":"+minute);
                        ehour = hourOfDay;
                        eminute = minute;

                    }
                },mc.get(java.util.Calendar.HOUR_OF_DAY), java.util.Calendar.MINUTE,true).show();

            }
        });

        Button clear_btn = (Button) findViewById(R.id.clear_editText_update_task);
        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_plan_name.setText("");
                edit_start_date.setText("");
                edit_start_time.setText("");
                edit_end_date.setText("");
                edit_end_time.setText("");
            }
        });

    }
}
