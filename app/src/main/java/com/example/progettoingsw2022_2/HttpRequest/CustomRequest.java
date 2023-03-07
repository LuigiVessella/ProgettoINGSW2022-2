package com.example.progettoingsw2022_2.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.progettoingsw2022_2.NetworkManager.VolleySingleton;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class CustomRequest {
    private String url = "http://192.168.1.10:8080";

    private  Map<String, String> params;

    private Context context;

    private String resultString = null;

    private VolleyCallback volleyCallback;


    public CustomRequest(String url, Map<String, String> params, Context context, VolleyCallback volleyCallback){
        if(url.contains("192.") || url.contains("20."))this.url = this.url.concat(url);
        else this.url = url;
        this.params = params;
        this.context = context;
        this.volleyCallback = volleyCallback;
    }

    public void sendGetRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                        volleyCallback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.i("INFO Params", params.get(0));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;

            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


    public void sendPostRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                        volleyCallback.onSuccess(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;

            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
