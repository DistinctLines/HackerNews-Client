package com.apps.disti.hackernews.data;

import android.content.Context;

import com.apps.disti.hackernews.HackerNewsApplication;
import com.apps.disti.hackernews.data.local.DatabaseHelper;
import com.apps.disti.hackernews.data.local.PreferencesHelper;
import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.data.model.User;
import com.apps.disti.hackernews.data.remote.HackerNewsService;
import com.apps.disti.hackernews.di.component.DaggerDataManagerComponent;
import com.apps.disti.hackernews.di.module.DataManagerModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

public class DataManager {

    public static final int MAX_ITEMS = 10;

    @Inject protected HackerNewsService mHackerNewsService;
    @Inject protected DatabaseHelper mDatabaseHelper;
    @Inject protected PreferencesHelper mPreferencesHelper;
    @Inject protected Scheduler mSubscribeScheduler;

    public DataManager(Context context) {
        injectDependencies(context);
    }

    /* This constructor is provided so we can set up a DataManager with mocks from unit test.
     * At the moment this is not possible to do with Dagger because the Gradle APT plugin doesn't
     * work for the unit test variant, plus Dagger 2 doesn't provide a nice way of overriding
     * modules */

    public DataManager(HackerNewsService watchTowerService,
                       DatabaseHelper databaseHelper,
                       PreferencesHelper preferencesHelper,
                       Scheduler subscribeScheduler) {
        mHackerNewsService = watchTowerService;
        mDatabaseHelper = databaseHelper;
        mPreferencesHelper = preferencesHelper;
        mSubscribeScheduler = subscribeScheduler;
    }

    protected void injectDependencies(Context context) {
        DaggerDataManagerComponent.builder()
                .applicationComponent(HackerNewsApplication.get(context).getApplicationComponent())
                .dataManagerModule(new DataManagerModule(context))
                .build()
                .inject(this);
    }

    public void setHackerNewsService(HackerNewsService hackerNewsService) {
        mHackerNewsService = hackerNewsService;
    }

    public void setScheduler(Scheduler scheduler) {
        mSubscribeScheduler = scheduler;
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Scheduler getScheduler() {
        return mSubscribeScheduler;
    }

    public Observable<Post> getTopStories() {
        return mHackerNewsService.getTopStories()
                .concatMap(new Func1<List<Long>, Observable<? extends Post>>() {
                    @Override
                    public Observable<? extends Post> call(List<Long> longs) {
                        return getPostsFromIds(longs);
                    }
                });
    }

    public Observable<Post> getBestStories(){
        return mHackerNewsService.getBestStories()
                .concatMap(new Func1<List<Long>, Observable<? extends Post>>() {
                    @Override
                    public Observable<? extends Post> call(List<Long> longs) {
                        return getPostsFromIds(longs);
                    }
                });
    }

    public Observable<Post> getNewStories(){
        return mHackerNewsService.getNewestStories()
                .concatMap(new Func1<List<Long>, Observable<? extends Post>>() {
                    @Override
                    public Observable<? extends Post> call(List<Long> longs) {
                        return getPostsFromIds(longs);
                    }
                });
    }

    public Observable<String> getNames(){
        List list = new ArrayList();
        list.add("Taylor");
        list.add("Houynd");
        list.add("Bat");

        return Observable.from(list)
                .concatMap(new Func1() {
                    @Override
                    public Object call(Object o) {
                        return Observable.just(o.toString());
                    }
                });
    }

    public Observable<Post> getUserPosts(String user) {
        return mHackerNewsService.getUser(user)
                .concatMap(new Func1<User, Observable<? extends Post>>() {
                    @Override
                    public Observable<? extends Post> call(User user) {
                        return getPostsFromIds(user.submitted);
                    }
                });
    }

    public Observable<User> getUserInformation(String user){
        return mHackerNewsService.getUser(user);
    }

    public Observable<Post> getMaxUserPost(){
        return mHackerNewsService.getMaxID()
                .concatMap(new Func1<Long, Observable<? extends Post>>() {
                    @Override
                    public Observable<? extends Post> call(Long aLong) {
                        return getPostFromId(aLong);
                    }
                });
    }

    public Observable<Post> getPostFromId(Long aLong){
        return Observable.just(aLong)
                .concatMap(new Func1<Long, Observable<? extends Post>>() {
                    @Override
                    public Observable<? extends Post> call(Long aLong) {
                        return mHackerNewsService.getStoryItem(String.valueOf(aLong));
                    }
                }).flatMap(new Func1<Post, Observable<Post>>() {
                    @Override
                    public Observable<Post> call(Post post) {
                        return Observable.just(post);
                    }
                });
    }

    public Observable<Post> getPostsFromIds(List<Long> storyIds) {
        return Observable.from(storyIds)
                .concatMap(new Func1<Long, Observable<Post>>() {
                    @Override
                    public Observable<Post> call(Long aLong) {
                        return mHackerNewsService.getStoryItem(String.valueOf(aLong));
                    }
                }).flatMap(new Func1<Post, Observable<Post>>() {
                    @Override
                    public Observable<Post> call(Post post) {
                        return post.title != null ? Observable.just(post) : Observable.<Post>empty();
                    }
                });
    }

    public Observable<Comment> getPostComments(final List<Long> commentIds, final int depth) {
        return Observable.from(commentIds)
                .concatMap(new Func1<Long, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Long aLong) {
                        return mHackerNewsService.getCommentItem(String.valueOf(aLong));
                    }
                }).concatMap(new Func1<Comment, Observable<Comment>>() {
                    @Override
                    public Observable<Comment> call(Comment comment) {
                        comment.depth = depth;
                        if (comment.kids == null || comment.kids.isEmpty()) {
                            return Observable.just(comment);
                        } else {
                            return Observable.just(comment)
                                    .mergeWith(getPostComments(comment.kids, depth + 1));
                        }
                    }
                }).filter(new Func1<Comment, Boolean>() {
                    @Override
                    public Boolean call(Comment comment) {
                        return (comment.by != null && !comment.by.trim().isEmpty()
                                && comment.text != null && !comment.text.trim().isEmpty());
                    }
                });
    }

    public Observable<Post> getBookmarks() {
        return mDatabaseHelper.getBookmarkedStories();
    }

    public Observable<Post> addBookmark(final Context context, final Post story) {
        return doesBookmarkExist(story)
                .flatMap(new Func1<Boolean, Observable<Post>>() {
                    @Override
                    public Observable<Post> call(Boolean doesExist) {
                        if (!doesExist) return mDatabaseHelper.bookmarkStory(story);
                        return Observable.empty();
                    }
                });
    }

    public Observable<Void> deleteBookmark(final Context context, Post story) {
        return mDatabaseHelper.deleteBookmark(story);
    }

    public Observable<Boolean> doesBookmarkExist(Post story) {
        return mDatabaseHelper.doesBookmarkExist(story);
    }

}
