package com.apps.disti.hackernews.mvp.ui.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.disti.hackernews.R;
import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.mvp.ui.adapter.CommentAdapter;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class CommentCollapsedViewHolder extends RecyclerView.ViewHolder {

    TextView mAuthor;
    TextView mTimeStamp;
    TextView mCollapsedNumComments;
    LinearLayout mLinearLayout;


    public CommentCollapsedViewHolder(View itemView) {
        super(itemView);
        this.mAuthor = (TextView) itemView.findViewById(R.id.collapsed_author);
        this.mTimeStamp = (TextView) itemView.findViewById(R.id.collapsed_timestamp);
        this.mCollapsedNumComments = (TextView) itemView.findViewById(R.id.collapsed_num_comments);
        this.mLinearLayout = (LinearLayout) itemView.findViewById(R.id.collapsed_linear);

    }

    public static CommentCollapsedViewHolder create(LayoutInflater layoutInflater, ViewGroup parent){
        return new CommentCollapsedViewHolder(layoutInflater.inflate(R.layout.item_comment_collapsed, parent, false));
    }

    public static void bind(Context context, final int position, final CommentCollapsedViewHolder commentCollapsedViewHolder, Comment comment, int hiddenChildrenCount, final CommentAdapter.StoryDetailRecyclerListener listener){

        commentCollapsedViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentClicked(position);
            }
        });

        commentCollapsedViewHolder.mAuthor.setText(comment.by);
        commentCollapsedViewHolder.mTimeStamp.setText(new PrettyTime().format(new Date(comment.time * 1000)));
        commentCollapsedViewHolder.mCollapsedNumComments.setText("" + hiddenChildrenCount);

    }

    public void setCommentInfo(Comment comment){

    }

}
