package com.example.igorleite.nameapplication.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.igorleite.nameapplication.R;
import com.example.igorleite.nameapplication.utils.DownloadImage;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

public class TwitterActivity extends AppCompatActivity {
    String name,email,imageUrl,title;
    private View includeTwitter;
    private TweetView adapter;
    private long maxId;
    TextView nameView,titulo;
    RecyclerView recyclerView;
    ListView listView;
    CircleImageView circleImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        getSupportActionBar().setTitle("Inicio");
        listView = (ListView)findViewById(R.id.listView);
        includeTwitter = findViewById(R.id.includeTwitter);
        nameView = (TextView)includeTwitter.findViewById(R.id.nameAndSurnameTwitter);
        circleImageView = (CircleImageView)includeTwitter.findViewById(R.id.profileImageTwitter);
        Intent intent = getIntent();
        name = intent.getStringExtra("nome");
        email = intent.getStringExtra("email");
        imageUrl = intent.getStringExtra("imageUrl");
        nameView.setText("@"+name);

        new DownloadImage((CircleImageView) findViewById(R.id.profileImageTwitter)).execute(imageUrl);
        UserTimeline userTimeLine = new UserTimeline.Builder()
                .screenName("skgaming")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(this, userTimeLine);
        listView.setAdapter(adapter);

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
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeSessionCookie();
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        Intent login = new Intent(TwitterActivity.this, Login.class);
        startActivity(login);
        finish();
    }
}
