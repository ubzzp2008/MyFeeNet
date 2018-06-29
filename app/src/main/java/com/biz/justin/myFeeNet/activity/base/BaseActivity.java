package com.biz.justin.myFeeNet.activity.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.util.StaticParams;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by wyouflf on 15/11/4.
 */
public class BaseActivity extends AppCompatActivity {
    @ViewInject(R.id.opt_batch_delete)
    protected CheckBox optBatchDel;
    //头文件的内容
    @ViewInject(value = R.id.top_text)
    protected TextView topText;
    //头文件的返回图标
    @ViewInject(value = R.id.top_back)
    protected ImageView topBack;

    public static List<Activity> activities = new ArrayList<Activity>();

    @Override
    protected void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        Log.d("BaseActivity", getClass().getSimpleName());
        addActivity(this);//将活动的activity添加进集合
        x.view().inject(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(this);//从集合中移除activity
    }

    public void showToast(String msg) {
        Toast.makeText(x.app(), msg, Toast.LENGTH_SHORT).show();
    }


    public String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    //新加activity到集合
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    //从集合中删除activity
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //结束所有的activity
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }


    /**
     * 改变头文件显示内容
     *
     * @param msg
     */
    protected void changeTopText(String msg) {
        topText.setText(msg);
    }

    /**
     * 头文件返回按钮事件
     */
    public void onClickTopBack(View view) {
        if(view.getId()==R.id.top_back){
            this.finish();
        }
    }


    /**
     * 清除登录状态信息
     */
//    protected void clearLoginState() {
//        SharedPreferences sp = getSharedPreferences(StaticParams.LOGIN_FILE, MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.clear();
//        edit.commit();
//    }

    /**
     * 获取当前登录人帐号
     *
     * @return
     */
    protected String getLoginName() {
        SharedPreferences sp = getSharedPreferences(StaticParams.LOGIN_FILE, MODE_PRIVATE);
        return sp.getString("userName", "");
    }
    /**
     * 获取当前登录人姓名
     *
     * @return
     */
    protected String getRealName() {
        SharedPreferences sp = getSharedPreferences(StaticParams.LOGIN_FILE, MODE_PRIVATE);
        return sp.getString("realName", "");
    }

}
