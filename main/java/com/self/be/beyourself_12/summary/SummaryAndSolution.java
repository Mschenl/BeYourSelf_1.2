package com.self.be.beyourself_12.summary;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.chart.MyBarChart;
import com.self.be.beyourself_12.chart.MyBubbleChart;
import com.self.be.beyourself_12.chart.MyLineChart;
import com.self.be.beyourself_12.chart.MyPieChart;
import com.self.be.beyourself_12.chart.MyRadarChart;
import com.self.be.beyourself_12.chart.MyScatterChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 针对的第三个界面 summaryAndSolution
 *
 */

public class SummaryAndSolution {
    private AppCompatActivity mainActivity;
    private GridView gridView;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private String[] iconName = { "扇形统计", "条形图", "折现图", "蜘蛛图", "散点图", "气泡图", "铃声",
            "设置" };
    private int icon = R.drawable.calender;
    private LinearLayout show_layout;
    public SummaryAndSolution(AppCompatActivity main){
        mainActivity = main;
    }

    public void MakeView(){

        gridView = (GridView) mainActivity.findViewById(R.id.gv);
        show_layout = (LinearLayout) mainActivity.findViewById(R.id.show_chart_layout_summmary);
        data_list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            //map.put("icon", icon);
            map.put("iconName", iconName[i]);
            data_list.add(map);
        }
        String[] from = { "iconName"};
        int[] to = {R.id.tv_1_summary};
        sim_adapter = new SimpleAdapter(mainActivity, data_list, R.layout.summary_gridview_item, from, to);
        gridView.setAdapter(sim_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemClick( parent,view, position, id);
            }
        });

    }
    public void summmary_click(View view){
        //summaryAndSolution 的点击绑定事件


    }

    public void ItemClick(AdapterView<?> parent, View view, int position, long id){
        show_layout.removeAllViewsInLayout();
        switch(position){
            case 0://扇形图
                new MyPieChart(mainActivity,show_layout).draw_chart();
                break;
            case 1://条形图
                new MyBarChart(mainActivity,show_layout).draw_chart();
                break;
            case 2://折现图
                new MyLineChart(mainActivity,show_layout).draw_chart();
                break;
            case 3://蜘蛛网图
                new MyRadarChart(mainActivity,show_layout).draw_chart();
                break;
            case 4://散点图
                new MyScatterChart(mainActivity,show_layout).draw_chart();
                break;
            case 5://气泡图
                new MyBubbleChart(mainActivity,show_layout).draw_chart();
                break;
            default:
                Toast.makeText(mainActivity,"当前功能还未推出！",Toast.LENGTH_SHORT).show();

        }
    }


}
