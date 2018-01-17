package com.self.be.beyourself_12;



import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.addtask.AddTask;
import com.self.be.beyourself_12.homeshow.HomeShow;
import com.self.be.beyourself_12.setting.Setting;
import com.self.be.beyourself_12.summary.SummaryAndSolution;


public class MainActivity extends AppCompatActivity {
    public static MainActivity mainActivity;    //将自己的指针进行封装进行共享
    private MyDataBase myDataBase;

    private HomeShow homeShow;
    private AddTask addTask;
    private SummaryAndSolution summaryAndSolution;
    private Setting setting;


    private ImageButton btn_show,btn_task,btn_solution,btn_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        init_datebase();
        setContentView(R.layout.activity_main);
        make_main_view();
        homeShow.MakeView();

        init_view();

    }
    //生成几个大的主界面
    private void make_main_view(){
        homeShow = new HomeShow(this);
        addTask = new AddTask(this);
        summaryAndSolution = new SummaryAndSolution(this);
        setting = new Setting(this);
    }

    private void init_view(){

        btn_show = (ImageButton) findViewById(R.id.bt_showData);
        btn_task = (ImageButton) findViewById(R.id.bt_addTask);
        btn_solution = (ImageButton) findViewById(R.id.bt_summaryAndSolution);
        btn_setting = (ImageButton) findViewById(R.id.bt_setting);
    }


    public void ChangeView(View view){

        //加载主页
        switch (view.getId()){
            case R.id.bt_showData:
                setContentView(R.layout.activity_main);
                homeShow.MakeView();
                break;
            case R.id.bt_addTask:
                setContentView(R.layout.addtask);
                addTask.MakeView();
                break;
            case R.id.bt_summaryAndSolution:
                setContentView(R.layout.summary_solution);
                summaryAndSolution.MakeView();
                break;
            case R.id.bt_setting:
                setContentView(R.layout.setting);
                setting.MakeView();
                break;
        }
        init_view();
    }
    private void init_datebase(){
        myDataBase = new MyDataBase(this);
    }

    public void HomeShow_click(View view){

    }
    public void AddTask_click(View view){
        addTask.AddTask_click(view);
    }
    public void SummaryAndSolution_click(View view){
        summaryAndSolution.summmary_click(view);
    }
    public void Setting_click(View view){setting.setting_click(view);}

}
