package com.apps.disti.hackernews.mvp.ui.adapter;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.apps.disti.hackernews.BuildConfig;
import com.apps.disti.hackernews.R;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.mvp.ui.activities.CommentsActivity;
import com.apps.disti.hackernews.mvp.ui.activities.ViewStoryActivity;

import org.w3c.dom.Text;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

import static java.security.AccessController.getContext;

@LayoutId(R.layout.item_story)
public class StoriesHolder extends ItemViewHolder<Post> {

    @ViewId(R.id.container_post)
    View mPostContainer;

    @ViewId(R.id.text_post_title)
    TextView mPostTitle;

    @ViewId(R.id.text_post_author)
    TextView mPostAuthor;

    @ViewId(R.id.text_post_points)
    TextView mPostPoints;

    @ViewId(R.id.text_view_comments)
    TextView mPostComments;

    @ViewId(R.id.text_view_post)
    TextView mViewPost;

    @ViewId(R.id.text_story_comments)
    TextView mNumComments;

    public StoriesHolder(View view) {
        super(view);
    }

    @Override
    public void onSetValues(Post story, PositionInfo positionInfo) {
        mPostTitle.setText(story.title);
        mPostAuthor.setText(Html.fromHtml(getContext().getString(R.string.story_by) + " " + "<u>" + story.by + "</u>"));
        mPostPoints.setText(story.score + " " + getContext().getString(R.string.story_points));
        if(story.kids != null)
            mNumComments.setText(story.kids.size() + " comments");

        if (getItem().postType == Post.PostType.STORY && story.kids == null) {
            mPostComments.setVisibility(View.GONE);
        } else {
            mPostComments.setVisibility(View.VISIBLE);
        }
        mViewPost.setVisibility(story.url != null && story.url.length() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSetListeners() {
        mPostAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getContext().startActivity(UserActivity.getStartIntent(getContext(), getItem().by));
            }
        });
        mPostComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCommentsActivity();
            }
        });
        mViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStoryActivity();
            }
        });
        mPostContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post.PostType postType = getItem().postType;
                if (postType == Post.PostType.JOB || postType == Post.PostType.STORY) {
                    launchStoryActivity();
                } else if (postType == Post.PostType.ASK) {
                    launchCommentsActivity();
                }
            }
        });
    }

    private void launchStoryActivity() {
        getContext().startActivity(ViewStoryActivity.getStartIntent(getContext(), getItem()));
    }

    private void launchCommentsActivity() {
        getContext().startActivity(CommentsActivity.getStartIntent(getContext(), getItem()));
    }
}