package com.apps.disti.hackernews.mvp.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.disti.hackernews.HackerNewsApplication;
import com.apps.disti.hackernews.data.DataManager;

import butterknife.ButterKnife;

import static android.support.v7.appcompat.R.styleable.View;

public abstract class BaseFragment<T> extends Fragment {

    protected T presenter;

    private View fragmentView;

    protected DataManager dataManager;

    public abstract void initInjector();

    public abstract void initViews(View view);

    public abstract int getLayoutId();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        dataManager = HackerNewsApplication.get(getContext()).getApplicationComponent().getDataManager();

        initInjector();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup continer, @Nullable Bundle savedInstanceState){

        View fragmentView = inflater.inflate(getLayoutId(), continer, false);
        ButterKnife.bind(this, fragmentView);

        initViews(fragmentView);

        return fragmentView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        if(presenter != null)
//            presenter.onDestroy();


    }

}
