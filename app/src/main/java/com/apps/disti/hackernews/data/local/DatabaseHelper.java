package com.apps.disti.hackernews.data.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.apps.disti.hackernews.data.model.Post;

import rx.Observable;
import rx.Subscriber;

public class DatabaseHelper {

    private DbOpenHelper mDatabaseOpenHelper;

    public DatabaseHelper(Context context) {
        mDatabaseOpenHelper = new DbOpenHelper(context);
    }

    public SQLiteDatabase getReadableDatabase() {
        return mDatabaseOpenHelper.getReadableDatabase();
    }

    public Observable<Void> deleteBookmark(final Post story) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
                db.delete(Db.BookmarkTable.TABLE_NAME, Db.BookmarkTable.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(story.id)});
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Post> getBookmarkedStories() {
        return Observable.create(new Observable.OnSubscribe<Post>() {
            @Override
            public void call(Subscriber<? super Post> subscriber) {
                SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
                Cursor bookmarkCursor =
                        db.rawQuery("SELECT * FROM " + Db.BookmarkTable.TABLE_NAME, null);
                while (bookmarkCursor.moveToNext()) {
                    subscriber.onNext(Db.BookmarkTable.parseCursor(bookmarkCursor));
                }
                bookmarkCursor.close();
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Post> bookmarkStory(final Post story) {
        return Observable.create(new Observable.OnSubscribe<Post>() {
            @Override
            public void call(Subscriber<? super Post> subscriber) {
                SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
                db.insertOrThrow(Db.BookmarkTable.TABLE_NAME, null, Db.BookmarkTable.toContentValues(story));
                subscriber.onNext(story);
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Boolean> doesBookmarkExist(final Post story) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                SQLiteDatabase db = mDatabaseOpenHelper.getReadableDatabase();
                Cursor bookmarkCursor =
                        db.rawQuery("SELECT * FROM " + Db.BookmarkTable.TABLE_NAME +
                                        " WHERE " + Db.BookmarkTable.COLUMN_ID + " = ?"
                                , new String[]{String.valueOf(story.id)});
                subscriber.onNext(bookmarkCursor.moveToNext());
                bookmarkCursor.close();
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Void> clearBookmarks() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                SQLiteDatabase db = mDatabaseOpenHelper.getWritableDatabase();
                db.beginTransaction();
                try {
                    Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
                    while (cursor.moveToNext()) {
                        db.delete(cursor.getString(cursor.getColumnIndex("name")), null, null);
                    }
                    cursor.close();
                    db.setTransactionSuccessful();
                    subscriber.onCompleted();
                } finally {
                    db.endTransaction();
                }
            }
        });
    }

}
