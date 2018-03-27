package com.yyy.djk.dropdownmenu;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SyncStatusObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fragment.IndexFragment;
import fragment.MyFragment;
import firstpage.adapter.RecAdapter;
import poi.TestObjectPoi;
import untils.Digest;
import untils.GetIp;
import untils.SignMethod;

import static untils.SignMethod.sign;

/************************************************
 * author:       myunhao
 ***********************************************/
public class FirstPage extends Activity {

    //自定义标题栏
    @InjectView(R.id.txt_topbar)
    TextView txt_topbar;
    //底部选项卡按钮
    @InjectView(R.id.txt_home)
    TextView txt_home;
    @InjectView(R.id.txt_my)
    TextView txt_my;
    //中央可切换视图
    @InjectView(R.id.ly_content)
    FrameLayout ly_content;


    //中央可切换视图
    private IndexFragment indexFragment;
    private MyFragment myFragment;
    private FragmentManager fManager;

    private FragmentTransaction fTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        ButterKnife.inject(this);

        fManager = getFragmentManager();
        bindViews();
        txt_home.performClick();
    }

    //UI组件初始化与事件绑定
    private void bindViews() {
        Drawable d_home = getResources().getDrawable(R.drawable.tab_menu_home);
        d_home.setBounds(0, 0, d_home.getMinimumWidth() - 100, d_home.getMinimumHeight() - 100);
        txt_home.setCompoundDrawables(null, d_home, null, null);

        Drawable d_my = getResources().getDrawable(R.drawable.tab_menu_my);
        d_my.setBounds(0, 0, d_my.getMinimumWidth() - 100, d_my.getMinimumHeight() - 100);
        txt_my.setCompoundDrawables(null, d_my, null, null);


    }

    //重置所有文本的选中状态
    private void setSelected() {
        txt_home.setSelected(false);
        txt_my.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (indexFragment != null) fragmentTransaction.hide(indexFragment);
        if (myFragment != null) fragmentTransaction.hide(myFragment);
    }


    @OnClick({R.id.txt_home, R.id.txt_my})
    public void onClick(View v) {
        fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()) {
            case R.id.txt_home:
                setSelected();
                txt_home.setSelected(true);
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                    fTransaction.add(R.id.ly_content, indexFragment);
                } else {
                    fTransaction.show(indexFragment);
                    txt_topbar.setText("首页");
                }
                break;
            case R.id.txt_my:
                setSelected();
                txt_my.setSelected(true);
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    fTransaction.add(R.id.ly_content, myFragment);
                    txt_topbar.setText("个人中心");
                } else {
                    fTransaction.show(myFragment);
                    txt_topbar.setText("个人中心");
                }
                break;
        }
        fTransaction.commit();
    }

}
