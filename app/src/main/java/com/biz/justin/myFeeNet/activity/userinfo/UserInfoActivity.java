package com.biz.justin.myFeeNet.activity.userinfo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.adapter.CommonAdapter;
import com.biz.justin.myFeeNet.activity.adapter.ViewHolder;
import com.biz.justin.myFeeNet.activity.base.BaseActivity;
import com.biz.justin.myFeeNet.activity.feeInfo.FeeIncomeActivity;
import com.biz.justin.myFeeNet.entity.feeInfo.FeeInfo;
import com.biz.justin.myFeeNet.entity.userinfo.UserInfo;
import com.biz.justin.myFeeNet.util.AjaxJson;
import com.biz.justin.myFeeNet.util.ProperTies;
import com.biz.justin.myFeeNet.util.StaticParams;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

@ContentView(value = R.layout.userinfo_layout)
public class UserInfoActivity extends BaseActivity {

    @ViewInject(value = R.id.list_userInfos)
    private ListView listItem;
    /**
     * loading提示框
     */
    private ProgressDialog proDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topText.setText("用户信息列表");
        getUserInfos();
    }

    private void getUserInfos() {
        proDialog = ProgressDialog.show(this, null,
                "数据加载中....", true, true);
        UserInfo userInfo = new UserInfo();
        //网络请求
        String url = ProperTies.getServerUrl(this.getApplicationContext(), "userInfo", "queryUserInfoList");
        this.callRemoteUserInfoList(url, JSONObject.toJSONString(userInfo));
    }

    /**
     * @discription 访问服务端接口---获取用户信息接口
     */
    private void callRemoteUserInfoList(String url, String jsonParam) {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);//使用json方式
        params.setBodyContent(jsonParam);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("网络请求返回的json：", result);
                AjaxJson json = JSONObject.parseObject(result, AjaxJson.class);
                if (json.isSuccess()) {
                    List<UserInfo> datas = JSONArray.parseArray(json.getObj(), UserInfo.class);
                    listItem.setAdapter(new CommonAdapter<UserInfo>(UserInfoActivity.this, datas, R.layout.userinfo_item) {
                        @Override
                        public void convert(ViewHolder helper, UserInfo item) {
                            helper.setText(R.id.item_user_username, item.getUserName());
                            helper.setText(R.id.item_user_realname, item.getRealName());
                            helper.setText(R.id.item_user_sex, item.getSex());
                            helper.setText(R.id.item_user_phone, item.getPhone());
                        }
                    });
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

}
