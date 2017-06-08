package com.apps.disti.hackernews.mvp.presenter;

import com.apps.disti.hackernews.mvp.presenter.base.BasePresenter;

import java.util.List;

public interface CommentPresenter extends BasePresenter {

    void loadStoryComments(List<Long> commentIds);

}
