package com.self.be.beyourself_12.chart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.self.be.beyourself_12.Database.MyDataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Administrator on 2017/2/21.
 */

public class MyScatterChart {

    private AppCompatActivity mainActivity;
    private LinearLayout layout;
    private SQLiteDatabase db;
    private int colors[];
    private ScatterChart scatterChart;

    public MyScatterChart(AppCompatActivity main,LinearLayout lay){
        init_database();
        mainActivity = main;
        layout = lay;
        colors = GanttChart.colors;
    }
    public void draw_chart(){
        scatterChart = new ScatterChart(mainActivity);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layout.addView(scatterChart,p);
        initScatterChart();
    }
    private void initScatterChart() {
        Cursor cursor = db.query("plan_daily",null,null,null,null,null,null);
        int plan_date_index = cursor.getColumnIndex("plan_date");
        int plan_time_index = cursor.getColumnIndex("plan_time");
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();//初始化横纵坐标内容

        ArrayList<String> plan_Date= new ArrayList<>();
        while(cursor.moveToNext()){
            String plan_date = cursor.getString(plan_date_index);
            if(!plan_Date.contains(plan_date)){
                plan_Date.add(cursor.getString(plan_date_index));
                xVals.add(cursor.getString(plan_date_index));
            }
            yVals.add(new Entry(cursor.getInt(plan_time_index),plan_Date.indexOf(plan_date)));

        }
        cursor.close();

        ScatterDataSet scatterDataSet = new ScatterDataSet(yVals, "日期");
        scatterDataSet.setColors(ColorTemplate.COLORFUL_COLORS);//设置丰富多彩的颜色

        ScatterData scatterData = new ScatterData(xVals, scatterDataSet);//生成Scatterdata对象

        scatterChart.setData(scatterData);//设置对应数据
        scatterChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        scatterChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        scatterChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                Toast.makeText(mainActivity, "该项目所用时间为" + entry.getVal(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected() {
            }
        });

        scatterChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴位置
        scatterChart.getAxisRight().setEnabled(false);//右侧Y轴关闭
        scatterChart.getAxisLeft().setAxisMinValue(0.0f);//设置最小Y值
        scatterChart.getXAxis().setGridColor(Color.RED);//设置纵向网格线条颜色
        scatterChart.getAxisLeft().setGridColor(Color.GREEN);//设置横向网格颜色
        scatterChart.getXAxis().setDrawGridLines(true);
        scatterChart.getXAxis().setDrawAxisLine(true);
        scatterChart.setDescription("每日时间分布散点图");//设置描述内容
        scatterChart.setDescriptionTextSize(20.f);//设置描述文字的字体颜色
        scatterChart.animateXY(1000, 1000);//动画效果
    }
    private void init_database(){
        db = MyDataBase.db;
    }
}
