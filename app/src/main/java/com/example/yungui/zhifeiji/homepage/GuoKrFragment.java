package com.example.yungui.zhifeiji.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yungui.zhifeiji.R;

/**
 * Created by yungui on 2017/2/14.
 */

public class GuoKrFragment extends Fragment implements GuoKrContract.View {

    public GuoKrFragment() {

    }

    public static GuoKrFragment getInstance() {

        return new GuoKrFragment();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        initViews(root);
        return root;
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showResult() {

    }

    @Override
    public void setPresenter(GuoKrContract.Presenter presenter) {

    }

    @Override
    public void initViews(View view) {

    }
}
