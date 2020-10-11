package com.example.home_automation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_automation.Device;
import com.example.home_automation.WifiMainActivity;
import com.example.home_automation.R;
import com.example.home_automation.database.room_list_DML;

import java.util.ArrayList;

public class Room_Adapter
        extends RecyclerView.Adapter<Room_Adapter.MyHolder> {
    Context context;
    ArrayList<String> room_name;
    private room_list_DML db;

    public Room_Adapter(Context context, ArrayList<String> room_name) {
        this.context = context;
        this.room_name = room_name;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.room_layout, viewGroup, false);

        MyHolder holder = new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.tv.setText(room_name.get(i));
        if(i%2 == 1)
        {
            myHolder.iv.setImageResource(R.drawable.room);
        }

    }

    @Override
    public int getItemCount() {
        return room_name.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;
        public MyHolder(@NonNull View v) {
            super(v);

            tv = v.findViewById(R.id.room_name);

            iv=v.findViewById(R.id.iv);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Device.class);
                    intent.putExtra("room",tv.getText().toString());
                    context.startActivity(intent);
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((WifiMainActivity)context).dialog(tv.getText().toString());
                  return true;
                }
            });


        }
    }
}
