package com.example.igorleite.nameapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.igorleite.nameapplication.activities.Login;
import com.example.igorleite.nameapplication.utils.DownloadImage;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.facebook.share.internal.OpenGraphJSONUtility;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;


import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private ShareDialog shareDialog;
    Bundle bundle;
    String name;
    String surname;
    String imageUrl;
    String id;
    TextView nameView,titulo;
    Profile profile;
    CircleImageView circleImage;
    private static final int REQUEST_CODE = 1;
    private View messengerButton;
    private boolean picking;
    private View includeView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        includeView = findViewById(R.id.include);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Meu Perfil");


        messengerButton = includeView.findViewById(R.id.messenger_send_button);
        nameView = (TextView)includeView.findViewById(R.id.nameAndSurname);
        titulo = (TextView)includeView.findViewById(R.id.titulo);
        circleImage = (CircleImageView)includeView.findViewById(R.id.profileImage);
        shareDialog = new ShareDialog(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        bundle = getIntent().getExtras();
        profile = bundle.getParcelable("profile");
        name = profile.getFirstName();
        surname = profile.getLastName();
        imageUrl = getIntent().getStringExtra("imageUrl").toString();
        id = profile.getId();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/didatic.ttf");
        titulo.setTypeface(typeface);
        nameView.setText(name +" "+surname);
        new DownloadImage((CircleImageView) findViewById(R.id.profileImage)).execute(imageUrl);


        messengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////TODO////[<o>]
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                logout();
                return true;
            case R.id.action_user:
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/igormelo"));
                startActivity(browser);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void logout(){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(MainActivity.this, Login.class);
        startActivity(login);
        finish();
    }

    private void sendImage(){
        String metadata = "{ \"image\" : \"trees\" }";
        Uri uri = Uri.parse("android.resource://com.example.igorleite.nameapplication/" + R.drawable.giticon);
        ShareToMessengerParams shareToMessengerParams = ShareToMessengerParams.newBuilder(uri,"image/png")
                .setMetaData(metadata)
                .build();

        if (picking){
            MessengerUtils.finishShareToMessenger(MainActivity.this, shareToMessengerParams);
        } else {
            MessengerUtils.shareToMessenger(MainActivity.this, REQUEST_CODE, shareToMessengerParams);
        }
    }
}
