package com.example.techandnotez4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "TechNotez.db";

    public DBHelper(Context context) {
        super(context, "TechNotez.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table users(userId INTEGER PRIMARY KEY AUTOINCREMENT,userName TEXT,userDob TEXT,userUsername TEXT, userPassword TEXT )");
        MyDB.execSQL("create table notes(noteId INTEGER PRIMARY KEY AUTOINCREMENT,noteName TEXT,noteDesc TEXT, userUsername TEXT, CONSTRAINT userUsername FOREIGN KEY (userUsername) REFERENCES users(userUsername))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop table if exists users");
        onCreate(MyDB);
    }

    public Boolean insertData(String name, String dob, String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", name);
        contentValues.put("userDob", dob);
        contentValues.put("userUsername", username);
        contentValues.put("userPassword", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) return false;
        else
            return true;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where userUsername = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where userUsername = ? and userPassword = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
