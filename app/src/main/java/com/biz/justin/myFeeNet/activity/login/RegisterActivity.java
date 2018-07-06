package com.biz.justin.myFeeNet.activity.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONObject;
import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.base.BaseActivity;
import com.biz.justin.myFeeNet.activity.home.MainActivity;
import com.biz.justin.myFeeNet.entity.userinfo.UserInfo;
import com.biz.justin.myFeeNet.util.AjaxJson;
import com.biz.justin.myFeeNet.util.ProperTies;

import org.apache.commons.lang.StringUtils;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.net.SocketTimeoutException;

//注册页面
@ContentView(R.layout.register_layout)
public class RegisterActivity extends BaseActivity {
    //用户名输入框
    @ViewInject(R.id.reg_id_username)
    private EditText etUserName;
    //真实名输入框
    @ViewInject(R.id.reg_id_realName)
    private EditText etRealName;
    //密码输入框
    @ViewInject(R.id.reg_id_pwd)
    private EditText etPassword;
    //密码输入框
    @ViewInject(R.id.reg_id_pwd_confirm)
    private EditText etPasswordConfirm;
    //联系电话输入框
    @ViewInject(R.id.reg_id_phone)
    private EditText etPhone;
    //费用类型
    @ViewInject(value = R.id.sex_radio)
    private RadioGroup radioGroup;
    /**
     * 登录loading提示框
     */
    private ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 注册按钮事件
     *
     * @param view
     */
    @Event(value = R.id.reg_id_save, type = View.OnClickListener.class)
    private void saveBtnOnClick(View view) {
        String userName = etUserName.getText().toString();
        String realName = etRealName.getText().toString();
        String pwd = etPassword.getText().toString();
        String pwdConfirm = etPasswordConfirm.getText().toString();
        String phone = etPhone.getText().toString();
        if (StringUtils.isEmpty(userName)) {
            showToast("用户名不能为空");
        } else if (StringUtils.isEmpty(realName)) {
            showToast("真实名不能为空");
        } else if (StringUtils.isEmpty(pwd)) {
            showToast("密码不能为空");
        } else if (StringUtils.isEmpty(pwdConfirm)) {
            showToast("确认密码不能为空");
        } else if (StringUtils.isEmpty(phone)) {
            showToast("联系电话不能为空");
        } else {
            String sexStr = "1";//默认为男
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.sex_radio_man:
                    sexStr = "1";
                    break;
                case R.id.sex_radio_woman:
                    sexStr = "0";
                    break;
                default:
                    break;
            }
            proDialog = ProgressDialog.show(this, null,
                    "数据加载中....", true, true);
            //网络请求
            String url = ProperTies.getServerUrl(this.getApplicationContext(), "login", "registerUser");
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(userName.toLowerCase());//转小写，服务端存储用户名均转小写
            userInfo.setRealName(realName);
            userInfo.setPassword(pwd);
            userInfo.setPhone(phone);
            userInfo.setSex(sexStr);
            this.callRemoteRegister(url, JSONObject.toJSONString(userInfo));
        }
    }

    /**
     * @discription 访问服务端接口---注册用户接口
     */
    private void callRemoteRegister(String url, String jsonParam) {
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
                    showToast(json.getMsg());
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
    /**
     * 保存用户操作
     *
     * @param userInfo
     * @return
     */
//    private MessageInfo saveUserInfo(UserInfo userInfo) {
//        MessageInfo message = new MessageInfo();
//        //根据用户名查询用户是否存在
//        UserInfo user = userInfoService.findUserInfoByName(userInfo.getUserName());
//        if (user != null) {
//            message.setSuccess(false);
//            message.setText("用户名已经被使用，请重新输入");
//        } else {
//            message = userInfoService.saveUserInfo(userInfo);
//            if (message.isSuccess()) {
//                message.setText("恭喜你！注册成功");
//            }
//        }
//        return message;
//    }

    /**
     * 注册成功后弹框提示
     */
    private void dialog(final UserInfo userInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage("恭喜你！注册成功");
        builder.setTitle("提示");
        builder.setPositiveButton("进入系统", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        });
        builder.setNegativeButton("返回登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                //跳转到登陆界面
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();//结束当前的activity
            }
        });
        builder.setCancelable(false);//这句代码设置这个对话框不能被用户按[返回键]而取消掉
        builder.create().show();
    }

    @Event(value = R.id.reg_id_pwd_confirm, type = View.OnFocusChangeListener.class)
    private void pwdConfirmOnFocus(View view, boolean hasFocus) {
        if (hasFocus) {
            // 此处为得到焦点时的处理内容
        } else {
            // 此处为失去焦点时的处理内容
            String pwd = etPassword.getText().toString();
            String pwdConfirm = etPasswordConfirm.getText().toString();
            if (!StringUtils.equals(pwd, pwdConfirm)) {//两次密码不一致
                showToast("两次输入的密码不一致！");
                etPasswordConfirm.setText("");//清空密码确认字段
            }
        }
    }

    /**
     * 清空按钮事件
     *
     * @param view
     */
    @Event(value = R.id.reg_id_reset, type = View.OnClickListener.class)
    private void resetBtnOnClick(View view) {
        etUserName.setText("");
        etPassword.setText("");
        etRealName.setText("");
        etPhone.setText("");
        etPasswordConfirm.setText("");
    }

}
