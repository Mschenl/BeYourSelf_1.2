package com.self.be.beyourself_12.setting;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.dataInfo.PlanDaily;
import com.self.be.beyourself_12.dataInfo.PlanDailySegment;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/17.
 */

public class UploadData {
    private static final int POST_UPLOAD = 1;
    private SQLiteDatabase db;
    private Activity mcontext;
    public UploadData(Activity main){
        mcontext = main;
        init_dataBase();
    }
    private void init_dataBase() {
        db = MyDataBase.db;
    }
    public void Upload_Plan_daily(){
        // Java --> Json 数据变化
        Cursor cursor = db.query("plan_daily",null,null,null,null,null,null);
        ArrayList<PlanDaily> list = new ArrayList<>();
        while(cursor.moveToNext()){
            int _id = cursor.getInt(0);
            String user_id = cursor.getString(1);
            String plan_name = cursor.getString(2);
            String start_time = cursor.getString(3);
            String end_time = cursor.getString(4);
            String plan_date = cursor.getString(5);
            int plan_time = cursor.getInt(6);
            String plan_type = cursor.getString(7);
            PlanDaily plan = new PlanDaily(_id,user_id,plan_name,start_time,end_time,plan_date,plan_type,plan_time);
            list.add(plan);
        }

        Gson gson = new Gson();
        String str = gson.toJson(list);
        Log.i("TAG", str);
        String url = "http://"+mcontext.getString(R.string.server_ip)+":8080/BeYourSelf/UploadDailyPlanData";
        CallHttp(url,str,"plan_daily");

    }
    public void Upload_Plan_daily_Segment(){
        Cursor cursor = db.query("plan_daily_segment",null,null,null,null,null,null);
        ArrayList<PlanDailySegment> list = new ArrayList<>();
        while(cursor.moveToNext()){
            int _id = cursor.getInt(0);
            String user_id = cursor.getString(1);
            int _id_plan_daily = cursor.getInt(2);
            String segment_name = cursor.getString(3);
            String finish_time = cursor.getString(4);
            String finish_date = cursor.getString(5);
            double score = cursor.getDouble(6);
            int if_finish = cursor.getInt(7);
            PlanDailySegment plan = new PlanDailySegment(_id,user_id,_id_plan_daily,segment_name,finish_time
            ,finish_date,score,if_finish);
            list.add(plan);
        }

        Gson gson = new Gson();
        String str = gson.toJson(list);
        Log.i("TAG", str);
        String url = "http://"+mcontext.getString(R.string.server_ip)+":8080/BeYourSelf/UploadDailyPlanSegment";
        CallHttp(url,str,"plan_daily_segment");
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case POST_UPLOAD:
                    Toast.makeText(mcontext,(String)msg.obj,Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    private void CallHttp(String url,String str,String table){
        //
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(table, str)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "onFailure() e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message meg = Message.obtain();
                meg.what = POST_UPLOAD;
                meg.obj = response.body().string();
                handler.sendMessage(meg);
            }
        });
    }

}
