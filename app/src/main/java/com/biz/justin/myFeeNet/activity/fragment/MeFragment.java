package com.biz.justin.myFeeNet.activity.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.userinfo.UserInfoActivity;
import com.biz.justin.myFeeNet.util.StaticParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lt on 2015/12/1.
 */
@ContentView(value = R.layout.fragment_me)
public class MeFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 用户查询按钮事件
     *
     * @param view
     */
    @Event(value = R.id.me_image_user_search, type = View.OnClickListener.class)
    private void queryUserOnClick(View view) {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        //跳转到用户查询页面
        startActivity(intent);
    }

}
