package com.apps.disti.hackernews;

import android.support.test.InstrumentationRegistry;

import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.listener.RequestCallback;
import com.apps.disti.hackernews.mvp.interactor.StoryListInteractor;
import com.apps.disti.hackernews.mvp.interactor.impl.StoryListInteractorImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.Assert.*;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;

public class StoryListInteractorTest {

    private static final String USER = "USERNAME";
    StoryListInteractor storyListInteractor;

    @Before
    public void setUp(){
        storyListInteractor = new StoryListInteractorImpl(InstrumentationRegistry.getContext());
    }

    @Test
    public void testGetTopStories() throws Exception {
        TestSubscriber<Post> subscriber = TestSubscriber.create();
        Subscription subscription = storyListInteractor.getTopStories(new RequestCallback() {
            @Override
            public void beforeRequest() {

            }

            @Override
            public void success(Object data) {

            }

            @Override
            public void onError(String data) {

            }
        });
        subscriber.add(subscription);
        subscriber.assertNoErrors();
        subscriber.assertCompleted();
        Assert.assertThat(subscriber.getOnNextEvents().get(0).title, is(USER)) ;

    }

}
