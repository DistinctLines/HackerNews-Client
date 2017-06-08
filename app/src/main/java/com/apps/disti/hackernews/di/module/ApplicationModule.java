package com.apps.disti.hackernews.di.module;

import android.app.Application;
import android.content.Context;

import com.apps.disti.hackernews.data.DataManager;
import com.apps.disti.hackernews.data.remote.HackerNewsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    protected final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application getApplication(){
        return this.application;
    }

    @Provides
    @Singleton
    DataManager getDataManager(){
        return new DataManager(application);
    }

    @Provides
    @Singleton
    Context getContext() { return this.application; }

}
