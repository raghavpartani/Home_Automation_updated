package com.example.home_automation.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_automation.Device;
import com.example.home_automation.R;
import com.example.home_automation.database.device_list_DML;
import com.example.home_automation.internetchck.InternetConnection;
import com.example.home_automation.server.Server;

import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.content.Context.MODE_PRIVATE;

public class Device_RecyclerViewadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "Device_RecyclerVadpt";
    private static final int DEVICE_TYPE_LIGHT = 0;
    private static final int DEVICE_TYPE_FAN = 1;
    private static final int DEVICE_TYPE_SOCKET = 2;
    private static final int DEVICE_TYPE_COLOR_LIGHT = 3;

    private ArrayList<String> mDeviceName ;
    private ArrayList<String> mDeviceType;
    private OnRecyclerViewListner mOnRecyclerViewListner;
    Context context;
    String room_name;
    Server volley=new Server();

    public Device_RecyclerViewadapter(ArrayList<String> devicename , ArrayList<String> devicetype, Context context,String room_name){
        this.mDeviceName = devicename;
        this.mDeviceType = devicetype;
        this.context=context;
        this.room_name=room_name;
        Log.d(TAG, "Device_RecyclerViewadapter constructor is called");
    }



    // Interface for OnRecyclerViewListner (OnClickListner).
    public interface OnRecyclerViewListner{
        void onRecyclerViewClicked(int position);
        void onSwitchClicked(int position, boolean b);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Log.d(TAG, "onCreateViewHolder is called");
        View v ;

        /*
        its time to check the view that was returned
         */
            if(i == DEVICE_TYPE_LIGHT ) {
                Log.d(TAG, "onCreateViewHolder: Device LIGHT is called "+i);
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.light_device_list, viewGroup, false);
                return new LightViewHolder(v, mOnRecyclerViewListner);

            }

        if(i == DEVICE_TYPE_SOCKET ) {
            Log.d(TAG, "onCreateViewHolder: Device SOCKET is called "+i);
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.socket_device_list, viewGroup, false);
            return new SocketViewHolder(v, mOnRecyclerViewListner);
        }

        if(i == DEVICE_TYPE_COLOR_LIGHT ) {
            Log.d(TAG, "onCreateViewHolder: Device COLORLIGHT is called "+i);
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.color_light_device_list, viewGroup, false);
            return new ColorLightViewHolder(v, mOnRecyclerViewListner);

        }
