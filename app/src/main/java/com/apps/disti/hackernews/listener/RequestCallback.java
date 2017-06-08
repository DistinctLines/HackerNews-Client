package com.apps.disti.hackernews.listener;

public interface RequestCallback<T> {

    void beforeRequest();

    void success(T data);

    void onError(String data);

}
