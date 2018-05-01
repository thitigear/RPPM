package com.example.gear.rppm.other;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class Utils {

    private static Ringtone ringtone;

    public Utils() {
    }

    public static String doubleToString(double num){return String.format("%.2f", num); }
    public static String intToString(int num){return String.format("%d", num); }

    /*Notification and Sound*/
    public static void setupSound(Context context){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ringtone = RingtoneManager.getRingtone(context, notification);
    }
    public static void playNotificationSound(){
        ringtone.play();
    }
    public static void stopNotificationSound(){
        ringtone.stop();
    }
}
