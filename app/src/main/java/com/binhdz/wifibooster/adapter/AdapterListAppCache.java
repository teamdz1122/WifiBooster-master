package com.binhdz.wifibooster.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.model.AppInfo;
import com.binhdz.wifibooster.util.StorageUtil;
import com.binhdz.wifibooster.view.MyTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by admin on 1/6/2018.
 */

public class AdapterListAppCache extends RecyclerView.Adapter<AdapterListAppCache.CacheAppViewHolder> {
    private Context mContext;
    private ArrayList<AppInfo> arrAppInfoUse;
    private View rootView;

    public AdapterListAppCache(Context mContext) {

        this.mContext = mContext;
        arrAppInfoUse = new ArrayList<>();

    }

    public void setUpListApp(ArrayList<AppInfo> arrAppInfo) {

        for (AppInfo appInfo : arrAppInfo) {
            if (appInfo.isUserApp() == true && appInfo.getCacheSize() > 12 * 1024)
                arrAppInfoUse.add(appInfo);
        }
        Collections.sort(arrAppInfoUse, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo a1, AppInfo a2) {
                if (a1.getCacheSize() > a2.getCacheSize()) {
                    return -1;
                } else {
                    if (a1.getCacheSize() == a2.getCacheSize()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public CacheAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.adapter_net_cache, parent, false);
        CacheAppViewHolder cacheAppViewHolder = new CacheAppViewHolder(rootView);
        return cacheAppViewHolder;
    }

    @Override
    public void onBindViewHolder(CacheAppViewHolder holder, int position) {
        if (arrAppInfoUse.get(position).isUserApp() == true) {
            Drawable appIcon = arrAppInfoUse.get(position).getAppIcon();
            Bitmap bitmap;
            if (appIcon instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) appIcon).getBitmap();
            } else {
                Bitmap bm = Bitmap.createBitmap(appIcon.getIntrinsicWidth(), appIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bm);
                appIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                appIcon.draw(canvas);
                bitmap = bm;
            }

            holder.ivIcon.setImageBitmap(bitmap);
            if (!arrAppInfoUse.get(position).isCkeck()) {
                holder.ivCkeck.setImageResource(R.drawable.ic_check_app_on);
            } else {
                holder.ivCkeck.setImageResource(R.drawable.ic_check_app_off);
            }
            holder.tvCachSize.setText(StorageUtil.convertStorage(arrAppInfoUse.get(position).getCacheSize()));
            holder.tvName.setText(arrAppInfoUse.get(position).getAppName());
        }
    }

    @Override
    public int getItemCount() {

        return arrAppInfoUse.size();
    }

    class CacheAppViewHolder extends RecyclerView.ViewHolder {
        private MyTextView tvName, tvCachSize;
        private ImageView ivCkeck, ivIcon;

        public CacheAppViewHolder(View itemView) {
            super(itemView);

            tvName = (MyTextView) itemView.findViewById(R.id.item_net_cache__tv_name);
            tvCachSize = (MyTextView) itemView.findViewById(R.id.item_net_cache__tv_cahce_size);

            ivCkeck = (ImageView) itemView.findViewById(R.id.item_net_cache__iv_ckeck);
            ivIcon = (ImageView) itemView.findViewById(R.id.item_net_cache__icon_app);
        }
    }
}
