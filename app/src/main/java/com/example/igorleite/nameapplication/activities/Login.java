package com.example.igorleite.nameapplication.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.example.igorleite.nameapplication.MainActivity;
import com.example.igorleite.nameapplication.R;
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
import java.util.Arrays;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {
    LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    TextView text, android;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        text = (TextView) findViewById(R.id.textView);
        android = (TextView)findViewById(R.id.textAndroid);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/didatic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        android.setTypeface(typeface2);
        text.setTypeface(typeface);
        loginButton = (LoginButton)findViewById(R.id.login_button);
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
    }

    private void nextActivity(Profile profile){
        if(profile != null){
            try {
                Intent main = new Intent(Login.this, MainActivity.class);
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
}
