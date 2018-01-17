package com.self.be.beyourself_12.homeshow;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.self.be.beyourself_12.addtask.AccomplishDailyTask;
import com.self.be.beyourself_12.Database.MyDataBase;
import com.self.be.beyourself_12.R;
import com.self.be.beyourself_12.http.Http;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/4.
 */

public class HomeShow {
    private SQLiteDatabase db;
    private AppCompatActivity mainActivity;
    private ViewPager viewPager;
    private List<ImageView> mlist;
    private static final int DOWNLOAD_PICTURE = 1;


    private ListView listView;
    private int [] listView_picture;
    private String TitleList[];


    public HomeShow(AppCompatActivity main){
        db = MyDataBase.db;
        mainActivity = main;
        MakeView();
    }
    public void MakeView(){
        init_pager();
        init_listView();

    }
    //加载ViewPager方法
    private void init_pager(){

        viewPager = (ViewPager) mainActivity.findViewById(R.id.vp_main);

        mlist = new ArrayList<ImageView>();
        int[] imRes = getImageRes();
        for(int i = 0; i<imRes.length ;i++){
            ImageView iv = new ImageView(mainActivity);
            iv.setBackgroundResource(imRes[i]);
            mlist.add(iv);
        }
        //DownloadPicture();
        viewPager.setAdapter(new MyAdapter());
    }


    private void init_listView(){
        listView = (ListView) mainActivity.findViewById(R.id.listView_main);

        listView_picture = new int[]{R.drawable.home_2,R.drawable.pin,R.drawable.light,R.drawable.report,
                R.drawable.adaptive_design};
        TitleList = new String[]{"自我认知","每日任务","我的计划","专业报表","计算分析"};
        MyBaseadapter mbadpter = new MyBaseadapter();

        listView.setAdapter(mbadpter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //打开自我分析
                        Intent intent0 = new Intent(mainActivity,PersonAnalysis.class);
                        mainActivity.startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(mainActivity,AccomplishDailyTask.class);
                        mainActivity.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(mainActivity,MakePlan.class);
                        mainActivity.startActivity(intent2);
                        break;
                }
            }
        });
    }

    class MyBaseadapter extends BaseAdapter {
        @Override
        public int getCount() {
            return TitleList.length;
        }
        //得到每个用于展示数据的item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =  (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view  = inflater.inflate(R.layout.main_list_view_item,null);
            ImageView im = (ImageView) view.findViewById(R.id.imageView);
            TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
            TextView tv_2 = (TextView) view.findViewById(R.id.tv_1);
            im.setBackgroundResource(listView_picture[position]);
            tv_1.setText(TitleList[position]);
            return view;
        }
        //表示根据当前下标获取指定的item的id
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return TitleList[position];
        }
    }



    private int[] getImageRes(){
        return new int[]{R.drawable.data_1,R.drawable.tech_2,R.drawable.tech_3,R.drawable.tech_4};
    }


    class MyAdapter extends PagerAdapter {
        //用来判断是否需要重新生成子视图
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        //得到所展示的个数
        @Override
        public int getCount() {
            return mlist.size();
        }
        //产生一个新的的视图
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mlist.get(position));
            return mlist.get(position);
        }
        //移除
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mlist.get(position));
        }
    }
}
