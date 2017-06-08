package com.apps.disti.hackernews.mvp.view;

import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.mvp.view.base.BaseView;

public interface MainActivityView extends BaseView {

    void addposts(Post post);

}
