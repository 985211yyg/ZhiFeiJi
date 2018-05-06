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
import com.example.yungui.zhifeiji.bean.guokr.GuoKrStory;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewItemClickListener;

import java.util.ArrayList;

/**
 * Created by yungui on 2017/3/22.
 */

public class GuoKrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<GuoKrStory.ResultBean> resultBeanArrayList;
    private onRecycleViewItemClickListener itemClickListener;
    private LayoutInflater layoutInflater;

    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_NORMAL = 2;


    public GuoKrAdapter(Context context, ArrayList<GuoKrStory.ResultBean> resultBeanArrayList) {

        this.context = context;
        this.resultBeanArrayList = resultBeanArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                return new normalViewHolder(layoutInflater.inflate(R.layout.home_list_layout, parent, false), itemClickListener);
            case TYPE_FOOTER:
                return new myFooterViewHolder(layoutInflater.inflate(R.layout.home_list_footer, parent, false));
        }
        return null;
    }
  /*
  绑定数据
   */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof normalViewHolder) {
            GuoKrStory.ResultBean resultBean = resultBeanArrayList.get(position);
            ((normalViewHolder) holder).textView.setText(resultBeanArrayList.get(position).getTitle());
            if (resultBean.getImages().size()!=0) {
                Glide.with(context)
                        .load(resultBeanArrayList.get(position).getImages().get(0))
                        .asBitmap()
                        .error(R.drawable.ic_blank_image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_blank_image)
                        .into(((normalViewHolder) holder).imageView);
            } else {
                ((normalViewHolder) holder).imageView.setImageResource(R.drawable.ic_blank_image);
            }
        }
    }
  /*
  根据item的数量那个决定，item layout 的类型
   */
    @Override
    public int getItemViewType(int position) {
        //加载底部布局
        if (position == resultBeanArrayList.size()) {
            return TYPE_FOOTER;

        }
        return TYPE_NORMAL;
    }
    /*
    返回有多少个item数据
     */
    @Override
    public int getItemCount() {
        //还有底部布局
        return resultBeanArrayList.size()+1;
    }
    //外面传入接口
    public void setOnRecycleViewItemClickListener(onRecycleViewItemClickListener recycleViewItemClickListener) {
        this.itemClickListener = recycleViewItemClickListener;
    }
    /*
    正常的布局
     */
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

   /*
    底部布局
    */
    private class myFooterViewHolder extends RecyclerView.ViewHolder {

        public myFooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
