package com.yyy.djk.dropdownmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import poi.HttpResponse;
import poi.TestObjectPoi;
import untils.ThreadClient;
import untils.JumpPoint;
import untils.SetUpMap;


public class MainActivity extends AppCompatActivity implements AMap.OnMarkerClickListener {

    private AMapLocationClient mLocationClient = null;
    private MarkerOptions markerOption;

    LatLng mypostion;

    double mylatitude  = 0;
    double mylongtitude = 0;
    double end_latitude  = 0;
    double end_longtitude = 0;
    String search_content = "";

    private AMap aMap;
    private MapView mapView;
    private UiSettings mUiSettings;//定义一个UiSettings对象

    private Marker markerResult;

    private LatLonPoint mStartPoint ;//起点
    private LatLonPoint mEndPoint ;//终点
    private final int ROUTE_TYPE_WALK = 3;



    @InjectView(R.id.btn_to_search) Button toSearch;
    @InjectView(R.id.input_edittext) EditText searchText;
//    @InjectView(R.id.btn_to_route) Button btnToRoute;

//    @InjectView(R.id.quert_btn) Button queryBt;

    private List<String> query_context = new ArrayList<String>();

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mLocationClient.startLocation();
                    break;
                case 0x11:
                    HttpResponse httpResponse = (HttpResponse)msg.obj;
                   /*
                    没有POI传回
                     */
                    if(httpResponse == null){
                        Toast.makeText(getApplicationContext(),"没有找到相应的商家",Toast.LENGTH_SHORT).show();
                    }
                    /*
                    最后执行在地图上面显示对应的POI
                     */
                    else {
                        aMap.clear();
//                        String[] poi = (String[]) msg.obj;
//                        int count =0;
//                        Toast.makeText(getApplicationContext(), "附近有" + poi.length + "个poi", Toast.LENGTH_SHORT).show();
                        Map<Integer, TestObjectPoi> poiOfSearch = new HashMap<>();
                        poiOfSearch = httpResponse.getPoi();
                        for (Map.Entry<Integer,TestObjectPoi> map:poiOfSearch.entrySet()){
                            double a1 = map.getValue().getLatitude();
                            double a2 = map.getValue().getLongtitude();
                            //设置终点的坐标
                            end_latitude = a1;
                            end_longtitude = a2;

                            //暂时保存目标位置
                            LatLng endLatlng = new LatLng(a1, a2);
                            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .position(endLatlng)
                                    .title(""+map.getValue().getName())
                                    .snippet("这是POI的信息")
                                    .draggable(true);
                            markerResult = aMap.addMarker(markerOption);

 //                           JumpPoint.jumpPoint(markerResult,aMap);
                            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(endLatlng,18,0,0)));
                        }

                    }
                    break;
                default:
                    Log.v("没有handler传入", "。。。");
                    break;
            }
        }
    };



    //定位监听器
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            // TODO Auto-generated method stub
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    //获取当前定位结果来源，如网络定位结果，详见定位类型表
                    double locationType = amapLocation.getLocationType();
                    //获取纬度
                    mylatitude = amapLocation.getLatitude();
                    //获取经度
                    mylongtitude = amapLocation.getLongitude();
                    Log.e("Amap==经度：纬度", "locationType:" + locationType + "longtitude:" + mylongtitude + ",latitude:" + mylatitude);
                   // Toast.makeText(getApplicationContext(), "纬度:" +mylatitude +"经度:" +mylongtitude , Toast.LENGTH_SHORT).show();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        ButterKnife.inject(this);

//
//        init();
//        setfromandtoMarker();
//        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationClient.startLocation();

        toSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_content = searchText.getText().toString();
                if(search_content.equals("")){
                    Toast.makeText(getApplicationContext(),"请输入你想要查找的商家",Toast.LENGTH_SHORT).show();
                }
                else{
                    new Thread(new ThreadClient(mHandler,search_content)).start();
                    searchText.setText("");
                }
            }
        });



//
        initView();
    }

    private void initView() {
        if(aMap == null){
            aMap = mapView.getMap();
            SetUpMap.setupmap(aMap, mLocationClient);
            //设置marker被点击监听
            aMap.setOnMarkerClickListener(this);

            mypostion = new LatLng(mylatitude,mylongtitude);
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
            mUiSettings.setZoomControlsEnabled(false);


        }
    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (aMap != null) {
            JumpPoint.jumpPoint(marker,aMap);

        }
        Intent intent = new Intent(MainActivity.this,RouteActivity.class);

        intent.putExtra("start_latitude",mylatitude);
        intent.putExtra("start_longtitude",mylongtitude);
        intent.putExtra("end_latitude",end_latitude);
        intent.putExtra("end_longtitude",end_longtitude);
        intent.putExtra("test","string");
        startActivity(intent);

        return true;
    }


    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {

        aMap.moveCamera(update);

    }

    private class QueryThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();
            try {
                //启动定位
                Message msg = Message.obtain();
                msg.what = 1;
                //      msg.arg1 = 传参数进去;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Looper.loop();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        mLocationClient.stopLocation();
    }
}
