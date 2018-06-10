package com.binhdz.wifibooster.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.util.CommomUtil;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2018.
 */

public class AdapterListWifi extends RecyclerView.Adapter<AdapterListWifi.WifiViewHolder> {
    private Context mContext;
    private ArrayList<ScanResult> arrListWifi;
    private boolean isTypeNonePass;
    private WifiInfo wifiConect;

    public AdapterListWifi(Context mContext) {
        this.mContext = mContext;
        arrListWifi = new ArrayList<>();
    }

    public void setUpListWifi(ArrayList<ScanResult> arrListWifi, WifiInfo wifiConect, boolean isTypeNonePass) {
        this.arrListWifi.clear();
        this.arrListWifi.addAll(arrListWifi);
        this.isTypeNonePass = isTypeNonePass;
        this.wifiConect = wifiConect;


        notifyDataSetChanged();
    }

    @Override
    public AdapterListWifi.WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_list_wifi, parent, false);

        return new AdapterListWifi.WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterListWifi.WifiViewHolder holder, int position) {
        holder.tvNameWifi.setText(arrListWifi.get(position).SSID);
        holder.icCkeck.setVisibility(View.GONE);
        if (!isTypeNonePass) {
            switch (CommomUtil.getLevelWifi(arrListWifi.get(position).level)) {
                case 1:
                    holder.ivStateWifi.setImageResource(R.drawable.ic_level_wifi_1);
                    break;
                case 2:
                    holder.ivStateWifi.setImageResource(R.drawable.ic_level_wifi_2);
                    break;
                case 3:
                    holder.ivStateWifi.setImageResource(R.drawable.ic_level_wifi_3);
                    break;
                case 4:
                    holder.ivStateWifi.setImageResource(R.drawable.ic_level_wifi_4);
                    break;

            }
        } else {
            if (arrListWifi.get(position).SSID.equals(CommomUtil.getStr(wifiConect.getSSID()))) {
                holder.icCkeck.setVisibility(View.VISIBLE);

            } else {
                holder.icCkeck.setVisibility(View.GONE);
            }
            holder.ivStateWifi.setImageResource(R.drawable.ic_wifi_none_pass);

        }
    }


    @Override
    public int getItemCount() {
        return arrListWifi.size();
    }

    class WifiViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivStateWifi, icCkeck;
        private TextView tvNameWifi;

        public WifiViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view) {
            ivStateWifi = (ImageView) view.findViewById(R.id.adapter_wifi_iv__icon);
            tvNameWifi = (TextView) view.findViewById(R.id.adapter_wifi_tv__name_wifi);
            icCkeck = (ImageView) view.findViewById(R.id.adapter_wifi_iv__ckeck);
        }

    }
}