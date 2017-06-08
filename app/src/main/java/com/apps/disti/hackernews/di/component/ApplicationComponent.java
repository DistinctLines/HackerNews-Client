package com.apps.disti.hackernews.di.component;

        import android.app.Application;
        import android.content.Context;

        import com.apps.disti.hackernews.data.DataManager;
        import com.apps.disti.hackernews.data.local.DatabaseHelper;
        import com.apps.disti.hackernews.data.local.PreferencesHelper;
        import com.apps.disti.hackernews.data.remote.HackerNewsService;
        import com.apps.disti.hackernews.di.module.ApplicationModule;
        import com.apps.disti.hackernews.mvp.ui.activities.CommentsActivity;
        import com.apps.disti.hackernews.mvp.ui.activities.MainActivity;

        import javax.inject.Singleton;

        import dagger.Component;
        import rx.Scheduler;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
    void inject(CommentsActivity commentsActivity);

    Application getApplication();
    DataManager getDataManager();
    Context getContext();

}
