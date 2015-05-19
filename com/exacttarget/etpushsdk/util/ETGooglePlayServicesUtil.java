/*    */ package com.exacttarget.etpushsdk.util;
/*    */ 
/*    */ import android.app.NotificationManager;
/*    */ import android.app.PendingIntent;
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.content.SharedPreferences;
/*    */ import android.content.SharedPreferences.Editor;
/*    */ import android.content.pm.ApplicationInfo;
/*    */ import android.net.Uri;
/*    */ import android.preference.PreferenceManager;
/*    */ import android.support.v4.app.NotificationCompat.Builder;
/*    */ import android.util.Log;
/*    */ import com.exacttarget.etpushsdk.Config;
/*    */ import com.exacttarget.etpushsdk.ETPush;
/*    */ import com.google.android.gms.common.GooglePlayServicesUtil;
/*    */ 
/*    */ public class ETGooglePlayServicesUtil
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETGooglePlayServicesUtil";
/* 21 */   private static boolean googleAvailable = false;
/*    */   private static final String NOTIFICATION_REQUEST_CODE = "et_notification_play_services_error_code_key";
/*    */   private static final int NOTIFICATION_ID = 913131313;
/*    */   
/*    */   public static boolean isAvailable(Context applicationContext, boolean enablingPush) {
/* 26 */     if (ETAmazonDeviceMessagingUtil.isAmazonDevice()) {
/* 27 */       googleAvailable = false;
/*    */     }
/* 29 */     else if (!googleAvailable)
/*    */     {
/* 31 */       int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(applicationContext);
/* 32 */       if (status == 0) {
/* 33 */         googleAvailable = true;
/*    */       }
/*    */       else {
/* 36 */         if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
/* 37 */           showErrorNotification(applicationContext, "Google Play Services Error: " + GooglePlayServicesUtil.getErrorString(status), true, enablingPush);
/*    */         }
/*    */         else
/*    */         {
/* 41 */           showErrorNotification(applicationContext, "Google Play Services is not supported on this Device.", false, enablingPush);
/*    */         }
/* 43 */         googleAvailable = false;
/*    */       }
/*    */     }
/* 46 */     return googleAvailable;
/*    */   }
/*    */   
/*    */   protected static void showErrorNotification(Context applicationContext, String alertMessage, boolean userRecoverable, boolean enablingPush)
/*    */   {
/*    */     try {
/* 52 */       if ((ETPush.pushManager().isPushEnabled() | enablingPush)) {
/* 53 */         displayErrorNotification(applicationContext, alertMessage, userRecoverable);
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 57 */       if (ETPush.getLogLevel() <= 6) {
/* 58 */         Log.e("jb4ASDK@ETGooglePlayServicesUtil", e.getMessage(), e);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected static void displayErrorNotification(Context applicationContext, String alertMessage, boolean userRecoverable)
/*    */   {
/* 65 */     NotificationCompat.Builder builder = new NotificationCompat.Builder(applicationContext);
/* 66 */     int appIconResourceId = applicationContext.getApplicationInfo().icon;
/* 67 */     builder.setSmallIcon(appIconResourceId);
/* 68 */     builder.setAutoCancel(true);
/*    */     
/* 70 */     int appLabelResourceId = applicationContext.getApplicationInfo().labelRes;
/* 71 */     String app_name = applicationContext.getString(appLabelResourceId);
/* 72 */     builder.setContentTitle(app_name);
/*    */     
/* 74 */     builder.setTicker(alertMessage);
/* 75 */     builder.setContentText(alertMessage);
/* 76 */     builder.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
/*    */     
/* 78 */     PendingIntent pendingIntent = null;
/*    */     
/* 80 */     if (userRecoverable) {
/* 81 */       Intent launchIntent = new Intent("android.intent.action.VIEW");
/* 82 */       launchIntent.setData(Uri.parse("market://details?id=com.google.android.gms"));
/*    */       
/* 84 */       SharedPreferences sp = applicationContext.getSharedPreferences("ETPush", 0);
/*    */       
/* 86 */       synchronized ("et_notification_play_services_error_code_key") {
/* 87 */         int uniqueId = ((Integer)Config.getETSharedPref(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext), "et_notification_play_services_error_code_key", Integer.valueOf(0))).intValue();
/* 88 */         pendingIntent = PendingIntent.getActivity(applicationContext, uniqueId, launchIntent, 0);
/* 89 */         uniqueId++;
/* 90 */         sp.edit().putInt("et_notification_play_services_error_code_key", uniqueId).commit();
/*    */       }
/*    */     }
/*    */     
/* 94 */     builder.setContentIntent(pendingIntent);
/*    */     
/* 96 */     android.app.Notification notification = builder.build();
/* 97 */     NotificationManager nm = (NotificationManager)applicationContext.getSystemService("notification");
/* 98 */     nm.notify(913131313, notification);
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\util\ETGooglePlayServicesUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */