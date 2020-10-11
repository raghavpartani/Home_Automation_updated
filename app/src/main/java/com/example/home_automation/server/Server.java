package com.example.home_automation.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.home_automation.Device;
import com.example.home_automation.database.device_list_DML;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {

    ArrayList<String> arrayList = new ArrayList<>();

    public void volleytoggle(final Context context, final String room_name, final boolean isChecked, final String device_name, final String device_type) {
        String url = "https://qrphp.000webhostapp.com/new.php";
        final device_list_DML db = new device_list_DML(context);
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait.....");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject responses = new JSONObject(response);
                    String key = responses.getString("key");
                    if (key.trim().equals("Saved")) {
                        dialog.dismiss();
                        Toast.makeText(context, "" + device_name + " On", Toast.LENGTH_SHORT).show();
                    } else if (key.trim().equals("Deleted")) {
                        dialog.dismiss();
                        Toast.makeText(context, "" + device_name + " Off", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("device_name", device_name);
                map.put("device_type", device_type);
                if (isChecked == true) {
                    map.put("status", "on");
                } else {
                    map.put("status", "off");
                }
                map.put("room_name", room_name);
                map.put("device_id", db.deviceid(device_name, room_name));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void volleyseekbar(final Context context, final String room_name, final boolean isChecked, final String device_name, final String device_type, final int seekbar) {
        String url = "https://qrphp.000webhostapp.com/new.php";
        final device_list_DML db = new device_list_DML(context);
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait.....");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Toast.makeText(context, "fan speed is" + seekbar, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("device_name", device_name);
                map.put("device_type", device_type);
                if (isChecked == true) {
                    map.put("status", "on");
                } else {
                    map.put("status", "off");
                }
                map.put("speed", String.valueOf(seekbar));
                map.put("room_name", room_name);
                map.put("device_id", db.deviceid(device_name, room_name));
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public ArrayList<String> checkingstate(final String room_name, final Context context) {
        final String[] url = {"https://qrphp.000webhostapp.com/new1.php"};
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Checking Device state...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url[0], new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String devicename = jsonObject.getString("name");
                        arrayList.add(devicename);
                    }
                    ((Device) context).arr(arrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("room_name", room_name);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return arrayList;
    }
}

