package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ComposeActivity extends AppCompatActivity {

    Button btnCompose;
    EditText etMessage;
    TwitterClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client=TwitterApp.getRestClient();

        btnCompose=(Button) findViewById(R.id.btnCompose);
        etMessage=(EditText) findViewById(R.id.etMessage);



    }


    public void onSubmit(View v) {

        String tweetText = etMessage.getText().toString();
        client.sendTweet(tweetText, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Tweet newTweet = Tweet.fromJSON(response);
                    Intent data = new Intent();
                    data.putExtra("TWEET_KEY", newTweet);
                    setResult(RESULT_OK, data);
                    // closes the activity and returns to first screen
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ComposeActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
