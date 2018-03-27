package com.yyy.djk.dropdownmenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import view.HotViewGroup;

/**
 * Created by Administrator on 2017/11/21.
 */

public class SearchActivity extends Activity implements View.OnClickListener{
    @InjectView(R.id.mid_text)
    EditText mid_text;
    @InjectView(R.id.send_query)
    TextView sendQuery;



    private String[] mLabels = {"购物","美食","游玩","北京"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_mid);
        ButterKnife.inject(this);



    }


    @OnClick({R.id.send_query})
    public void onClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.send_query:
                intent.setClass(SearchActivity.this,ClassfiedActivity.class);
                startActivity(intent);
            default:
                break;
        }

    }
//    private void initLabel() {
//        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(30, 30, 10, 10);// 设置边距
//        for (int i = 0; i < mLabels.length; i++) {
//            final TextView textView = new TextView(SearchActivity.this);
//            textView.setTag(i);
//            textView.setTextSize(15);
//            textView.setText(mLabels[i]);
//            textView.setPadding(24, 11, 24, 11);
//            textView.setTextColor(Color.BLACK);
//            textView.setBackgroundResource(R.drawable.check_bg);
//            hotViewGroup.addView(textView, layoutParams);
//            // 标签点击事件
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(), mLabels[(int) textView.getTag()], Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
}
