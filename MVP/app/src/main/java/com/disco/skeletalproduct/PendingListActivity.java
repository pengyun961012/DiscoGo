package com.disco.skeletalproduct;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
import java.util.List;

public class PendingListActivity extends ListActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
    private List<PendingFriend> pendingFriendList = new ArrayList<>();
    private PendingListAdapter pendingFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populatePendingList();
        pendingFriendAdapter = new PendingListAdapter(pendingFriendList, this);
        setListAdapter(pendingFriendAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PendingFriend c = pendingFriendList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("userId", c.getUserId());
                returnIntent.putExtra("userName", c.getUserName());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    private void populatePendingList() {
        String myId = getResources().getString(R.string.my_user_id);
        String url = getResources().getString(R.string.url) + "getpending/" + myId + "/";
        getAllPendings(url);
    }

    private void getAllPendings(String url){
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)  {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray array = response.getJSONArray("pending_friends");
                            for (int i = 0; i < array.length(); i++) {
                                int userId = array.getJSONObject(i).getInt("u_id");
                                String pendingUsername = array.getJSONObject(i).getString("username");
                                int imageId = array.getJSONObject(i).getInt("img_id");
                                int tokenNum = array.getJSONObject(i).getInt("token");
                                Log.d(TAG, "onResponse: username " + pendingUsername);
                                PendingFriend pf = new PendingFriend(userId, imageId, tokenNum, pendingUsername);
                                pendingFriendList.add(pf);
                            }
                            pendingFriendAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onResponse: get pending success");
                        }
                        catch (JSONException e) {
                            Log.d(TAG, "onResponse: pending json error" + e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: error receive request error!" + error);
                    }
                }
        );

        queue.add(getRequest);
    }
}
