package com.apps.disti.hackernews.mvp.interactor;

import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.listener.RequestCallback;

import rx.Observable;
import rx.Subscription;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public interface StoryListInteractor<T> {

    void initInteractor();
    Subscription getTopStories(RequestCallback<T> callback);
    Subscription getBestStories(RequestCallback<T> callback);
    Subscription getNewStories(RequestCallback<T> callback);
}
