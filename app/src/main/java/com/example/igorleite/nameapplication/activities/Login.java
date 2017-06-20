package com.example.igorleite.nameapplication.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.igorleite.nameapplication.R;
import com.example.igorleite.nameapplication.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;


import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class Login extends AppCompatActivity {
    LoginButton loginButton;
    TwitterLoginButton twitterLoginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private VideoView videoView;
    TextView text, android;
    TwitterAuthConfig twitterAuthConfig;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        FacebookSdk.sdkInitialize(getApplicationContext());
        twitterAuthConfig = new TwitterAuthConfig(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
        Fabric.with(this, new Twitter(twitterAuthConfig));
        setContentView(R.layout.activity_login);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_button);
        videoView = (VideoView)findViewById(R.id.videoView);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        text = (TextView) findViewById(R.id.textView);
        android = (TextView)findViewById(R.id.textAndroid);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/didatic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        android.setTypeface(typeface2);
        text.setTypeface(typeface);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.cover);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                nextActivityTwitter(result);

            }

            @Override
            public void failure(TwitterException exception) {
                new AlertDialog.Builder(Login.this).setTitle("Error").setMessage(exception.getLocalizedMessage())
                        .setNeutralButton("Aceitar",null)
                        .show();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                      nextActivity(currentProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(Login.this, "Logando...", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email","user_birthday"));
        //loginButton.setPublishPermissions(Arrays.asList("email", "public_profile","user_friends"));
        loginButton.registerCallback(callbackManager, callback);

    }
    @Override
    protected void onResume(){
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }
    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
        twitterLoginButton.onActivityResult(requestCode, responseCode, intent);
    }

    private void nextActivity(Profile profile){
        if(profile != null){
            try {
                Intent main = new Intent(Login.this, FacebookActivity.class);
                Bundle b = new Bundle();
                main.putExtra("imageUrl", profile.getProfilePictureUri(400,400).toString());

                //main.putExtra("name", profile.getFirstName());
                //main.putExtra("surname", profile.getLastName());
                //main.putExtra("id", profile.getId());
                b.putParcelable("profile", profile);

                main.putExtras(b);
                startActivity(main);
            }catch (Exception e){
                Log.e("Igor: ", e.getLocalizedMessage());

            }

        }
    }
    public void nextActivityTwitter(Result<TwitterSession> result){
        TwitterSession session = result.data;
       final Call<User> user = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
        user.enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                User user = result.data;
                String profileImage = user.profileImageUrl.replace("_normal", "");
                Intent i = new Intent(Login.this, TwitterActivity.class);
                i.putExtra("email", user.email);
                i.putExtra("nome", session.getUserName());
                i.putExtra("imageUrl", profileImage);
                startActivity(i);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

    }
}
