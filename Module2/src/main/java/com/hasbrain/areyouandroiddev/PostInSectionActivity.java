package com.hasbrain.areyouandroiddev;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasbrain.areyouandroiddev.adapter.ExpandedPostAdapter;
import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.FileBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/9/15.
 */
public class PostInSectionActivity extends PostListActivity {

    private FeedDataStore feedDataStore;

    private ExpandableListView list;
    List<String> listDataHeader;
    HashMap<String, List<RedditPost>> listDataChild;
    List<RedditPost> stickyPosts;
    List<RedditPost> normalPosts;

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

        if (tvReddit != null) {
            tvReddit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PostViewActivity.class);
                    intent.putExtra("PostListActivity.POST_URL", REDDIT_PAGE_LINK);
                    getApplicationContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void initialize() {
        list = findViewById(R.id.listSection);
        footerView = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_post_list, null, false);
        list.addFooterView(footerView);
        tvReddit = footerView.findViewById(R.id.tvRedditLink);
    }

    @Override
    protected void displayPostList(List<RedditPost> postList) {
        listDataHeader = new ArrayList<>();
        listDataHeader.add("Sticky posts");
        listDataHeader.add("Normal posts");
        listDataChild = new HashMap<>();

        stickyPosts = new ArrayList<RedditPost>();
        normalPosts = new ArrayList<RedditPost>();

        int i;
        for (i = 0; i < postList.size(); i++) {
            if (postList.get(i).isStickyPost()) {
                stickyPosts.add(postList.get(i));
            } else {
                normalPosts.add(postList.get(i));
            }
        }

        listDataChild.put(listDataHeader.get(0), stickyPosts);
        listDataChild.put(listDataHeader.get(1), normalPosts);

        ExpandedPostAdapter adapter = new ExpandedPostAdapter(getApplicationContext(), listDataHeader, listDataChild);

        list.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_in_section;
    }
}
