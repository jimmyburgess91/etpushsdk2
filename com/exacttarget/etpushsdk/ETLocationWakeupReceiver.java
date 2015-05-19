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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ETLocationWakeupReceiver
/*    */   extends WakefulBroadcastReceiver
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETLocationWakeupReceiver";
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 42 */     if (ETPush.getLogLevel() <= 3) {
/* 43 */       Log.d("jb4ASDK@ETLocationWakeupReceiver", "onReceive()");
/*    */     }
/* 45 */     Intent serviceIntent = new Intent(context, ETLocationWakeupService.class);
/* 46 */     startWakefulService(context, serviceIntent);
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETLocationWakeupReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */