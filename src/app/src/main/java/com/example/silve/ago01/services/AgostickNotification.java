package com.example.silve.ago01.services;

import com.example.silve.ago01.R;

import android.app.Notification;
import android.content.Context;
import android.app.Service;
import android.support.v7.app.NotificationCompat;
import android.app.NotificationManager;

import java.util.List;

/**
 * ローカル通知するやつ
 */
public class AgostickNotification {

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param mContext
     */
    public AgostickNotification(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 通知を実行（小さいやつ）
     */
    public void doNotice(String message, String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("通知概要");
        builder.setContentInfo("情報欄");
        builder.setContentTitle(title);
        builder.setContentText(message);
        NotificationManager manager =
                (NotificationManager) this.mContext.getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    /**
     * 通知を実行（複数行）
     *
     * @param list
     * @param title
     */
    public void doNotice(List<String> list, String title)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle(builder);

        inboxStyle.setBigContentTitle(title);
        for (int i = 0, size = list.size(); i < size; i++) {
            inboxStyle.addLine(list.get(i));
        }

        NotificationManager manager =
                (NotificationManager) this.mContext.getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
