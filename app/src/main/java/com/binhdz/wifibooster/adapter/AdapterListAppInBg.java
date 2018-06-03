package com.binhdz.wifibooster.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

public class AdapterListAppInBg extends RecyclerView.Adapter<AdapterListAppInBg.AppInBgViewHolder> {
    private Context mContext;
    private ArrayList<AppInfo> arrAppInfos;

    public AdapterListAppInBg(Context mContext) {
        this.mContext = mContext;
        arrAppInfos = new ArrayList<>();
    }

    public void setUpData(ArrayList<AppInfo> marrAppInfos) {
        this.arrAppInfos.clear();
        this.arrAppInfos.addAll(marrAppInfos);

        Collections.sort(this.arrAppInfos, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo s1, AppInfo s2) {
                if (s1.getPkgSize() < s2.getPkgSize()) {
                    return 1;
                } else {
                    if (s1.getPkgSize() == s2.getPkgSize()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }

            }
        });
        notifyDataSetChanged();
    }

    @Override
    public AppInBgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adapter_app_in_background, parent, false);
        AppInBgViewHolder viewHolder = new AppInBgViewHolder(rootView);
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(AppInBgViewHolder holder, int position) {
        Drawable appIcon = arrAppInfos.get(position).getAppIcon();
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
        holder.tvName.setText(arrAppInfos.get(position).getAppName());
        holder.tvDes.setText("Memory : " + StorageUtil.convertStorage(arrAppInfos.get(position).getPkgSize()));
        if (arrAppInfos.get(position).isUserApp()) {
            //   holder.tvStop.setBackground(mContext.getResources().getDrawable(R.drawable.border_text_not_stop));
            holder.tvStop.setBackground(mContext.getResources().getDrawable(R.drawable.border_text_not_stop));
        } else {
            holder.tvStop.setBackground(mContext.getResources().getDrawable(R.drawable.border_text_stop));
        }

    }


    @Override
    public int getItemCount() {
        return arrAppInfos.size();
    }

    class AppInBgViewHolder extends RecyclerView.ViewHolder {
        private MyTextView tvName, tvDes, tvStop;
        private ImageView ivIcon;

        public AppInBgViewHolder(View itemView) {
            super(itemView);
            tvName = (MyTextView) itemView.findViewById(R.id.adapter_app_in_background__tv_name_app);
            tvDes = (MyTextView) itemView.findViewById(R.id.adapter_app_in_background__tv_des);
            tvStop = (MyTextView) itemView.findViewById(R.id.adapter_app_in_background__tv_stop);
            ivIcon = (ImageView) itemView.findViewById(R.id.adapter_app_in_background__iv_icon);

            tvStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!arrAppInfos.get(getAdapterPosition()).isUserApp()) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + arrAppInfos.get(getAdapterPosition()).getPackName()));
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
