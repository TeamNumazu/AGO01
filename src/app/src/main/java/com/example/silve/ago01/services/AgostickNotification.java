package com.example.silve.ago01.services;

import com.example.silve.ago01.R;
import android.content.Context;
import android.app.Service;
import android.support.v7.app.NotificationCompat;
import android.app.NotificationManager;

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
     * 通知を実行
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

}
