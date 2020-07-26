package com.bagasbest.belajarsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {


    public MyDbHelper (@Nullable Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table on that db
        db.execSQL(Constant.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade database (if there any structure change  the change db version)

        //drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + Constant.TABLE_NAME);
        //create table again
        onCreate(db);
    }

    public long insertRecord(String name, String image, String bio, String phone,
                             String job, String dob, String addedTime,
                             String updatedTime ){
        //get writeable database becauser we want to erite data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        //id will be inserted automatically

        //insert data
        cv.put(Constant.C_NAME, name);
        cv.put(Constant.C_IMAGE, image);
        cv.put(Constant.C_BIO, bio);
        cv.put(Constant.C_PHONE, phone);
        cv.put(Constant.C_JOB, job);
        cv.put(Constant.C_DOB, dob);
        cv.put(Constant.C_ADDED_TIME, addedTime);
        cv.put(Constant.C_UPDATED_TIME, updatedTime);

        //insert row, it will retiuern record id of asved revcord
        long id = db.insert(Constant.TABLE_NAME, null, cv);

        //close db connection
        db.close();

        return id;

    }
}
