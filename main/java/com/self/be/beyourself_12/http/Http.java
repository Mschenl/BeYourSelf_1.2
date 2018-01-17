package com.self.be.beyourself_12.http;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/19.
 */

public class Http {
    private Handler handler;
    public Http(Handler handler){
        this.handler = handler;
    }

    public void CallHttp(String url, HashMap map, String parm_name[]){
        //  1
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        for (int i = 0;i<parm_name.length;i++){
            builder.add(parm_name[i],(String)map.get(parm_name[i]));
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "CallHttp 1 onFailure() e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message mes = Message.obtain();
                mes.what = 1;
                mes.obj = response.body().string();
                handler.sendMessage(mes);
            }
        });
    }

    public void CallHttp(String url, String str, String parm){
        //  2
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(parm, str)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "CallHttp 2 onFailure() e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message meg = Message.obtain();
                meg.what = 1;
                meg.obj = response.body().string();
                handler.sendMessage(meg);
            }
        });
    }
    public void CallHttpPicture(String url, String str, String parm){
        //  2
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(parm, str)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "CallHttp 2 onFailure() e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message meg = Message.obtain();
                meg.what = 1;
                InputStream in = response.body().byteStream();//获得图片数据
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                meg.obj = bitmap;
                handler.sendMessage(meg);
            }
        });
    }
}
