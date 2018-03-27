package fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.yyy.djk.dropdownmenu.ClassfiedActivity;
import com.yyy.djk.dropdownmenu.InfoActivity;
import com.yyy.djk.dropdownmenu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import firstpage.adapter.GridViewAdapter;
import firstpage.adapter.RecAdapter;
import firstpage.adapter.ViewPagerAdapter;
import poi.HttpResponse;
import poi.Model;
import poi.TestObjectPoi;
import untils.ThreadRecommend;

/************************************************
 * author:       火中燕.
 * email:        wangzhong0116@foxmail.com.
 * version:      1.0.1.
 * date:         2016/12/9 20:17.
 * description: 首页
 ***********************************************/

public class IndexFragment extends Fragment {

    private String[] titles = {"不限", "美食",
            "KTV", "咖啡厅",
            "电影", "演出",
            "酒店住宿", "休闲娱乐",
            "水果超市","外卖",
            "机票/火车票","周边游",
            "旅行","丽人/美发",
            "运动健身","景点门票",
            "结婚/摄影","母婴/亲子"};
    //homepage上面的图片
    private int[] img = {
            R.mipmap.homepage_icon_light_all_b, R.mipmap.homepage_icon_light_food_b,
            R.mipmap.homepage_icon_light_ktv_b, R.mipmap.homepage_icon_light_amusement_b,
            R.mipmap.homepage_icon_light_movie_b,R.mipmap.homepage_icon_light_default_b,
            R.mipmap.homepage_icon_light_hotel_b,R.mipmap.homepage_icon_light_toursaround_b,
            R.mipmap.homepage_icon_light_shopping_b,R.mipmap.homepage_icon_light_takeout_b,
            R.mipmap.homepage_icon_light_transportation_b,R.mipmap.homepage_icon_light_travel_b,
            R.mipmap.homepage_icon_light_travel_b,R.mipmap.homepage_icon_light_default_b,
            R.mipmap.homepage_icon_light_spots_b,R.mipmap.homepage_icon_light_default_b,
            R.mipmap.homepage_icon_light_weddingphoto_b,R.mipmap.homepage_icon_light_default_b
    };
    //推荐列表中的图片
    private int[] recImg = {
            R.mipmap.ic_category_36
    };

    private List<String> titleFromS = new ArrayList<String>();
    private List<String> infoFromS = new ArrayList<String>();

    @InjectView(R.id.viewpager)
    ViewPager mPager;
    @InjectView(R.id.ll_dot)
    LinearLayout mLlDot;

    @InjectView(R.id.guest_like)
    TextView guest_like;
    @InjectView(R.id.recommend_item)
    ListView lv;

    private List<Map<String, Object>> data;

    private List<View> mPagerList;
    private List<Model> mDatas;
    private LayoutInflater inflater;

    /**
     * 总的页数
     */
    private int pageCount;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 10;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    public IndexFragment(){super();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.activity_index, container, false);

        ButterKnife.inject(this, myview);

        /**
        *从ThreadRecommend网络线程中，获取推荐商家
        */
        new Thread(new ThreadRecommend(mHandler)).start();

        //初始化数据源

        initDatas();
        inflater = LayoutInflater.from(getActivity());
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
            gridView.setAdapter(new GridViewAdapter(getActivity(), mDatas, i, pageSize));
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    Intent intent = new Intent();

//                    intent.setClass(getActivity(),MainActivity.class);
                    intent.setClass(getActivity(),ClassfiedActivity.class);
                    startActivity(intent);
                    //弹出被按的名字
//                  Toast.makeText(getActivity(), mDatas.get(pos).getName(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        //设置适配器
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置圆点
        setOvalLayout();

        //显示推荐数据

        return myview;
    }


    /**
     * 初始化数据源
     */
    private void initDatas() {
        mDatas = new ArrayList<Model>();
        for (int i = 0; i < titles.length; i++) {
            //动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
            int imageId = getResources().getIdentifier("ic_category_" + i, "mipmap", getActivity().getPackageName());
            mDatas.add(new Model(titles[i], img[i]));
        }
    }

    /**
     * 设置圆点
     */
    public void setOvalLayout() {
        inflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    /**在推荐列表里面添加数据
     *
     * @return
     */
    private List<Map<String, Object>> getData()
    {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for(int i=0;i<titleFromS.size();i++)
        {
            map = new HashMap<String, Object>();
            map.put("img", recImg[0]);
            map.put("title", titleFromS.get(i));
            map.put("info", infoFromS.get(i));
            list.add(map);
        }
        return list;
    }

    private List<Map<String, Object>> SetGradeViewFromS( Map<Integer, TestObjectPoi> s){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (Map.Entry<Integer, TestObjectPoi> entry : s.entrySet()) {
            map = new HashMap<String,Object>();
            map.put("img",recImg[0]);
            map.put("title",entry.getValue().getName());
            map.put("info","地址："+entry.getValue().getAddress()
            );
            list.add(map);
        }
        return list;
    }


    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    HttpResponse httpResponse = (HttpResponse) msg.obj;
                    data = SetGradeViewFromS(httpResponse.getPoi());
                    RecAdapter recAdapter = new RecAdapter(getActivity(),data);
                    lv.setAdapter(recAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String text = lv.getItemAtPosition(i)+"";
                            // Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("address", String.valueOf(data.get(i).get("info")));
                            intent.setClass(getActivity(), InfoActivity.class);
                            startActivity(intent);
                        }
                    });
                    break;
                default:
                    break;
            }

        }
    };



}
