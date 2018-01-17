package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/19.
 */

public class CheckTask extends Activity {

    private ListView listView;
    private SQLiteDatabase db;
    private ArrayList<HashMap<String,Object>> result;
    private String from[] = {"plan_name"};
    private int to[] = {R.id.plan_name_item_task};
    private boolean longClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init_Database();
        setContentView(R.layout.check_task);
        init_view();
    }
    private void init_view(){
        listView = (ListView) findViewById(R.id.listview_CheckTask);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CheckTask.this,UpdateTaskItem.class);
                intent.putExtra("plan_name",(String) result.get(position).get("plan_name"));
                startActivity(intent);
            }
        });
        //listView.setOnCreateContextMenuListener(this);
        registerForContextMenu(listView);

        query();
    }
    //查询并展示相应数据
    private void query(){
        Calendar taday = Calendar.getInstance();
        String taday_date = taday.get(Calendar.YEAR)+"/"+taday.get(Calendar.MONTH)+"/"+taday.get(Calendar.DAY_OF_MONTH);
        String where = "plan_date = ?";
        String whereArg[] = {taday_date};
        Cursor cursor = db.query("plan_daily",null,where,whereArg,null,null,null);
        result= new ArrayList<HashMap<String,Object>>();
        int index = cursor.getColumnIndex("plan_name");
        while(cursor.moveToNext()){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("plan_name",cursor.getString(index));
            map.put("_id",cursor.getInt(0));
            result.add(map);
        }
        cursor.close();
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,result,R.layout.item_task,from,to);
        Toast.makeText(this,"发现"+result.size()+"个计划",Toast.LENGTH_SHORT).show();

        listView.setAdapter(simpleAdapter);
    }
    public void Button_click(View view){
        //跳转到添加计划界面
        switch (view.getId()){
            case R.id.return_check_task:
                //结束当前界面返回
                finish();
                break;
            case R.id.add_task_check_task:
                //转到添加任务界面
                Intent intent=  new Intent(this,AddTaskItem.class);
                startActivity(intent);
                break;
            case R.id.clear_task_added_check_task:
                //清除所有计划
                int delete = db.delete("plan",null,null);
                Toast.makeText(this,"已删除"+delete+"个计划！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.refresh_check_task:
                query();    //刷新
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("编辑");
        //menu.setHeaderIcon(R.drawable.); 设定上下文的icon
        menu.add(0,0, Menu.NONE,"删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()){
            case 0:
                String id[] = {result.get(menuInfo.position).get("_id").toString()};
                int delete = db.delete("plan","_id = ?",id);
                if(delete != 0){
                    Toast.makeText(this,"删除成功！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    private void init_Database(){
        db = MyDataBase.db;
    }

}
