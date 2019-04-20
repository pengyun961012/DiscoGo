package com.disco.skeletalproduct;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    // UI references.
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 996;

    private Button signOutButton;
    private Button homeButton;
    private TextView loginView;
    private TextView userInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        homeButton = (Button) findViewById(R.id.homeButton);
        loginView = (TextView) findViewById(R.id.loginTextView);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        userInfoView = (TextView) findViewById(R.id.userInfoTextView);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Pair<View, String> pair = Pair.create((View)loginView, "loginButton");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(LoginActivity.this, pair);
                startActivity(intent, options.toBundle());
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    //already signed in
                    AuthUI.getInstance().signOut(LoginActivity.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Pair<View, String> pair = Pair.create((View)loginView, "loginButton");
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeSceneTransitionAnimation(LoginActivity.this, pair);
                                    startActivity(intent, options.toBundle());
                                    finish();
                                }
                            });
                    userInfoView.setText("Please log in!");
                } else{
                    Toast.makeText(LoginActivity.this, "User is not logged in!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           //already signed in
            //currentUser.sendEmailVerification();
            String currentUserName = currentUser.getDisplayName();
            userInfoView.setText("Hi " + currentUserName + "! Welcome to Disco GO!");
        } else{
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    RC_SIGN_IN);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RC_SIGN_IN) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                String currentUserName = currentUser.getDisplayName();
                userInfoView.setText("Hi " + currentUserName + "! Welcome to Disco GO!");
            }
        }
    }


}

