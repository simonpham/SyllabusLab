package com.hasbrain.areyouandroiddev.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

public class RedditPostAdapter extends ArrayAdapter<RedditPost> {

    private final Context context;
    private final List<RedditPost> postList;

    public RedditPostAdapter(Context context, List<RedditPost> postList) {
        super(context, -1, postList);
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.reddit_post_list_item, parent, false);

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

        return rowView;
    }

    private String getDisplayTime(long time) {
        // TODO: convert timestamp to "A fews seconds ago", "3 minutes ago",...
//        long currentTime = System.currentTimeMillis()/1000;
//        long elapsedTime = currentTime - time;
//
//        if (elapsedTime < 60) {
//            return "A few seconds ago";
//        } else if (elapsedTime >= 60 && elapsedTime < 3600) {
//            return "minutes ago";
//        } else if (elapsedTime >= 216000 && elapsedTime < 5184000) {
//            return (Long.parseLong(new java.text.SimpleDateFormat("HH").format(new java.util.Date (currentTime*1000)))
//            - Long.parseLong(new java.text.SimpleDateFormat("HH").format(new java.util.Date (time*1000)))) + "hours ago";
//        } else if (elapsedTime >= 5184000) {
        return new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(time * 1000));
//        }

//        return "n/a";
    }
}