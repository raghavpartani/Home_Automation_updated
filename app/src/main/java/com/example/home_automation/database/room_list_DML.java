package com.example.home_automation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class room_list_DML extends Database_sqlite {
    private static final String TAG = "room_list_DML";
    private  static final String TABLE_NAME ="room_list";
    private static final String COL_ID = "_id";
    private static final String COL1 = "room_name";

    private ArrayList<String> mRoom = new ArrayList();
    Context mContext;
    SQLiteDatabase sqLiteDatabase;
    private Database_sqlite database;
    ContentValues contentvalues;

    public room_list_DML(Context context) {
        super(context);
        mContext = context;
        database = Database_sqlite.getInstance(context);
        contentvalues = new ContentValues();
//        sqLiteDatabase = database.getReadableDatabase();

    }
    public void save_room_name( String Roomname) {
        try {
            sqLiteDatabase = database.getWritableDatabase();
            contentvalues.put(COL1, Roomname);
            Long result = sqLiteDatabase.insert(TABLE_NAME, "Blank", contentvalues);
            Toast.makeText(mContext, "New Room added :"+Roomname, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "save_room_name: "+e.getMessage());
        }
    }

    public ArrayList<String> view_room_name(){

            sqLiteDatabase = database.getReadableDatabase();

            Cursor cursor = sqLiteDatabase.rawQuery("Select * from "+TABLE_NAME+" ;", null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String viewdata = cursor.getString(cursor.getColumnIndex(COL1));
                mRoom.add(viewdata);
            }
            cursor.close();
            return mRoom;
        } else {
            Toast.makeText(mContext, "No Rooms found add new rooms", Toast.LENGTH_SHORT).show();
            cursor.close();
            return mRoom;
        }
    }
    public void delete_room_name(String Roomname){
        try {
            sqLiteDatabase = database.getWritableDatabase();
            String[] arg = new String[] {Roomname};
            sqLiteDatabase.delete(FIRST_TABLE_NAME, COL1 + "=?" ,arg);
            Toast.makeText(mContext, "Deleted "+Roomname, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "delete_room_name: "+e.getMessage());
        }
    }
    public  boolean update_room_name(String old_Roomname, String new_Roomname) {

        try {
            sqLiteDatabase = database.getWritableDatabase();
            contentvalues.put(COL1, new_Roomname);

            String[] args = new String[]{old_Roomname};
            sqLiteDatabase.update(TABLE_NAME, contentvalues, COL1 + "=?", args);
            Toast.makeText(mContext, "Room updated to "+new_Roomname, Toast.LENGTH_SHORT).show();
            return true;
        } catch (SQLiteException e) {

            return false;
        }
    }

}
