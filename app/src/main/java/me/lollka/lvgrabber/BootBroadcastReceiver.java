package me.lollka.lvgrabber;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
/**
 * Created by lollka on 23.03.2017.
 */

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, DomainService.class);
        startWakefulService(context, startServiceIntent);
    }
}
