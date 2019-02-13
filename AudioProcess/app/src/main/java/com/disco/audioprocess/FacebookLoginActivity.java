package com.disco.audioprocess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class FacebookLoginActivity extends AppCompatActivity {
    private String TAG = "DISCO_AUDIO-----" + this.getClass().getSimpleName();

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView loginStatus;
    private TextView loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginStatus = (TextView) findViewById(R.id.login_status);
        loginName = (TextView) findViewById(R.id.login_name);

        loginButton.setReadPermissions("email");

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            loginStatus.setText("Log back");
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            loginButton.setReadPermissions("email");
        }

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                loginStatus.setText("Successful login");
                String userLoginId = loginResult.getAccessToken().getUserId();
                Profile mProfile = Profile.getCurrentProfile();
                String firstName = mProfile.getFirstName();
                String lastName = mProfile.getLastName();
                String userId = mProfile.getId().toString();
                String profileImageUrl = mProfile.getProfilePictureUri(96, 96).toString();
                loginName.setText("Welcome " + firstName + " " + lastName);
            }

            @Override
            public void onCancel() {
                // App code
                loginStatus.setText("Login failed");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                loginStatus.setText("Login error");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
