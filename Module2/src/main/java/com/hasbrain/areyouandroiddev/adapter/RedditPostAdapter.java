package com.hasbrain.areyouandroiddev.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.PostViewActivity;
import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

public class RedditPostAdapter extends ArrayAdapter<RedditPost> {

    private final Context context;
    private final List<RedditPost> postList;

    // Human readable time  https://www.epochconverter.com/
    private final long ONE_MINUTE = 60;
    private final long ONE_HOUR = 3600;
    private final long ONE_DAY = 86400;
    private final long ONE_WEEK = 604800;
    private final long ONE_MONTH = 2629743;
    private final long ONE_YEAR = 31556926;

    public RedditPostAdapter(Context context, List<RedditPost> postList) {
        super(context, -1, postList);
        this.context = context;
        this.postList = postList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.reddit_post_list_item, parent, false);

        TextView tvScore = rowView.findViewById(R.id.tvScore);
        TextView tvAuthor = rowView.findViewById(R.id.tvAuthor);
        TextView tvRedditName = rowView.findViewById(R.id.tvRedditName);
        TextView tvTitle = rowView.findViewById(R.id.tvTitle);
        TextView tvCommentCount = rowView.findViewById(R.id.tvCommentCount);
        TextView tvDomain = rowView.findViewById(R.id.tvDomain);
        TextView tvTime = rowView.findViewById(R.id.tvTime);

        if (postList != null) {
            tvScore.setText(postList.get(position).getScore() + "");
            tvAuthor.setText(postList.get(position).getAuthor());
            tvRedditName.setText(postList.get(position).getSubreddit());
            tvTitle.setText(postList.get(position).getTitle());
            tvCommentCount.setText(postList.get(position).getCommentCount() + "");
            tvDomain.setText(postList.get(position).getDomain());

            if (postList.get(position).isStickyPost()) {
                tvTitle.setTextColor(context.getResources().getColor(R.color.post_title_sticky));
            }

            long time = postList.get(position).getCreatedUTC();
            tvTime.setText(getDisplayTime(time));
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostViewActivity.class);
                intent.putExtra("PostListActivity.POST_URL", postList.get(position).getUrl());
                context.startActivity(intent);
            }
        });

        return rowView;
    }

    private String getDisplayTime(long time) {
        // TODO: convert timestamp to "A fews seconds ago", "3 minutes ago",...
        String displayTime = getTimeAgo(time);
        if (!(displayTime.startsWith("-") || displayTime.startsWith("0"))) {
            return displayTime;
        }

        return new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy ").format(new java.util.Date(time * 1000));
    }

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

    private int toMinutes(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("mm").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("mm").format(new java.util.Date(time * 1000)));
    }

    private int toHours(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("hh").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("hh").format(new java.util.Date(time * 1000)));
    }

    private int toDays(long time) {
        long currentTime = System.currentTimeMillis() / 1000;
        return Integer.parseInt(new java.text.SimpleDateFormat("DD").format(new java.util.Date(currentTime * 1000)))
                - Integer.parseInt(new java.text.SimpleDateFormat("DD").format(new java.util.Date(time * 1000)));
    }

}