package com.self.be.beyourself_12.dataInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.self.be.beyourself_12.MainActivity;

/**
 * Created by Administrator on 2017/2/19.
 */

public class PersonInfo {
    private String user_name;
    private String password;
    private String user_id;
    public PersonInfo(String user_name,String password,String user_id){
        this.setUser_name(user_name);
        this.setPassword(password);
        this.setUser_id(user_id);
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_id() {
        return user_id;
    }


    public static String get_user_id(){
        SharedPreferences logindata = MainActivity.mainActivity.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String user_id = logindata.getString("user_id","");
        if(user_id.isEmpty()){
            Log.i("warning","user_id id empty!");
        }
        return user_id;
    }
    public static boolean LogOff(){
        SharedPreferences logindata = MainActivity.mainActivity.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = logindata.edit();
        //edit.remove("cl");
        edit.clear();
        return edit.commit();

    }
}
