package com.apps.disti.hackernews.mvp.ui.holders;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    RelativeLayout mRelativeLayout;

    public CommentCollapsedViewHolder(View itemView) {
        super(itemView);
        this.mAuthor = (TextView) itemView.findViewById(R.id.collapsed_author);
        this.mTimeStamp = (TextView) itemView.findViewById(R.id.collapsed_timestamp);
        this.mCollapsedNumComments = (TextView) itemView.findViewById(R.id.collapsed_num_comments);
        this.mLinearLayout = (LinearLayout) itemView.findViewById(R.id.collapsed_linear);
        this.mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.collapsed_relative);

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

        commentCollapsedViewHolder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentClicked(position);
            }
        });

        commentCollapsedViewHolder.setComment(comment, position, context, hiddenChildrenCount);

//        commentCollapsedViewHolder.mAuthor.setText(comment.by);
//        commentCollapsedViewHolder.mTimeStamp.setText(new PrettyTime().format(new Date(comment.time * 1000)));
//        commentCollapsedViewHolder.mCollapsedNumComments.setText("" + hiddenChildrenCount);
//
//        commentCollapsedViewHolder.setIndent(comment.depth, context);

    }

    public void setComment(Comment comment, int position, Context context, int hiddenChildrenCount){

        mAuthor.setText(comment.by);
        mTimeStamp.setText(new PrettyTime().format(new Date(comment.time * 1000)));
        mCollapsedNumComments.setText("" + hiddenChildrenCount);

        setIndent(comment.depth, context);

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mRelativeLayout.getLayoutParams();
        Resources resources = context.getResources();
        float topMargin = (position == 0) ? resources.getDimension(R.dimen.activity_vertical_margin) : 0;
        marginLayoutParams.setMargins(10, (int) topMargin, 10, 0);
        mRelativeLayout.setLayoutParams(marginLayoutParams);

    }

    public void setIndent(int depth, Context context){

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLinearLayout.getLayoutParams();
        float margin = com.apps.disti.hackernews.utils.ViewUtils.convertPixelsToDp(depth * 30, context);
        layoutParams.setMargins((int) margin, 0, 0, 0);
        mLinearLayout.setLayoutParams(layoutParams);

    }

    public void setCommentInfo(Comment comment){

    }

}
