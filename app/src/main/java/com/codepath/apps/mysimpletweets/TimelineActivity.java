package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.mysimpletweets.R.id.rvTweet;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    TweetsAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    Button btnCompose;
    Button btnProfile;
    private final int REQUEST_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setting up toolbar icon
        btnCompose = (Button) findViewById(R.id.miCompose);


        client = TwitterApp.getRestClient();

        tweets = new ArrayList<>();
        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(rvTweet);
        // int the arraylist (data source)
        // construct the adapter from this datasource
        tweetAdapter = new TweetsAdapter(tweets);
        // RecycleView setup (layout manager, use adpater)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        populateTimeline();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.miCompose:
                composeMessage();
                return true;
            case R.id.miProfile:
                showProfileView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void composeMessage(){

        Intent i = new Intent(this,ComposeActivity.class);
        startActivityForResult(i,REQUEST_CODE);


        //Toast.makeText(TimelineActivity.this,"compose",Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            // Extract name value from result extras
            Tweet newtweet = data.getParcelableExtra("TWEET_KEY");
            tweets.add(0,newtweet);
            tweetAdapter.notifyDataSetChanged();
            rvTweets.scrollToPosition(0);
        }
    }


    public void showProfileView(){
        Toast.makeText(TimelineActivity.this,"profile",Toast.LENGTH_SHORT).show();
    }



    private void populateTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              // new Tweet(jsonObject)
      //          Log.d("TwitterClient", response.toString());
                //iterate through the JSON array
                // for each entry, deserialize the JSON object

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i=0; i< response.length(); i++) {
                    // convert each object to a Tweet model
                    // add that Tweet model to our data source
                    // notify the adapter that we're added an item
                    try {

                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() -1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
