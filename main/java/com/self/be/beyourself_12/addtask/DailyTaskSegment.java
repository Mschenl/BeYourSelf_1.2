package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.dataInfo.PersonInfo;
import com.self.be.beyourself_12.time.NowTime;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Administrator on 2017/2/10.
 */

public class DailyTaskSegment extends Activity{
    private SQLiteDatabase db;
    private GridView gridView;
    private LinearLayout gv_layout;
    private View now_view = null;
    private int _id;
    private ArrayList<HashMap<String,Object>> segment_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_task_segment);
        init_database();
        init_view();
        init_data_gridView_segment();

    }
    private void init_view(){
        _id = getIntent().getIntExtra("_id",-1);
        gridView = (GridView) findViewById(R.id.gv_daily_task_segment);
        gv_layout = (LinearLayout) findViewById(R.id.root_layout_daily_task_segment);

        now_view = gridView;
    }
    private void init_data_gridView_segment(){


        String selectionArg[] = {String.valueOf(_id)};
        Cursor cursor = db.query("plan_daily_segment",null,"_id_plan_daily = ?",selectionArg,null,null,null);
        int segment_name_index = cursor.getColumnIndex("segment_name") ;
        int segment_id_index = cursor.getColumnIndex("_id");
        segment_info = new ArrayList<>();
        while(cursor.moveToNext()){
            HashMap<String,Object> map = new HashMap<>();
            map.put("segment_name",cursor.getString(segment_name_index));
            map.put("segment_id",cursor.getInt(segment_id_index));
            segment_info.add(map);
        }
        String from [] = {"segment_name"};
        int to [] = {R.id.tv_1_item_segment};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,segment_info,R.layout.item_segment,from,to);
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //int _id_segment_item = (int) segment_info.get(position).get("segment_id");
                init_update_segment(segment_info,position);
            }
        });


    }
    public void Btn_Click(View view){
        switch (view.getId()){
            case R.id.imb_home_segment_daily_task:
                gv_layout.removeView(now_view);
                init_data_gridView_segment();
                gv_layout.addView(gridView);
                now_view = gridView;
                break;
            case R.id.imb_add_segment_daily_task:
                init_addTaskSegment();
                break;
            case R.id.return_daily_task_segment:
                finish();
                break;
            case R.id.imb_delete_segment_daily_task:
                init_delete_segment();
                break;
        }
    }

    private void init_addTaskSegment(){
        //显示添加segment 界面
        gv_layout.removeView(now_view);
        LayoutInflater inflater = getLayoutInflater();
        final View add_segment = inflater.inflate(R.layout.add_daily_task_segment,null);
        gv_layout.addView(add_segment);
        now_view = add_segment;//当前界面为add_segment

        final EditText edit_segment = (EditText) add_segment.findViewById(R.id.edit_segment_name_daily_task);

        Button btn_finsh_add = (Button) add_segment.findViewById(R.id.confirm_add_segment);
        btn_finsh_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_segment.getText().toString().isEmpty()){
                    Toast.makeText(DailyTaskSegment.this,"请将信息填写完整！",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values  = new ContentValues();
                values.put("user_id", PersonInfo.get_user_id());
                values.put("_id_plan_daily",_id);
                values.put("segment_name",edit_segment.getText().toString());
                values.put("if_finish",0);
                values.put("finish_date",new NowTime().getNowDate());
                values.put("score",0);
                long insert = db.insert("plan_daily_segment",null,values);
                if(insert > 0){
                    Toast.makeText(DailyTaskSegment.this,"添加环节成功！",Toast.LENGTH_SHORT).show();
                    gv_layout.removeView(add_segment);
                    init_data_gridView_segment();
                    gv_layout.addView(gridView);
                    now_view = gridView;
                }
            }
        });

    }
    private void init_update_segment(ArrayList<HashMap<String,Object>> segment_info,int position){
        gv_layout.removeView(now_view);
        LayoutInflater inflater = getLayoutInflater();
        final View update_segment = inflater.inflate(R.layout.add_daily_task_segment,null);
        gv_layout.addView(update_segment);
        now_view = update_segment;
        final EditText edit_segment_name = (EditText) update_segment.findViewById(R.id.edit_segment_name_daily_task);
        Button btn_confirm = (Button)update_segment.findViewById(R.id.confirm_add_segment);
        String segment_name = (String) segment_info.get(position).get("segment_name");
        final int segment_id = (int) segment_info.get(position).get("segment_id");
        edit_segment_name.setText(segment_name);
        btn_confirm.setText("确认修改");
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_segment_name.getText().toString().isEmpty()){
                    Toast.makeText(DailyTaskSegment.this,"请将修改信息填写完整！",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                values.put("segment_name",edit_segment_name.getText().toString());
                String whereClause = "_id = ?";
                String whereArg [] = {String.valueOf(segment_id)};

                int update = db.update("plan_daily_segment",values,whereClause,whereArg);
                if (update > 0 ){
                    Toast.makeText(DailyTaskSegment.this,"修改成功！",Toast.LENGTH_SHORT).show();
                    gv_layout.removeView(update_segment);
                    init_data_gridView_segment();
                    gv_layout.addView(gridView);
                    now_view = gridView;
                }
            }
        });
    }
    private void init_delete_segment(){

        LinearLayout layout_root = new LinearLayout(this);
        layout_root.setOrientation(LinearLayout.VERTICAL);

        final CheckBox check_box_list [] = new CheckBox[segment_info.size()];
        for(int i = 0 ;i < segment_info.size();i++){
            TextView tv_name = new TextView(this);
            tv_name.setText(segment_info.get(i).get("segment_name").toString());
            LinearLayout layout = new LinearLayout(this);
            CheckBox c = new CheckBox(this);
            check_box_list[i] = c;
            layout.addView(c);
            layout.addView(tv_name);
            layout_root.addView(layout);
        }
        Button btn_confirm_delete = new Button(this);
        btn_confirm_delete.setText("删除");
        btn_confirm_delete.setTextSize(15);
        btn_confirm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean sign = true;
                for(int i = 0;i<check_box_list.length;i++){

                    if(check_box_list[i].isChecked()){
                        //进行删除操作
                        String selected_segment[] = {segment_info.get(i).get("segment_id").toString()};
                        int delete = db.delete("plan_daily_segment","_id = ?",selected_segment);
                        if(delete <= 0 ){
                            Toast.makeText(DailyTaskSegment.this,"删除失败！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                Toast.makeText(DailyTaskSegment.this,"删除成功！",Toast.LENGTH_SHORT).show();

            }
        });
        layout_root.addView(btn_confirm_delete);
        gv_layout.removeView(now_view);
        gv_layout.addView(layout_root);
        now_view = layout_root;

    }
    private void init_database(){
        db = MyDataBase.db;
    }


}
