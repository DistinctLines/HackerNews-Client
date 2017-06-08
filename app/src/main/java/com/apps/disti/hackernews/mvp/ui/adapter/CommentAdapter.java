package com.apps.disti.hackernews.mvp.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.disti.hackernews.R;
import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.mvp.ui.holders.CommentCollapsedViewHolder;
import com.apps.disti.hackernews.mvp.ui.holders.CommentViewHolder;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_COMMENT = 0;
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_COLLAPSED_COMMENT = 2;

    private Post mPost;
    private List<Comment> mComments;
    private StoryDetailRecyclerListener recyclerListener;
    private Map<Long, List<Comment>> hiddenComments;
    private Context context;

    public CommentAdapter(Context context, Post post, List<Comment> comments, StoryDetailRecyclerListener storyDetailRecyclerListener) {
        mPost = post;
        mComments = comments;
        recyclerListener = storyDetailRecyclerListener;
        this.context = context;
        hiddenComments = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch(viewType){
            case(VIEW_TYPE_COMMENT):
                viewHolder = CommentViewHolder.create(LayoutInflater.from(context), parent);
                break;
            case(VIEW_TYPE_HEADER):
                viewHolder = new StoryTextViewHolder(LayoutInflater.from(context).inflate(R.layout.item_story_text_list, parent, false), context);
                break;
            case(VIEW_TYPE_COLLAPSED_COMMENT):
//                viewHolder = new CommentCollapsedViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_collapsed, parent, false));
                viewHolder = CommentCollapsedViewHolder.create(LayoutInflater.from(context), parent);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int hiddenCommentsCount = 0;

        switch(getItemViewType(position)){
            case(VIEW_TYPE_COMMENT):
                Comment comment = (Comment) mComments.get(position);
                if(hiddenComments.containsKey(comment.id))
                    hiddenCommentsCount = hiddenComments.get(comment.id).size();
                CommentViewHolder.bind(context, position, (CommentViewHolder) holder,comment, hiddenCommentsCount, recyclerListener);
                break;
            case(VIEW_TYPE_HEADER):
                StoryTextViewHolder s = (StoryTextViewHolder) holder;
                s.setPost(mPost);
                break;
            case(VIEW_TYPE_COLLAPSED_COMMENT):

                Comment commentTemp = (Comment) mComments.get(position);
                if(hiddenComments.containsKey(commentTemp.id) && hiddenComments.get(commentTemp.id) != null) {
                    Log.e("hidden comments", " " + hiddenComments.get(commentTemp.id));
                    hiddenCommentsCount = hiddenComments.get(commentTemp.id).size();
                }
                CommentCollapsedViewHolder.bind(context, position, (CommentCollapsedViewHolder) holder, commentTemp, hiddenCommentsCount, recyclerListener);

//                CommentCollapsedViewHolder c = (CommentCollapsedViewHolder) holder;
//                Comment commentTemp = (Comment) mComments.get(position);
//                c.setCommentInfo(commentTemp);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (postHasText()) {
            return mComments.size() + 1;
        } else {
            return mComments.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        // TODO - change to 0
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else if(hiddenComments.containsKey(mComments.get(position).id)) {
            return VIEW_TYPE_COLLAPSED_COMMENT;
        }else {
            return VIEW_TYPE_COMMENT;
        }
    }

    private boolean postHasText() {
        return (mPost.text != null && !mPost.text.equals(""));
    }

    public class StoryTextViewHolder extends RecyclerView.ViewHolder {
        TextView mPostTitle;
        TextView mPostAuthor;
        TextView mPostDate;
        TextView mPostWebsite;
        RelativeLayout relativeLayout;
        LinearLayout linearLayout;
        TextView mStoryPoints;

        public StoryTextViewHolder(View view, Context context) {
            super(view);
            mPostTitle = (TextView)view.findViewById(R.id.story_title);
            mPostWebsite = (TextView)view.findViewById(R.id.story_website);
            mPostAuthor = (TextView)view.findViewById(R.id.story_author);
            mPostDate = (TextView)view.findViewById(R.id.story_time);
            relativeLayout = (RelativeLayout)view.findViewById(R.id.story_container);
            linearLayout = (LinearLayout)view.findViewById(R.id.story_container_main);
            relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            mStoryPoints = (TextView)view.findViewById(R.id.story_points);
        }

        public void setPost(Post post) {
            mPostTitle.setText(post.title);
            mStoryPoints.setText(String.valueOf(post.score));
            mPostAuthor.setText(post.by);

            mPostWebsite.setText(" | " + trim(post.url) + " | ");

            long millisecond = post.time * 1000;
            mPostDate.setText(new PrettyTime().format(new Date(millisecond)));
        }

        private String trim(String url){
            String[] s_URL = url.split("//");
            String s1 = s_URL[1];
            String[] s2 = s1.split("/");
            String s3 = s2[0];
            return s3;
        }
    }

    public boolean areChildrenHidden(int position){
        Log.e("Clicked","position" + position);
        Comment comment = (Comment) mComments.get(position);
        return hiddenComments.containsKey(comment.id);
    }

    public void hideChildComments(int position){

        ArrayList<Comment> childrenComments = new ArrayList<>();
        ArrayList<Integer> childrenCommentPositions = new ArrayList<>();
        Comment parentComment = mComments.get(position);
        List<Comment> possibleChildrenComments = mComments.subList(position + 1, mComments.size());
        for(Comment comment : possibleChildrenComments){
            if(comment.depth > parentComment.depth){
                childrenComments.add(comment);
                childrenCommentPositions.add(mComments.indexOf(comment));
            }
            else{
                break;
            }
        }

        if(!childrenComments.isEmpty()){
            for(Comment c : childrenComments)
                mComments.remove(c);
        }else{
            Comment c = mComments.get(position);
            notifyItemRemoved(position);
            hiddenComments.put(parentComment.id, null);
            notifyItemChanged(position);
        }

        if(!childrenCommentPositions.isEmpty()){
            notifyItemRangeRemoved(childrenCommentPositions.get(0), childrenCommentPositions.size());
            hiddenComments.put(parentComment.id, childrenComments);
            notifyItemChanged(position);
        }

    }

    public void showChildComments(int position){
        Comment comment = mComments.get(position);
        List<Comment> hiddenCommentsForParent = hiddenComments.get(comment.id);
        int insertIndex = mComments.indexOf(comment) + 1;

        if(hiddenCommentsForParent == null) {
            hiddenComments.remove(comment.id);
            return;
        }

        for(Comment com: hiddenCommentsForParent){
            addComment(insertIndex, com);
            insertIndex++;
        }

        notifyItemRangeInserted(mComments.indexOf(comment) + 1, hiddenCommentsForParent.size() - 1);
        hiddenComments.remove(comment.id);
        notifyItemChanged(position);
    }

    private void addComment(Comment comment){
        mComments.add(comment);
        notifyItemInserted(mComments.size());
    }

    private void addComment(int position, Comment comment){
        mComments.add(position, comment);
        notifyItemInserted(position);
    }

    public interface StoryDetailRecyclerListener{
        void onCommentClicked(int position);
    }

}
