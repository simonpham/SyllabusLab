package com.hasbrain.areyouandroiddev;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasbrain.areyouandroiddev.adapter.RedditPostAdapter;
import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.FileBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

    public static final String REDDIT_PAGE_LINK = "https://www.reddit.com/r/androiddev/";
    public static final String DATA_JSON_FILE_NAME = "data.json";
    private FeedDataStore feedDataStore;

    private TextView tvReddit;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        initialize();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
        Gson gson = gsonBuilder.create();
        InputStream is = null;
        try {
            is = getAssets().open(DATA_JSON_FILE_NAME);
            feedDataStore = new FileBasedFeedDataStore(gson, is);
            feedDataStore.getPostList(new FeedDataStore.OnRedditPostsRetrievedListener() {
                @Override
                public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
                    displayPostList(postList);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        tvReddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostViewActivity.class);
                intent.putExtra("PostListActivity.POST_URL", REDDIT_PAGE_LINK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    protected void initialize() {
        list = findViewById(R.id.lvPosts);
        View footerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_post_list, null, false);
        list.addFooterView(footerView);
        tvReddit = footerView.findViewById(R.id.tvRedditLink);
    }

    protected void displayPostList(List<RedditPost> postList) {
        RedditPostAdapter adapter = new RedditPostAdapter(getApplicationContext(), postList);

        list.setAdapter(adapter);
    }

    protected int getLayoutResource() {
        return R.layout.activity_post_list;
    }
}
