package me.lollka.lvgrabber;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import br.com.goncalves.pugnotification.notification.PugNotification;

public class DomainService extends IntentService {

    String domain;
    String domain1;
    String domain2;
    String domain3;
    String domain4;
    String found = "mākslīgo";
    String NIC1 = "https://www.nic.lv/domain/check?domain=";
    String NIC2 = "&btn_search=P%C4%81rbaud%C4%ABt";

    int dom = 0;
    int dom1 = 0;
    int dom2 = 0;
    int dom3 = 0;
    int dom4 = 0;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            new autoDomainCheck().execute();//uzsāk songDisplay
        }
    };

    public DomainService() {
        super("test-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences settings = getApplicationContext().getSharedPreferences("lvgrabberdata", 0);
        domain = settings.getString("domain",null);
        domain1 = settings.getString("domain1",null);
        domain2 = settings.getString("domain2",null);
        domain3 = settings.getString("domain3",null);
        domain4 = settings.getString("domain4",null);
        runnable.run();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
        domain = intent.getStringExtra("domain");
        domain1 = intent.getStringExtra("domain1");
        domain2 = intent.getStringExtra("domain2");
        domain3 = intent.getStringExtra("domain3");
        domain4 = intent.getStringExtra("domain4");
        runnable.run();
        /*
        Bundle bundle = new Bundle();
        bundle.putString("resultValue", "My Result Value. Passed in: " + val);
        rec.send(Activity.RESULT_OK, bundle);
        */
    }

    //DOMENU PARBAUDITAJS

    private class autoDomainCheck extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if (domain!=null){
                try {
                    String parse = Jsoup.connect(NIC1 + domain + NIC2).execute().body();
                    Document result = Jsoup.parse(parse);
                    String txt = result.select("div[class=round-box msgbox-error]").text();
                    if (txt.toLowerCase().contains(found.toLowerCase())) {
                        dom = 0;
                    } else {
                        dom = 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                dom = 0;
            }
            if (domain1!=null){
                try {
                    String parse = Jsoup.connect(NIC1 + domain1 + NIC2).execute().body();
                    Document result = Jsoup.parse(parse);
                    String txt = result.select("div[class=round-box msgbox-error]").text();
                    if (txt.toLowerCase().contains(found.toLowerCase())) {
                        dom1 = 0;
                    } else {
                        dom1 = 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                dom1 = 0;
            }
            if (domain2!=null){
                try {
                    String parse = Jsoup.connect(NIC1 + domain2 + NIC2).execute().body();
                    Document result = Jsoup.parse(parse);
                    String txt = result.select("div[class=round-box msgbox-error]").text();
                    if (txt.toLowerCase().contains(found.toLowerCase())) {
                        dom2 = 0;
                    } else {
                        dom2 = 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                dom2 = 0;
            }
            if (domain3!=null){
                try {
                    String parse = Jsoup.connect(NIC1 + domain3 + NIC2).execute().body();
                    Document result = Jsoup.parse(parse);
                    String txt = result.select("div[class=round-box msgbox-error]").text();
                    if (txt.toLowerCase().contains(found.toLowerCase())) {
                        dom3 = 0;
                    } else {
                        dom3 = 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                dom3 = 0;
            }
            if (domain4!=null){
                try {
                    String parse = Jsoup.connect(NIC1 + domain4 + NIC2).execute().body();
                    Document result = Jsoup.parse(parse);
                    String txt = result.select("div[class=round-box msgbox-error]").text();
                    if (txt.toLowerCase().contains(found.toLowerCase())) {
                        dom4 = 0;
                    } else {
                        dom4 = 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                dom4 = 0;
            }
            //Log.d("DOMAIN_CHECK","Still running? kek XD");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dom==1){
                PugNotification.with(DomainService.this)
                        .load()
                        .title(".LV Grabber")
                        .message("Domēns "+domain.toUpperCase()+".LV ir brīvs!")
                        .smallIcon(R.mipmap.ic_launcher)
                        .largeIcon(R.mipmap.ic_launcher)
                        .flags(Notification.DEFAULT_ALL)
                        .click(MainActivity.class)
                        .autoCancel(true)
                        .simple()
                        .build();
            }
            if (dom1==1){
                PugNotification.with(DomainService.this)
                        .load()
                        .title(".LV Grabber")
                        .message("Domēns "+domain1.toUpperCase()+".LV ir brīvs!")
                        .smallIcon(R.mipmap.ic_launcher)
                        .largeIcon(R.mipmap.ic_launcher)
                        .flags(Notification.DEFAULT_ALL)
                        .click(MainActivity.class)
                        .autoCancel(true)
                        .simple()
                        .build();
            }
            if (dom2==1){
                PugNotification.with(DomainService.this)
                        .load()
                        .title(".LV Grabber")
                        .message("Domēns "+domain2.toUpperCase()+".LV ir brīvs!")
                        .smallIcon(R.mipmap.ic_launcher)
                        .largeIcon(R.mipmap.ic_launcher)
                        .flags(Notification.DEFAULT_ALL)
                        .click(MainActivity.class)
                        .autoCancel(true)
                        .simple()
                        .build();
            }
            if (dom3==1){
                PugNotification.with(DomainService.this)
                        .load()
                        .title(".LV Grabber")
                        .message("Domēns "+domain3.toUpperCase()+".LV ir brīvs!")
                        .smallIcon(R.mipmap.ic_launcher)
                        .largeIcon(R.mipmap.ic_launcher)
                        .flags(Notification.DEFAULT_ALL)
                        .click(MainActivity.class)
                        .autoCancel(true)
                        .simple()
                        .build();
            }
            if (dom4==1){
                PugNotification.with(DomainService.this)
                        .load()
                        .title(".LV Grabber")
                        .message("Domēns "+domain4.toUpperCase()+".LV ir brīvs!")
                        .smallIcon(R.mipmap.ic_launcher)
                        .largeIcon(R.mipmap.ic_launcher)
                        .flags(Notification.DEFAULT_ALL)
                        .click(MainActivity.class)
                        .autoCancel(true)
                        .simple()
                        .build();
            }
            handler.postDelayed(runnable, 21600000);
            //handler.postDelayed(runnable, 43200000);
        }
    }

    //WIFI PĀRBAUDĪTĀJS
    /*
    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }
    */
}
