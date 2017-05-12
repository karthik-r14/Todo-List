package com.todolist.todolist.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.StringRes;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tasklist.db";
    public static final String TABLE_NAME = "tasks";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "Task";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ? (ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT)",  new String[] {TABLE_NAME});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, task);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        if(result == -1) {
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("Select * from " + TABLE_NAME, null);
        return result;
    }

    public void deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("delete from " + TABLE_NAME + " where Task = " + "'" + task + "'" , null);
    }
}
