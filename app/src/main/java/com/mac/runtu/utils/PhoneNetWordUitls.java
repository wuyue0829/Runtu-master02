package com.mac.runtu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Description:得到当前手机的网络环境
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/24 0024 上午 10:35
 */
public class PhoneNetWordUitls {

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP     = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G      = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_3G      = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI    = 4;


    //private static NetworkInfo networkInfo;
    private static int mNetWorkType;


    public static String getIPAddress(Context context) {

        NetworkInfo info = getNetworkInfo(context);

        if (info != null && info.isConnected()) {

            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
            {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface
                    // .getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface
                            .getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf
                                .getInetAddresses(); enumIpAddr
                                     .hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() &&
                                    inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI)
            {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                //得到IPV4地址

                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }

        return null;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * 判断是否链接联网
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {

        NetworkInfo networkInfo = getNetworkInfo(context);

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * 方法不管用
     *
     * @param context
     * @return
     */
    public static boolean isLowNetPath(Context context) {


        NetworkInfo networkInfo = getNetworkInfo(context);

        String type = networkInfo.getTypeName();

        if (type.equalsIgnoreCase("MOBILE")) {
            String proxyHost = android.net.Proxy.getDefaultHost();

            mNetWorkType = TextUtils.isEmpty(proxyHost)
                    ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G :
                    NETWORKTYPE_2G)
                    : NETWORKTYPE_WAP;
        }

        LogUtils.e("手机网路测算!", mNetWorkType + "");

        return mNetWorkType <= 3;
    }

    public static boolean isWife(Context context) {

        NetworkInfo networkInfo = getNetworkInfo(context);

        String type = networkInfo.getTypeName();

        return type.equalsIgnoreCase("WIFI");

    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {

            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }
}
