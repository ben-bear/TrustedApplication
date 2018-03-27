package com.yyy.djk.dropdownmenu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yyydjk.library.DropDownMenu;

import junit.framework.Test;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.ConstellationAdapter;
import adapter.GirdDropDownAdapter;
import adapter.InfoAdapter;
import adapter.ListDropDownAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import firstpage.adapter.RecAdapter;
import poi.DefinedPoi;
import poi.HttpRequest;
import poi.HttpResponse;
import poi.TestObjectPoi;
import untils.Digest;
import untils.SignMethod;
import untils.ThreadCorrect;
import untils.ThreadDistance;
import untils.ThreadGrade;
import untils.ViewHolder;

/**
 * Created by Administrator on 2017/12/18.
 */

public class ClassfiedActivity extends AppCompatActivity {

    @InjectView(R.id.dropDownMenu_in2)
    DropDownMenu mDropDownMenu;
    @InjectView(R.id.back_in2)
    Button backIn2;
    @InjectView(R.id.btn_tomap)
    Button btnToMap;
    @InjectView(R.id.classfied_item_lv)
    ListView item_lv;
    @InjectView(R.id.to_midsearchpage)
    TextView toSearchPage;

    //申请权限列表
    private String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private AlertDialog alertDialog;
    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;

    private List<Map<String, Object>> items;

    private GirdDropDownAdapter cityAdapter;
    private ListDropDownAdapter gradeAdapter;
    private ListDropDownAdapter disAdapter;
    private ConstellationAdapter constellationAdapter;

    private String headers[] = {"种类", "附近", "智能排序", "筛选"};
    private List<View> popupViews = new ArrayList<>();

    private String citys[] = {"不限", "餐厅", "KTV", "咖啡厅", "火锅", "自助餐", "水吧", "西餐厅", "蔬果店", "快餐"};
    private String distance[] = {"不限", "500m以内", "1000m以内", "2000m以内", "3000m以内", "5000m以内"};
    private String grade[] = {"智能排序", "距离优先", "价格最低", "评分优先"};
    private String constellations[] = {"不限", "早饭", "午饭", "晚饭"};

    private int constellationPosition = 0;
    private TestObjectPoi myPosition;
    private int sortid = 0;
    private int intDistance[] = {10000, 500, 1000, 2000, 3000, 5000};

    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient = null;


    private List<Integer> washedList = new ArrayList<>();
    private Map<Integer, String> verifyOb;
    private List<Map<String, Object>> mList;
    private Map<Integer, TestObjectPoi> truePoi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classfied);
        ButterKnife.inject(this);
        /**
         * 初始化密钥
         */
        try {
            KeyPairGenerator keyPairGenerator = null;
            keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        /**
         * 检查权限
         */
        checkPermissions(permissions);
        /**
         * 获取用户当前位置
         */
        getMyPostion();
        /**
         * 初始化布局
         */
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(permissions);
        }
    }

    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //获取当前地理位置
    private void getMyPostion() {
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        mLocationClient.startLocation();

    }

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
                    double mylatitude = amapLocation.getLatitude();
                    //获取经度
                    double mylongtitude = amapLocation.getLongitude();
                    myPosition = new TestObjectPoi(mylatitude, mylongtitude);
                    Log.e("用户所在位置==经度：纬度", "locationType:" + locationType + "longtitude:" + mylongtitude + ",latitude:" + mylatitude);
                    //   Toast.makeText(getApplicationContext(), "纬度:" +mylatitude +"经度:" +mylongtitude , Toast.LENGTH_LONG).show();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    //加载选择栏布局视图
    private void initView() {
        //init city menu
        final ListView cityView = new ListView(this);
        cityAdapter = new GirdDropDownAdapter(this, Arrays.asList(citys));
        cityView.setDividerHeight(0);
        cityView.setAdapter(cityAdapter);

        //init distance menu
        final ListView disView = new ListView(this);
        disView.setDividerHeight(0);
        disAdapter = new ListDropDownAdapter(this, Arrays.asList(distance));
        disView.setAdapter(disAdapter);

        //init sex menu
        final ListView gradeView = new ListView(this);
        gradeView.setDividerHeight(0);
        gradeAdapter = new ListDropDownAdapter(this, Arrays.asList(grade));
        gradeView.setAdapter(gradeAdapter);

        //init constellation
        final View constellationView = getLayoutInflater().inflate(R.layout.custom_layout, null);
        GridView constellation = ButterKnife.findById(constellationView, R.id.constellation);
        constellationAdapter = new ConstellationAdapter(this, Arrays.asList(constellations));
        constellation.setAdapter(constellationAdapter);
        TextView ok = ButterKnife.findById(constellationView, R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu.setTabText(constellationPosition == 0 ? headers[3] : constellations[constellationPosition]);

                mDropDownMenu.closeMenu();
            }
        });

        //init popupViews
        popupViews.add(cityView);
        popupViews.add(disView);
        popupViews.add(gradeView);
        popupViews.add(constellationView);

        //add item click event
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : citys[position]);
                mDropDownMenu.closeMenu();
            }
        });

        disView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                disAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : distance[position]);
                new Thread(new ThreadDistance(mHandler, myPosition, intDistance[position])).start();
                mDropDownMenu.closeMenu();
            }
        });

        gradeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gradeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : grade[position]);
