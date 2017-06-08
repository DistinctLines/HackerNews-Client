package com.apps.disti.hackernews.mvp.presenter.base;

import com.apps.disti.hackernews.mvp.view.base.BaseView;

public interface BasePresenter {

    void onCreate();

    void onDestroy();

    void attachView(BaseView view);

}
