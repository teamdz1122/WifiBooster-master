package com.binhdz.wifibooster.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by admin on 1/6/2018.
 */

public class ItemClickSupport {
    public static final String TAG = "noteeverything";
    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private OnLongItemClickListener mOnItemLongClickListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), view,holder);
            }
        }
    };
    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
                mOnItemLongClickListener.onLongItemClicked(mRecyclerView, holder.getAdapterPosition(), view,holder);
            }
            return true;
        }
    };
    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
                view.setOnLongClickListener(mOnLongClickListener);
            }

        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    public ItemClickSupport(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        mRecyclerView.setTag(this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);

    }

    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public ItemClickSupport setOnLongClickListener(OnLongItemClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    public interface OnItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int position, View v, RecyclerView.ViewHolder holder);
    }

    public interface OnLongItemClickListener {
        void onLongItemClicked(RecyclerView recyclerView, int posotion, View v, RecyclerView.ViewHolder holder);
    }
}
