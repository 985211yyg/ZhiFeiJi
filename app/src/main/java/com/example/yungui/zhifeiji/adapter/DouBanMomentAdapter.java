package com.example.yungui.zhifeiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentNews;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewItemClickListener;

import java.util.ArrayList;

/**
 * Created by yungui on 2017/3/21.
 */

public class DouBanMomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<DouBanMomentNews.PostsBean> photosBean;

    private static final int VIEWTYPE_NORMAL = 0x1;
    private static final int VIEWTYPE_NOIMAGE = 0x2;
    private static final int VIEWTYPE_FOOTER = 0x3;

    private LayoutInflater layoutInflater;
    //回调接口
    private onRecycleViewItemClickListener recycleViewItemClickListener;

    public static final String TAG = DouBanMomentAdapter.class.getSimpleName();

    public DouBanMomentAdapter(Context context, ArrayList<DouBanMomentNews.PostsBean> photosBean) {

        this.photosBean = photosBean;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);


    }

    //f返回布局
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {

            case VIEWTYPE_NOIMAGE:
                return new NoImageViewHolder(layoutInflater.inflate(R.layout.home_list_without_image, parent, false),recycleViewItemClickListener);
            case VIEWTYPE_FOOTER:
                return new FooterViewHolder(layoutInflater.inflate(R.layout.home_list_footer, parent, false));
            case VIEWTYPE_NORMAL:
                return new NormalViewHolder(layoutInflater.inflate(R.layout.home_list_layout, parent, false),recycleViewItemClickListener);
        }
        return null;
    }

    /*
    绑定数据
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (!(holder instanceof FooterViewHolder)) {
            DouBanMomentNews.PostsBean bean = photosBean.get(position);

            if (holder instanceof NormalViewHolder) {

            Glide.with(context)
                    .load(bean.getThumbs().get(0).getMedium().getUrl())
                    .asBitmap()
                    .placeholder(R.drawable.ic_blank_image)
                    .centerCrop()
                    .error(R.drawable.ic_blank_image)
                    .into(((NormalViewHolder) holder).imageView);

                ((NormalViewHolder) holder).textTitle.setText(bean.getTitle());

            } else if (holder instanceof NoImageViewHolder) {
                ((NoImageViewHolder) holder).textView.setText(bean.getTitle());

            }
        }


    }

    @Override
    public int getItemCount() {
        //footer添加进去
        return photosBean.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        //当移动到列表底部是，返回footer布局
        if (photosBean.size() == position) {
            return VIEWTYPE_FOOTER;
        }
        //如果thumbs中没有图片，则返回无图布局
        if (photosBean.get(position).getThumbs().size() == 0) {
            return VIEWTYPE_NOIMAGE;
        }
        return VIEWTYPE_NORMAL;
    }


    //设置点击事件
    public void setOnRecycleViewItemClickListener(onRecycleViewItemClickListener onRecycleViewItemClickListener) {
        //实现外部接口
        this.recycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textTitle;
        private onRecycleViewItemClickListener ItemClickListener;

        public NormalViewHolder(View itemView,onRecycleViewItemClickListener listener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_iv);
            textTitle = (TextView) itemView.findViewById(R.id.item_tv);
            this.ItemClickListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (ItemClickListener != null) {
                ItemClickListener.OnItemClickListener(getLayoutPosition(),view);

            }

        }
    }

    class NoImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private onRecycleViewItemClickListener onRecycleViewItemClickListener;
        public NoImageViewHolder(View itemView, onRecycleViewItemClickListener recycleViewItemClickListener) {
            super(itemView);
            onRecycleViewItemClickListener = recycleViewItemClickListener;
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.item_tv_no_image);
        }

        @Override
        public void onClick(View view) {
            if (onRecycleViewItemClickListener != null) {
                onRecycleViewItemClickListener.OnItemClickListener(getLayoutPosition(), view);
            }

        }

    }
    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

    }

}
