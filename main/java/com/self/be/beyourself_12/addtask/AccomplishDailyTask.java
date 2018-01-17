package com.self.be.beyourself_12.addtask;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.time.NowTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/13.
 */

public class AccomplishDailyTask extends Activity {
    private SQLiteDatabase db;
    private ListView listView;
    private View now_view;
    private GridView gv_segment_list;
    private LinearLayout root_layout;
    ArrayList <HashMap<String,Object>> segment_list;
    private float plan_score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accomplish_daily_task);
        init_database();
        init_view();
        init_listview_data();
        now_view = listView;
    }
    private void init_view(){
        listView = (ListView) findViewById(R.id.lv_accomplish_daily_task);
        root_layout = (LinearLayout) findViewById(R.id.root_layout_accomplish_daily_task);
    }
    public void Btn_Click(View view){
        switch (view.getId()){
            case R.id.return_accomplish_daily_task:
                finish();
                break;
            case R.id.imb_home_segment_accomplish:
                refresh_listView_data();
                break;
        }
    }
    private void init_database(){
        db = MyDataBase.db;
    }

    private void refresh_listView_data(){
        root_layout.removeView(now_view);
        init_listview_data();
        root_layout.addView(listView);
        now_view = listView;
    }
    private void init_listview_data(){

        String taday_date = new NowTime().getNowDate();
        String where = "plan_date = ?";
        String whereArg[] = {taday_date};
        Cursor cursor = db.query("plan_daily",null,where,whereArg,null,null,null);

        int plan_name_index = cursor.getColumnIndex("plan_name");
        final ArrayList<HashMap<String,Object>> list = new ArrayList<>();
        while(cursor.moveToNext()){
            String name = cursor.getString(plan_name_index);
            HashMap<String,Object> map = new HashMap<>();
            map.put("plan_name",name);
            map.put("_id_plan_daily",cursor.getInt(0));
            list.add(map);
        }
        cursor.close();
        String from[] = {"plan_name"};
        int to[] = {R.id.tv_item_segment_name_accomplish};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list,R.layout.listview_item_accomplish_dailytask,from,to);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                init_task_segment((int) list.get(position).get("_id_plan_daily"));
            }
        });

    }
    private void init_task_segment(int _id_plan){

        segment_list = new ArrayList<>();
        String _id_plan_daily[] = {String.valueOf(_id_plan)};
        final Cursor cursor = db.query("plan_daily_segment",null,"_id_plan_daily = ?",_id_plan_daily,null,null,null);
        int segment_name_index = cursor.getColumnIndex("segment_name");
        int if_finsh_index = cursor.getColumnIndex("if_finish");
        int score_index = cursor.getColumnIndex("score");
        while(cursor.moveToNext()){
            HashMap<String,Object> map = new HashMap<>();
            map.put("segment_name",cursor.getString(segment_name_index));
            map.put("segment_id",cursor.getInt(0));
            map.put("if_finish",cursor.getInt(if_finsh_index));
            map.put("score",cursor.getDouble(score_index));
            segment_list.add(map);
        }

        gv_segment_list = new GridView(this);
        gv_segment_list.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        gv_segment_list.setNumColumns(GridView.AUTO_FIT);
        gv_segment_list.setColumnWidth(150);
        gv_segment_list.setHorizontalSpacing(10);
        gv_segment_list.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gv_segment_list.setVerticalSpacing(10);

        gv_segment_list.setAdapter(new MyAdapter());
        gv_segment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish_segment(cursor,position);
            }
        });
        root_layout.removeView(now_view);
        root_layout.addView(gv_segment_list);
        now_view = gv_segment_list;
    }
    private void finish_segment(Cursor cursor,int position){
        Button  btn = (Button) findViewById(R.id.return_accomplish_daily_task);
        LayoutInflater inflater = getLayoutInflater();
        View finish_layout = inflater.inflate(R.layout.finish_segment_daily_plan,null);
        final PopupWindow popupWindow = new PopupWindow(finish_layout,RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.showAsDropDown(btn,0,0);

        TextView tv_show_segment_name = (TextView) finish_layout.findViewById(R.id.segment_name_finish_daily_plan);
        cursor.moveToPosition(position);
        int name_index = cursor.getColumnIndex("segment_name");
        tv_show_segment_name.setText(cursor.getString(name_index));
        final int segment_id = cursor.getInt(0);
        Button btn_return = (Button)finish_layout.findViewById(R.id.return_finish_daily_plan_segment);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        Button btn_finish = (Button)finish_layout.findViewById(R.id.finish_segment_daily_plan);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("if_finish",true);
                values.put("finish_time", new NowTime().getNowTime());
                values.put("score",plan_score);
                int update = db.update("plan_daily_segment",values,"_id = ?",new String[]{String.valueOf(segment_id)});
                if(update > 0 ){
                    Toast.makeText(AccomplishDailyTask.this,"该环节成功完成！",Toast.LENGTH_SHORT).show();
                    refresh_listView_data();
                    popupWindow.dismiss();
                }
            }
        });
        final TextView showscore = (TextView)finish_layout.findViewById(R.id.show_score_accomplish_Daily);
        RatingBar ratingBar = (RatingBar) finish_layout.findViewById(R.id.ratingBar_finish_segment);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                showscore.setText("自我评分"+rating+"分");
                plan_score = rating;
            }
        });
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return segment_list.size() ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.gridview_accomplish_daily_task,null);
            TextView tv_segment_name = (TextView) view.findViewById(R.id.tv_segment_List_name_gv);
            tv_segment_name.setText(segment_list.get(position).get("segment_name").toString());
            TextView tv_if_finish = (TextView) view.findViewById(R.id.if_finsh_sign_segment_list);

            int sign = (int)segment_list.get(position).get("if_finish");
            double score = (double)segment_list.get(position).get("score");
            if (sign != 0){
                tv_if_finish.setText("已完成 "+score+"分");
            }
            else {
                tv_if_finish.setText("未完成");
            }

            return view;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return segment_list.get(position);
        }
    }

}
