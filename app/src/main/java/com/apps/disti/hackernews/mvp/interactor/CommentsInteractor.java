package com.apps.disti.hackernews.mvp.interactor;

import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.listener.RequestCallback;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public interface CommentsInteractor {

    Subscription fetchComments(List<Long> kids, RequestCallback<Comment> callback);

    void initInteractor();

}
