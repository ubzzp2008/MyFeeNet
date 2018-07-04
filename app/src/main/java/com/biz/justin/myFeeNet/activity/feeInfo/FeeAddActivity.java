package com.biz.justin.myFeeNet.activity.feeInfo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONObject;
import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.base.BaseActivity;
import com.biz.justin.myFeeNet.activity.home.MainActivity;
import com.biz.justin.myFeeNet.entity.feeInfo.FeeInfo;
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

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ContentView(value = R.layout.fee_add_layout)
public class FeeAddActivity extends BaseActivity {

    //费用类型
    @ViewInject(value = R.id.feeType_radio)
    private RadioGroup radioGroup;

    //费用日期
    @ViewInject(value = R.id.fee_id_useDate)
    private EditText useDate;

    //费用金额
    @ViewInject(value = R.id.fee_id_money)
    private EditText useFee;

    //费用说明
    @ViewInject(value = R.id.fee_id_useContent)
    private EditText useContent;

    //备注
    @ViewInject(value = R.id.fee_id_note)
    private EditText note;
    /**
     * loading提示框
     */
    private ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeTopText(this.getString(R.string.home_fee_add));
        useDate.setInputType(InputType.TYPE_NULL);//不显示系统输入键盘

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new Date());

        useDate.setText(date);

    }

    @Event(value = R.id.fee_id_useDate, type = View.OnFocusChangeListener.class)
    private void useDateOnFocus(View view, boolean hasFocus) {
        if (hasFocus) {//焦点获得
            Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String m = "";
                    String d = "";
                    if (monthOfYear + 1 < 10) {
                        m = "0" + (monthOfYear + 1);
                    } else {
                        m = "" + (monthOfYear + 1);
                    }
                    if (dayOfMonth < 10) {
                        d = "0" + dayOfMonth;
                    } else {
                        d = "" + dayOfMonth;
                    }
                    useDate.setText(year + "-" + m + "-" + d);
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    useDate.setText("");
                }
            });
            dialog.show();
        }
    }

    //日期选择 点击事件
    @Event(value = R.id.fee_id_useDate, type = View.OnClickListener.class)
    private void useDateOnClick(View view) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String m = "";
                String d = "";
                if (monthOfYear + 1 < 10) {
                    m = "0" + (monthOfYear + 1);
                } else {
                    m = "" + (monthOfYear + 1);
                }
                if (dayOfMonth < 10) {
                    d = "0" + dayOfMonth;
                } else {
                    d = "" + dayOfMonth;
                }
                useDate.setText(year + "-" + m + "-" + d);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                useDate.setText("");
            }
        });
        dialog.show();
    }

    /**
     * 保存按钮事件
     *
     * @param view
     */
    @Event(value = R.id.fee_add_id_save, type = View.OnClickListener.class)
    private void saveBtnOnClick(View view) {
        String uDate = useDate.getText().toString();
        String uFee = useFee.getText().toString();
        String uNote = useContent.getText().toString();
        if (StringUtils.isEmpty(uDate)) {
            showToast("请填写消费日期");
        } else if (StringUtils.isEmpty(uFee)) {
            showToast("请填写消费金额");
        } else if (StringUtils.isEmpty(uNote)) {
            showToast("请填写消费说明");
        } else {
            String feeType = "";
            BigDecimal fee = new BigDecimal(0);
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.feeType_radio_income:
                    feeType = StaticParams.FEE_TYPE_IN;
                    fee = new BigDecimal(uFee);
                    break;
                case R.id.feeType_radio_pay:
                    feeType = StaticParams.FEE_TYPE_OUT;
                    fee = new BigDecimal(-1).multiply(new BigDecimal(uFee));
                    break;
                default:
                    break;
            }
            proDialog = ProgressDialog.show(this, null,
                    "数据加载中....", true, true);
            FeeInfo feeInfo = new FeeInfo();
            feeInfo.setFeeType(feeType);
            feeInfo.setUseDate(uDate);
            feeInfo.setMoney(fee);
            feeInfo.setUseContent(uNote);
            feeInfo.setNote(note.getText().toString());
            feeInfo.setAddUser(getLoginName());
            //网络请求
            String url = ProperTies.getServerUrl(this.getApplicationContext(), "feeInfoAI", "addFeeInfo");
            this.callRemoteLogin(url, JSONObject.toJSONString(feeInfo));
        }
    }

    /**
     * @discription 访问服务端接口---登录接口
     */
    private void callRemoteLogin(String url, String jsonParam) {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);//使用json方式
        params.setBodyContent(jsonParam);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("网络请求返回的json：", result);
                AjaxJson json = JSONObject.parseObject(result, AjaxJson.class);
                if (json.isSuccess()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FeeAddActivity.this);
                    builder.setMessage(json.getMsg());
                    builder.setTitle("提示");
                    builder.setPositiveButton("继续添加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String date = sDateFormat.format(new Date());
                            useDate.setText(date);
                            useFee.setText("");
                            useContent.setText("");
                            note.setText("");
                        }
                    });
                    builder.setNegativeButton("返回首页", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                            //跳转到登陆界面
                            Intent intent = new Intent(FeeAddActivity.this, MainActivity.class);
                            startActivity(intent);
                            FeeAddActivity.this.finish();//结束当前的activity
                        }
                    });
                    builder.setCancelable(false);//这句代码设置这个对话框不能被用户按[返回键]而取消掉
                    builder.create().show();
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
     * 清空按钮事件
     *
     * @param view
     */
    @Event(value = R.id.fee_add_id_reset, type = View.OnClickListener.class)
    private void resetBtnOnClick(View view) {
        useDate.setText("");
        useFee.setText("");
        useContent.setText("");
        note.setText("");
    }

}
