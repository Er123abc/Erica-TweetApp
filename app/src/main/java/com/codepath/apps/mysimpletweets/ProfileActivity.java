package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragments;
import com.codepath.apps.mysimpletweets.models.User;
//import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String screenName = getIntent().getStringExtra("screen_name");
        //create user fragment
        UserTimelineFragments userTimelineFragments = UserTimelineFragments.newInstance(screenName);
        // display the user timeline fragment inside the container (dyanamic)

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // make change
        ft.replace(R.id.flContainer, userTimelineFragments);

        //commit
        ft.commit();

        client = TwitterApp.getRestClient();
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //deserialze the User Object
                try {
                    User user = User.fromJSON(response);
                    // set the title of the ActionBar based on the user information
                    getSupportActionBar().setTitle(user.screenName);
                    // populate the user headline
                    populateUserHeadline(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        if (screenName == null) {
            client.getUserInfo(handler);
        } else {
            client.getInfo(screenName, handler);
        }
    }


    public void populateUserHeadline (User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);


        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);


        tvTagline.setText(user.tagLine);
        tvFollowers.setText(user.followersCount + "Followers");
        tvFollowing.setText(user.followingCount + "Following");
        //load profile image with Glide
        Glide.with(this).load(user.profileImageUrl).into(ivProfileImage);

    }

}