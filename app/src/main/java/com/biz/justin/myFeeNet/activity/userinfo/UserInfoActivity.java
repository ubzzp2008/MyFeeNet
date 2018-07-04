package com.biz.justin.myFeeNet.activity.userinfo;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.biz.justin.myFeeNet.R;
import com.biz.justin.myFeeNet.activity.adapter.CommonAdapter;
import com.biz.justin.myFeeNet.activity.adapter.ViewHolder;
import com.biz.justin.myFeeNet.activity.base.BaseActivity;
import com.biz.justin.myFeeNet.entity.userinfo.UserInfo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(value = R.layout.userinfo_layout)
public class UserInfoActivity extends BaseActivity {

    @ViewInject(value = R.id.list_userInfos)
    private ListView listItem;

    // 适配器
    private SimpleAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topText.setText("用户信息列表");
        getUserInfos();
    }

    private void getUserInfos() {

        List<UserInfo> datas = new ArrayList<UserInfo>();
        for (int i = 1; i <= 50; i++) {
            UserInfo userinfo = new UserInfo();
            userinfo.setUserName("zzp" + i);
            userinfo.setRealName("周宗平" + i);
            datas.add(userinfo);
        }
        listItem.setAdapter(new CommonAdapter<UserInfo>(this, datas, R.layout.userinfo_item) {
            @Override
            public void convert(ViewHolder helper, UserInfo item) {
                helper.setText(R.id.list_username, item.getUserName());
                helper.setText(R.id.list_realname, item.getRealName());
            }
        });




        // 存储数据的数组列表
//        List<UserInfo> list = userInfoService.findAllUserList();
//        ArrayList<HashMap<String, Object>> users = new ArrayList<HashMap<String, Object>>();
//        if(CollectionUtils.isNotEmpty(list)){
//            for (int i = 0; i < list.size(); i++) {
//                HashMap<String, Object> map = new HashMap<String, Object>();
//                map.put("id", list.get(i).getId());
//                map.put("userName", list.get(i).getUserName());
//                map.put("realName", list.get(i).getRealName());
//                map.put("password", list.get(i).getPassword());
//                map.put("birthday", list.get(i).getBirthday());
//                map.put("email", list.get(i).getEmail());
//                map.put("phone", list.get(i).getPhone());
//                map.put("createDate", list.get(i).getCreateDate());
//                users.add(map);
//            }
//        }
//        listItemAdapter = new SimpleAdapter(this,
//                users,// 数据源
//                R.layout.userinfo_item,// ListItem的XML实现
//                // 动态数组与ImageItem对应的子项
////                new String[] { "id", "userName","realName","password","birthday","email","phone","createDate" },
//                new String[]{"userName", "realName"},
//                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
//                new int[]{R.id.list_username, R.id.list_realname});
//        listItem.setAdapter(listItemAdapter);
    }

}
