package com.apps.disti.hackernews.mvp.ui.activities.base;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.apps.disti.hackernews.HackerNewsApplication;
import com.apps.disti.hackernews.R;
import com.apps.disti.hackernews.di.component.ApplicationComponent;
import com.apps.disti.hackernews.di.component.DaggerApplicationComponent;
import com.apps.disti.hackernews.di.module.ApplicationModule;
import com.apps.disti.hackernews.mvp.presenter.base.BasePresenter;

import butterknife.ButterKnife;
import rx.Subscription;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity{

    protected ApplicationComponent applicationComponent;
    protected T presenter;
    protected Subscription subscription;

    public abstract int getLayoutId();

    public abstract void initInjector();

    public abstract void initViews();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initApplicationComponent();

        setContentView(getLayoutId());
        initInjector();
        ButterKnife.bind(this);

        initToolbar();
        initViews();

        if(presenter != null)
            presenter.onCreate();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void initApplicationComponent(){
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(HackerNewsApplication.get(this)))
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){

            case android.R.id.home:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if(fragmentManager.getBackStackEntryCount() > 0)
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                else
                    finish();
                return true;

            default :
                return super.onOptionsItemSelected(item);
        }
    }

}
