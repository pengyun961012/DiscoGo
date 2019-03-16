package com.disco.skeletalproduct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LeaderboardActivity extends AppCompatActivity {

    // UI reference.
    private TextView ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ranking = findViewById(R.id.ranking);
        ranking.setText("Your Rank:" + getRanking());
    }


    /**
     * Return the current user's ranking in leaderboard
     * Should pass a user id to it
     */
    public String getRanking() {
        return "36000";
    }
}
