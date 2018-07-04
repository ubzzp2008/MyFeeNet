package com.biz.justin.myFeeNet.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.base.BaseActivity;
import com.biz.justin.myFeeNet.activity.home.MainActivity;
import com.biz.justin.myFeeNet.entity.userinfo.UserInfo;
import com.biz.justin.myFeeNet.util.AjaxJson;
import com.biz.justin.myFeeNet.util.ProperTies;
import com.biz.justin.myFeeNet.util.StaticParams;

import org.apache.commons.lang.StringUtils;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.net.SocketTimeoutException;

//登录页面
//viewUtils加载Activity布局,使用了这句之后oncreate里面不需要调用setcontentView
@ContentView(R.layout.login_layout)
public class LoginActivity extends BaseActivity {
    // 显示密码的复选框
    @ViewInject(R.id.login_id_remember_pwd)
    private CheckBox rememberPwd;
    //密码输入框
    @ViewInject(R.id.login_id_pwd)
    private EditText etPassWord;
    //用户名输入框
    @ViewInject(R.id.login_id_username)
    private EditText etUserName;

    /**
     * 登录loading提示框
     */
    private ProgressDialog proDialog;

//    private UserInfoService userInfoService = new UserInfoServiceImpl();

    private long currentTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(StaticParams.LOGIN_FILE, MODE_PRIVATE);
        etUserName.setText(sp.getString("userName", ""));
        boolean isMemory = sp.getBoolean("isMemory", false);
        if (isMemory) {
            etPassWord.setText(sp.getString("password", ""));
            rememberPwd.setChecked(true);
        }
    }

    @Event(value = R.id.eyes_pwd, type = CompoundButton.OnCheckedChangeListener.class)
    private void showPwdOnClick(CompoundButton view, boolean isChecked) {
        if (isChecked) {
            //如果选中，则显示密码
            etPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //否则隐藏密码
            etPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etPassWord.setSelection(etPassWord.getText().length());//移动光标到之前的开始位置
    }

    @Event(value = R.id.login_id_submit, type = View.OnClickListener.class)
    private void loginBtnOnClick(View view) {
        String pwd = etPassWord.getText().toString();
        String userName = etUserName.getText().toString();
        if (StringUtils.isEmpty(userName)) {
            showToast("请输入用户名");
        } else if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
        } else {
            proDialog = ProgressDialog.show(LoginActivity.this, null,
                    "数据加载中....", true, true);
            //网络请求
            String url = ProperTies.getServerUrl(this.getApplicationContext(), "loginAI", "login");
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(userName);
            userInfo.setPassword(pwd);
            this.callRemoteLogin(url, JSONObject.toJSONString(userInfo),pwd);
        }
    }

    /**
     * @discription 访问服务端接口---登录接口
     */
    private void callRemoteLogin(String url, String jsonParam,final String pwd) {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);//使用json方式
        params.setBodyContent(jsonParam);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                proDialog.dismiss();
                Log.e("网络请求返回的json：", result);
                AjaxJson json = JSONObject.parseObject(result, AjaxJson.class);
                if (json.isSuccess()) {
                    UserInfo user = JSONObject.parseObject(json.getObj(), UserInfo.class);
                    remenber(user,pwd);//判断是否记住用户名密码
                    showToast(json.getMsg());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(json.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    showToast("网络异常，请检查后重试");
                } else if (ex instanceof SocketTimeoutException) { // 其他错误
                    showToast("网络请求超时，请检查后重试");
                } else {
                    showToast("获取数据失败，请检查后重试");
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                proDialog.dismiss();
            }
        });
    }


    // remenber方法用于判断是否记住密码
    private void remenber(UserInfo userInfo,String pwd) {
        SharedPreferences sp = getSharedPreferences(StaticParams.LOGIN_FILE, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("userName", userInfo.getUserName());
        edit.putString("realName", userInfo.getRealName());
        if (rememberPwd.isChecked()) {
            edit.putString("password", pwd);
            edit.putBoolean("isMemory", true);
        }else{
            edit.putString("password", "");
            edit.putBoolean("isMemory", false);
        }
        edit.commit();
    }

    //注册按钮事件
    @Event(value = R.id.login_id_register, type = View.OnClickListener.class)
    private void registerTvOnClick(View view) {
//        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//        startActivity(intent);
    }

    //忘记密码事件
    @Event(value = R.id.login_id_forget_pwd, type = View.OnClickListener.class)
    private void forgetPwdTvOnClick(View view) {
//        Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
//        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//按下返回键
            if (System.currentTimeMillis() - currentTime > 2000) {
                showToast("再按一次退出系统");
                currentTime = System.currentTimeMillis();
            } else {
                //关闭所有的activity
                finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);//不是按下返回键，执行系统默认操作

    }
}
