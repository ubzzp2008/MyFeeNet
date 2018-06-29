package com.biz.justin.myFeeNet.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.x;

/**
 * Created by justin on 2016/12/30.
 */

public class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);
    }


    public void showToast(String msg) {
        Toast.makeText(x.app(), msg, Toast.LENGTH_SHORT).show();
    }
}
