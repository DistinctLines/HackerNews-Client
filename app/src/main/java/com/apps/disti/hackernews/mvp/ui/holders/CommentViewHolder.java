package com.apps.disti.hackernews.mvp.ui.holders;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    Context mContext;

//    @Bind(R.id.text_comment)
    TextView mCommentText;

//    @Bind(R.id.text_post_author)
    TextView mCommentAuthor;

//    @Bind(R.id.text_post_date)
    TextView mCommentDate;

//    @Bind(R.id.hidden_comment_count)
    TextView mHiddenCommentCount;

//    @Bind(R.id.container_item)
    RelativeLayout mContainer;

//    @Bind(R.id.comment_item)
    LinearLayout mComment;

    private View itemView;

    public CommentViewHolder(View view) {
        super(view);
        this.itemView = view;
        mCommentText = (TextView)view.findViewById(R.id.text_comment);
        mCommentAuthor = (TextView)view.findViewById(R.id.text_post_author);
        mCommentDate = (TextView)view.findViewById(R.id.text_post_date);
        mContainer = (RelativeLayout)view.findViewById(R.id.container_item);
        mComment = (LinearLayout)view.findViewById(R.id.comment_item);
        mHiddenCommentCount = (TextView)view.findViewById(R.id.hidden_comment_count);
    }

    public static CommentViewHolder create(LayoutInflater inflater, ViewGroup parent){
        return new CommentViewHolder(inflater.inflate(R.layout.item_comments_list, parent, false));
    }

    public static void bind(Context context, final int position, final CommentViewHolder holder, Comment comment, int hiddenChildrenCount, final CommentAdapter.StoryDetailRecyclerListener listener){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentClicked(holder.getLayoutPosition());
            }
        });
        holder.mCommentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentClicked(position);
            }
        });
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentClicked(position);
            }
        });

        holder.setComment(comment, position, context);

        if(hiddenChildrenCount == 0 ){
            holder.mHiddenCommentCount.setVisibility(View.GONE);
        }else{
            holder.mHiddenCommentCount.setVisibility(View.VISIBLE);
            holder.mHiddenCommentCount.setText("+" + hiddenChildrenCount);
        }

    }

    public void setComment(Comment comment, int position, Context context) {
        if (comment.text != null) mCommentText.setText(Html.fromHtml(comment.text.trim()));
        if (comment.by != null) mCommentAuthor.setText(comment.by.toString());
        long millisecond = comment.time * 1000;
        mCommentDate.setText(new PrettyTime().format(new Date(millisecond)));
        setCommentIndent(comment.depth, context);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)
                mContainer.getLayoutParams();
        Resources resources = context.getResources();
        float horizontalMargin = resources.getDimension(R.dimen.activity_horizontal_margin);
        float topMargin = (position == 0) ? resources.getDimension(R.dimen.activity_vertical_margin) : 0;
        layoutParams.setMargins(10, (int) topMargin, 10, 0);
        mContainer.setLayoutParams(layoutParams);
    }

    private void setCommentIndent(int depth, Context context) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                mComment.getLayoutParams();
        float margin = com.apps.disti.hackernews.utils.ViewUtils.convertPixelsToDp(depth * 30, context);
        layoutParams.setMargins((int) margin, 0, 0, 0);
        mComment.setLayoutParams(layoutParams);
    }
}