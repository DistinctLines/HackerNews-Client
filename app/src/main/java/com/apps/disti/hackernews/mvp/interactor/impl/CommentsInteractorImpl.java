package com.apps.disti.hackernews.mvp.interactor.impl;

import android.content.Context;

import com.apps.disti.hackernews.HackerNewsApplication;
import com.apps.disti.hackernews.data.DataManager;
import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.listener.RequestCallback;
import com.apps.disti.hackernews.mvp.interactor.CommentsInteractor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class CommentsInteractorImpl implements CommentsInteractor {

    private DataManager dataManager;
    private Context context;

    @Inject
    public CommentsInteractorImpl (Context context){
        this.context = context;
    }

    @Override
    public Subscription fetchComments(List<Long> kids, final RequestCallback<Comment> commentRequestCallback) {
        return dataManager.getPostComments(kids, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataManager.getScheduler())
                .subscribe(new Action1<Comment>() {
                    @Override
                    public void call(Comment comment) {
                        commentRequestCallback.success(comment);
                    }
                });
    }

    @Override
    public void initInteractor() {
        dataManager = HackerNewsApplication.get(context).getApplicationComponent().getDataManager();
    }
}
