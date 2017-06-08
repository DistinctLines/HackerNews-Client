package com.apps.disti.hackernews.mvp.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.apps.disti.hackernews.R;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.data.model.User;
import com.apps.disti.hackernews.mvp.ui.adapter.StoriesHolder;
import com.apps.disti.hackernews.mvp.ui.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

public class StoriesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recycler_stories)
    RecyclerView recyclerView;

    @Bind(R.id.progress_indicator)
    ProgressBar progressBar;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static final String ARG_USER = "ARG_USER";

    private String user;
    private EasyRecyclerAdapter<Post> easyRecyclerAdapter;
    private List<Subscription> mSubscriptions;
    private List<Post> mStories;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mSubscriptions = new ArrayList<>();
        mStories = new ArrayList<>();

        Bundle bundle = getArguments();
        if(bundle != null) user = bundle.getString(ARG_USER, null);
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews(View view) {

        initSwipeRefreshLayout();
        initRecylerView();
        initToolbar(view);
        loadStoriesIfNetworkConnected();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_stories;
    }

    private void initRecylerView(){
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        easyRecyclerAdapter = new EasyRecyclerAdapter<Post>(getActivity(),
                StoriesHolder.class, mStories);
        recyclerView.setAdapter(easyRecyclerAdapter);
    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initToolbar(View v){
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            if(user != null){
                Log.e("User not null", " " + user);
                actionBar.setTitle(user);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void loadStoriesIfNetworkConnected() {
        if (user != null) {
//            getUserStories();
        } else {
            getTopStories();
        }
    }

    private void getTopStories() {
        mSubscriptions.add(dataManager.getTopStories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataManager.getScheduler())
                .subscribe(new Subscriber<Post>() {
                    @Override
                    public void onCompleted() {
                        Timber.e("Completed", "Get top Stories");
                        Log.e("count", " " + easyRecyclerAdapter.getItemCount());
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingViews();
                        Log.e("There was a problem ", "asd");
                    }

                    @Override
                    public void onNext(Post post) {
                        hideLoadingViews();
                        easyRecyclerAdapter.addItem(post);
 //                        easyRecyclerAdapter.notifyItemInserted(easyRecyclerAdapter.getItemCount() - 1);
                        easyRecyclerAdapter.notifyDataSetChanged();
                    }
                }));

    }

//    private void getUserStories() {
//        mSubscriptions.add(dataManager.getUserPosts(user)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(dataManager.getScheduler())
//                .subscribe(new Subscriber<Post>() {
//                    @Override
//                    public void onCompleted() { }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        hideLoadingViews();
//                        Timber.e("There was a problem loading the user stories " + e);
//                    }
//
//                    @Override
//                    public void onNext(Post story) {
//                        hideLoadingViews();
//                        easyRecyclerAdapter.addItem(story);
//                        easyRecyclerAdapter.notifyItemInserted(easyRecyclerAdapter.getItemCount() - 1);
//                    }
//                }));
//    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        for(Subscription sub : mSubscriptions)
            sub.unsubscribe();
    }

    @Override
    public void onRefresh() {
        for(Subscription sub : mSubscriptions)
            sub.unsubscribe();

        easyRecyclerAdapter.setItems(new ArrayList<Post>());
        getTopStories();
        // presenter.refreshData();
    }

    private void hideLoadingViews() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }
}
