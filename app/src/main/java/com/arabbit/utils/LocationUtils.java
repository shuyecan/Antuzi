package com.arabbit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.File;

public class LocationUtils {
    private static AMapLocationClient mlocationClient;
    public static AMapLocationClientOption mLocationOption = null;
    public static AMapLocation sLocation = null;
    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    /**
     *
     * @Title: init
     * @Description: 初始化地图导航，在Application onCreate中调用，只需调用一次
     * @param context
     */
    public static void init(Context context) {
        // 声明mLocationOption对象
        mlocationClient = new AMapLocationClient(context);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    }
    public interface MyLocationListener {
        public void result(AMapLocation location);
    }
    /**
     *
     * @Title: getLocation
     * @Description: 获取位置，如果之前获取过定位结果，则不会重复获取
     * @param listener
     */
    public static void getLocation(MyLocationListener listener) {
        if (sLocation == null) {
            getCurrentLocation(listener);
        } else {
            listener.result(sLocation);
        }
    }
    /**
     *
     * @Title: getCurrentLocation
     * @Description: 获取位置，重新发起获取位置请求
     * @param listener
     */
    public static void getCurrentLocation(final MyLocationListener listener) {
        if (mlocationClient==null) {
            return;
        }
        // 设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation location) {
                if (location != null) {
                    //定位成功，取消定位
                    mlocationClient.stopLocation();
                    sLocation=location;
                    listener.result(location);
                }else {
                    //获取定位数据失败
                }
            }
        });
        // 启动定位
        mlocationClient.startLocation();
    }
    public static boolean isGdMapInstalled(){
        return isInstallPackage(PN_GAODE_MAP);
    }
    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static void openGaoDeNavi(Context context,double slat, double slon, double dlat, double dlon, String dname){
        String uriString = null;
        StringBuilder builder = new StringBuilder("androidamap://poi?sourceApplication=softname");
        if (slat != 0) {
            builder.append("&lat1=").append(slat)
                    .append("&lon1=").append(slon);
        }
        builder.append("&keywords=").append(dname);
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_GAODE_MAP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }



    /**
     *
     * @Title: destroy
     * @Description: 销毁定位，必须在退出程序时调用，否则定位会发生异常
     */
    public static void destroy() {
        mlocationClient.onDestroy();
    }
}