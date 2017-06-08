package com.apps.disti.hackernews.mvp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.disti.hackernews.R;
import com.apps.disti.hackernews.data.DataManager;
import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.mvp.presenter.CommentPresenter;
import com.apps.disti.hackernews.mvp.presenter.impl.CommentPresenterImpl;
import com.apps.disti.hackernews.mvp.ui.activities.base.BaseActivity;
import com.apps.disti.hackernews.mvp.ui.adapter.CommentAdapter;
import com.apps.disti.hackernews.mvp.view.CommentActivityView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class CommentsActivity extends BaseActivity implements CommentActivityView{

    @Bind(R.id.layout_comments)
    RelativeLayout commentsLayout;

    @Bind(R.id.progress_indicator)
    LinearLayout progressBar;

    @Bind(R.id.recycler_comments)
    RecyclerView commentsRecycler;

    @Bind(R.id.text_no_comments)
    TextView noCommentsText;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static final String EXTRA_POST =
            "com.hitherejoe.HackerNews.ui.activity.CommentsActivity.EXTRA_POST";

    @Inject
    DataManager dataManager;

    @Inject
    CommentPresenterImpl commentPresenter;

    private Post post;
//    private DataManager dataManager;
    private List<Subscription> subscriptionList;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> comments;

    public static Intent getStartIntent(Context context, Post post) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(EXTRA_POST, post);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        post = getIntent().getParcelableExtra(EXTRA_POST);

//        dataManager = HackerNewsApplication.get(this).getApplicationComponent().getDataManager();
        subscriptionList = new ArrayList<>();
        comments = new ArrayList<>();

        setupToolbar();
        setupRecyclerView();
        loadStories();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_comments;
    }

    @Override
    public void initInjector() {
        applicationComponent.inject(this);
    }

    @Override
    public void initViews() {
        presenter = commentPresenter;
        presenter.attachView(this);

    }

    private void setupToolbar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(post.title);
        }
    }

    private void setupRecyclerView(){
        commentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, post, comments, new CommentAdapter.StoryDetailRecyclerListener() {
            @Override
            public void onCommentClicked(int position) {
                if(commentAdapter.areChildrenHidden(position))
                    commentAdapter.showChildComments(position);
                else
                    commentAdapter.hideChildComments(position);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        commentsRecycler.setLayoutManager(new WrapContentLinearLayoutManager(this));
        commentsRecycler.setAdapter(commentAdapter);
    }

    private void loadStories(){
        commentPresenter.loadStoryComments(post.kids);
    }


    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
        comments.addAll(comment.comments);
        commentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {

        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state){
            try{
                super.onLayoutChildren(recycler, state);
            }catch(IndexOutOfBoundsException e){
                Log.e("Error", "IndexOutOfBoundsException in recyclerview");
            }
        }
    }
}
