package com.example.home_automation.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_sqlite extends SQLiteOpenHelper {

    private static final String TAG = "Database_sqlite";

    private static Database_sqlite db = null ;
    private Context mContext;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_home_automation";
    protected static final String FIRST_TABLE_NAME = "room_list";
    protected static final String SECOND_TABLE_NAME = "device_list";

    public static final String CREATE_FIRST_TABLE = "create table if not exists "
            + FIRST_TABLE_NAME
            + " ( _id integer primary key autoincrement, room_name TEXT NOT NULL);";

    public static final String CREATE_SECOND_TABLE = "create table if not exists "
            + SECOND_TABLE_NAME
            + " ( _id integer primary key autoincrement, room_name_ofdevice TEXT NOT NULL "+
            " , device_name TEXT NOT NULL , device_type TEXT NOT NULL,device_id TEXT NOT NULL,ip_address TEXT NOT NULL );";


    public Database_sqlite(Context context){
        super(context , DATABASE_NAME,null,DATABASE_VERSION);

    }
    public static Database_sqlite getInstance(Context context){
        if(db == null){
            db = new Database_sqlite(context);
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FIRST_TABLE);
        db.execSQL(CREATE_SECOND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
