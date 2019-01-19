package com.disco.starterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final Button submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submitChatt(v);
            }
        });
    }

    public void submitChatt(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://159.89.181.188/addchatt/";
        TextView usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        TextView messageTextView = (TextView) findViewById(R.id.messageEditText);
        String username = usernameTextView.getText().toString();
        String message = messageTextView.getText().toString();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("message", message);

        JsonObjectRequest postRequest = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
            }
        });

        queue.add(postRequest);
    }
}