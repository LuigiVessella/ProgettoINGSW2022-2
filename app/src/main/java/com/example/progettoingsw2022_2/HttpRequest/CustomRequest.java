package com.example.progettoingsw2022_2.HttpRequest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.progettoingsw2022_2.NetworkManager.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

//Per Luigi: quando pushi pls commenta il tuo IP, se no penso che non funge per motivi arcani e bestemmio
public class CustomRequest {
    //private String url = "http://20.86.153.84:8080";
    private String url = "http://192.168.1.4:8080";
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
                    volleyCallback.onResponse(response);
                },
                error -> {
                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(context, "Errore di connettività",Toast.LENGTH_LONG).show();

                }) {
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
                    volleyCallback.onResponse(response);
                },
                error -> {
                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(context, "Errore di connettività, uscire",Toast.LENGTH_LONG).show();

                }) {
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

    public void sendPatchRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, url,
                response -> {
                    Log.i("VOLLEY", response);
                    volleyCallback.onResponse(response);
                },
                error -> Log.e("VOLLEY", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;

            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }



    public void sendDeleteRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Log.i("VOLLEY", response);
                    volleyCallback.onResponse(response);
                },
                error -> {
                    Log.e("VOLLEY", error.toString());
                    Toast.makeText(context, "Errore di connettività, uscire",Toast.LENGTH_LONG).show();

                }) {
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
