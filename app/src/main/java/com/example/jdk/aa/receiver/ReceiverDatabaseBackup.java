package com.example.jdk.aa.receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.example.jdk.aa.R;
import com.example.jdk.aa.activity.ActivityMain;
import com.example.jdk.aa.service.ServiceDatabaseBackup;

public class ReceiverDatabaseBackup extends BroadcastReceiver {

    NotificationManager m_NotificationManager;
    Notification m_Notification;

    Intent m_Intent;
    PendingIntent m_PendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        m_NotificationManager=(NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Log.i("GoDutch", "广播：" + (intent.getLongExtra("Date",0)/1000));
        //点击通知时转移内容
        m_Intent=new Intent(context,ActivityMain.class);
        m_PendingIntent=PendingIntent.getActivity(context, 0, m_Intent, 0);
        Notification.Builder builder = new Notification.Builder(context).setTicker("AA费用小助手已执行数据备份")
                .setSmallIcon(R.mipmap.icon);
        m_Notification = builder.setContentIntent(m_PendingIntent).setContentTitle("AA费用小助手已执行数据备份").setContentText("AA费用小助手已执行数据备份").build();
        //这个可以理解为开始执行这个通知
        m_NotificationManager.notify(0,m_Notification);
        Intent _Intent = new Intent(context, ServiceDatabaseBackup.class);
        context.startService(_Intent);
    }

}
