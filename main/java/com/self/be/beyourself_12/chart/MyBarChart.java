package com.self.be.beyourself_12.chart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.time.NowTime;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/9.
 */

public class MyBarChart {
    private AppCompatActivity mainActivity;
    private LinearLayout layout;
    private SQLiteDatabase db;
    private BarChart mBarChart;
    private BarData mBarData;

    public MyBarChart(AppCompatActivity main,LinearLayout lay){
        init_database();
        mainActivity = main;
        layout = lay;
    }
    public void draw_chart(){
        mBarChart = new BarChart(mainActivity);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layout.addView(mBarChart,p);
        mBarData = getBarData();
        showBarChart(mBarChart, mBarData);
    }
    private void showBarChart(BarChart barChart, BarData barData) {
        barChart.setDrawBorders(true);  ////是否在折线图上添加边框

        barChart.setDescription("各项任务所占时间统计图");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("You need to provide data for the chart.");

        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        barChart.setTouchEnabled(true); // 设置是否可以触摸

        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放

        barChart.setPinchZoom(false);//

//      barChart.setBackgroundColor();// 设置背景

        barChart.setDrawBarShadow(true);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

//      X轴设定
//      XAxis xAxis = barChart.getXAxis();
//      xAxis.setPosition(XAxisPosition.BOTTOM);

        barChart.animateX(2500); // 立即执行的动画,x轴
    }

    private BarData getBarData() {
        String selection = "plan_date = ?";
        String selectionArg [] = {new NowTime().getNowDate()};
        Cursor cursor = db.query("plan_daily",null,selection,selectionArg,null,null,null);
        int plan_name_index = cursor.getColumnIndex("plan_name");
        int time_index = cursor.getColumnIndex("plan_time");
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        ArrayList<String> xValues = new ArrayList<String>();
        int i = 0;
        while(cursor.moveToNext()){
            xValues.add(cursor.getString(plan_name_index));
            int plan_time = cursor.getInt(time_index);
            yValues.add(new BarEntry(plan_time,i));
            i ++;
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "各项任务所占时间统计图");

        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);

        return barData;
    }
    private void init_database(){db = MyDataBase.db;}
}
