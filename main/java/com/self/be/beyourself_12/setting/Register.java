package com.self.be.beyourself_12.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.http.Http;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/19.
 */

public class Register {
    private Activity main;
    EditText edit_user_id,edit_paasword,edit_ok_paasword,edit_user_name;
    private AlertDialog registerDialog = null;
    public Register(Activity main){
        this.main = main;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(main,msg.obj.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    };
    public void init_register(){
        LayoutInflater inflater = main.getLayoutInflater();
        View registerView = inflater.inflate(R.layout.register,null);
        AlertDialog.Builder registerBuilder = new AlertDialog.Builder(main);
        registerBuilder.setIcon(R.drawable.books);
        registerBuilder.setTitle("用户注册");
        registerBuilder.setView(registerView);
        edit_user_id = (EditText) registerView.findViewById(R.id.user_id_register) ;
        edit_paasword = (EditText) registerView.findViewById(R.id.password_register) ;
        edit_ok_paasword = (EditText) registerView.findViewById(R.id.ok_password_register) ;
        edit_user_name = (EditText) registerView.findViewById(R.id.user_name_register) ;


        registerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user_id = edit_user_id.getText().toString().trim();
                String password = edit_paasword.getText().toString().trim();
                String ok_password = edit_paasword.getText().toString().trim();
                String user_name = edit_user_name.getText().toString().trim();
                if(user_id.isEmpty()||password.isEmpty()||ok_password.isEmpty()||user_name.isEmpty()){
                    Toast.makeText(main,"请将信息填写完整！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(ok_password)){
                    Toast.makeText(main,"两次密码不一致！",Toast.LENGTH_SHORT).show();
                }
                register(user_id,password,user_name);
            }
        });
        registerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registerDialog.dismiss();
            }
        });
        registerDialog = registerBuilder.create();
        registerDialog.show();
    }
    private void register(String user_id,String password,String user_name){
        String url = "http://"+main.getString(R.string.server_ip)+":8080/BeYourSelf/UserRegister";
        HashMap<String,String> map = new HashMap<>();
        map.put("user_id",user_id);
        map.put("password",password);
        map.put("user_name",user_name);
        Http http = new Http(handler);
        http.CallHttp(url,map,new String[]{"user_id","password","user_name"});

    }
}
