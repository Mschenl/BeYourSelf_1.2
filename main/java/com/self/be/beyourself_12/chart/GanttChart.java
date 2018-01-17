package com.self.be.beyourself_12.chart;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.addtask.AddDailyTask;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/2/6.
 */

public class GanttChart extends View{
    private AppCompatActivity mainActivity;
    private SQLiteDatabase db;
    private int screenWidth,screenHeight;
    private int win_left;
    private int win_top ;
    private int win_right;
    private int win_bottom;
    private float time_step,category_step;
    private float minute_step;
    private String types[];
    public static int colors[]={Color.BLUE,Color.GREEN,Color.RED,Color.YELLOW, Color.MAGENTA};
    private String plan_date;
    public GanttChart(Context context,String choose_date){
        super(context);
        init_database();
        mainActivity = (AppCompatActivity) context;
        init_device_info();
        plan_date = choose_date;
    }
    private void init_data(Canvas canvas){

        if(!MyDataBase.have_data_plan_daily(plan_date)){
            Toast.makeText(mainActivity,"今日任务数据为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String where = "plan_date = ?";
        String whereArg[] = {plan_date};
        Cursor cursor = db.query("plan_daily",null,where,whereArg,null,null,null);
        int name_index = cursor.getColumnIndex("plan_name");
        int start_index = cursor.getColumnIndex("start_time");
        int end_index = cursor.getColumnIndex("end_time");
        int type_index = cursor.getColumnIndex("plan_type");
        category_step = (win_bottom - win_top)/cursor.getCount();
        int i = 0;
        while(cursor.moveToNext()){
            String plan_name = cursor.getString(name_index);
            String start []= cursor.getString(start_index).split(":");
            String end [] = cursor.getString(end_index).split(":");
            int start_hour = Integer.valueOf(start[0]);
            int start_minute = Integer.valueOf(start[1]);
            int end_hour = Integer.valueOf(end[0]);
            int end_minute = Integer.valueOf(end[1]);
            String plan_type = cursor.getString(type_index);
            int color = colors[0];//默认为blue
            for(int k =0 ;k<types.length;k++){
                if(types[k].equals(plan_type)){
                    color = colors[k];
                    break;
                }
            }
            if(start_hour*60 +start_minute > end_hour*60+end_minute){
                addTask(canvas,plan_name,start_hour,start_minute,0,0,i,color);
                addTask(canvas,plan_name,0,0,end_hour,end_minute,i,color);
            }
            else{
                addTask(canvas,plan_name,start_hour,start_minute,end_hour,end_minute,i,color);
            }

            i++;
        }
        cursor.close();
    }
    private void init_device_info(){
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        mainActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        screenWidth = dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        win_left = 80;
        win_top = 40;
        win_right = screenWidth - 60;
        win_bottom = screenHeight - 365;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置甘特图的外轮廓
        add_frame(canvas);
        init_data(canvas);

    }
    private void add_frame(Canvas canvas){

        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawRect(win_left,win_top,win_right,win_bottom,p);

        time_step = (win_right - win_left)/24;
        minute_step = time_step/60;
        System.out.print(minute_step);

        p.setStyle(Paint.Style.FILL);
        p.setTextSize(20);
        int y1 = win_top;
        int y2 = win_bottom;
        for (int i =1; i<8;i++){
            float x = win_left + time_step*i*3;
            canvas.drawText(i*3+"点",x,y1-15,p);
            canvas.drawLine(x,y1,x,y2,p);
        }
        canvas.drawText("0点",win_left,y1-15,p);
        canvas.drawText("24点",win_right,y1-15,p);
    }

    public void addTask(Canvas canvas,String plan_name, int hour1,int minute1,int hour2,int minute2,int index,int color){

        Paint p = new Paint();
        p.setColor(color);
//      Canvas canvas = new Canvas();
        float x1 = hour1 * time_step + minute1* minute_step + win_left;
        float y1 = index * category_step + win_top;
        float x2 = hour2 * time_step + minute2* minute_step + win_left;
        float y2 = y1 + category_step;
        RectF rectF = new RectF(x1,y1,x2,y2);
        canvas.drawRoundRect(rectF,5,5,p);
        //设置任务名称的位置
        int x = 15;
        float y = (y1+y2)/2;
        //添加任务名称
        p.setColor(Color.BLACK);
        p.setTextSize(25);
        canvas.drawText(plan_name,x,y,p);
    }
    public void init_database(){
        db = MyDataBase.db;
        types = AddDailyTask.types;
    }
}
