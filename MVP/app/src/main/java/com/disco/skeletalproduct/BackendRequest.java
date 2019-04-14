package com.disco.skeletalproduct;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BackendRequest {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();

    public void sendRequest(String url, HashMap<String, String> data, Context mContent) {
        RequestQueue queue = Volley.newRequestQueue(mContent);

//        String url = "http://165.227.98.119/addchatt/";
//        TextView usernameTextView = (TextView) findViewById(R.id.usernameTextView);
//        TextView messageTextView = (TextView) findViewById(R.id.messageEditText);
//        String username = usernameTextView.getText().toString();
//        String message = messageTextView.getText().toString();
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("username", username);
//        params.put("message", message);
//
        JsonObjectRequest postRequest = new JsonObjectRequest(url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: submit request success!");
                        return;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onResponse: submit request error!");
                return;
            }
        });

        queue.add(postRequest);
    }

    public void receiveRequest(String url, final String requestName, Context mContext) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
//        final String url = "http://165.227.98.119/getchatts/";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)  {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray array = response.getJSONArray(requestName);
//                            for (int i = 0; i < array.length(); i++) {
//                                String username = array.getJSONArray(i).getString(0);
//                                String message = array.getJSONArray(i).getString(1);
//                                String timestamp = array.getJSONArray(i).getString(2);
//                                Log.d(TAG, "onResponse: refresh success");
//                            }
//                            return array;
                        }
                        catch (JSONException e) {
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: error receive request error!");
                    }
                }
        );

        queue.add(getRequest);
    }

}
