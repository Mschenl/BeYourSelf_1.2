package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/9.
 */

public class InfoDailyTask extends Activity{
    private SQLiteDatabase db;
    private TextView tv_start_time,tv_end_time,tv_plan_name,tv_plan_type,tv_plan_time;
    private GridView gridView;
    private int _id;
    private String[] iconName = { "环节", "标记", "统计",
            "设置" };
    private int icons = R.drawable.key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_daily_task);
        init_database();
        init_view();
        init_data();
    }
    private void init_view(){
        tv_plan_name = (TextView) findViewById(R.id.plan_name_info_daily_task);
        tv_start_time = (TextView) findViewById(R.id.start_time_info_daily_task);
        tv_end_time = (TextView) findViewById(R.id.end_time_info_daily_task);
        tv_plan_type = (TextView) findViewById(R.id.plan_type_info_daily_task);
        tv_plan_time = (TextView) findViewById(R.id.plan_time_info_daily_task);
        gridView = (GridView) findViewById(R.id.grid_view_info_daily_task);
        ArrayList<HashMap<String,Object>> data_list = new ArrayList<>();
        for (int i = 0;i<iconName.length;i++){
            HashMap<String,Object> map = new HashMap<>();
            map.put("icon_name",iconName[i]);
            map.put("icon",icons);
            data_list.add(map);
        }
        String[] from = { "icon_name","icon"};
        int[] to = {R.id.tv_1_summary,R.id.im_btn_1_summary};
        SimpleAdapter sim_adapter = new SimpleAdapter(this, data_list, R.layout.summary_gridview_item, from, to);
        gridView.setAdapter(sim_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemClick(parent,view,position,id);
            }
        });
    }
    private void ItemClick(AdapterView<?> parent, View view, int position, long id){
        switch (position){
            case 0:
                //打开环节界面
                Intent intent = new Intent(this,DailyTaskSegment.class);
                intent.putExtra("_id",_id);
                startActivity(intent);
                break;


        }
    }
    private void init_data(){
        Intent intent = getIntent();
        _id = intent.getIntExtra("_id",-1);
        if(_id == -1){
            Toast.makeText(this,"初始化错误!",Toast.LENGTH_SHORT).show();
        }
        Cursor cursor = db.query("plan_daily",null,"_id = ?",new String []{String.valueOf(_id)},null,null,null);
        int name_index = cursor.getColumnIndex("plan_name");
        int start_index = cursor.getColumnIndex("start_time");
        int end_index = cursor.getColumnIndex("end_time");
        int type_index = cursor.getColumnIndex("plan_type");
        int time_index = cursor.getColumnIndex("plan_time");
        cursor.moveToNext();
        tv_plan_name.setText(cursor.getString(name_index));
        tv_start_time.setText(cursor.getString(start_index));
        tv_end_time.setText(cursor.getString(end_index));
        tv_plan_time.setText(cursor.getInt(time_index)+"分钟");
        tv_plan_type.setText(cursor.getString(type_index));
    }
    private void init_database(){
        db = MyDataBase.db;
    }
    public void Btn_Click(View view){
        switch (view.getId()){
            case R.id.update_info_daily_task:
                Intent intent= new Intent(this,UpdateDailyTask.class);
                intent.putExtra("_id",_id);
                startActivity(intent);
                break;
        }
    }
}
