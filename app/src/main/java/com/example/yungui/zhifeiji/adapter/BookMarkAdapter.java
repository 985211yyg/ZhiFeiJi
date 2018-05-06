package com.example.yungui.zhifeiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentNews;
import com.example.yungui.zhifeiji.bean.guokr.GuoKrStory;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyNews;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewItemClickListener;

import java.util.ArrayList;

/**
 * Created by yungui on 2017/3/25.
 */

public class BookMarkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Integer> itemTypes = new ArrayList<>();
    private ArrayList<ZhiHuDailyNews.Question> zhiHuList = new ArrayList<>();
    private ArrayList<DouBanMomentNews.PostsBean> douBanList = new ArrayList<>();
    private ArrayList<GuoKrStory.ResultBean> guoKrList = new ArrayList<>();

    private LayoutInflater layoutInflater;

    private onRecycleViewItemClickListener itemClickListener;

    public static final int TYPE_ZhiHuiHeader = 0;
    public static final int TYPE_GuoKrHeader = 1;
    public static final int TYPE_DouBanHeader = 2;

    public static final int TYPE_ZhiHuiItem = 3;
    public static final int TYPE_GuoKrItem = 4;
    public static final int TYPE_DouBanItem = 5;

    public static final String TAG = BookMarkAdapter.class.getSimpleName();

    public BookMarkAdapter(Context context, ArrayList<DouBanMomentNews.PostsBean> douBanList,
                           ArrayList<GuoKrStory.ResultBean> guoKrList,
                           ArrayList<ZhiHuDailyNews.Question> zhiHuList,
                           ArrayList<Integer> itemType) {
        this.context = context;
        this.douBanList = douBanList;
        this.guoKrList = guoKrList;
        this.zhiHuList = zhiHuList;
//        itemType.size() = douBanList.size() + guoKrList.size() + zhiHuList.size();
        this.itemTypes = itemType;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ZhiHuiHeader
                || viewType == TYPE_GuoKrHeader
                || viewType == TYPE_DouBanHeader) {

            return new HeaderViewHolder(layoutInflater.inflate(R.layout.bookmark_item_type, parent, false));
        } else {

            return new NormalViewHolder(layoutInflater.inflate(R.layout.home_list_layout, parent, false), itemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (itemTypes.get(position)) {
            case TYPE_ZhiHuiHeader:
                ((HeaderViewHolder) holder).textView.setText("知乎日报");
                break;

            case TYPE_GuoKrHeader:
                ((HeaderViewHolder) holder).textView.setText("果壳精选");

                break;


            case TYPE_DouBanHeader:
                ((HeaderViewHolder) holder).textView.setText("豆瓣时刻");
                break;

            case TYPE_ZhiHuiItem:
                ((NormalViewHolder) holder).tv.setText(zhiHuList.get(position - 1).getTitle());

                if (zhiHuList.get(position - 1).getImages().size() != 0) {
                    Glide.with(context)
                            .load(zhiHuList.get(position - 1).getImages().get(0))
                            .asBitmap()
                            .placeholder(R.drawable.error_image)
                            .centerCrop()
                            .error(R.drawable.error_image)
                            .into(((NormalViewHolder) holder).iv);
                }
                break;

            case TYPE_GuoKrItem:
                ((NormalViewHolder) holder).tv.setText(guoKrList.get(position - 2- zhiHuList.size()).getTitle());

                if (guoKrList.get(position - 2 - zhiHuList.size()).getImages().size() != 0) {
                    Glide.with(context)
                            .load(guoKrList.get(position - 2 - zhiHuList.size()).getImages().get(0))
                            .asBitmap()
                            .placeholder(R.drawable.error_image)
                            .centerCrop()
                            .error(R.drawable.error_image)
                            .into(((NormalViewHolder) holder).iv);
                }
                break;

            case TYPE_DouBanItem:
                ((NormalViewHolder) holder).tv.setText(douBanList.get(position -  zhiHuList.size() - guoKrList.size()-3).getTitle());

                if (douBanList.get(position -  zhiHuList.size() - guoKrList.size()-3).getThumbs().size() != 0) {
                    Glide.with(context)
                            .load(douBanList.get(position -  zhiHuList.size() -guoKrList.size()- 3).getThumbs().get(0).getMedium().getUrl())
                            .asBitmap()
                            .placeholder(R.drawable.error_image)
                            .centerCrop()
                            .error(R.drawable.error_image)
                            .into(((NormalViewHolder) holder).iv);
                }

                break;


        }

    }

    /*
    根据position判读view的类型并返回类型值viewType
     */

    @Override
    public int getItemViewType(int position) {
        return itemTypes.get(position);
    }

    @Override
    public int getItemCount() {

        return itemTypes.size();
    }

    public void setOnItemClickListener(onRecycleViewItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private onRecycleViewItemClickListener listener;
        private TextView tv;
        private ImageView iv;
        private View view;

        public NormalViewHolder(View itemView, onRecycleViewItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            itemView.setOnClickListener(this);
            view = itemView;
            tv = (TextView) itemView.findViewById(R.id.item_tv);
            iv = (ImageView) itemView.findViewById(R.id.item_iv);
        }

        @Override
        public void onClick(View view) {
            //回调接口，接点击的item和view传递出去
            itemClickListener.OnItemClickListener(getLayoutPosition(), view);
        }
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.bookmark_item_withType);
        }

        @Override
        public void onClick(View view) {

        }
    }


}
