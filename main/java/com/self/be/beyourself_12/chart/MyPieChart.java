package com.self.be.beyourself_12.chart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.time.NowTime;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/5.
 */

public class MyPieChart {
    private AppCompatActivity mainActivity;
    private LinearLayout layout;
    private SQLiteDatabase db;
    private int colors[];

    public MyPieChart(AppCompatActivity main,LinearLayout lay){
        init_database();
        mainActivity = main;
        layout = lay;
        colors = GanttChart.colors;
    }
    public void draw_chart(){
        PieChart pieChart = new PieChart(mainActivity);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        layout.addView(pieChart,p);
        PieData mPieData = getPieData();
        showChart(pieChart, mPieData);
    }
    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription("个人时间分布图");

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText("个人时间分布图");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }


    private PieData getPieData() {
        String selection = "plan_date = ?";
        String selectionArg [] = {new NowTime().getNowDate()};
        Cursor cursor = db.query("plan_daily",null,selection,selectionArg,null,null,null);

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        int name_index = cursor.getColumnIndex("plan_name");
        int time_index = cursor.getColumnIndex("plan_time");
        float precent[] = new float[cursor.getCount()];
        int i = 0,sum= 0;
        while (cursor.moveToNext()) {
            xValues.add(cursor.getString(name_index));
            precent[i] = cursor.getInt(time_index);
            //Toast.makeText(mainActivity,cursor.getString(1)+"为"+cursor.getInt(time_index),Toast.LENGTH_SHORT).show();
            sum += precent[i];
            i++;
        }
        for (i=0; i<precent.length;i++){
            precent[i] = precent[i] / sum * 100;
        }

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        //添加数据
        for(i=0;i< precent.length;i++){
            yValues.add(new Entry(precent[i],i));
        }
//        int quarterly1 = 24;
//        int quarterly2 = 26;
//        int quarterly3 = 36;
//        int quarterly4 = 14;
//        yValues.add(new Entry(quarterly1, 0));
//        yValues.add(new Entry(quarterly2, 1));
//        yValues.add(new Entry(quarterly3, 2));
//        yValues.add(new Entry(quarterly4, 3));

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "图例"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> mycolors = new ArrayList<Integer>();

        // 饼图颜色
        for(i = 0;i<precent.length;i++){
            mycolors.add(colors[i%colors.length]);
        }

        pieDataSet.setColors(mycolors);

        DisplayMetrics metrics = mainActivity.getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }
    private void init_database(){db = MyDataBase.db;}
}
