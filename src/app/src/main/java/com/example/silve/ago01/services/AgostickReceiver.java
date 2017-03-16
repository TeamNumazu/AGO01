package com.example.silve.ago01.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Receiver
 */
public class AgostickReceiver extends BroadcastReceiver {

    private static final String TAG = AgostickReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startActivityIntent = new Intent(context, );
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }

}