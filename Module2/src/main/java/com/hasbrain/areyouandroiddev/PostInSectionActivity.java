package com.hasbrain.areyouandroiddev;

import android.widget.ExpandableListView;

import com.hasbrain.areyouandroiddev.adapter.ExpandablePostAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/9/15.
 */
public class PostInSectionActivity extends PostListActivity {

    List<String> listDataHeader;
    HashMap<String, List<RedditPost>> listDataChild;
    List<RedditPost> stickyPosts;
    List<RedditPost> normalPosts;

    ExpandableListView listExp;

    @Override
    protected void displayPostList(List<RedditPost> postList) {
        listExp = findViewById(R.id.listExpandable);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        stickyPosts = new ArrayList<>();
        normalPosts = new ArrayList<>();

        listDataHeader.add("Sticky posts");
        listDataHeader.add("Normal posts");

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

        ExpandablePostAdapter adapter = new ExpandablePostAdapter(getApplicationContext(), listDataHeader, listDataChild);

        listExp.setAdapter(adapter);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_post_in_section;
    }
}
