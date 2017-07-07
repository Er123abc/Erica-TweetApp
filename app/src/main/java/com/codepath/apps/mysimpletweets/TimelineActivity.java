package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragement;
import com.codepath.apps.mysimpletweets.fragments.TweetsPagerAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends AppCompatActivity {


    private final int REQUEST_CODE = 20;
    private SwipeRefreshLayout swipeContainer;
    private ViewPager vpPager;
    private TweetsPagerAdapter adapter;
    private HomeTimelineFragement homeTimelineFragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        // get the view pager
        vpPager = (ViewPager) findViewById(R.id.viewpager);

        // set the adapter for the pager
        adapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(adapter);

        // setup the TabLayout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

//        tweets = new ArrayList<>();
//        // find the RecyclerView
//        rvTweets = (RecyclerView) findViewById(rvTweet);
//        // init the arraylist (data source)
//        // construct the adapter from this datasource
//        tweetAdapter = new TweetsAdapter(tweets, TweetsAdapterListener);
//        // RecycleView setup (layout manager, use adpater)
//        rvTweets.setLayoutManager(new LinearLayoutManager(this));
//        //set the adapter
//        rvTweets.setAdapter(tweetAdapter);
//        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.miCompose:
                composeMessage();
                return true;
            case R.id.miProfile:
                showProfileView(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showProfileView(String screenName) {
        Toast.makeText(TimelineActivity.this, "profile", Toast.LENGTH_SHORT).show();
        Intent  i = new Intent(this, ProfileActivity.class);
        i.putExtra("screen_name", screenName);
        startActivity(i);
    }

    public void composeMessage() {

        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);


        //Toast.makeText(TimelineActivity.this,"compose",Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            // Extract name value from result extras
            Tweet newtweet = data.getExtras().getParcelable("TWEET_KEY");
            homeTimelineFragement = (HomeTimelineFragement) adapter.getItem(0);
            vpPager.setCurrentItem(0);
            homeTimelineFragement.addTweet(newtweet);
        }
    }


}
