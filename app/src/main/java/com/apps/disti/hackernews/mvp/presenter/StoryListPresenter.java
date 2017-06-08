package com.apps.disti.hackernews.mvp.presenter;

import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.mvp.presenter.base.BasePresenter;

import java.util.List;

import rx.Observable;

public interface StoryListPresenter extends BasePresenter{

    void loadStories(int type);

}
