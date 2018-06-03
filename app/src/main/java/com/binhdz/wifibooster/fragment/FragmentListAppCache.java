package com.binhdz.wifibooster.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.adapter.AdapterListAppCache;
import com.binhdz.wifibooster.callback.CompleteClearCache;
import com.binhdz.wifibooster.loadcache.AsynCleanCache;
import com.binhdz.wifibooster.model.AppInfo;
import com.binhdz.wifibooster.util.ItemClickSupport;
import com.binhdz.wifibooster.util.StorageUtil;
import com.binhdz.wifibooster.view.MyTextView;
import com.binhdz.wifibooster.view.MyTextViewBold;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2018.
 */

public class FragmentListAppCache extends Fragment implements View.OnClickListener {
    private View rootView;
    private MyTextViewBold tvNetWorkBoots;
    private MyTextView tvAppCount, tvCacheSystem, tvSizeCacheSystem;
    private ImageView ivCkeckCacheSystem;
    private RecyclerView rcvListApp;
    private boolean isCkeckCacheSystem;
    private AdapterListAppCache mAdapterListAppCache;
    private CompleteClearCache mCompleteClearCache;

    public FragmentListAppCache() {
    }

    public void setCallback(CompleteClearCache completeClearCache) {
        mCompleteClearCache = completeClearCache;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_app_cache, container, false);
        initViews();
        return rootView;
    }

    private void initViews() {
        isCkeckCacheSystem = true;

        tvAppCount = (MyTextView) rootView.findViewById(R.id.fr_list_app__tv_count_app);
        tvCacheSystem = (MyTextView) rootView.findViewById(R.id.fr_list_app__tv_cache_system);
        tvSizeCacheSystem = (MyTextView) rootView.findViewById(R.id.fr_list_app__tv_size_cache_system);

        ivCkeckCacheSystem = (ImageView) rootView.findViewById(R.id.fr_list_app__iv_ckeck_system);

        rcvListApp = (RecyclerView) rootView.findViewById(R.id.fr_list_app__rcv_list_app);
        tvNetWorkBoots = (MyTextViewBold) rootView.findViewById(R.id.fr_list_app__tv_boost);
        tvNetWorkBoots.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rcvListApp.setLayoutManager(layoutManager);

        ItemClickSupport itemClickSupport = new ItemClickSupport(rcvListApp);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v, RecyclerView.ViewHolder holder) {

            }
        });
        itemClickSupport.setOnLongClickListener(null);
        mAdapterListAppCache = new AdapterListAppCache(getActivity());
        rcvListApp.setAdapter(mAdapterListAppCache);
    }


    public void setData(ArrayList<AppInfo> arrAppinfo, long cacheSystemSize) {

        mAdapterListAppCache.setUpListApp(arrAppinfo);

        tvSizeCacheSystem.setText(StorageUtil.convertStorage(cacheSystemSize));


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fr_list_app__iv_ckeck_system:
                if (isCkeckCacheSystem) {
                    ivCkeckCacheSystem.setImageResource(R.drawable.ic_check_app_on);
                } else {
                    ivCkeckCacheSystem.setImageResource(R.drawable.ic_check_app_off);
                }
                break;
            case R.id.fr_list_app__tv_boost:
                AsynCleanCache asynCleanCache = new AsynCleanCache(getActivity(), mCompleteClearCache);
                asynCleanCache.execute();

                break;
        }
    }


}
