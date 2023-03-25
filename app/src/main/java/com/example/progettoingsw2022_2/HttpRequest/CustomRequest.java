package com.example.progettoingsw2022_2.HttpRequest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.progettoingsw2022_2.NetworkManager.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class CustomRequest {
    private String url = "http://192.168.1.9:8080";
    private  Map<String, String> params;
    private Context context;

    private VolleyCallback volleyCallback;


    public CustomRequest(String url, Map<String, String> params, Context context, VolleyCallback volleyCallback){
        this.url = this.url.concat(url);
        if(url.contains("openfoodfacts")) this.url = url;
        this.params = params;
        this.context = context;
        this.volleyCallback = volleyCallback;
    }

    public void sendGetRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                    Log.i("VOLLEY", response);
                    volleyCallback.onSuccess(response);
                },
                error -> Log.e("VOLLEY", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Log.i("INFO Params", params.get(0));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();

                if(url.contains("openfoodfacts"))params.put("Content-Type", "application/json");
                else params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;

            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


    public void sendPostRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.i("VOLLEY", response);
                    volleyCallback.onSuccess(response);

                },
                error -> Log.e("VOLLEY", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                if(url.contains("openfoodfacts"))params.put("Content-Type", "application/json");
                else params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;

            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
