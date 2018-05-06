package com.example.yungui.zhifeiji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyNews;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewItemClickListener;

import java.util.ArrayList;

/**
 * Created by yungui on 2017/2/14.
 */

public class ZhuHuDailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<ZhiHuDailyNews.Question> questionList = new ArrayList<>();
    private onRecycleViewItemClickListener itemClickListener;
    private LayoutInflater inflater;

    //布局类型
    //文字+图片
    private static final int TYPE_NORMAL = 0;
    //footer,加载更多
    private static final int TYPE_FOOTER = 1;

    public ZhuHuDailyAdapter(Context context, ArrayList<ZhiHuDailyNews.Question> questionList) {
        this.mContext = context;
        this.questionList = questionList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //更具viewtype的类型加载不同的布局
        switch (viewType) {
            case TYPE_NORMAL:
                //加载文字+图片布局
                return new normalViewHolder(inflater.inflate(R.layout.home_list_layout, parent, false), itemClickListener);
            case TYPE_FOOTER:
                //加载更多布局
                return new myFooterViewHolder(inflater.inflate(R.layout.home_list_footer, parent, false));

        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {
        //如果列表滑动到最底部则加载更多布局
        if (position == questionList.size()) {
            return ZhuHuDailyAdapter.TYPE_FOOTER;
        }
        return ZhuHuDailyAdapter.TYPE_NORMAL;
    }

    /*
    判断viewholder的类型,然后绑定数据
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //如果是普通的item项
        if (holder instanceof ZhuHuDailyAdapter.normalViewHolder) {
            ZhiHuDailyNews.Question item = questionList.get(position);
            //如果第一张图片为空，则设置默认的图片
            if (item.getImages().get(0) == null) {
                ((normalViewHolder) holder).imageView.setImageResource(R.drawable.ic_blank_image);
            } else {
                Glide.with(mContext)
                        .load(item.getImages().get(0))
                        .asBitmap()
                        .placeholder(R.drawable.ic_blank_image)
                        //缓存
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        //加载出错是显示的图片
                        .error(R.drawable.ic_blank_image)
                        .centerCrop()
                        .into(((normalViewHolder) holder).imageView);


            }
            ((normalViewHolder) holder).textView.setText(item.getTitle());

        }

    }

    /**
     * 应为含有footer所以count+1
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return questionList.size() + 1;
    }


    /*
    由于recycleAdapter没有提供默认的点击监听器所以自己设置

     */
    public void setItemClickListener(onRecycleViewItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private class normalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView;
        private onRecycleViewItemClickListener listener;

        public normalViewHolder(View itemView, onRecycleViewItemClickListener listener) {

            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_iv);
            textView = (TextView) itemView.findViewById(R.id.item_tv);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.OnItemClickListener(getLayoutPosition(), view);
            }
        }
    }


    private class myFooterViewHolder extends RecyclerView.ViewHolder {

        public myFooterViewHolder(View itemView) {
            super(itemView);
        }
    }


}
