package com.binhdz.wifibooster.loadcache;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.binhdz.wifibooster.callback.CompleteClearCache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by admin on 1/6/2018.
 */

public class AsynCleanCache extends AsyncTask<Void, Void, Void> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private CompleteClearCache mCompleteClearCache;

    public AsynCleanCache(Context mContext, CompleteClearCache mCompleteClearCache) {
        this.mContext = mContext;
        this.mCompleteClearCache = mCompleteClearCache;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCompleteClearCache.startClearCache();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            clearCache();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mCompleteClearCache != null) {
            mCompleteClearCache.completeClear();

        }

    }

    private static final long ALL_YOUR_CACHE_ARE_BELONG_TO_US = Long.MAX_VALUE;
    private CachePackageDataObserver mClearCacheObserver;

    private final void clearCache() {
        if (mClearCacheObserver == null) {
            mClearCacheObserver = new CachePackageDataObserver();
        }
        PackageManager mPM = mContext.getPackageManager();

        final Class[] classes = {Long.TYPE, IPackageDataObserver.class};

        Long localLong = Long.valueOf(ALL_YOUR_CACHE_ARE_BELONG_TO_US);
        try {
            Method localMethod = mPM.getClass().getMethod("freeStorageAndNotify", classes);

            try {
                localMethod.invoke(mPM, localLong, mClearCacheObserver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        }
    }

    private class CachePackageDataObserver extends
            IPackageDataObserver.Stub {
        public void onRemoveCompleted(String packageName, boolean succeeded) {

        }
    }

    public interface ChangeUI {
        void changeUI(String text);
    }
}
