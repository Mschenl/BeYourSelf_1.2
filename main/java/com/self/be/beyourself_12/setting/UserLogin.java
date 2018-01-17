package com.self.be.beyourself_12.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.http.Http;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/19.
 */

public class UserLogin {
    private String user_id;
    private String password;
    private Activity main;
    public UserLogin(String user_id,String password,Activity main){
        this.user_id = user_id;
        this.password = password;
        this.main = main;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(main,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    TextView tv_show_id = (TextView) main.findViewById(R.id.tv_user_id_setting);
                    tv_show_id.setText(user_id);
                    SharedPreferences logindata = main.getSharedPreferences("user_data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor medit = logindata.edit();
                    medit.putString("user_id",user_id);
                    medit.commit();
                    break;
            }
        }
    };
    public void login(){
        Http http = new Http(handler);
        String url = "http://"+main.getString(R.string.server_ip)+":8080/BeYourSelf/UserLogin";
        HashMap<String,String> map = new HashMap<>();
        map.put("user_id",user_id);
        map.put("password",password);
        Log.i("Tag","user_id "+user_id);
        String parm [] = {"user_id","password"};
        http.CallHttp(url,map,parm);
    }


}
