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
/*    */ public class ETLocationTimeoutReceiver
/*    */   extends WakefulBroadcastReceiver
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETLocationTimeoutReceiver";
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 42 */     if (ETPush.getLogLevel() <= 3) {
/* 43 */       Log.d("jb4ASDK@ETLocationTimeoutReceiver", "onReceive()");
/*    */     }
/* 45 */     Intent serviceIntent = new Intent(context, ETLocationTimeoutService.class);
/* 46 */     startWakefulService(context, serviceIntent);
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETLocationTimeoutReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */