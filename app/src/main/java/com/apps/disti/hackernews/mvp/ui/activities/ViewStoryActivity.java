package com.apps.disti.hackernews.mvp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.apps.disti.hackernews.HackerNewsApplication;
import com.apps.disti.hackernews.R;
import com.apps.disti.hackernews.data.DataManager;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.mvp.ui.activities.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;

public class ViewStoryActivity extends BaseActivity{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.progress_indicator)
    LinearLayout progressBar;

    @Bind(R.id.web_view)
    WebView webView;

    @Bind(R.id.layout_story)
    RelativeLayout relativeLayout;

    public static final String EXTRA_POST = "com.apps.disti.hackernews.mvp.ui.activities.WebPageActivity.EXTRA_POST";
    private static final String KEY_PDF = "pdf";
    private static final String URL_GOOGLE_DOCS = "http://docs.google.com/gview?embedded=true&url=";
    private static final String URL_PLAY_STORE = "https://play.google.com/store/apps/details?id=com.hitherejoe.hackernews&hl=en_GB";

    private Post post;
    private DataManager dataManager;
    private List<Subscription> subscriptionList;

    public static Intent getStartIntent(Context context, Post post){
        Intent intent = new Intent(context, ViewStoryActivity.class);
        intent.putExtra(EXTRA_POST, post);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        post = bundle.getParcelable(EXTRA_POST);

        dataManager = HackerNewsApplication.get(this).getApplicationComponent().getDataManager();
        subscriptionList = new ArrayList<>();

        setupToolbar();
        setupWebView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_view_story;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews() {

    }

    private void setupToolbar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(post.title);
        }
    }

    private void setupWebView(){
        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress){
                if(progress == 100) progressBar.setVisibility(ProgressBar.GONE);
            }
        });
        webView.setWebViewClient(new ProgressWebViewClient());
        webView.setInitialScale(1);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        if (post.postType == Post.PostType.STORY) {
            String strippedUrl = post.url.split("\\?")[0].split("#")[0];
            webView.loadUrl(strippedUrl.endsWith(KEY_PDF) ? URL_GOOGLE_DOCS + post.url : post.url);
        } else {
            webView.loadUrl(post.url);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class ProgressWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String page){
            progressBar.setVisibility(ProgressBar.GONE);
        }
    }
}
