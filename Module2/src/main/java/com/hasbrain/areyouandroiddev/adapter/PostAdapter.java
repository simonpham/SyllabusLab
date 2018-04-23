package com.hasbrain.areyouandroiddev.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.PostViewActivity;
import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

/**
 * Created by Simon Pham on 4/22/18.
 * Email: simonpham.dn@gmail.com
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public static final String REDDIT_PAGE_LINK = "https://www.reddit.com/r/androiddev/";

    private final long ONE_MINUTE = 60;
    private final long ONE_HOUR = 3600;
    private final long ONE_DAY = 86400;
    private final long ONE_WEEK = 604800;
    private final long ONE_MONTH = 2629743;
    private final long ONE_YEAR = 31556926;

    private final int ITEM_POST = 0;
    private final int ITEM_FOOTER = 1;

    private Context context;
    private List<RedditPost> postList;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvScore;
        TextView tvAuthor;
        TextView tvRedditName;
        TextView tvTitle;
        TextView tvCommentCount;
        View layout;


        ViewHolder(View v, int itemType) {
            super(v);
            this.layout = v;

            if (itemType == ITEM_POST) {
                tvScore = v.findViewById(R.id.tvScore);
                tvAuthor = v.findViewById(R.id.tvAuthor);
                tvRedditName = v.findViewById(R.id.tvRedditName);
                tvTitle = v.findViewById(R.id.tvTitle);
                tvCommentCount = v.findViewById(R.id.tvCommentCount);
            }
        }
    }

    public PostAdapter(Context context, List<RedditPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount() - 1) {
            return ITEM_POST;
        }
        return ITEM_FOOTER;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_POST) {
            View v = inflater.inflate(R.layout.item_reddit_post, parent, false);
            return new ViewHolder(v, ITEM_POST);
        } else {
            View v = inflater.inflate(R.layout.item_footer, parent, false);
            return new ViewHolder(v, ITEM_FOOTER);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        switch (getItemViewType(position)) {
            case ITEM_POST:
                long time = postList.get(position).getCreatedUTC();
                viewHolder.tvScore.setText(postList.get(position).getScore() + "");
                viewHolder.tvAuthor.setText(postList.get(position).getAuthor());
                viewHolder.tvRedditName.setText(postList.get(position).getSubreddit());
                viewHolder.tvTitle.setText(postList.get(position).getTitle());
                viewHolder.tvCommentCount.setText(postList.get(position).getCommentCount()
                        + " Comments"
                        + " • "
                        + postList.get(position).getDomain()
                        + " • "
                        + getDisplayTime(time));

                if (postList.get(position).isStickyPost()) {
                    viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.post_title_sticky));
                }

                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostViewActivity.class);
                        intent.putExtra("PostListActivity.POST_URL", postList.get(viewHolder.getAdapterPosition()).getUrl());
                        context.startActivity(intent);
                    }
                });
                break;
            case ITEM_FOOTER:
                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PostViewActivity.class);
                        intent.putExtra("PostListActivity.POST_URL", REDDIT_PAGE_LINK);
                        context.startActivity(intent);
                    }
                });
                break;
        }


    }

    @Override
    public int getItemCount() {
        return postList.size() + 1;
    }

    @SuppressLint("SimpleDateFormat")
    private String getDisplayTime(long time) {
        String displayTime = getTimeAgo(time);
        if (!(displayTime.startsWith("-") || displayTime.startsWith("0"))) {
            return displayTime;
        }

        return new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format(new java.util.Date(time * 1000));
    }

    @SuppressLint("SimpleDateFormat")
    private String getTimeAgo(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        long elapsedTime = currentTime - time;

        if (elapsedTime >= 0 && elapsedTime < ONE_MINUTE) {
            return "A few seconds ago";
        } else if (elapsedTime < ONE_HOUR) {
            return toMinutes(time) + " minutes ago";
        } else if (elapsedTime < ONE_DAY) {
            return toHours(time) + " hours ago";
        } else if (elapsedTime < ONE_MONTH) {
            return toDays(time) + " days ago";
        }

        return new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format(new java.util.Date(time * 1000));
    }

    @SuppressLint("SimpleDateFormat")
    private int toMinutes(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("m").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("m").format(new java.util.Date(time * 1000)));
    }

    @SuppressLint("SimpleDateFormat")
    private int toHours(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("H").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("H").format(new java.util.Date(time * 1000)));
    }

    @SuppressLint("SimpleDateFormat")
    private int toDays(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("D").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("D").format(new java.util.Date(time * 1000)));
    }

}
