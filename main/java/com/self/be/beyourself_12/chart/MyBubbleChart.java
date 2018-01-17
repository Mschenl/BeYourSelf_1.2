package com.self.be.beyourself_12.chart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.time.NowTime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/21.
 */

public class MyBubbleChart {
    private AppCompatActivity mainActivity;
    private LinearLayout layout;
    private SQLiteDatabase db;
    private int colors[];
    private BubbleChart bubbleChart;

    public MyBubbleChart(AppCompatActivity main,LinearLayout lay){
        init_database();
        mainActivity = main;
        layout = lay;
        colors = GanttChart.colors;
    }
    public void draw_chart(){
        bubbleChart = new BubbleChart(mainActivity);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layout.addView(bubbleChart,p);
        initScatterChart();
    }
    private void initScatterChart() {
        String selection = "finish_date = ?";
        String selectionArg [] = {new NowTime().getNowDate()};
        String groupby = "_id_plan_daily";
        String columns [] ={"_id_plan_daily","avg(score) as mean_score"};
        Cursor cursor = db.query("plan_daily_segment",columns,selection,selectionArg,groupby,null,null);
        if(cursor.getCount() == 0){
            Toast.makeText(mainActivity,"今日还没有添加任何环节！",Toast.LENGTH_SHORT).show();
            return ;
        }
        int _id_plan_daily_index = cursor.getColumnIndex("_id_plan_daily");
        int mean_score_index = cursor.getColumnIndex("mean_score");
        HashMap<Object,Object> score_map = new HashMap<>();
        while(cursor.moveToNext()){
            score_map.put(cursor.getInt(_id_plan_daily_index),cursor.getFloat(mean_score_index));
        }

        String selection2 = "plan_date = ?";
        Cursor cursor2 = db.query("plan_daily",null,selection2,selectionArg,null,null,null);
        int plan_name_index = cursor2.getColumnIndex("plan_name");
        int plan_time_index = cursor2.getColumnIndex("plan_time");
        ArrayList<BubbleEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();//初始化横纵坐标内容
        int i = 0;
        while(cursor2.moveToNext()){
            xVals.add(cursor2.getString(plan_name_index));
            float score = (float)score_map.get(cursor2.getInt(0));
            yVals.add(new BubbleEntry(i,cursor2.getInt(plan_time_index),score));
            i++;
        }
        cursor.close();
        cursor2.close();

        BubbleDataSet bubbleDataSet = new BubbleDataSet(yVals,"日期");
        bubbleDataSet.setColors(ColorTemplate.COLORFUL_COLORS);//设置丰富多彩的颜色

        BubbleData bubbleData = new BubbleData(xVals, bubbleDataSet);//生成Scatterdata对象

        bubbleChart.setData(bubbleData);//设置对应数据
        bubbleChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        bubbleChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
//        bubbleChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//            @Override
//            public void onValueSelected(Entry entry, int i, Highlight highlight) {
//                Toast.makeText(mainActivity, "该项目所用时间为" + entry.getVal(), Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onNothingSelected() {
//            }
//        });

        bubbleChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴位置
        bubbleChart.getAxisRight().setEnabled(false);//右侧Y轴关闭
        bubbleChart.getAxisLeft().setAxisMinValue(0.0f);//设置最小Y值
        bubbleChart.getXAxis().setGridColor(Color.RED);//设置纵向网格线条颜色
        bubbleChart.getAxisLeft().setGridColor(Color.GREEN);//设置横向网格颜色
        bubbleChart.getXAxis().setDrawGridLines(true);
        bubbleChart.getXAxis().setDrawAxisLine(true);
        bubbleChart.setDescription("每日时间分布散点图");//设置描述内容
        bubbleChart.setDescriptionTextSize(20.f);//设置描述文字的字体颜色
        bubbleChart.animateXY(1000, 1000);//动画效果
    }
    private void init_database(){
        db = MyDataBase.db;
    }
}
