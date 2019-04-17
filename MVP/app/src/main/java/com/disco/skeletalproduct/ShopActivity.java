package com.disco.skeletalproduct;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ShopActivity extends AppCompatActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();

    private GridView gridViewItems;
    private ImageButton profileButton;
    private ImageButton playButton;
    private ImageButton shopButton;
    private ImageButton leaderBoardButton;
    private ImageButton friendButton;
    private ImageView shopItemImage;
    private Button buyButton;

    private List<Shop> shopItemList = new ArrayList<>();
    private ShopListAdapter shopAdapter;
    private int chosenPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shop);

        gridViewItems = (GridView) findViewById(R.id.gridViewItems);
        profileButton = (ImageButton) findViewById(R.id.profileImageButton);
        playButton = (ImageButton) findViewById(R.id.playImageButton);
        shopButton = (ImageButton) findViewById(R.id.shopImageButton);
        leaderBoardButton = (ImageButton) findViewById(R.id.leaderboardImageButton);
        friendButton = (ImageButton) findViewById(R.id.friendImageButton);
        shopItemImage = (ImageView) findViewById(R.id.imageViewCurrent);
        buyButton = (Button) findViewById(R.id.buyButton);

        populateList();
        shopAdapter = new ShopListAdapter(shopItemList, ShopActivity.this);

        gridViewItems.setAdapter(shopAdapter);

        gridViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                Shop item = shopItemList.get(position);
                shopItemImage.setImageResource(item.getImageId());
                chosenPosition = position;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, PlayActivity.class);
                Pair<View, String> pair = Pair.create((View)playButton, "playCircle");
                Pair<View, String> pair2 = Pair.create((View)profileButton, "profileButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(ShopActivity.this, pair2);
                startActivity(intent, options.toBundle());
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                Pair<View, String> pair = Pair.create((View)profileButton, "profileButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(ShopActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        leaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, LeaderboardActivity.class);
                Pair<View, String> pair = Pair.create((View)leaderBoardButton, "leaderButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(ShopActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, FriendActivity.class);
                Pair<View, String> pair = Pair.create((View)friendButton, "friendButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(ShopActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ShopActivity.this.getApplicationContext(), "Buy", Toast.LENGTH_SHORT).show();
                Shop item = shopItemList.get(chosenPosition);
                new AlertDialog.Builder(ShopActivity.this)
                        .setTitle("Buy it?")
                        .setMessage("Are you sure you want to buy " + item.getItemName() + " with " + String.valueOf(item.getItemPrice()) + " coins?")
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        });

    }

    private void populateList(){
        Shop nitem = new Shop(R.drawable.cutebird, "Cute Bird", 10);
        shopItemList.add(nitem);
        nitem = new Shop(R.drawable.alive_bird2_glasses, "Sunglasses Bird", 50);
        shopItemList.add(nitem);
        nitem = new Shop(R.drawable.eagle, "Eagle", 10);
        shopItemList.add(nitem);
    }
}
