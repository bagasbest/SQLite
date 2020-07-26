package com.bagasbest.belajarsqlite;

public class Constant {
    //db name
    public  static final  String DB_NAME = "MY_RECORD_DB";
    //db version
    public static final  int DB_VERSION = 1;
    //table name
    public static final String TABLE_NAME = "MY_RECORD_TABLE";
    //column/fields of table
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_BIO = "BIO";
    public static final String C_PHONE = "PHONE";
    public static final String C_JOB = "JOB";
    public static final String C_DOB = "DON";
    public static final String C_ADDED_TIME = "ADDED_TIME_STAMP";
    public static final String C_UPDATED_TIME = "UPDATED_TIME_STAMP";
    //create table query

    public static final String CREATE_TABLE = "CREATE TABLE" + TABLE_NAME +
            "(" + C_ID + "INTEGER PRIMARY KEY AUTO INCREMENT,"
            + C_NAME + "TEXT,"
            + C_IMAGE + "TEXT,"
            + C_BIO + "TEXT,"
            + C_PHONE + "TEXT,"
            + C_JOB + "JOB,"
            + C_DOB + "TEXT,"
            + C_ADDED_TIME + "TEXT,"
            + C_UPDATED_TIME + "TEXT,"
            + ")";

}
