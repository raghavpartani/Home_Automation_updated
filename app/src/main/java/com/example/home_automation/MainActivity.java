package com.example.home_automation;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_automation.adapter.Room_Adapter;
import com.example.home_automation.database.device_list_DML;
import com.example.home_automation.database.room_list_DML;
import com.example.home_automation.ip.IPAddressValidation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText room_name;
    Button add, cancel;
    ImageView fb;
    String room;

    ArrayList<String> mNames;

    RecyclerView rcv;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager mgr;

    device_list_DML dbdevice=new device_list_DML(MainActivity.this);
    room_list_DML db = new room_list_DML(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fb = findViewById(R.id.fb);
        rcv = findViewById(R.id.rcv);

        mNames = new ArrayList<>();
        mNames.addAll(db.view_room_name());

        init();

        if(mNames.isEmpty())
        {
            Toast.makeText(this, "Please add room to continue", Toast.LENGTH_LONG).show();
        }

       fb.setOnClickListener(this);
    }



    void addroomdialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.add_room);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        room_name = dialog.findViewById(R.id.room_name);
        add = dialog.findViewById(R.id.add);
        cancel = dialog.findViewById(R.id.cancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                room = room_name.getText().toString().trim();
                if(room.equals(""))
                {
                    room_name.setError("Value Required");
                }
                else {
                    if (mNames.contains(room)) {
                        Toast.makeText(MainActivity.this, "Room name:" + room + " already exists.", Toast.LENGTH_SHORT).show();
                    } else {
                        db.save_room_name(room);
                        mNames.add(room);
                        init();
                        dialog.dismiss();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    void init(){
        mgr = new LinearLayoutManager(MainActivity.this);
        rcv.setLayoutManager(mgr);
        adapter = new Room_Adapter(MainActivity.this, mNames);
        rcv.setAdapter(adapter);
    }


    public void dialog(final String room)
    {
                    final Dialog dialog=new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.delete_update);
                    dialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    final Button update,delete;
                    update=dialog.findViewById(R.id.update);
                    delete=dialog.findViewById(R.id.delete);

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog1=new Dialog(MainActivity.this);
                            dialog1.setContentView(R.layout.update_room_name);
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog1.show();

                            final EditText room_name;
                            Button update,cancel;
                            update=dialog1.findViewById(R.id.update);
                            cancel=dialog1.findViewById(R.id.cancel);
                            room_name=dialog1.findViewById(R.id.room_name);
                            room_name.setText(room);

                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String room1=room_name.getText().toString().trim();
                                    if(room1.equals(""))
                                    {
                                        room_name.setError("Value Required");
                                    }
                                    else {
                                    if (mNames.contains(room_name.getText().toString())) {
                                        Toast.makeText(MainActivity.this, "Room name:" + room1 + " already exists.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        db.update_room_name(room, room_name.getText().toString());
                                        int index = mNames.indexOf(room);
                                        mNames.set(index, room_name.getText().toString());
                                        dbdevice.update_room_name(room, room_name.getText().toString());
                                        adapter.notifyDataSetChanged();
                                        dialog1.dismiss();
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
                            dialog.dismiss();
                        }

                    });
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "delete", Toast.LENGTH_SHORT).show();
                            db.delete_room_name(room);
                            dbdevice.deleteallforaroom(room);
                            mNames.remove(room);
                            dialog.dismiss();
                            adapter.notifyDataSetChanged();
                        }
                    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_acivity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shetting:
                dialogsetting();
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this,About.class));
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(MainActivity.this,Privacy_Policy.class));
                break;
            case R.id.feedback:
                Intent intent = new Intent(Intent.ACTION_SENDTO).setData(new Uri.Builder().scheme("mailto").build()).putExtra(Intent.EXTRA_EMAIL, new String[]{ "Info@Slogfy.com" });
                startActivity(intent);
        }
        return true;
    }

    private void dialogsetting() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.ip_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final EditText ip;
        Button save, cancel;
        Button getip;
        ip = dialog.findViewById(R.id.ipaddress);
        save = dialog.findViewById(R.id.save);
        cancel = dialog.findViewById(R.id.cancel);
        getip=dialog.findViewById(R.id.getip);

        SharedPreferences preferences = getSharedPreferences("ip", MODE_PRIVATE);
        ip.setText(preferences.getString("ip", null));

        getip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String connectedip=getWifiIP();
                if(connectedip.trim().equals("0.0.0.0"))
                {
                    Toast.makeText(MainActivity.this, "No devices connected via wifi", Toast.LENGTH_SHORT).show();
                }
                else {
                    ip.setText(connectedip);
                }}
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IPAddressValidation ipAddressValidation = new IPAddressValidation();
                boolean valid = ipAddressValidation.isValidIPAddress(ip.getText().toString());
                if (valid) {
                    SharedPreferences.Editor editor = getSharedPreferences("ip", MODE_PRIVATE).edit();
                    editor.putString("ip", ip.getText().toString());
                    editor.commit();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Not a valid ip address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
            addroomdialog();
        }
    }
}
