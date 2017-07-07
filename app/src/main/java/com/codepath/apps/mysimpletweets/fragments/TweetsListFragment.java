package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.codepath.apps.mysimpletweets.R.id.rvTweet;

/**
 * Created by ericar on 7/3/17.
 */

public class TweetsListFragment extends Fragment implements TweetsAdapter.TweetAdapterListener {


    public interface TweetSelectedListener {
        //handle tweets selection
        public void onTweetSelected(Tweet tweet);
    }

    TweetsAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    //inflation happens inside onCreateView

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        tweets = new ArrayList<>(); // initialize the ArrayList
        //inflate the layout
        View v = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        // find the RecyclerView
        rvTweets = (RecyclerView) v.findViewById(rvTweet);
        // int the arraylist (data source)
        // construct the adapter from this datasource
        tweetAdapter = new TweetsAdapter(tweets, this);
        // RecycleView setup (layout manager, use adpater)
        rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        return v;
    }

    public void addItems(JSONArray response) {

        for (int i = 0; i < response.length(); i++) {
            try {
                Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                tweets.add(tweet);
                tweetAdapter.notifyItemInserted(tweets.size() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTweet(Tweet newtweet) {
        tweets.add(0, newtweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        //((TweetSelectedListener) .getActvity()).onTweetSelected(tweet);
    }
}






