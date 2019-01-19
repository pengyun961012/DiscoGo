package com.disco.starterapp;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EnteringActivity extends AppCompatActivity {
    String TAG = "disco";
    private ArrayList<Chatt> chattArrayList;
    private ChattAdapter chattAdapter;
    private SwipeRefreshLayout refreshContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entering);

        chattArrayList = new ArrayList<Chatt>();
        chattAdapter = new ChattAdapter(this, chattArrayList);
        ListView lView = (ListView) findViewById(R.id.chattListView);
        FloatingActionButton postButton = (FloatingActionButton) findViewById(R.id.postChattActionButton);
        lView.setAdapter(chattAdapter);

        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                postChatt(v);
            }
        });

        refreshContainer = (SwipeRefreshLayout) findViewById(R.id.refreshContainer);
        refreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });

        refreshTimeline();
    }

    private void refreshTimeline() {
        chattAdapter.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://165.227.98.119/getchatts/";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)  {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray array = response.getJSONArray("chatts");
                            for (int i = 0; i < array.length(); i++) {
                                String username = array.getJSONArray(i).getString(0);
                                String message = array.getJSONArray(i).getString(1);
                                String timestamp = array.getJSONArray(i).getString(2);
                                chattAdapter.add(new Chatt(username, message, timestamp));
                                Log.d(TAG, "onResponse: refresh success");
                            }
                        }
                        catch (JSONException e) {
                        }
                        refreshContainer.setRefreshing(false);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        refreshContainer.setRefreshing(false);
                    }
                }
        );

        queue.add(getRequest);
    }

    public void postChatt(View view) {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }
}