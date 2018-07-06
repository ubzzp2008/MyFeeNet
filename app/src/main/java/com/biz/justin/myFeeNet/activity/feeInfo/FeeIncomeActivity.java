package com.biz.justin.myFeeNet.activity.feeInfo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.adapter.CommonAdapter;
import com.biz.justin.myFeeNet.activity.adapter.ViewHolder;
import com.biz.justin.myFeeNet.activity.base.BaseActivity;
import com.biz.justin.myFeeNet.entity.feeInfo.FeeInfo;
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
import java.util.List;

@ContentView(value = R.layout.fee_income_layout)
public class FeeIncomeActivity extends BaseActivity {

    @ViewInject(R.id.fee_income_count)
    private TextView feeIncomeCount;

    @ViewInject(R.id.list_income)
    private ListView listItem;

    /**
     * loading提示框
     */
    private ProgressDialog proDialog;

//    @ViewInject(R.id.fee_income_bottom)
//    private LinearLayout feeIncomeBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeTopText(this.getString(R.string.home_fee_income));
//        optBatchDel.setVisibility(View.VISIBLE);
////        optBatchDel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
////                if (isChecked) {
////                    listItemAdapter = new MyListViewAdapter(feeList, getApplicationContext(), true);
////                    listItem.setAdapter(listItemAdapter);
////                    feeIncomeBottom.setVisibility(View.VISIBLE);
////                } else {
////                    listItemAdapter = new MyListViewAdapter(feeList, getApplicationContext(), false);
////                    listItem.setAdapter(listItemAdapter);
////                    feeIncomeBottom.setVisibility(View.GONE);
////                }
////            }
////        });

