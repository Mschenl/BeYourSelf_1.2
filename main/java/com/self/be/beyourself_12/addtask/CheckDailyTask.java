package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/6.
 */

public class CheckDailyTask extends Activity{
    private SQLiteDatabase db;
    private ArrayList<HashMap<String,Object>> result;
    private TextView btn_title;
    private String choose_date="";

    private String from[] = {"plan_name"};
    private int to[] = {R.id.plan_name_item_task};
    private ListView listView;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_Database();
        setContentView(R.layout.check_daily_task);
        init_view();
    }
    private void init_view(){
        choose_date = getIntent().getStringExtra("plan_date");
        if(choose_date.isEmpty()){
            choose_date = new NowTime().getNowDate();
        }
        btn_title = (Button) findViewById(R.id.btn_top_title_check_daily_task);
        btn_title.setText(choose_date);
        listView = (ListView) findViewById(R.id.listview_daily_task);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CheckDailyTask.this,InfoDailyTask.class);
                intent.putExtra("_id",(int) result.get(position).get("_id"));
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);
        query_show();
    }
    public void Button_click(View view){

        switch (view.getId()){
            case R.id.return_check_daily_task:
                finish();
                break;
            case R.id.add_daily_task:
                //进入添加日常任务界面
                Intent intent = new Intent(this,AddDailyTask.class);
                intent.putExtra("plan_date",choose_date);
                startActivity(intent);
                query_show();//刷新
                break;
            case R.id.refresh_check_daily_task:
                query_show();
                break;
            case R.id.clear_daily_task_added:
                clear_taday_planDaily();
                break;
            case R.id.btn_top_title_check_daily_task:
                init_calender_view();
                break;

        }

    }
    private void clear_taday_planDaily(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckDailyTask.this);
        builder.setIcon(R.drawable.books);
        builder.setTitle("清除");
        builder.setMessage("确认清除本日计划和相关数据？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String where = "plan_date = ?";
                String whereArg[] = {choose_date};
                int delete = db.delete("plan_daily",where,whereArg);
                where = "finish_date = ?";
                int delete_segment = db.delete("plan_daily_segment",where,whereArg);
                query_show();//刷新
                Toast.makeText(CheckDailyTask.this,"已删除"+delete+"个计划！"+delete_segment+"个相关环节！",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
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
        Toast.makeText(this,"发现"+result.size()+"个日常任务",Toast.LENGTH_SHORT).show();

        listView.setAdapter(simpleAdapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("编辑");
        //menu.setHeaderIcon(R.drawable.); 设定上下文的icon
        menu.add(0,0, Menu.NONE,"删除");
        menu.add(0,1,menu.NONE,"修改");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()){
            case 0:
                String id[] = {result.get(menuInfo.position).get("_id").toString()};
                int delete = db.delete("plan_daily","_id = ?",id);

                if(delete != 0){
                    Toast.makeText(this,"删除成功！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                Intent intent = new Intent(this,UpdateDailyTask.class);
                intent.putExtra("_id",(int)result.get(menuInfo.position).get("_id"));
                startActivity(intent);
        }
        return true;
    }
    private String temp_date;
    private void init_calender_view(){
        LayoutInflater layoutInflater =  getLayoutInflater();
        final View calender = layoutInflater.inflate(R.layout.calender_choose,null);

        final PopupWindow popupWindow = new PopupWindow(calender, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,true);
        popupWindow.showAsDropDown(btn_title,0,0);
        CalendarView calendarView = (CalendarView) calender.findViewById(R.id.calender_view);
        temp_date = new NowTime().getNowDate();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                temp_date = year+"/"+month+"/"+dayOfMonth;
            }

        });
        Button btn_finsh = (Button) calender.findViewById(R.id.btn_finish_choose_date);
        btn_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_date = temp_date;
                if(!choose_date.isEmpty()){
                    btn_title.setText(choose_date);
                    popupWindow.dismiss();
                    query_show();//刷新
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
    private void init_Database(){
        db = MyDataBase.db;
    }
}
