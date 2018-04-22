package com.hasbrain.areyouandroiddev.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

/**
 * Created by Simon Pham on 4/22/18.
 * Email: simonpham.dn@gmail.com
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<RedditPost> postList;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvScore;
        TextView tvAuthor;
        TextView tvRedditName;
        TextView tvTitle;
        TextView tvCommentCount;


        ViewHolder(View v) {
            super(v);

            tvScore = v.findViewById(R.id.tvScore);
            tvAuthor = v.findViewById(R.id.tvAuthor);
            tvRedditName = v.findViewById(R.id.tvRedditName);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvCommentCount = v.findViewById(R.id.tvCommentCount);
        }
    }

    public PostAdapter(List<RedditPost> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_reddit_post, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

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
            viewHolder.tvTitle.setTextColor(.getResources().getColor(R.color.post_title_sticky));
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}