//                if(mList == null) {
                new Thread(new ThreadGrade(mHandler, position)).start();
//                }
//                else {
//                    if(position == 1) {
//                     Toast.makeText(getApplicationContext(),"距离优先",Toast.LENGTH_SHORT).show();
//                    }
//                    if(position == 2) {
//                        Toast.makeText(getApplicationContext(),"价格最低",Toast.LENGTH_SHORT).show();
//                    }
//                    if(position == 3) {
//                        Toast.makeText(getApplicationContext(),"评分优先",Toast.LENGTH_SHORT).show();
//                    }
//                }

                mDropDownMenu.closeMenu();
            }
        });

        constellation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constellationAdapter.setCheckItem(position);
                constellationPosition = position;
            }
        });

        //init context view
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//      contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);


        //返回按钮监听器
        backIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //跳转地图按钮监听器
        btnToMap.setTextSize(18);
        btnToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ClassfiedActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        toSearchPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ClassfiedActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        items = getData();
        InfoAdapter infoAdapter = new InfoAdapter(getApplicationContext(), items, item_lv, null);
        item_lv.setAdapter(infoAdapter);
    }


    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (int i = 0; i < 1; i++) {
            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.restaurant);
            map.put("title", "请选择一种查询类型");
            map.put("info", "-----------");
            list.add(map);
        }
        return list;
    }

    private List<Map<String, Object>> SetGradeViewFromS(Map<Integer, TestObjectPoi> s) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (Map.Entry<Integer, TestObjectPoi> entry : s.entrySet()) {
            map = new HashMap<String, Object>();
            map.put("pos", entry.getKey());
            map.put("img", R.mipmap.restaurant);
            map.put("title", entry.getValue().getName());
            map.put("info", "地址：" + entry.getValue().getAddress()
                    + "\n纬度：" + entry.getValue().getLatitude()
                    + "\n经度:" + entry.getValue().getLongtitude()
                    + "\n距离你：" + entry.getValue().getDistanceAwayFrom() + "米"
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

                case 200:
                    HttpResponse httpResponse = (HttpResponse) msg.obj;
                    /**
                     *取出正确点集和验证对象点集
                     */
                    List<Integer> errorPos = new ArrayList<>();
                    truePoi = httpResponse.getPoi();
                    verifyOb = httpResponse.getVerifyPoi();
                    mList = SetGradeViewFromS(truePoi);

//
//                    View view = item_lv.getChildAt(2);
//                    ViewHolder viewHolder  = (ViewHolder) view.getTag();
//                    viewHolder.title.setTextColor(getResources().getColor(R.color.gray));
//                    mList.get(2).put("title","测试数据");

                    for (Map.Entry<Integer, TestObjectPoi> map : truePoi.entrySet()) {
                        System.out.println(map.getValue() + "测试" + map.getKey());
                    }
                    for (Map.Entry<Integer, String> map : verifyOb.entrySet()) {
                        System.out.println(map.getValue() + "错误点集" + map.getKey());
                    }
                    //错误集合点的list
                    List<Integer> listofCorrect = new ArrayList<>();
                    //测试这里得到的签名
                    for (Map.Entry<Integer, TestObjectPoi> map : truePoi.entrySet()) {
                        System.out.println(Arrays.toString(map.getValue().getSignature()));
                    }
                    //这里开始验证，取出验证对象集合，然后对正确数据集中数据点进行验证
                    boolean statusForAll = true;
                    byte[] signGen = new byte[0];
                    String digestGen = "";
                    for (Map.Entry<Integer, TestObjectPoi> map : truePoi.entrySet()) {
                        boolean statusForSingle = true;
                        int position = map.getKey();
                        String left = "";
                        String right = "";
                        //检查正确集合中是否包含这个点的左边一个点
                        if (verifyOb.containsKey(position - 1)) {
                            left = verifyOb.get(position - 1);
                        }
                        //如果没有包含在正确集合中，那么在错误集合中去找这个点
                        else if (truePoi.containsKey(position - 1)) {
                            try {
                                left = Digest.getDigest(truePoi.get(position - 1));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        //如果两个集合中都没有包含，那么这个点是被省略掉的正确点
                        else {
                            statusForSingle = false;

                        }

                        if (verifyOb.containsKey(position + 1)) {
                            right = verifyOb.get(position + 1);
                        } else if (truePoi.containsKey(position + 1)) {
                            try {
                                right = Digest.getDigest(truePoi.get(position + 1));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            statusForSingle = false;

                        }
                        try {
                            digestGen = left + Digest.getDigest(map.getValue()) + right;
                            signGen = SignMethod.sign(digestGen.getBytes());
                            System.out.println("自己生成的签名为：" + Arrays.toString(signGen));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!Arrays.equals(signGen, map.getValue().getSignature())) {
                            statusForSingle = false;
                        }
                        System.out.println("第" + map.getKey() + "个点验证情况：" + statusForSingle);
                        //如果一个点错了，那么全局验证情况就位false
                        if (!statusForSingle) {
                            statusForAll = false;
                            listofCorrect.add(position);
                        }
                    }
                    System.out.println("未清洗：" + listofCorrect);

                    /**
                     *对纠错数组进行处理
                     */
                    if(!washedList.isEmpty()){
                        washedList.clear();
                    }
                    for (int i = 0; i < listofCorrect.size(); i++) {

                        if (listofCorrect.size() < 3) {
                            washedList.add(listofCorrect.get(0));
                            break;
                        } else if (listofCorrect.contains(listofCorrect.get(i) - 1) &&
                                listofCorrect.contains(listofCorrect.get(i) + 1)) {
                            //如果list中一个错了，他的前一个和后一个都错了，那么肯定是这个出错
                            washedList.add(listofCorrect.get(i));
                        }
                    }
                    System.out.println("清洗后的：" + washedList);
                    InfoAdapter infoAdapter = new InfoAdapter(getApplicationContext(), mList, item_lv, washedList);
                    item_lv.setAdapter(infoAdapter);

                    /**
                     *如果status验证标志不正确，那么则提醒用户是否进行纠正
                     */
                    if (!statusForAll) {
                        Dialog alertDialog = new AlertDialog.Builder(ClassfiedActivity.this)
                                .setTitle("验证过程发生错误")
                                .setMessage("你希望纠正错误数据么？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new Thread(new ThreadCorrect(mHandler, washedList)).start();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getApplicationContext(), "您取消了纠正！！！！！！！！！！", Toast.LENGTH_SHORT).show();
                                    }
                                }).create();
                        alertDialog.show();
                    }
                    break;
                //205表示纠错进程
                case 205:
                    HttpResponse correctHttpResponse = (HttpResponse) msg.obj;
                    System.out.println("205handler" + washedList.size());
                    for (int i = 0; i < washedList.size(); i++) {
                        //定义错误数据的位置
                        int correctPos = washedList.get(i);
                        if (correctPos % 2 == 1) {
                            double x = (correctHttpResponse.getCorrectPoi().get(correctPos + 2).getLatitude() -
                                    truePoi.get(correctPos + 1).getLatitude() * 0.5) * 2;
                            double y = (correctHttpResponse.getCorrectPoi().get(correctPos + 2).getLongtitude() -
                                    truePoi.get(correctPos + 1).getLongtitude() * 0.5) * 2;
                            TestObjectPoi temp = truePoi.get(correctPos);
                            temp.setLatitude(x);
                            temp.setLongtitude(y);
                            truePoi.put(correctPos, temp);
                        } else {
                            double x = (correctHttpResponse.getCorrectPoi().get(correctPos + 1).getLatitude() -
                                    truePoi.get(correctPos - 1).getLatitude() * 0.5) * 2;
                            double y = (correctHttpResponse.getCorrectPoi().get(correctPos + 1).getLongtitude() -
                                    truePoi.get(correctPos - 1).getLongtitude() * 0.5) * 2;
                            TestObjectPoi temp = truePoi.get(correctPos);
                            temp.setLatitude(x);
                            temp.setLongtitude(y);
                            truePoi.put(correctPos, temp);
                        }
                        System.out.println(truePoi.get(7).getLatitude());
                    }
                    mList = SetGradeViewFromS(truePoi);
                    InfoAdapter infoAdapterCorrect = new InfoAdapter(getApplicationContext(), mList, item_lv, null);
                    item_lv.setAdapter(infoAdapterCorrect);
                    Toast.makeText(getApplicationContext(), "还原数据成功...............", Toast.LENGTH_SHORT).show();
                    break;
                //300表示智能排序
                case 300:
                    HttpResponse intelHttpResponse = (HttpResponse) msg.obj;
                    truePoi = intelHttpResponse.getPoi();
                    mList = SetGradeViewFromS(truePoi);
                    InfoAdapter infoAdapterIntel = new InfoAdapter(getApplicationContext(), mList, item_lv, null);
                    item_lv.setAdapter(infoAdapterIntel);

                    break;
                default:
                    break;
            }
        }
    };

}
