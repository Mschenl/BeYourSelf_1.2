package com.self.be.beyourself_12.chart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.self.be.beyourself_12.Database.MyDataBase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/21.
 */

public class MyRadarChart {
    private AppCompatActivity mainActivity;
    private LinearLayout layout;
    private SQLiteDatabase db;
    private int colors[];
    private RadarData mRadarDate;

    public MyRadarChart(AppCompatActivity main,LinearLayout lay){
        init_database();
        mainActivity = main;
        layout = lay;
        colors = GanttChart.colors;
    }
    public void draw_chart(){
        RadarChart radarChart = new RadarChart(mainActivity);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layout.addView(radarChart,p);

        mRadarDate = getRadarData();
        showChart(radarChart, mRadarDate, Color.rgb(255, 255, 255));
    }
    private void showChart(RadarChart radarChart, RadarData pieData, int color) {

        radarChart.setDescription("各项时间分布图");



        radarChart.setRotationAngle(90); // 初始旋转角度


        // enable rotation of the chart by touch
        radarChart.setDrawWeb(true);
        radarChart.setRotationEnabled(true); // 可以手动旋转
        radarChart.setBackgroundColor(color);
        radarChart.setWebColor(Color.rgb(114, 188, 223));
        radarChart.setWebColorInner(Color.YELLOW);
        radarChart.setWebLineWidthInner(1.75f);
        //设置数据
        radarChart.setData(pieData);


        Legend mLegend = radarChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        radarChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }
    private RadarData getRadarData() {
        String groupby = "plan_type";
        String columns[] = {"plan_type","sum(plan_time) as total_time"};
        Cursor cursor = db.query("plan_daily",columns,null,null,groupby,null,null);
        int plan_type_index = cursor.getColumnIndex("plan_type");
        int total_time_index = cursor.getColumnIndex("total_time");

        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        int i = 0;
        while(cursor.moveToNext()) {
            xValues.add(cursor.getString(plan_type_index));
            int total_time = cursor.getInt(total_time_index);
            yValues.add(new Entry(total_time, i));
            i++;
        }


        //y轴的集合
        RadarDataSet radarDataSet = new RadarDataSet(yValues, "图例");


        ArrayList<Integer> mycolors = new ArrayList<Integer>();

        // 颜色
        for(i = 0;i<xValues.size();i++){
            mycolors.add(colors[i%colors.length]);
        }

        radarDataSet.setColors(mycolors);

//        DisplayMetrics metrics = mainActivity.getResources().getDisplayMetrics();
//        float px = 5 * (metrics.densityDpi / 160f);
//        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        RadarData radarData = new RadarData(xValues, radarDataSet);

        return radarData;
    }
    private void init_database(){
        db = MyDataBase.db;
    }
}
