/*    */ package com.exacttarget.etpushsdk;
/*    */ 
/*    */ import android.app.IntentService;
/*    */ import android.content.Intent;
/*    */ import android.util.Log;
/*    */ import com.exacttarget.etpushsdk.location.ILastLocationFinder;
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
/*    */ public class ETLocationTimeoutService
/*    */   extends IntentService
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETLocationTimeoutService";
/*    */   
/*    */   public ETLocationTimeoutService()
/*    */   {
/* 40 */     super("ETLocationTimeoutService");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void onHandleIntent(Intent intent)
/*    */   {
/* 46 */     if (ETPush.getLogLevel() <= 3) {
/* 47 */       Log.d("jb4ASDK@ETLocationTimeoutService", "onHandleIntent()");
/*    */     }
/*    */     try {
/* 50 */       if (ETLocationManager.locationManager().lastLocationFinder != null) {
/* 51 */         ETLocationManager.locationManager().lastLocationFinder.cancel();
/*    */       }
/* 53 */       ETLocationTimeoutReceiver.completeWakefulIntent(intent);
/* 54 */       ETLocationManager.locationManager().completeWakefulIntent();
/*    */     }
/*    */     catch (ETException e) {
/* 57 */       if (ETPush.getLogLevel() <= 6) {
/* 58 */         Log.e("jb4ASDK@ETLocationTimeoutService", e.getMessage(), e);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETLocationTimeoutService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */