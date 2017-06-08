package com.apps.disti.hackernews.mvp.interactor.impl;

import android.content.Context;
import android.util.Log;

import com.apps.disti.hackernews.HackerNewsApplication;
import com.apps.disti.hackernews.data.DataManager;
import com.apps.disti.hackernews.data.local.DatabaseHelper;
import com.apps.disti.hackernews.data.local.PreferencesHelper;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.data.remote.HackerNewsService;
import com.apps.disti.hackernews.di.component.DaggerDataManagerComponent;
import com.apps.disti.hackernews.di.module.DataManagerModule;
import com.apps.disti.hackernews.listener.RequestCallback;
import com.apps.disti.hackernews.mvp.interactor.StoryListInteractor;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class StoryListInteractorImpl implements StoryListInteractor<Post> {

    private DataManager dataManager;
    private Context context;

    @Inject
    public StoryListInteractorImpl(Context context){
        this.context = context;
    }

    @Override
    public void initInteractor(){
        dataManager = HackerNewsApplication.get(context).getApplicationComponent().getDataManager();
    }

    @Override
    public Subscription getTopStories(final RequestCallback<Post> callback) {
        return dataManager.getTopStories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataManager.getScheduler())
                .subscribe(new Subscriber<Post>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Post post) {
                        callback.success(post);
                    }
                });
    }

    @Override
    public Subscription getBestStories(final RequestCallback<Post> callback){
        return dataManager.getBestStories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataManager.getScheduler())
                .subscribe(new Action1<Post>() {
                    @Override
                    public void call(Post post) {
                        callback.success(post);
                    }
                });
    }

    @Override
    public Subscription getNewStories(final RequestCallback<Post> callback) {
        return dataManager.getNewStories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataManager.getScheduler())
                .subscribe(new Action1<Post>() {
                    @Override
                    public void call(Post post) {
                        callback.success(post);
                    }
                });
    }

    public Subscription getList(){
        return dataManager.getNames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(dataManager.getScheduler())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("String", " " + s);
                    }
                });
    }

//    @Override
//    public Subscription getTopStories(final RequestCallback<Post> callback) {
//        return hackerNewsService.getTopStories()
//                .concatMap(new Func1<List<Long>, Observable<? extends Post>>() {
//                    @Override
//                    public Observable<? extends Post> call(List<Long> longs) {
//                        return getPostsFromIds(longs);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(subscribeScheduler)
//                .subscribe(new Subscriber<Post>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("Error", "Unable to get Top stories");
//                    }
//
//                    @Override
//                    public void onNext(Post post) {
//                        callback.success(post);
//                    }
//                });
//    }
//
//    private Observable<Post> getPostsFromIds(List<Long> ids){
//        return Observable.from(ids)
//                .concatMap(new Func1<Long, Observable<? extends Post>>() {
//                    @Override
//                    public Observable<? extends Post> call(Long aLong) {
//                        return hackerNewsService.getStoryItem(String.valueOf(aLong));
//                    }
//                }).flatMap(new Func1<Post, Observable<Post>>() {
//                    @Override
//                    public Observable<Post> call(Post post) {
//                        return post.title != null ? Observable.just(post) : Observable.<Post>empty();
//                    }
//                });
//    }
}
