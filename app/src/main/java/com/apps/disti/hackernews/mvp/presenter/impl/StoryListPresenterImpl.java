package com.apps.disti.hackernews.mvp.presenter.impl;

import android.util.Log;

import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.listener.RequestCallback;
import com.apps.disti.hackernews.mvp.interactor.StoryListInteractor;
import com.apps.disti.hackernews.mvp.interactor.impl.StoryListInteractorImpl;
import com.apps.disti.hackernews.mvp.presenter.StoryListPresenter;
import com.apps.disti.hackernews.mvp.presenter.base.BasePresentImpl;
import com.apps.disti.hackernews.mvp.view.MainActivityView;
import com.apps.disti.hackernews.mvp.view.base.BaseView;

import javax.inject.Inject;

import rx.Observable;

import static com.apps.disti.hackernews.utils.Constants.BEST_STORIES;
import static com.apps.disti.hackernews.utils.Constants.NEW_STORIES;
import static com.apps.disti.hackernews.utils.Constants.TOP_STORIES;

public class StoryListPresenterImpl extends BasePresentImpl<MainActivityView, Post> implements StoryListPresenter, RequestCallback<Post>{

    private StoryListInteractor storyListInteractor;

    @Inject
    public StoryListPresenterImpl(StoryListInteractorImpl storyListInteractor){
        this.storyListInteractor = storyListInteractor;
    }

    @Override
    public void onCreate(){
        storyListInteractor.initInteractor();
    }

    @Override
    public void onDestroy(){

    }

    @Override
    public void loadStories(int type){

        storyListInteractor.getList();

        switch(type){
            case TOP_STORIES:
                storyListInteractor.getTopStories(this);
                break;

            case NEW_STORIES:
                storyListInteractor.getNewStories(this);
                break;

            case BEST_STORIES:
                storyListInteractor.getBestStories(this);
                break;

        }

//        storyListInteractor.getTopStories(this);
//        storyListInteractor.getMaxUserPost(this);
    }

    @Override
    public void beforeRequest() {

    }

    @Override
    public void success(Post data) {
        super.success(data);
        Log.e("Post", " " + data.id);
        view.addposts(data);
    }

    @Override
    public void onError(String data) {

    }
}
