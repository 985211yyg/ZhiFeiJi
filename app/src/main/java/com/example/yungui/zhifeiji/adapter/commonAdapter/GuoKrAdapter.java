package com.example.yungui.zhifeiji.adapter.commonAdapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.adapter.commonAdapter.MultiTypeSupport;
import com.example.yungui.zhifeiji.adapter.commonAdapter.ViewHolder;
import com.example.yungui.zhifeiji.adapter.commonAdapter.commonRecyclerAdapter;
import com.example.yungui.zhifeiji.bean.guokr.GuoKrStory;

import java.util.ArrayList;

/**
 * Created by yungui on 2017/3/21.
 */

public class GuoKrAdapter extends commonRecyclerAdapter<GuoKrStory.ResultBean> {

    private Context context;
    private ArrayList<GuoKrStory.ResultBean> data = new ArrayList<>();

    public GuoKrAdapter(Context context, ArrayList<GuoKrStory.ResultBean> data, int layoutId) {
        super(context, data, layoutId);
        this.context = context;
        this.data = data;
    }

    public GuoKrAdapter(Context context, ArrayList<GuoKrStory.ResultBean> data,
                        MultiTypeSupport<GuoKrStory.ResultBean> multiTypeSupport) {
        super(context, data, multiTypeSupport);
        this.context = context;
        this.data = data;


    }


    @Override
    protected void covert(ViewHolder holder,int position) {

            holder.setText(R.id.item_tv, data.get(position).getTitle());
            Glide.with(context)
                    .load(data.get(position).getImages().get(0))
                    .asBitmap()
                    .placeholder(R.drawable.ic_blank_image)
                    .centerCrop()
                    .error(R.drawable.ic_blank_image)
                    .into((ImageView) holder.getView(R.id.item_iv));


    }

}
