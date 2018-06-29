package com.biz.justin.myFeeNet.activity.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.adapter.TabPageAdapter;
import com.biz.justin.myFeeNet.activity.base.BaseActivity;
import com.biz.justin.myFeeNet.activity.fragment.HomeFragment;
import com.biz.justin.myFeeNet.activity.fragment.MeFragment;
import com.biz.justin.myFeeNet.activity.login.LoginActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

//主页面首页
@ContentView(value = R.layout.main_layout)
public class MainActivity extends BaseActivity {


    @ViewInject(value = R.id.radioGroup)
    protected RadioGroup mRadioGroup;

    @ViewInject(value = R.id.viewPager)
    private ViewPager mViewPager;

    private ArrayList<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewPager();
    }

    //初始化ViewPager
    private void initViewPager() {
        HomeFragment homeFragment = new HomeFragment();
        MeFragment meFragment = new MeFragment();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(homeFragment);
        fragmentList.add(meFragment);
        //ViewPager设置适配器
        mViewPager.setAdapter(new TabPageAdapter(getSupportFragmentManager(), fragmentList));
        //ViewPager显示第一个Fragment
        mViewPager.setCurrentItem(0);
        changeTopText("您好："+getRealName());//改变头文件内容
        topBack.setVisibility(View.GONE);
        //第一个radio选中
        mRadioGroup.check(R.id.radio_home);
    }

    /**
     * 底部radioGroup 选中监听事件
     *
     * @param group
     * @param checkedId
     */
    @Event(value = R.id.radioGroup, type = RadioGroup.OnCheckedChangeListener.class)
    private void radioGroupOnClick(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_home:
                //ViewPager显示第一个Fragment且关闭页面切换动画效果
                mViewPager.setCurrentItem(0, false);
                changeTopText("您好："+getRealName());//改变头文件内容
                break;
            case R.id.radio_me:
                mViewPager.setCurrentItem(1, false);
                changeTopText(this.getString(R.string.tab_me));
                break;
        }
    }


    //ViewPager页面切换监听
    @Event(value = R.id.viewPager, type = ViewPager.OnPageChangeListener.class, method = "onPageSelected")
    private void mViewPagerOnChange(int position) {
        switch (position) {
            case 0:
                mRadioGroup.check(R.id.radio_home);
                break;
            case 1:
                mRadioGroup.check(R.id.radio_me);
                break;
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //按下返回键
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确定退出系统？");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    finishAll();
                }
            });
            builder.setNegativeButton("返回登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    Intent intent =new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
//            builder.setCancelable(false);//这句代码设置这个对话框不能被用户按[返回键]而取消掉
            builder.create().show();
            return true;
        }
        return super.onKeyDown(keyCode, event);//不是按下返回键，执行系统默认操作
    }

}
