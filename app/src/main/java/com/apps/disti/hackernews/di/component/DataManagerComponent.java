package com.apps.disti.hackernews.di.component;

import com.apps.disti.hackernews.data.DataManager;
import com.apps.disti.hackernews.di.module.DataManagerModule;
import com.apps.disti.hackernews.di.scope.PerDataManager;
import com.apps.disti.hackernews.mvp.interactor.impl.StoryListInteractorImpl;

import dagger.Component;

@PerDataManager
@Component(dependencies = ApplicationComponent.class, modules = DataManagerModule.class)
public interface DataManagerComponent {

    void inject(DataManager dataManager);
}
