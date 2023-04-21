package com.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class MainActivity extends AppCompatActivity {

    public AMapLocationClient mLocationClient;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    // 打印获取的信息
                    System.out.println("===================================");
                    System.out.println("定位类型：" + aMapLocation.getLocationType());
                    System.out.println("纬度：" + aMapLocation.getLatitude());
                    System.out.println("经度：" + aMapLocation.getLongitude());
                    System.out.println("精度：" + aMapLocation.getAccuracy());
                    System.out.println("地址：" + aMapLocation.getAddress());
                    System.out.println("国家：" + aMapLocation.getCountry());
                    System.out.println("省：" + aMapLocation.getProvince());
                    System.out.println("市：" + aMapLocation.getCity());
                    System.out.println("===================================");

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }

        }
    };


    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AMapLocationClient.updatePrivacyShow(getApplicationContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getApplicationContext(), true);
        // 校验是否有定位权限，没有则申请
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
            }, 1);
        }
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
            mLocationOption = getDefaultOption();
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.setLocationListener(mLocationListener);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //启动定位
        startLocation();


    }

    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    private void startLocation(){
        try {
            //根据控件的选择，重新设置定位参数
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}