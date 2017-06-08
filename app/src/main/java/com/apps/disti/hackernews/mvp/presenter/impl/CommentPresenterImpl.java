package com.apps.disti.hackernews.mvp.presenter.impl;


import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.listener.RequestCallback;
import com.apps.disti.hackernews.mvp.interactor.CommentsInteractor;
import com.apps.disti.hackernews.mvp.interactor.impl.CommentsInteractorImpl;
import com.apps.disti.hackernews.mvp.presenter.CommentPresenter;
import com.apps.disti.hackernews.mvp.presenter.base.BasePresentImpl;
import com.apps.disti.hackernews.mvp.presenter.base.BasePresenter;
import com.apps.disti.hackernews.mvp.view.CommentActivityView;

import java.util.List;

import javax.inject.Inject;

public class CommentPresenterImpl extends BasePresentImpl<CommentActivityView, Comment> implements CommentPresenter, RequestCallback<Comment> {

    private CommentsInteractor commentsInteractor;

    @Inject
    public CommentPresenterImpl(CommentsInteractorImpl commentsInteractor){
        this.commentsInteractor = commentsInteractor;
    }

    @Override
    public void onCreate(){
        commentsInteractor.initInteractor();
    }

    @Override
    public void loadStoryComments(List<Long> commentIds) {
        commentsInteractor.fetchComments(commentIds, this);
    }

    @Override
    public void success(Comment comment){
        super.success(comment);
        view.addComment(comment);
    }

}
