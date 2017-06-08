package com.apps.disti.hackernews.mvp.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.apps.disti.hackernews.R;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.mvp.presenter.StoryListPresenter;
import com.apps.disti.hackernews.mvp.presenter.impl.StoryListPresenterImpl;
import com.apps.disti.hackernews.mvp.ui.activities.base.BaseActivity;
import com.apps.disti.hackernews.mvp.ui.adapter.StoriesHolder;
import com.apps.disti.hackernews.mvp.ui.fragment.StoriesFragment;
import com.apps.disti.hackernews.mvp.view.MainActivityView;
import com.apps.disti.hackernews.mvp.view.base.BaseView;
import com.codemonkeylabs.fpslibrary.TinyDancer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Subscription;
import uk.co.ribot.easyadapter.EasyRecyclerAdapter;

import static com.apps.disti.hackernews.utils.Constants.BEST_STORIES;
import static com.apps.disti.hackernews.utils.Constants.NEW_STORIES;
import static com.apps.disti.hackernews.utils.Constants.TOP_STORIES;

public class MainActivity extends BaseActivity implements MainActivityView, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recycler_stories)
    RecyclerView recyclerView;

    @Bind(R.id.progress_indicator)
    ProgressBar progressBar;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Inject
    StoryListPresenterImpl storyListPresenter;

    public static final String ARG_USER = "ARG_USER";

    private String user;
    private EasyRecyclerAdapter<Post> easyRecyclerAdapter;
    private List<Subscription> subscriptionList;
    private List<Post> mStories;

    private int currentMode = TOP_STORIES;

    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        subscriptionList = new ArrayList<>();
        mStories = new ArrayList<>();

        super.onCreate(savedInstanceState);

        storyListPresenter.loadStories(currentMode);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
        applicationComponent.inject(this);
    }

    @Override
    public void initViews() {

        presenter = storyListPresenter;
        presenter.attachView(this);

        initSwipeRefreshLayout();
        initRecyclerView();
        setupToolbar();

//        TinyDancer.create().show(this);

    }

    private void setupToolbar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){

            switch(currentMode){
                case BEST_STORIES:
                    actionBar.setTitle("Best Stories");
                    break;
                case TOP_STORIES:
                    actionBar.setTitle("Top Stories");
                    break;
                case NEW_STORIES:
                    actionBar.setTitle("New Stories");
                    break;
            }
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int newMode = 0;

                switch(item.getItemId()){
                    case R.id.nav_top:
                        Log.e("Clicked","Top");
                        newMode = TOP_STORIES;
                        break;
                    case R.id.nav_new:
                        Log.e("Clicked","New");
                        newMode = NEW_STORIES;
                        break;
                    case R.id.nav_best:
                        Log.e("Clicked","Best");
                        newMode = BEST_STORIES;
                        break;
                }

                drawerLayout.closeDrawers();

                initAcitivtyType(newMode);

                return true;
            }
        });

    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView(){
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        easyRecyclerAdapter = new EasyRecyclerAdapter<Post>(this, StoriesHolder.class, mStories);
        recyclerView.setAdapter(easyRecyclerAdapter);
    }

    private void initAcitivtyType(int selectedMode){

        if(selectedMode == currentMode)
            return;

        switch(selectedMode){
            case BEST_STORIES:
                getSupportActionBar().setTitle("Best Stories");
                break;
            case TOP_STORIES:
                getSupportActionBar().setTitle("Top Stories");
                break;
            case NEW_STORIES:
                getSupportActionBar().setTitle("New Stories");
                break;
        }

        for(Subscription sub : subscriptionList)
            sub.unsubscribe();

        easyRecyclerAdapter.setItems(new ArrayList<Post>());

        storyListPresenter.loadStories(selectedMode);
    }

    @Override
    public void addposts(Post post) {
        hideProgress();
        easyRecyclerAdapter.addItem(post);
        easyRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        for(Subscription sub : subscriptionList)
            sub.unsubscribe();

        easyRecyclerAdapter.setItems(new ArrayList<Post>());
        storyListPresenter.loadStories(currentMode);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }
}
