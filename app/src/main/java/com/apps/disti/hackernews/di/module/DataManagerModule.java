package com.apps.disti.hackernews.di.module;

import android.content.Context;

import com.apps.disti.hackernews.data.local.DatabaseHelper;
import com.apps.disti.hackernews.data.local.PreferencesHelper;
import com.apps.disti.hackernews.data.remote.HackerNewsService;
import com.apps.disti.hackernews.data.remote.RetrofitHelper;
import com.apps.disti.hackernews.di.scope.PerDataManager;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.schedulers.Schedulers;

@Module
public class DataManagerModule {

    private final Context context;

    public DataManagerModule(Context context) {
        this.context = context;
    }

    @Provides
    @PerDataManager
    DatabaseHelper provideDatabaseHelper(){
        return new DatabaseHelper(context);
    }

    @Provides
    @PerDataManager
    PreferencesHelper providePreferencesHelper(){
        return new PreferencesHelper(context);
    }

    @Provides
    @PerDataManager
    HackerNewsService provideHackerNewsService(){
        return new RetrofitHelper().newHackerNewsService();
    }

    @Provides
    @PerDataManager
    Scheduler provideSubscribeScheduler(){
        return Schedulers.io();
    }
}
