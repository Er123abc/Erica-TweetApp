package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ericar on 6/28/17.
 */

public class Tweet implements Parcelable {
    //list out the attributes
    public String body;
    public long uid; // database ID for the tweet
    public User user;
    public String createdAt;

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract the values from
        tweet.body = jsonObject.getString("text");
        tweet.uid =  jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }
     public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
         ArrayList<Tweet> tweets = new ArrayList<>();
         for (int i = 0; i < jsonArray.length(); i++) {
             // convert each object to a Tweet model
             // add that Tweet model to our data source
             // notify the adapter that we're added an item
             try {
                 Tweet tweet = Tweet.fromJSON(jsonArray.getJSONObject(i));
                 tweets.add(tweet);
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
         return tweets;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.createdAt);
    }

    public Tweet() {
    }

    protected Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
