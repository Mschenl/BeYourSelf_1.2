package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.dataInfo.PersonInfo;
import com.self.be.beyourself_12.time.NowTime;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/2/5.
 */

public class AddDailyTask extends Activity{

    private SQLiteDatabase db;
    private EditText edit_start_time,edit_end_time,edit_plan_name;

    private Spinner spinner_plan_type;
    private String start_time,end_time,plan_type;
    private Button btn_finsh;
    private Calendar mc;
    private int begin,end;
    private String plan_date;
    public static String types[] = {"学习工作","日常必需","休闲娱乐","体育锻炼","空闲自由"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_daily_task);
        init_Database();
        make_view();
    }

    private void make_view(){
        mc = Calendar.getInstance();
        plan_type = null;
        plan_date = getIntent().getStringExtra("plan_date");
        edit_plan_name = (EditText) findViewById(R.id.plan_name_daily_task);
        edit_start_time = (EditText) findViewById(R.id.start_time_daily_task);
        edit_end_time = (EditText) findViewById(R.id.end_time_daily_task);
        spinner_plan_type = (Spinner) findViewById(R.id.plan_type_daily_task);
        btn_finsh = (Button) findViewById(R.id.finish_add_daily_task);
        init_picker();
        init_click();
    }
    private void init_picker(){
        edit_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(AddDailyTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edit_start_time.setText(hourOfDay+":"+minute);
                        begin = hourOfDay * 60 +minute;
                        start_time = hourOfDay+":"+minute;
                    }
                },mc.get(Calendar.HOUR_OF_DAY),mc.get(Calendar.MINUTE),true).show();

            }
        });

        edit_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(AddDailyTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edit_end_time.setText(hourOfDay+":"+minute);
                        end = hourOfDay * 60 +minute;
                        end_time = hourOfDay+":"+minute;
                    }
                },mc.get(Calendar.HOUR_OF_DAY),mc.get(Calendar.MINUTE),true).show();

            }
        });


    }
    private void init_click(){
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        int resid = android.R.layout.simple_spinner_dropdown_item;
        adapter.setDropDownViewResource(resid);
        spinner_plan_type.setAdapter(adapter);
        spinner_plan_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plan_type = types[position];
                Toast.makeText(AddDailyTask.this,"已为该项选中"+plan_type+"类型",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                plan_type = types[0];
                Toast.makeText(AddDailyTask.this,"默认选择"+plan_type+"类型",Toast.LENGTH_SHORT).show();
            }
        });

        btn_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plan_name = edit_plan_name.getText().toString();
                if( plan_name.isEmpty()||plan_type.isEmpty()||end_time.isEmpty()||start_time.isEmpty()){
                    Toast.makeText(AddDailyTask.this,"添加失败，请将信息填写完整！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(begin > end){
                    end = 1440 + end;
                }
                if(plan_date.isEmpty()){
                    plan_date = new NowTime().getNowDate();
                }
                int total_time = end - begin;
                ContentValues values = new ContentValues();
                values.put("user_id", PersonInfo.get_user_id());
                values.put("plan_name",plan_name);
                values.put("start_time",start_time);
                values.put("end_time",end_time);
                values.put("plan_time",total_time);
                values.put("plan_date",plan_date);
                values.put("plan_type",plan_type);
                long insert = db.insert("plan_daily",null,values);
                if(insert != 0){
                    Toast.makeText(AddDailyTask.this,"成功添加日常规划！",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        Button btn_clear = (Button) findViewById(R.id.clear_editText_add_daily_task);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_end_time.setText("");
                edit_start_time.setText("");
                edit_plan_name.setText("");
            }
        });

    }
    public void init_Database(){
        db = MyDataBase.db;
    }

}
