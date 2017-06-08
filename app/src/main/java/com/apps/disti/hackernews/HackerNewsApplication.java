package com.apps.disti.hackernews;

import android.app.Application;
import android.content.Context;

import com.apps.disti.hackernews.di.component.ApplicationComponent;
import com.apps.disti.hackernews.di.component.DaggerApplicationComponent;
import com.apps.disti.hackernews.di.module.ApplicationModule;

public class HackerNewsApplication extends Application {

    ApplicationComponent applicationComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static HackerNewsApplication get(Context context){
        return (HackerNewsApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent){
        this.applicationComponent = applicationComponent;
    }



}
