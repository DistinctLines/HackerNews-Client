package com.apps.disti.hackernews.data.remote;

import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.data.model.User;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface HackerNewsService {

    String ENDPOINT = "https://hacker-news.firebaseio.com/v0/";

    /**
     * Return a list of the top post IDs.
     */
    @GET("/topstories.json")
    Observable<List<Long>> getTopStories();

    /**
     * Return list of the newest post IDs.
     */
    @GET("/newstories.json")
    Observable<List<Long>> getNewestStories();

    /**
     * Return list of the best post IDs.
     */
    @GET("/beststories.json")
    Observable<List<Long>> getBestStories();

    /**
     * Return a list of a users post IDs.
     */
    @GET("/user/{user}.json")
    Observable<User> getUser(@Path("user") String user);

    /**
     * Return story item.
     */
    @GET("/item/{itemId}.json")
    Observable<Post> getStoryItem(@Path("itemId") String itemId);

    /**
     * Returns a comment item.
     */
    @GET("/item/{itemId}.json")
    Observable<Comment> getCommentItem(@Path("itemId") String itemId);

    @GET("/maxitem.json")
    Observable<Long> getMaxID();

}
