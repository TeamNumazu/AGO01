package com.example.silve.ago01.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * 商品期限を監視して通知するやつ
 */
public class ExpireNotifierService extends Service {

    /**
     * 初期化するだけ
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * サービスで実行させたいコードはここに記述
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "AGO！", Toast.LENGTH_LONG).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
