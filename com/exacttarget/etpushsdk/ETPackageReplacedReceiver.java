/*    */ package com.exacttarget.etpushsdk;
/*    */ 
/*    */ import android.content.BroadcastReceiver;
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
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
/*    */ 
/*    */ 
/*    */ public class ETPackageReplacedReceiver
/*    */   extends BroadcastReceiver
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETPackageReplacedReceiver";
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 44 */     if (ETPush.getLogLevel() <= 3) {
/* 45 */       Log.d("jb4ASDK@ETPackageReplacedReceiver", "onReceive");
/*    */     }
/*    */     try {
/* 48 */       if (ETPush.getLogLevel() <= 3) {
/* 49 */         Log.d("jb4ASDK@ETPackageReplacedReceiver", "Re-registering for remote notifications due to app upgrade.");
/*    */       }
/* 51 */       ETPush.pushManager().registerForRemoteNotifications(true);
/*    */     } catch (ETException e) {
/* 53 */       if (ETPush.getLogLevel() <= 6) {
/* 54 */         Log.e("jb4ASDK@ETPackageReplacedReceiver", e.getMessage(), e);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETPackageReplacedReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */