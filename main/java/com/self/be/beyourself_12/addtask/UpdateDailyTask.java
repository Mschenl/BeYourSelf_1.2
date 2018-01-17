package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;

/**
 * Created by Administrator on 2017/2/7.
 */

public class UpdateDailyTask extends Activity{
    private Button btn_finsh,btn_clear;
    private TextView tv_title;
    private Spinner edit_plan_type;
    private EditText edit_plan_name,edit_start_time,edit_end_time;
    private int _id;
    private SQLiteDatabase db;
    private String start_time,end_time,plan_name,plan_type;
    private int begin,end,total_time;
    private TextView tv_start_time,tv_end_time;
    private String types[] = {"学习工作","日常必需","休闲娱乐","体育锻炼","空闲自由"} ;
//    private String s[] = new String [2];
//    private String e[] = new String [2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_daily_task);
        init_database();
        init_view();
        init_click();
        init_data();
        init_picker();
    }
    private void init_view(){
        btn_finsh = (Button) findViewById(R.id.finish_add_daily_task);
        btn_clear = (Button) findViewById(R.id.clear_editText_add_daily_task);
        edit_plan_name = (EditText) findViewById(R.id.plan_name_daily_task);
        edit_start_time = (EditText) findViewById(R.id.start_time_daily_task);
        edit_end_time = (EditText) findViewById(R.id.end_time_daily_task);
        edit_plan_type = (Spinner) findViewById(R.id.plan_type_daily_task);
        tv_title = (TextView) findViewById(R.id.title_add_daily_task_item);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time_add_daily_task);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time_add_daily_task);
        tv_title.setText("修改日常规划");
    }
    private void init_click(){
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_plan_name.setText("");
                edit_start_time.setText("");
                edit_end_time.setText("");
            }
        });
        //进行修改数据操作
        btn_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_plan_name.getText().toString().isEmpty()||
                        edit_start_time.toString().isEmpty()||
                        edit_end_time.getText().toString().isEmpty()||
                        plan_type.isEmpty()){
                    Toast.makeText(UpdateDailyTask.this,"请将信息填写完整！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(end < begin){
                    end += 1440;
                }
                total_time = end - begin;
                ContentValues values = new ContentValues();
                values.put("plan_name", plan_name);
                values.put("start_time",start_time);
                values.put("end_time",end_time);
                values.put("plan_type",plan_type);
                values.put("plan_time",total_time);
                String whereClause = "_id = ?";
                String whereArg [] = {String.valueOf(_id)};
                int update = db.update("plan_daily",values,whereClause,whereArg);
                if(update != 0){
                    Toast.makeText(UpdateDailyTask.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(UpdateDailyTask.this,"修改失败！请重试",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void init_picker(){
        final String s[] = start_time.split(":");
        final String e[] = end_time.split(":");


        edit_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(UpdateDailyTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edit_start_time.setText(hourOfDay+":"+minute);
                        begin = hourOfDay * 60 +minute;
                        start_time = hourOfDay+":"+minute;
                    }
                },Integer.parseInt(s[0]) ,Integer.parseInt(s[1]),true).show();

            }
        });

        edit_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new TimePickerDialog(UpdateDailyTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edit_end_time.setText(hourOfDay+":"+minute);
                        end = hourOfDay * 60 +minute;
                        end_time = hourOfDay+":"+minute;
                    }
                },Integer.parseInt(e[0]),Integer.parseInt(e[1]),true).show();

            }
        });

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        int resid = android.R.layout.simple_spinner_dropdown_item;
        adapter.setDropDownViewResource(resid);
        edit_plan_type.setAdapter(adapter);
        edit_plan_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                plan_type = types[position];
                Toast.makeText(UpdateDailyTask.this,"已为该项选中"+plan_type+"类型",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                plan_type = types[types.length-1];
                Toast.makeText(UpdateDailyTask.this,"默认选择"+plan_type+"类型",Toast.LENGTH_SHORT).show();
            }
        });
        for(int i =0;i<types.length;i++){
            if (types[i].equals(plan_type)) {
                edit_plan_type.setSelection(i);
                break;
            }
        }
    }
    private void init_data(){
        Intent intent = getIntent();
        _id = intent.getIntExtra("_id",-1);
        if(_id == -1){
            Toast.makeText(this,"初始化数据错误！",Toast.LENGTH_LONG).show();
            return;
        }
        String whereArg[] = {String.valueOf(_id)};
        Cursor cursor = db.query("plan_daily",null,"_id = ?",whereArg,null,null,null);
        int start_index = cursor.getColumnIndex("start_time");
        int end_index = cursor.getColumnIndex("end_time");
        int name_index = cursor.getColumnIndex("plan_name");
        int type_index = cursor.getColumnIndex("plan_type");
        cursor.moveToFirst();
        end = begin = 0;
        start_time = cursor.getString(start_index);
        tv_start_time.setText(tv_start_time.getText().toString()+"（当前为"+start_time+")");
        end_time = cursor.getString(end_index);
        tv_end_time.setText(tv_end_time.getText().toString()+"（当前为"+end_time+")");
        plan_name = cursor.getString(name_index);
        plan_type = cursor.getString(type_index);
        edit_plan_name.setText(plan_name);


    }
    private void init_database(){
        db = MyDataBase.db;
    }
}
