package com.apps.disti.hackernews.mvp.view;

import com.apps.disti.hackernews.data.model.Comment;
import com.apps.disti.hackernews.data.model.Post;
import com.apps.disti.hackernews.mvp.view.base.BaseView;

import java.util.List;

public interface CommentActivityView extends BaseView{

    void addComment(Comment comment);

}
