/*    */ package com.exacttarget.etpushsdk;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.support.v4.content.WakefulBroadcastReceiver;
/*    */ import android.util.Log;
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
/*    */ public class ETLocationProviderChangeReceiver
/*    */   extends WakefulBroadcastReceiver
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETLocationProviderChangeReceiver";
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 34 */     if (ETPush.getLogLevel() <= 3) {
/* 35 */       Log.d("jb4ASDK@ETLocationProviderChangeReceiver", "onReceive()");
/*    */     }
/*    */     
/* 38 */     if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
/* 39 */       if (ETPush.getLogLevel() <= 3) {
/* 40 */         Log.d("jb4ASDK@ETLocationProviderChangeReceiver", "Providers changed.");
/*    */       }
/* 42 */       Intent serviceIntent = new Intent(context, ETLocationProviderChangeService.class);
/* 43 */       startWakefulService(context, serviceIntent);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETLocationProviderChangeReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */