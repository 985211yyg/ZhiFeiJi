package com.example.yungui.zhifeiji.adapter.commonAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yungui on 2017/2/14.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    //通过ID获取view
    public <T extends View> T getView(int ViewId) {
        //先从缓存中找view
        View view = mViews.get(ViewId);
        //如果没有则通过findViewById
        if (view == null) {
            view = itemView.findViewById(ViewId);
            mViews.put(ViewId,view);
        }
        //返回view
        return (T) view;
    }

    //设置TextView的文本
    public ViewHolder setText(int ViewId, CharSequence text) {
        TextView textView = getView(ViewId);
        textView.setText(text);
        return this;
    }

    //设置ImageView的资源
    public ViewHolder setImage(int ViewId, int resourceId) {
        ImageView imageView = getView(ViewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    //设置view的可见性
    public ViewHolder setVisibility(int ViewId, int Visibility) {
        getView(ViewId).setVisibility(Visibility);
        return this;
    }


    //设置条目的单击事件
    public void setOnItemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);

    }

    //设置体哦啊母的长按事件
    public void setOnItemLongClickListener(View.OnLongClickListener longClickListener) {
        itemView.setOnLongClickListener(longClickListener);

    }

    //通过路径加载图片
    public ViewHolder setImageByUri(int viewId, HolderImageLoader holderImageLoader) {
        ImageView imageView = getView(viewId);
        if (holderImageLoader == null) {
            throw new NullPointerException("holderImageLoader is null");

        }
        holderImageLoader.displayImage(imageView.getContext(), imageView, holderImageLoader.getImagePath());
        return this;
    }

    //图片加载
    public static abstract class HolderImageLoader {
        private String imagePath;

        public HolderImageLoader(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }

        public abstract void displayImage(Context context, ImageView imageView, String imagePath);
    }
}

