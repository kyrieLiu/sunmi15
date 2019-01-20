package ys.app.pad.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.greenrobot.greendao.query.CursorQuery;

import ys.app.pad.db.GreenDaoUtils;

/**
 * Created by aaa on 2017/5/26.
 */

public class PetProvider extends ContentProvider {

    private static final UriMatcher MATCHER;
    private static final int LOGIN_CODE = 1;

    static {
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI("ys.app.pad.provider.petProvider", "login", LOGIN_CODE);
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (MATCHER.match(uri)) {
            case LOGIN_CODE:
                CursorQuery greendao = GreenDaoUtils.getmDaoSession().getLoginInfoDao().queryBuilder().buildCursor();
                return greendao.query();
//                return db.query("person", projection, selection, selectionArgs, null, null, sortOrder);

            default:
                throw new IllegalArgumentException("Unknow Uri:" + uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case LOGIN_CODE:
                return "vnd.android.cursor.dir/login";
            default:
                throw new IllegalArgumentException("Unkwon Uri:" + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
