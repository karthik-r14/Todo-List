package com.todolist.todolist.helper;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tasklist.db";
    public static final String TABLE_NAME = "tasks";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "Task";
    public static final String COL_3 = "State";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String task, String state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, task);
        contentValues.put(COL_3, state);
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

    public void storeState(boolean checked, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("Checked : " + checked);
        ContentValues contentValues = new ContentValues();
        if(checked) {
            contentValues.put(COL_3, 0);
        } else {
            contentValues.put(COL_3, 1);
        }
        db.update(TABLE_NAME, contentValues, "TASK=" + "'" + task + "'", null);
    }
}
