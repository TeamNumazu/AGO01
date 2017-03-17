package com.example.silve.ago01.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;

/**
 * 商品期限を監視して通知するやつ
 */
public class ExpireNotifierService extends Service {

    /**
     * 何分おきに実行するか
     */
    private static final int NOTIFIER_EXEC_PER = 2;

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
        if (this.shouldTaskStart() == true) {
            Toast.makeText(this, "AGO！", Toast.LENGTH_LONG).show();
        }

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

    /**
     * 処理を実行する条件
     *
     * @return boolean
     */
    private boolean shouldTaskStart()
    {
        CharSequence textMinutes  = android.text.format.DateFormat.format("mm", Calendar.getInstance());
        int intMinutes = Integer.parseInt(textMinutes.toString());

        return (intMinutes % NOTIFIER_EXEC_PER) == 0;
    }

}
