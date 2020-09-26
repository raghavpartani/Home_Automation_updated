package com.example.home_automation;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_automation.adapter.Device_RecyclerViewadapter;
import com.example.home_automation.database.device_list_DML;
import com.example.home_automation.ip.IPAddressValidation;

import java.util.ArrayList;

public class Device extends AppCompatActivity implements View.OnClickListener {
    ImageView fb;
    RecyclerView rcv;

    device_list_DML db;

    Intent intent;

    ArrayList<String> arrayList_device_name;
    ArrayList<String> arrayList_device_type;

    String devicetype1;
    String room_name;
    String devicetype2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        fb = findViewById(R.id.fb);
        rcv = findViewById(R.id.rcv);

        db = new device_list_DML(Device.this);

        intent = getIntent();
        room_name = intent.getStringExtra("room");

        arrayList_device_name = new ArrayList<>();
        arrayList_device_type = new ArrayList<>();

        arrayList_device_name = db.view_DeviceName(room_name);
        arrayList_device_type = db.view_DeviceType(room_name);


        if (arrayList_device_name.isEmpty()) {
            Toast.makeText(this, "Please Add Device To Continue", Toast.LENGTH_SHORT).show();
        }

        init();

        fb.setOnClickListener(this);
    }

    void adddevicedialog() {
        final Dialog dialog = new Dialog(Device.this);
        dialog.setContentView(R.layout.device_details);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText device_name, ip, deviceid;
        final Spinner devicetype;
        Button save, cancel,getip;

        device_name = dialog.findViewById(R.id.device_name);
        ip = dialog.findViewById(R.id.ipaddress);
        deviceid = dialog.findViewById(R.id.deviceid);
        getip=dialog.findViewById(R.id.getip);

        devicetype = dialog.findViewById(R.id.device_type);
        save = dialog.findViewById(R.id.save);
        cancel = dialog.findViewById(R.id.cancel);

        SharedPreferences preferences = getSharedPreferences("ip", MODE_PRIVATE);
        ip.setText(preferences.getString("ip", null));

        final String[] arraySpinner = new String[]{"LIGHT", "FAN", "SOCKET","COLORLIGHT"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Device.this, R.layout.spinner, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devicetype.setAdapter(adapter);


        devicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                devicetype1 = arraySpinner[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String connectedip=getWifiIP();
                if(connectedip.trim().equals("0.0.0.0"))
                {
                    Toast.makeText(Device.this, "No devices connected via wifi", Toast.LENGTH_SHORT).show();
                }
                else {
                    ip.setText(connectedip);
                }}
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DeviceName = device_name.getText().toString().trim();
                String DeviceID = deviceid.getText().toString().trim();
                String IpAddress = ip.getText().toString().trim();
                IPAddressValidation ipAddressValidation = new IPAddressValidation();
                if (DeviceName.trim().equals("")) {
                    device_name.setError("value required");
                } else if (DeviceID.trim().equals("")) {
                    deviceid.setError("value required");
                } else if (IpAddress.trim().equals("")) {
                    ip.setError("value required");
                } else {
                    if (arrayList_device_name.contains(device_name.getText().toString().trim())) {
                        Toast.makeText(Device.this, "Device Name is already registered with us", Toast.LENGTH_SHORT).show();
                    } else {
                        if (ipAddressValidation.isValidIPAddress(ip.getText().toString())) {
                            db.save_device(room_name, device_name.getText().toString().trim(), devicetype1.trim(), deviceid.getText().toString(), ip.getText().toString().trim());
                            arrayList_device_name.add(device_name.getText().toString().trim());
                            arrayList_device_type.add(devicetype1.trim());
                            dialog.dismiss();
                            adapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(Device.this, "Not a valid ip address", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Device.this, "" + room_name, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Device.this, LinearLayoutManager.VERTICAL, false);
        rcv.setLayoutManager(linearLayoutManager);
        Device_RecyclerViewadapter adapter = new Device_RecyclerViewadapter(arrayList_device_name, arrayList_device_type, Device.this, room_name);
        rcv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void dialog(final String DeviceName, final String DeviceType){
        final Dialog dialog = new Dialog(Device.this);
        dialog.setContentView(R.layout.delete_update);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Button update, delete;
        update = dialog.findViewById(R.id.update);
        delete = dialog.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete_device(DeviceName, room_name);
                arrayList_device_name.remove(DeviceName);
                arrayList_device_type.remove(DeviceType);
                init();
                dialog.dismiss();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final Dialog dialog1 = new Dialog(Device.this);
                dialog1.setContentView(R.layout.update_device_details);
                dialog1.show();
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final EditText device_name, ip, deviceid;
                Spinner devicetype;
                final Button update, cancel,getip;

                device_name = dialog1.findViewById(R.id.device_name);
                ip = dialog1.findViewById(R.id.ipaddress);
                deviceid = dialog1.findViewById(R.id.deviceid);
                getip=dialog1.findViewById(R.id.getip);

                devicetype = dialog1.findViewById(R.id.device_type);
                update = dialog1.findViewById(R.id.update);
                cancel = dialog1.findViewById(R.id.cancel);

                final String[] arraySpinner = new String[]{"LIGHT", "FAN", "SOCKET","COLORLIGHT"};
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Device.this, R.layout.spinner, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                devicetype.setAdapter(adapter);


                devicetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        devicetype2 = arraySpinner[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                if (DeviceType.equals("FAN"))
                    devicetype.setSelection(1);
                else if (DeviceType.equals("SOCKET"))
                    devicetype.setSelection(2);
                else if (DeviceType.equals("COLORLIGHT"))
                    devicetype.setSelection(3);

                device_name.setText(DeviceName);
                deviceid.setText(db.deviceid(DeviceName, room_name));
                ip.setText(db.deviceip(DeviceName, room_name));

                getip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String connectedip=getWifiIP();
                        if(connectedip.trim().equals("0.0.0.0"))
                        {
                            Toast.makeText(Device.this, "No devices connected via wifi", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ip.setText(connectedip);
                        }}
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IPAddressValidation ipAddressValidation = new IPAddressValidation();
                        String NewDeviceName = device_name.getText().toString().trim();
                        String NewDeviceID = deviceid.getText().toString().trim();
                        String NewIpAddress = ip.getText().toString().trim();
                        if (NewDeviceName.equals("")) {
                            device_name.setError("value required");
                        } else if (NewDeviceID.equals("")) {
                            deviceid.setError("value required");
                        } else if (NewIpAddress.equals("")) {
                            ip.setError("value required");
                        } else {
                            if (DeviceName.equals(NewDeviceName)) {
                                if (ipAddressValidation.isValidIPAddress(ip.getText().toString())) {
                                    db.update_device(DeviceName, NewDeviceName, devicetype2, NewIpAddress, NewDeviceID, room_name);
                                    int index = arrayList_device_name.indexOf(DeviceName);
                                    arrayList_device_name.set(index, NewDeviceName);
                                    arrayList_device_type.set(index, devicetype2);
                                    init();
                                    dialog1.dismiss();
                                } else {
                                    Toast.makeText(Device.this, "Not a valid ip address", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (arrayList_device_name.contains(NewDeviceName)) {
                                    Toast.makeText(Device.this, "Device Name is associated with another device", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (ipAddressValidation.isValidIPAddress(ip.getText().toString())) {
                                        db.update_device(DeviceName, NewDeviceName, devicetype2, NewIpAddress, NewDeviceID, room_name);
                                        int index = arrayList_device_name.indexOf(DeviceName);
                                        arrayList_device_name.set(index, NewDeviceName);
                                        arrayList_device_type.set(index, devicetype2);
                                        init();
                                        dialog1.dismiss();
                                    } else
                                        Toast.makeText(Device.this, "Not a valid Ip address", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

            }
        });
    }
    public String getWifiIP(){
        WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        int ip=wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }

    @Override
    public void onClick(View v) {

        if(v==fb)
        {
            adddevicedialog();
        }
    }
}