//             if(i == DEVICE_TYPE_FAN)
            else{
                Log.d(TAG, "onCreateViewHolder: Device FAN is called "+i);
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fan_device_list, viewGroup, false);
                return new FanViewHolder(v, mOnRecyclerViewListner);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final device_list_DML db=new device_list_DML(context);
        switch (getItemViewType(i)){
            case DEVICE_TYPE_LIGHT:
                Log.d(TAG,"onBindViewHolder: LIGHT is called "+viewHolder.getItemViewType());
                LightViewHolder lightViewHolder =(LightViewHolder) viewHolder;
                lightViewHolder.add_data_light(mDeviceName.get(i),mDeviceType.get(i));
                String light_name=((LightViewHolder) viewHolder).device_name_light.getText().toString().trim();
                SharedPreferences sharedPref_light = context.getSharedPreferences(""+db.deviceid(light_name,room_name)+light_name, MODE_PRIVATE);
                ((LightViewHolder) viewHolder).switch_light.setChecked(sharedPref_light.getBoolean("currentstatus", false));
                break;

            case DEVICE_TYPE_FAN:
                Log.d(TAG,"onBindViewHolder: FAN is called "+viewHolder.getItemViewType());
                FanViewHolder fanViewHolder = (FanViewHolder) viewHolder;
                fanViewHolder.add_data_fan(mDeviceName.get(i),mDeviceType.get(i));
                String fan_name=((FanViewHolder) viewHolder).device_name_fan.getText().toString().trim();
                SharedPreferences sharedPref_fan = context.getSharedPreferences(""+db.deviceid(fan_name,room_name)+fan_name, MODE_PRIVATE);
                SharedPreferences sharedPref_fan_speed =context.getSharedPreferences(""+"" + db.deviceid(fan_name,room_name)+fan_name+"speed", MODE_PRIVATE);
                ((FanViewHolder) viewHolder).seekBar_fan.setProgress(sharedPref_fan_speed.getInt("speed",0));
                ((FanViewHolder) viewHolder).switch_fan.setChecked(sharedPref_fan.getBoolean("currentstatus", false));
                break;

            case DEVICE_TYPE_SOCKET:
                Log.d(TAG,"onBindViewHolder: FAN is called "+viewHolder.getItemViewType());
                SocketViewHolder socketViewHolder = (SocketViewHolder) viewHolder;
                socketViewHolder.add_data_socket(mDeviceName.get(i),mDeviceType.get(i));
                String socket_name=((SocketViewHolder) viewHolder).device_name_socket.getText().toString().trim();
                SharedPreferences sharedPref_socket = context.getSharedPreferences(""+db.deviceid(socket_name,room_name)+socket_name, MODE_PRIVATE);
                ((SocketViewHolder) viewHolder).switch_socket.setChecked(sharedPref_socket.getBoolean("currentstatus", false));
                break;

            case DEVICE_TYPE_COLOR_LIGHT:
                Log.d(TAG,"onBindViewHolder: COLOR LIGHT is called "+viewHolder.getItemViewType());
                ColorLightViewHolder colorlightViewHolder =(ColorLightViewHolder) viewHolder;
                colorlightViewHolder.add_data_color_light(mDeviceName.get(i),mDeviceType.get(i));
                String color_light_name=((ColorLightViewHolder) viewHolder).device_name_color_light.getText().toString().trim();
                SharedPreferences sharedPref_color_light = context.getSharedPreferences(""+db.deviceid(color_light_name,room_name)+color_light_name, MODE_PRIVATE);
                SharedPreferences sharedPref_color_light_color = context.getSharedPreferences(""+room_name+color_light_name, MODE_PRIVATE);
                ((ColorLightViewHolder) viewHolder).colorimage.setColorFilter(sharedPref_color_light_color.getInt("color",0));
                ((ColorLightViewHolder) viewHolder).switch_color_light.setChecked(sharedPref_color_light.getBoolean("currentstatus", false));
                break;
            default:
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(mDeviceType.get(position).trim().equals("FAN")){
            Log.d(TAG, "getItemViewType: FAN is called "+mDeviceType.get(position));
            return DEVICE_TYPE_FAN;
        }
        if(mDeviceType.get(position).trim().equals("LIGHT")){
            Log.d(TAG, "getItemViewType: LIGHT is called "+mDeviceType.get(position));
            return DEVICE_TYPE_LIGHT;
        }
        if(mDeviceType.get(position).trim().equals("SOCKET")){
            Log.d(TAG, "getItemViewType: SOCKET is called "+mDeviceType.get(position));
            return DEVICE_TYPE_SOCKET;
        }
        if(mDeviceType.get(position).trim().equals("COLORLIGHT")){
            Log.d(TAG, "getItemViewType: SOCKET is called "+mDeviceType.get(position));
            return DEVICE_TYPE_COLOR_LIGHT;
        }
        Log.d(TAG, "getItemViewType: default is called "+mDeviceType.get(position));
        return -1;
    }

    @Override
    public int getItemCount() {
        return mDeviceName.size();
    }

    public class LightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        TextView device_name_light ;
        TextView device_type_light;
        Switch switch_light ;

        OnRecyclerViewListner mOnRecyclerViewListner ;

        public LightViewHolder(@NonNull View itemView, OnRecyclerViewListner mOnRecyclerViewListner) {
            super(itemView);
            this.mOnRecyclerViewListner = mOnRecyclerViewListner;
            device_name_light = itemView.findViewById(R.id.device_name_light);
            device_type_light = itemView.findViewById(R.id.device_type_light);
            switch_light =itemView.findViewById(R.id.switch_light);
            itemView.setOnLongClickListener(this);
            switch_light.setOnCheckedChangeListener(this);
        }

        public void add_data_light(String device_name , String device_type){
            device_name_light.setText(device_name);
            device_type_light.setText(device_type);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            if(v==itemView)
            {
                if(switch_light.isChecked())
                {
                    Toast.makeText(context, "you cant delete or update the device while its working", Toast.LENGTH_SHORT).show();
                }
                else {
                    ((Device) context).dialog(device_name_light.getText().toString(), device_type_light.getText().toString());
                }
            }
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            InternetConnection internetConnection=new InternetConnection();
            boolean b=internetConnection.checkConnection(context);
            if(b==true) {
                volley.volleytoggle(context,room_name,isChecked,device_name_light.getText().toString().trim(),device_type_light.getText().toString().trim());
            }
            else {
                Toast.makeText(context, "check internet connection", Toast.LENGTH_SHORT).show();
                if(isChecked == true)
                {
                    switch_light.setChecked(false);
                }
                else
                {
                    switch_light.setChecked(true);
                }
            }
        }
    }

    public class ColorLightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener , CompoundButton.OnCheckedChangeListener {
        TextView device_name_color_light ;
        TextView device_type_color_light;
        Switch switch_color_light ;
        ImageView colorimage;
        int mDefaultColor;
        OnRecyclerViewListner mOnRecyclerViewListner ;

        public ColorLightViewHolder(@NonNull View itemView, OnRecyclerViewListner mOnRecyclerViewListner)  {
            super(itemView);
            this.mOnRecyclerViewListner = mOnRecyclerViewListner;
            device_name_color_light = itemView.findViewById(R.id.device_name_color_light);
            device_type_color_light = itemView.findViewById(R.id.device_type_color_light);
            switch_color_light =itemView.findViewById(R.id.switch_color_light);
            colorimage=itemView.findViewById(R.id.color);

            mDefaultColor = ContextCompat.getColor(context, R.color.colorPrimary);
            itemView.setOnLongClickListener(this);
            colorimage.setOnClickListener(this);
            switch_color_light.setOnCheckedChangeListener(this);
        }

       public void openColorPicker() {
           AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(context, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
               @Override
               public void onCancel(AmbilWarnaDialog dialog) {
               }
               @Override
               public void onOk(AmbilWarnaDialog dialog, int color) {
                   mDefaultColor = color;
                   colorimage.setColorFilter(mDefaultColor);
                   SharedPreferences.Editor editor = context.getSharedPreferences(""+room_name+device_name_color_light.getText().toString().trim(), MODE_PRIVATE).edit();
                   editor.putInt("color",mDefaultColor);
                   editor.commit();
                   Toast.makeText(context, ""+mDefaultColor, Toast.LENGTH_SHORT).show();
               }
           });
           colorPicker.show();
       }



    public void add_data_color_light(String device_name , String device_type){
            device_name_color_light.setText(device_name);
            device_type_color_light.setText(device_type);
        }

        @Override
        public void onClick(View v) {
            if(v==colorimage){
                openColorPicker();
            }

        }

        @Override
        public boolean onLongClick(View v) {
            if(v==itemView)
            {
                if(switch_color_light.isChecked())
                {
                    Toast.makeText(context, "you cant delete or update the device while its working", Toast.LENGTH_SHORT).show();
                }
                else {
                    ((Device) context).dialog(device_name_color_light.getText().toString(), device_type_color_light.getText().toString());
                }
            }
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            InternetConnection internetConnection=new InternetConnection();
            boolean b=internetConnection.checkConnection(context);
            if(b==true) {
                volley.volleytoggle(context,room_name,isChecked,device_name_color_light.getText().toString().trim(),device_type_color_light.getText().toString().trim());
            }
            else {
                Toast.makeText(context, "check internet connection", Toast.LENGTH_SHORT).show();
                if(isChecked == true)
                {
                    switch_color_light.setChecked(false);
                }
                else
                {
                    switch_color_light.setChecked(true);
                }
            }
        }
    }

    public class SocketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        TextView device_name_socket ;
        TextView device_type_socket;
        Switch switch_socket;
        OnRecyclerViewListner mOnRecyclerViewListner ;

        public SocketViewHolder(@NonNull View itemView, OnRecyclerViewListner mOnRecyclerViewListner) {
            super(itemView);
            this.mOnRecyclerViewListner = mOnRecyclerViewListner;
            device_name_socket = itemView.findViewById(R.id.device_name_socket);
            device_type_socket = itemView.findViewById(R.id.device_type_socket);
            switch_socket =itemView.findViewById(R.id.switch_socket);
            switch_socket.setOnCheckedChangeListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void add_data_socket(String device_name , String device_type){
            device_name_socket.setText(device_name);
            device_type_socket.setText(device_type);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            if(v==itemView) {
                if (switch_socket.isChecked()) {
                    Toast.makeText(context, "you cant delete or update the device while its working", Toast.LENGTH_SHORT).show();
                } else
                    ((Device) context).dialog(device_name_socket.getText().toString(), device_type_socket.getText().toString());
            }
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            final InternetConnection internetConnection=new InternetConnection();
            boolean b=internetConnection.checkConnection(context);
            if(b==true) {
                volley.volleytoggle(context,room_name,isChecked,device_name_socket.getText().toString().trim(),device_type_socket.getText().toString().trim());
                }
            else {
                Toast.makeText(context, "check internet connection", Toast.LENGTH_SHORT).show();
                if(isChecked == true)
                {
                    switch_socket.setChecked(false);
                }
                else
                {
                    switch_socket.setChecked(true);
                }
            }
        }
    }

    public class FanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        TextView device_name_fan ;
        TextView device_type_fan;
        Switch switch_fan ;
        SeekBar seekBar_fan ;
        OnRecyclerViewListner mOnRecyclerViewListner ;

        public FanViewHolder(@NonNull View itemView, OnRecyclerViewListner mOnRecyclerViewListner)  {
            super(itemView);
            this.mOnRecyclerViewListner = mOnRecyclerViewListner;
            device_name_fan = itemView.findViewById(R.id.device_name_fan);
            device_type_fan = itemView.findViewById(R.id.device_type_fan);
            switch_fan =itemView.findViewById(R.id.switch_fan);
            seekBar_fan =itemView.findViewById(R.id.seekbar_fan);

            itemView.setOnLongClickListener(this);
            switch_fan.setOnCheckedChangeListener(this);
            seekBar_fan.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                    final device_list_DML db=new device_list_DML(context);
                    InternetConnection internetConnection=new InternetConnection();
                    boolean b=internetConnection.checkConnection(context);
                    if(b) {
                        volley.volleyseekbar(context,room_name,switch_fan.isChecked(),device_name_fan.getText().toString().trim(),device_type_fan.getText().toString().trim(),seekBar.getProgress());
                    }
                    else {
                        Toast.makeText(context, "check internet connection", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPref_fan_speed =context.getSharedPreferences(""+ db.deviceid(device_name_fan.getText().toString().trim(), room_name) + device_name_fan.getText().toString().trim()+"speed", MODE_PRIVATE);
                        seekBar_fan.setProgress(sharedPref_fan_speed.getInt("speed",0));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        public void add_data_fan(String device_name , String device_type){
            device_name_fan.setText(device_name);
            device_type_fan.setText(device_type);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            if(v==itemView){
                if(switch_fan.isChecked())
                {
                    Toast.makeText(context, "you cant delete or update the device while its working", Toast.LENGTH_SHORT).show();
                }
                else
                    ((Device)context).dialog(device_name_fan.getText().toString(),device_type_fan.getText().toString());
            }
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            InternetConnection internetConnection=new InternetConnection();
            boolean b=internetConnection.checkConnection(context);
            if(b==true) {
                volley.volleytoggle(context,room_name,isChecked,device_name_fan.getText().toString().trim(),device_type_fan.getText().toString().trim());
            }
            else {
                Toast.makeText(context, "check internet connection", Toast.LENGTH_SHORT).show();
                if(isChecked == true)
                {
                    switch_fan.setChecked(false);
                }
                else
                {
                    switch_fan.setChecked(true);
                }
            }
        }
    }
}