        getFeeIncomeInfos();
    }

    private void getFeeIncomeInfos() {
        proDialog = ProgressDialog.show(this, null,
                "数据加载中....", true, true);
        FeeInfo feeInfo = new FeeInfo();
        feeInfo.setFeeType(StaticParams.FEE_TYPE_IN);
        feeInfo.setCreateBy(getLoginName());
        //网络请求
        String url = ProperTies.getServerUrl(this.getApplicationContext(), "feeInfo", "queryFeeInfoList");
        this.callRemoteFeeInfoList(url, JSONObject.toJSONString(feeInfo));
    }

    /**
     * @discription 访问服务端接口---获取费用明细接口
     */
    private void callRemoteFeeInfoList(String url, String jsonParam) {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);//使用json方式
        params.setBodyContent(jsonParam);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("网络请求返回的json：", result);
                AjaxJson json = JSONObject.parseObject(result, AjaxJson.class);
                if (json.isSuccess()) {
                    String str = String.format(FeeIncomeActivity.this.getString(R.string.fee_count), Double.parseDouble(json.getMsg()));
                    feeIncomeCount.setText(str);
                    List<FeeInfo> datas = JSONArray.parseArray(json.getObj(), FeeInfo.class);
                    listItem.setAdapter(new CommonAdapter<FeeInfo>(FeeIncomeActivity.this, datas, R.layout.fee_list_item) {
                        @Override
                        public void convert(ViewHolder helper, FeeInfo item) {
                            helper.setText(R.id.item_fee_useDate, item.getUseDate());
                            helper.setText(R.id.item_fee_money, item.getMoney().toString());
                            helper.setText(R.id.item_fee_useContent, item.getUseContent());
                            helper.setText(R.id.item_fee_note, item.getNote());
                            helper.setText(R.id.item_fee_createDate, item.getCreateDateStr());
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

    /*@Event(value = R.id.list_income, type = AdapterView.OnItemLongClickListener.class)
    private boolean listItemOnLongClick(AdapterView<MyListViewAdapter> adapterView, final View view, final int position, long l) {
        TextView tv = (TextView) view.findViewById(R.id.item_fee_id);
        String id = tv.getText().toString();//主键ID
        final FeeInfo feeInfo = feeInfoService.findFeeInfoById(Integer.parseInt(id));
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.fee_update_layout,
                (ViewGroup) findViewById(R.id.fee_update_layout));
        //初始化修改页面数据
        final TextView updateId = (TextView) layout.findViewById(R.id.update_fee_id);
        final TextView updateUseDate = (TextView) layout.findViewById(R.id.update_fee_id_useDate);
        final EditText updateMoney = (EditText) layout.findViewById(R.id.update_fee_id_money);
        final EditText updateUseContent = (EditText) layout.findViewById(R.id.update_fee_id_useContent);
        final EditText updateNote = (EditText) layout.findViewById(R.id.update_fee_id_note);
        updateId.setText(feeInfo.getId() + "");
        updateUseDate.setText(feeInfo.getUseDate());
        updateMoney.setText(feeInfo.getMoney() + "");
        updateMoney.setSelection(updateMoney.getText().length());
        updateUseContent.setText(feeInfo.getUseContent());
        updateNote.setText(feeInfo.getNote());
        //初始化数据结束
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setTitle("费用修改");
        //添加AlertDialog.Builder对象的setPositiveButton()方法
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String uId = updateId.getText().toString();
                String money = updateMoney.getText().toString();
                String ucon = updateUseContent.getText().toString();
                String note = updateNote.getText().toString();
                if (StringUtils.isEmpty(money)) {
                    showToast("请填写费用金额");
                } else if (StringUtils.isEmpty(ucon)) {
                    showToast("请填写费用说明");
                } else {
                    FeeInfo f = new FeeInfo();
                    f.setId(Integer.parseInt(uId));
                    f.setMoney(new Double(money));
                    f.setUseContent(ucon);
                    f.setNote(note);
                    MessageInfo messageInfo = feeInfoService.updateFeeInfoById(f);
                    if (messageInfo.isSuccess()) {
                        showToast(messageInfo.getText());
                        getFeeIncomeInfos();
                        optBatchDel.setChecked(false);
                        dialog.dismiss();
                    } else {
                        showToast(messageInfo.getText());
                    }
                }
            }
        });

        //添加AlertDialog.Builder对象的setNegativeButton()方法
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
        return true;
    }*/

    /*@Event(value = R.id.list_income, type = AdapterView.OnItemClickListener.class)
    private void listItemOnItemClick(AdapterView<MyListViewAdapter> adapterView, View view, int position, long l) {
        // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
        MyListViewAdapter.ViewHolder holder = (MyListViewAdapter.ViewHolder) view.getTag();
        // CheckBox被选中时，则更改状态为未选中，且从集合中移除
        if (holder.itemCkb.isChecked()) {
            MyListViewAdapter.getIsSelected().put(position, false);
            holder.itemCkb.toggle();//更改选中状态
        } else {
            MyListViewAdapter.getIsSelected().put(position, true);
            holder.itemCkb.toggle();//更改选中状态
        }
    }*/

    //全选、反选、删除按钮绑定点击事件
    /*@Event(value = {R.id.fee_list_income_delete, R.id.fee_list_income_select_all,
            R.id.fee_list_income_select_back}, type = View.OnClickListener.class)
    private void bottonButOnClick(View view) {
        switch (view.getId()) {
            //删除按钮点击
            case R.id.fee_list_income_delete:
                deleteData();
                break;
            //全选按钮点击
            case R.id.fee_list_income_select_all:
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < feeList.size(); i++) {
                    MyListViewAdapter.getIsSelected().put(i, true);
                }
                listItemAdapter.notifyDataSetChanged();
                break;
            //反选按钮点击
            case R.id.fee_list_income_select_back:
                // 遍历list的长度，将MyAdapter中的map值全部设为false
                for (int i = 0; i < feeList.size(); i++) {
                    if (MyListViewAdapter.getIsSelected().get(i)) {
                        MyListViewAdapter.getIsSelected().put(i, false);
                    } else {
                        MyListViewAdapter.getIsSelected().put(i, true);
                    }
                }
                listItemAdapter.notifyDataSetChanged();
                break;
        }
    }*/

    //删除操作
    /*private void deleteData() {
        HashMap<Integer, Boolean> map = MyListViewAdapter.getIsSelected();
        ArrayList<FeeInfo> list = new ArrayList<FeeInfo>();
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                Long id = listItemAdapter.getItemId(entry.getKey());
                list.add((FeeInfo) listItemAdapter.getItem(entry.getKey()));
                //根据Id删除数据
                feeInfoService.deleteFeeInfoById(id.intValue());
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            showToast("请选择需要删除的数据");
        } else {
            feeList.removeAll(list);//移除选中的数据
            listItemAdapter.notifyDataSetChanged();//刷新数据
            optBatchDel.toggle();//改变头文件的删除图标
            countTotalFee();//重新统计数据
        }
    }*/
}
