package com.example.yungui.zhifeiji.search;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yungui.zhifeiji.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MySearchActivityFragment extends Fragment {

    public MySearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_search, container, false);
    }
}
