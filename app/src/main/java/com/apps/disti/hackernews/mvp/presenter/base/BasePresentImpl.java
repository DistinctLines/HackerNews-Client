package com.apps.disti.hackernews.mvp.presenter.base;

import com.apps.disti.hackernews.listener.RequestCallback;
import com.apps.disti.hackernews.mvp.view.base.BaseView;

import rx.Subscription;

public class BasePresentImpl<T extends BaseView, E> implements BasePresenter, RequestCallback<E>{

    protected T view;
    protected Subscription subscription;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    @Override
    public void attachView(BaseView view) {
        this.view = (T) view;
    }

    @Override
    public void beforeRequest() {

    }

    @Override
    public void success(E data) {

    }

    @Override
    public void onError(String data) {

    }
}
