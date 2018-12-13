package com.mac.runtu.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.mac.runtu.R;
import com.mac.runtu.activity.personcentre.LoginActivity;

/**
 * 通知服务
 */
public class MyNotificationService extends Service {

    private static final String TAG = "MyNotificationService";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            //显示通知栏
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            //通知栏布局
            //RemoteViews contentView = new RemoteViews(getPackageName(),
            //R.layout.notify_layout);

            //给通知栏设置点击事件
            PendingIntent intent2 = PendingIntent.getActivity(this, 0, new Intent(
                    this, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

            //状态栏

            mBuilder.setSmallIcon(R.mipmap.image_app_table);
            mBuilder.setContentTitle("闰土农业");
            mBuilder.setContentText("您的账号在其他设备上登陆了");
            //通知栏

            //点击通知栏空白处
            mBuilder.setContentIntent(intent2);
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

            Notification notification = mBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;

            nm.notify(0, notification);



        //UiUtils.go2LoginAcivity();

        return super.onStartCommand(intent, flags, startId);
    }
}
