package com.yyy.djk.dropdownmenu;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.InfoAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class InfoActivity extends Activity {
    @InjectView(R.id.recommend_item_img)
    ImageView recommendItemImg;
    @InjectView(R.id.recommend_item_txt)
    TextView recommendItemTxt;
    @InjectView(R.id.recommend_item_info)
    ListView recommend_lv;

    private List<Map<String,Object>> dishes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.inject(this);
        //设置商家页面的图片
        recommendItemImg.setImageDrawable(getResources().getDrawable(R.mipmap.restaurant));
        //商家地址
        recommendItemTxt.setText(getIntent().getStringExtra("address"));
        recommendItemTxt.setTextSize(20);

        dishes = getData();
        InfoAdapter infoAdapter = new InfoAdapter(getApplicationContext(),dishes,recommend_lv,null);
        recommend_lv.setAdapter(infoAdapter);

        recommend_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"我是个假菜吧。。。",Toast.LENGTH_SHORT).show();
            }
        });
    }



    //在推荐列表里面添加数据
    private List<Map<String, Object>> getData()
    {
        //此处从可以修改成从网络获取数据，为了简便，直接本地生成数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(int i=0;i<20;i++)
        {
            map = new HashMap<String, Object>();
            if(i<10) {
                map.put("img", R.mipmap.takeout_ic_category_public);
            }
            else
                map.put("img",R.mipmap.takeout_ic_category_public_disable);
            map.put("title", "\n"+"第"+i+"个菜品"+"\n");
            map.put("info", "价格:"+((i+10)*2+i-10));
            list.add(map);
        }
        return list;
    }



}
