package com.self.be.beyourself_12.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/1/21.
 */

public class DBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "data.db";
    private static final int version = 1;

    public DBHelper(Context context) {
        super(context,DB_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_create_plan = "create table plan (_id integer primary key autoincrement not null,plan_name char(15)," +
                "start_time integer,end_time integer)";

        db.execSQL(sql_create_plan);

        String sql_create_plan_daily = "create table plan_daily (_id integer primary key autoincrement not null,user_id char(20)," +
                "plan_name char(15),start_time char(15),end_time char(15),plan_date char(15),plan_time integer,plan_type char(15))";

        db.execSQL(sql_create_plan_daily);

        String sql_create_segment = "create table plan_daily_segment (_id integer primary key autoincrement not null,user_id char(20)," +
                "_id_plan_daily integer,segment_name char(15),finish_time char(15),finish_date char(15),score double,if_finish integer)";

        db.execSQL(sql_create_segment);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exist plan";
        db.execSQL(sql);
        onCreate(db);
    }
}
