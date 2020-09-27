package com.example.home_automation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class device_list_DML extends Database_sqlite {

    private static final String TAG = "device_list_DML";
    private static final String TABLE_NAME = "device_list";
    private static final String COL_ID = "_id";
    private static final String COL1 = "room_name_ofdevice";
    private static final String COL2 = "device_name";
    private static final String COL3 = "device_type";


    private static final String COL4 = "device_id";
    private static final String COL5 = "ip_address";

    private ArrayList<String> mDeviceName = new ArrayList();
    private ArrayList<String> mDeviceType = new ArrayList();


    private Context mContext;
    private SQLiteDatabase sqLiteDatabase;
    private Database_sqlite database;
    private ContentValues contentvalues;

    public device_list_DML(Context context) {
        super(context);
        this.mContext = context ;
        database =  Database_sqlite.getInstance(context);
        contentvalues = new ContentValues();
    }

    public void save_device(String RoomName, String DeviceName, String DeviceType,String DeviceID,String IpAddress) {
        try {
            sqLiteDatabase = database.getWritableDatabase();

            contentvalues.put(COL1, RoomName);
            contentvalues.put(COL2, DeviceName);
            contentvalues.put(COL3, DeviceType);
            contentvalues.put(COL4,DeviceID);
            contentvalues.put(COL5,IpAddress);
            Long result = sqLiteDatabase.insert(TABLE_NAME, "Blank", contentvalues);
            Toast.makeText(mContext, "New Device added :" + RoomName
                            + "\nDevice: " + DeviceName + "\nType: " + DeviceType,
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.d(TAG, "save_room_name: " + e.getMessage());

        }
    }

    public ArrayList<String> view_DeviceName(String RoomName) {

        Log.d(TAG, "view_DeviceName is called");
        sqLiteDatabase = database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" select * from " + TABLE_NAME
                + " where " + COL1 + " like '" +RoomName+ "' ;", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String viewdata_device_name = cursor.getString(cursor.getColumnIndex(COL2));
                    mDeviceName.add(viewdata_device_name);
                Log.d(TAG, "view_DeviceName: mDeviceName "+mDeviceName);
            }
            cursor.close();

            return mDeviceName;
        } else {
            Toast.makeText(mContext, "No Devices found add new devices", Toast.LENGTH_SHORT).show();
            cursor.close();

            return mDeviceName;
        }
    }
    public ArrayList<String> view_DeviceType(String RoomName) {
        Log.d(TAG, "view_DeviceType is called");
        sqLiteDatabase = database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" select * from " + TABLE_NAME
                + " where " + COL1 + " like '"+ RoomName +"' ;", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String viewdata_device_type = cursor.getString(cursor.getColumnIndex(COL3));
                mDeviceType.add(viewdata_device_type);
                Log.d(TAG, "view_DeviceType: mDeviceType "+mDeviceType);
            }
            cursor.close();

            return mDeviceType;
        } else {
//            Toast.makeText(mContext, "No Devices found add new devices", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "view_DeviceType: No Devices TYPE found add new devices");
            cursor.close();

            return mDeviceType;
        }
    }

    public void delete_device(String DeviceName,String RoomName) {
        sqLiteDatabase = database.getWritableDatabase();
        String[] arg = new String[]{DeviceName,RoomName};
        sqLiteDatabase.delete(TABLE_NAME, COL2 + "=? AND "+COL1+"=?", arg);
        Toast.makeText(mContext, "Deleted " + DeviceName, Toast.LENGTH_SHORT).show();
        }

    public boolean update_device(String old_DeviceName, String new_DeviceName, String new_DeviceType,String new_IpAddress,String new_DeviceId,String RoomName) {

        try {
            sqLiteDatabase = database.getWritableDatabase();
            contentvalues.put(COL2, new_DeviceName);
            contentvalues.put(COL3, new_DeviceType);
            contentvalues.put(COL4, new_DeviceId);
            contentvalues.put(COL5, new_IpAddress);
            String[] args = new String[]{old_DeviceName,RoomName};
            sqLiteDatabase.update(TABLE_NAME, contentvalues, COL2 + " = ? AND "+COL1 +" = ?", args);
            Toast.makeText(mContext, "Device updated to " + new_DeviceName + "\n Type: " + new_DeviceType,
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        catch (SQLiteException e) {
            return false;
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

    public void deleteallforaroom(String room) {
        sqLiteDatabase = database.getWritableDatabase();
        String[] arg = new String[]{room};
        sqLiteDatabase.delete(TABLE_NAME, COL1 + "=?", arg);
        Toast.makeText(mContext, "Deleted " + room, Toast.LENGTH_SHORT).show();
        }

    public String deviceip(String devicename,String roomname)
    {
        String viewdata_device_ip=null;
        sqLiteDatabase = database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" select * from " + TABLE_NAME
                + " where " + COL2 + " like '" +devicename+ "' AND "+ COL1+ " like '" +roomname+ "'", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                viewdata_device_ip= cursor.getString(cursor.getColumnIndex(COL5));
                }
            cursor.close();
            return viewdata_device_ip;
        } else {
            Toast.makeText(mContext, "No ip address found", Toast.LENGTH_SHORT).show();
            cursor.close();
            return null;
        }
    }

    public String deviceid(String deviceName,String roomName) {

        String viewdata_device_id=null;
        sqLiteDatabase = database.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" select * from " + TABLE_NAME
                + " where " + COL2 + " like '" +deviceName+ "' AND "+ COL1+ " like '" +roomName+ "'", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                viewdata_device_id= cursor.getString(cursor.getColumnIndex(COL4));
            }
            cursor.close();
            return viewdata_device_id;
        } else {
            Toast.makeText(mContext, "No Device Id found", Toast.LENGTH_SHORT).show();
            cursor.close();
            return null;
        }
    }
}
