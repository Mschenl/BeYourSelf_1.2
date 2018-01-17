package com.self.be.beyourself_12.homeshow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.dataInfo.PersonInfo;
import com.self.be.beyourself_12.http.Http;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/27.
 */

public class PersonAnalysis extends Activity {
    private static final int DOWNLOAD_PICTURE = 1;
    private ListView listView;
    private TextView tv_title;
    private String [] title = {"总体时间分布","周计划报表"};
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init_Main_Layout();
    }
    private void Init_Main_Layout(){
        setContentView(R.layout.personal_analysis);
        init_view();
        init_list_view();
    }
    private void init_view(){

        tv_title = (TextView) findViewById(R.id.tv_title_homeshow);
        tv_title.setText(R.string.knowself);
        listView = (ListView) findViewById(R.id.lv_psersonal_analysis);
    }
    private void init_list_view(){
        ArrayList <HashMap<String,Object>> mlist = new ArrayList<>();
        for(int i = 0;i<title.length;i++){
            HashMap<String,Object> map = new HashMap<>();
            map.put("title",title[i]);
            map.put("icon",R.drawable.tools);
            mlist.add(map);
        }
        String from [] = {"title","icon"};
        int to[] = {R.id.tv_1_set,R.id.im_1_set};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,mlist,R.layout.listview_item_setting,from,to);
        listView .setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //打开总体时间分布
                        init_TimeDistributeTotal();
                        break;
                }
            }
        });
    }
    private void init_TimeDistributeTotal(){
        setContentView(R.layout.result_personal_analysis);
//        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.root_layout_result_person_analysis);
//        ImageView image = new ImageView(this);
//        image.setLayoutParams(new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//                ));
        ImageView image = (ImageView) findViewById(R.id.im_result_personal_analysis);
        DownloadPicture(image);
        TextView tv_describe = (TextView) findViewById(R.id.tv_describe_result);
        tv_describe.setText("根据您所有可知数据所得时间分布");
    }
    private void DownloadPicture(final ImageView image){
         handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case DOWNLOAD_PICTURE:
                        Bitmap bitmap = (Bitmap) msg.obj;
                        image.setImageBitmap(bitmap);
                        break;
                }
            }
        };
        String url = "http://"+getString(R.string.server_ip)
                +":8080/BeYourSelf/TimeDistributeTotal";
        Http http = new Http(handler);
        http.CallHttpPicture(url,PersonInfo.get_user_id(),"user_id" );
    }
    public void OnClick_personal_analysis(View view){
        switch (view.getId()){
            case R.id.return_btn_presonal_analysis:
                finish();
                break;
        }
    }

}
