package com.sct.mobile.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database.dЬ";
    private static final int DATABASE_VERSION = 11;
    private static final String TABLE_NAME = "contact_table";
    private static final String UID = "_id";
    private static final String UNAME = "uname";

    private static final String SQL_CREATE_ENTRIES = "CREATE ТАВLЕ"
            + TABLE_NAME + " (" + UID +" INTEGER PRIMARY КЕУ AUTOINCREMENT,"
            + UNAME + "VARCHAR(255));";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public MyDataBase(Context context) {
        // TODO Auto-generated constructor stuЬ
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w("LOG_TAG", "Upgrading DB version " + oldVersion
        + " to version " + newVersion);

        //Удаляем предыдущую таблицу при обновлении
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}