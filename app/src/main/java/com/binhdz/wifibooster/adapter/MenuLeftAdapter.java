package com.binhdz.wifibooster.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.model.MenuModel;

import java.util.List;

/**
 * Created by admin on 3/29/2018.
 */

public class MenuLeftAdapter extends BaseAdapter {
    private List<MenuModel> listModel;
    private Context context;

    public MenuLeftAdapter(List<MenuModel> listModel, Context context) {
        this.listModel = listModel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listModel.size();
    }

    @Override
    public Object getItem(int position) {
        return listModel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View conView, ViewGroup parent) {
        final ViewHolder holder;
        if (conView == null) {
            conView = View.inflate(context, R.layout.item_left_draw, null);

            holder = new ViewHolder();
            holder.name = (TextView) conView.findViewById(R.id.name);
            holder.icon = (ImageView) conView.findViewById(R.id.icon);

            conView.setTag(holder);
        } else {
            holder = (ViewHolder) conView.getTag();
        }
        MenuModel mdl = listModel.get(position);
        if (mdl != null) {
            holder.name.setText(mdl.getName());
            holder.icon.setImageResource(mdl.getIcon());

        }

        return conView;
    }

    private class ViewHolder {
        private TextView name;
        private ImageView icon;
    }
}