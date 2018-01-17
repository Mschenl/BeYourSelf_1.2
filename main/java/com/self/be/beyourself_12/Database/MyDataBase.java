package com.self.be.beyourself_12.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/1/23.
 */

public class MyDataBase {
    public static SQLiteDatabase db = null;

    public MyDataBase(Context context){
        if(db == null){
            DBHelper dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        }
        else{
            System.out.println("数据库已经建立，无需再次建立！");
        }

    }
    public static boolean have_data_plan_daily(String plan_date){
        String where = "plan_date = ?";
        String whereArg[] = {plan_date};
        Cursor cursor = db.query("plan_daily",null,where,whereArg,null,null,null);
        if(cursor.getCount() > 0 ){
            return true;
        }
        else {
            return false;
        }
    }

}
