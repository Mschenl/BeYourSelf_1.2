package com.self.be.beyourself_12.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.MainActivity;
import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.dataInfo.PersonInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/16.
 */

public class Setting {
    private AppCompatActivity mainActivity;
    private ListView listView;
    private SimpleAdapter sim_adapter;
    private List<HashMap<String,Object>> resList;
    private AlertDialog dialog;

    private AlertDialog loginDialog = null;
    private AlertDialog listDialog= null;
    public Setting(AppCompatActivity main){
        mainActivity = main;

    }
    public void MakeView(){
        listView = (ListView) mainActivity.findViewById(R.id.listView_setting);
        int picture[] = {R.drawable.flashlight,R.drawable.calender,
                R.drawable.home_main,R.drawable.key};
        String TitleList [] = new String[]{"解决方案","专业报表","清除本地所有","上传数据"};

        resList = new ArrayList<HashMap<String, Object>>();
        for(int i =0;i<TitleList.length;i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("icon",picture[i]);
            map.put("Title",TitleList[i]);
            resList.add(map);
        }
        String from[] = {"icon","Title"};
        int to [] = {R.id.im_1_set,R.id.tv_1_set};
        sim_adapter = new SimpleAdapter(mainActivity,resList,R.layout.listview_item_setting,from,to);
        listView.setAdapter(sim_adapter);
        init_user_data();
        init_listView_click();
    }
    private void init_user_data(){
        String user_id = PersonInfo.get_user_id();
        if(user_id.isEmpty()){
            Toast.makeText(mainActivity,"用户还未登录！",Toast.LENGTH_SHORT).show();
            return;
        }
        TextView tv_show_id = (TextView) mainActivity.findViewById(R.id.tv_user_id_setting);
        tv_show_id.setText(user_id);
    }
    private void init_listView_click(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        //清除本地所有
                        clear_localdata();
                        break;
                    case 3:
                        //进行上传数据操作
                        init_upload();
                        break;
                }
            }
        });
    }
    public void setting_click(View view){
        switch (view.getId()){
            case R.id.user_setting:
                init_user_center();
                break;
        }
    }
    private void init_user_center(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setIcon(R.drawable.books);
        builder.setTitle("用户");
        //    指定下拉列表的显示数据
        final String[] names = {"登录", "注册", "注销", "取消"};
        //    设置一个下拉的列表选择项
        builder.setItems(names, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //进入登录界面
                        Log.e("TAG","login!!!!!!");
                        init_login();
                        break;
                    case 1:
                        //注册
                        new Register(mainActivity).init_register();
                        break;
                    case 2:
                        //注销
                        init_logoff();
                        break;
                    case 3:
                        listDialog.dismiss();
                        break;
                }
            }
        });
        listDialog = builder.create();
        listDialog.show();
    }
    private void init_login(){

        LayoutInflater inflater = mainActivity.getLayoutInflater();
        View loginDialogView = inflater.inflate(R.layout.login_dialog,null);
        AlertDialog.Builder loginBuilder = new AlertDialog.Builder(mainActivity);
        loginBuilder.setIcon(R.drawable.books);
        loginBuilder.setTitle("用户登录");
        loginBuilder.setView(loginDialogView);
        final EditText edit_user_id = (EditText) loginDialogView.findViewById(R.id.user_id_login_dialog) ;
        final EditText edit_paasword = (EditText) loginDialogView.findViewById(R.id.password_login_dialog) ;

        loginBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user_id = edit_user_id.getText().toString().trim();
                String password = edit_paasword.getText().toString().trim();
                new UserLogin(user_id,password,mainActivity).login();
            }
        });
        loginBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 loginDialog.dismiss();
            }
        });
        loginDialog = loginBuilder.create();
        loginDialog.show();
    }
    private void init_logoff(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setIcon(R.drawable.books);
        builder.setTitle("注销");
        builder.setMessage("确认注销？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(PersonInfo.LogOff()){
                    Toast.makeText(mainActivity,"注销成功！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    private void init_upload(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setIcon(R.drawable.books);
        builder.setTitle("上传数据");
        builder.setMessage("确定上传数据？\n 上传后你将会享受进一步的数据服务。");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UploadData uploadData = new UploadData(mainActivity);
                uploadData.Upload_Plan_daily();
                uploadData.Upload_Plan_daily_Segment();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    private void clear_localdata(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setIcon(R.drawable.books);
        builder.setTitle("清除本地数据");
        builder.setMessage("确定清除本地数据？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int delete_plan = MyDataBase.db.delete("plan_daily",null,null);
                int delete_segment = MyDataBase.db.delete("plan_daily_segment",null,null);
                Toast.makeText(MainActivity.mainActivity,"已删除"+delete_plan+"个计划！"+delete_segment+"个相关环节！",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}
