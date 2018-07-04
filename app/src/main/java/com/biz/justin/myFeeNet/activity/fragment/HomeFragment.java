package com.biz.justin.myFeeNet.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.feeInfo.FeeAddActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * Created by lt on 2015/12/1.
 */
@ContentView(value = R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 费用新增事件
     *
     * @param view
     */
    @Event(value = R.id.home_image_addFee)
    private void addFeeOnClick(View view) {
        Intent intent = new Intent(getActivity(), FeeAddActivity.class);
        //跳转到费用新增页面
        startActivity(intent);
    }
}
