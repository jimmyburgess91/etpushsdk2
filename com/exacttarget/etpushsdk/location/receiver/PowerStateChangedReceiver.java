/*    */ package com.exacttarget.etpushsdk.location.receiver;
/*    */ 
/*    */ import android.content.BroadcastReceiver;
/*    */ import android.content.ComponentName;
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.content.pm.PackageManager;
/*    */ import android.util.Log;
/*    */ import com.exacttarget.etpushsdk.Config;
/*    */ import com.exacttarget.etpushsdk.ETException;
/*    */ import com.exacttarget.etpushsdk.ETLocationManager;
/*    */ import com.exacttarget.etpushsdk.ETPush;
/*    */ import com.exacttarget.etpushsdk.event.PowerStatusChangedEvent;
/*    */ import com.exacttarget.etpushsdk.util.EventBus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PowerStateChangedReceiver
/*    */   extends BroadcastReceiver
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@PowerStateChangedReceiver";
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 51 */     if (ETPush.getLogLevel() <= 3) {
/* 52 */       Log.d("jb4ASDK@PowerStateChangedReceiver", "onReceive()");
/*    */     }
/* 54 */     boolean batteryLow = intent.getAction().equals("android.intent.action.BATTERY_LOW");
/*    */     
/* 56 */     PackageManager pm = context.getPackageManager();
/* 57 */     ComponentName passiveLocationReceiver = new ComponentName(context, PassiveLocationChangedReceiver.class);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 62 */     pm.setComponentEnabledSetting(passiveLocationReceiver, batteryLow ? 2 : 0, 1);
/*    */     
/*    */     try
/*    */     {
/* 66 */       if ((Config.isLocationManagerActive()) && 
/* 67 */         (ETLocationManager.locationManager().isWatchingLocation())) {
/* 68 */         if (batteryLow) {
/* 69 */           ETLocationManager.locationManager().enterLowPowerMode();
/*    */         }
/*    */         else {
/* 72 */           ETLocationManager.locationManager().exitLowPowerMode();
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (ETException e)
/*    */     {
/* 78 */       if (ETPush.getLogLevel() <= 6) {
/* 79 */         Log.e("jb4ASDK@PowerStateChangedReceiver", e.getMessage(), e);
/*    */       }
/*    */     }
/* 82 */     EventBus.getDefault().postSticky(new PowerStatusChangedEvent(batteryLow ? 0 : 1));
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\receiver\PowerStateChangedReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */