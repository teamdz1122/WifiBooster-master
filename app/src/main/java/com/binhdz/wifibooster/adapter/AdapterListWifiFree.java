package com.binhdz.wifibooster.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.callback.ShowBottomDialog;
import com.binhdz.wifibooster.callback.UpdateApdaterView;
import com.binhdz.wifibooster.util.CommomUtil;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2018.
 */

public class AdapterListWifiFree extends RecyclerView.Adapter<AdapterListWifiFree.WifiFreeViewHolder> implements UpdateApdaterView {
    private Context mContext;
    private ArrayList<ScanResult> arrListWifi;
    private WifiInfo wifiConect;
    private ShowBottomDialog mShowBottomDialog;


    public AdapterListWifiFree(Context mContext) {
        this.mContext = mContext;
        arrListWifi = new ArrayList<>();
    }

    public void setUpListWifi(ArrayList<ScanResult> arrListWifis, WifiInfo wifiConect) {
        this.arrListWifi.clear();
        this.arrListWifi.addAll(arrListWifis);
        this.wifiConect = wifiConect;

        notifyDataSetChanged();
    }

    public void setCallBack(ShowBottomDialog mShowBottomDialog) {
        this.mShowBottomDialog = mShowBottomDialog;
    }

    @Override
    public WifiFreeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_wifi_free, parent, false);

        return new WifiFreeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiFreeViewHolder holder, int position) {
        holder.tvNameWifi.setText(arrListWifi.get(position).SSID);

        switch (CommomUtil.getLevelWifi(arrListWifi.get(position).level)) {
            case 1:
                holder.ivStateWifi.setImageResource(R.drawable.ic_level_wifi_connect_1);
                break;
            case 2:
                holder.ivStateWifi.setImageResource(R.drawable.ic_level_wifi_connect_2);
                break;
            case 3:
                holder.ivStateWifi.setImageResource(R.drawable.ic_level_wifi_connect_3);
                break;
            case 4:
                holder.ivStateWifi.setImageResource(R.drawable.ic_level_wifi_connect_4);
                break;
        }

        if (arrListWifi.get(position).SSID.equals(CommomUtil.getStr(wifiConect.getSSID()))) {
            holder.ivCkeck.setVisibility(View.VISIBLE);
            holder.ivMenu.setVisibility(View.GONE);
        } else {
            holder.ivCkeck.setVisibility(View.GONE);
            holder.ivMenu.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return arrListWifi.size();
    }

  /*  public void swapItems(int i, int position) {
        if (arrListWifi.size() >= 2) {

         *//* arrListWifi.get(i).setIsConnect(false);*//*
            Collections.swap(arrListWifi, i, position);

            notifyDataSetChanged();

        }
    }*/

    @Override
    public void updateIconConnect(ArrayList<ScanResult> arrWifi) {
        arrListWifi.clear();
        arrListWifi.addAll(arrWifi);
        notifyDataSetChanged();
    }


    class WifiFreeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivStateWifi, ivCkeck, ivMenu;
        private TextView tvNameWifi, tvState;
        private LinearLayout mLyContent;

        public WifiFreeViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view) {
            ivStateWifi = (ImageView) view.findViewById(R.id.adapter_wifi_free_iv__icon);
            tvNameWifi = (TextView) view.findViewById(R.id.adapter_wifi_free_tv__name_wifi);
            tvState = (TextView) view.findViewById(R.id.adapter_wifi_free_tv__name_state);
            ivCkeck = (ImageView) view.findViewById(R.id.adapter_wifi_free_iv__ckeck);
            ivMenu = (ImageView) view.findViewById(R.id.adapter_wifi_free_iv__menu);
            mLyContent = (LinearLayout) view.findViewById(R.id.adapter_wifi__ly_content);
            mLyContent.setOnClickListener(this);
            ivMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.adapter_wifi_free_iv__menu) {
                mShowBottomDialog.showDialogWifiFree(getAdapterPosition());
            } else if (view.getId() == R.id.adapter_wifi__ly_content) {
                mShowBottomDialog.ClickContent(getAdapterPosition());
            }
        }
    }
}
