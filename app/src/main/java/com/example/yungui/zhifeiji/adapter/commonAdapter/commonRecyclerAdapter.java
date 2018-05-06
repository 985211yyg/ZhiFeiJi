package com.example.yungui.zhifeiji.adapter.commonAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yungui on 2017/2/14.
 */

public abstract class commonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    //数据
    protected ArrayList<T> mData;
    //布局
    protected int mLayoutId;
    //多布局支持
    protected MultiTypeSupport multiTypeSupport;

    /*
    单一布局
     */
    public commonRecyclerAdapter(Context context, ArrayList<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = layoutId;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    /*
    多布局支持
     */
    public commonRecyclerAdapter(Context context,
                                 ArrayList<T> data,
                                 MultiTypeSupport<T> multiTypeSupport) {
        //调用单布局
        this(context, data, -1);
        this.multiTypeSupport = multiTypeSupport;
    }



    @Override
    public int getItemViewType(int position) {
        /*
        多布局支持，根据position来确定ViewType

        */
        if (multiTypeSupport != null) {
            //携带数据并返回对应的布局
            //调用接口并回传数据
            return multiTypeSupport.getLayoutId(position);
        }
        //返回0值
        return super.getItemViewType(position);
    }
    //这里的viewtype直接是布局的ID
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //多布局支持.viewType,有两种情况，0和layoutID
        if (multiTypeSupport != null) {
            //确定要使用的布局
            mLayoutId = viewType;
        }
        //inflater布局
        View itemView = mLayoutInflater.inflate(mLayoutId, parent,false);
        //实例化ViewHolder,两布布局传递过去
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    /*

   设置view的绑定事件、数据
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //设置单击和长安时间
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(position);

                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return mLongClickListener.onLongClick(holder.getAdapterPosition());
                }
            });
        }

        //绑定数据，回传出去,
        covert(holder, position);

    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }


    /*
    利用抽象方法将数据传出去，
    @param item   当前数据
     */

    protected abstract void covert(ViewHolder holder, int position);



    /***
     * 设置点击和长按事件
     */
    public OnItemClickListener mItemClickListener;

    public OnLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener mLongClickListener) {
        this.mLongClickListener = mLongClickListener;
    }
}
