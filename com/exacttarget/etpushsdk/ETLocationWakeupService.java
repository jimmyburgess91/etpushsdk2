/*    */ package com.exacttarget.etpushsdk;
/*    */ 
/*    */ import android.app.IntentService;
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
/*    */ public class ETLocationWakeupService
/*    */   extends IntentService
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETLocationWakeupService";
/*    */   
/*    */   public ETLocationWakeupService()
/*    */   {
/* 40 */     super("ETLocationWakeupService");
/*    */   }
/*    */   
/*    */   protected void onHandleIntent(Intent intent)
/*    */   {
/* 45 */     if (ETPush.getLogLevel() <= 3) {
/* 46 */       Log.d("jb4ASDK@ETLocationWakeupService", "onHandleIntent()");
/*    */     }
/*    */     try {
/* 49 */       ETLocationManager.locationManager().awokenByIntent = intent;
/* 50 */       if (ETLocationManager.locationManager().isWatchingLocation()) {
/* 51 */         ETLocationManager.locationManager().startWatchingLocation();
/*    */       }
/*    */       else {
/* 54 */         ETLocationManager.locationManager().stopWatchingLocation();
/*    */       }
/*    */     }
/*    */     catch (ETException e) {
/* 58 */       if (ETPush.getLogLevel() <= 6) {
/* 59 */         Log.e("jb4ASDK@ETLocationWakeupService", e.getMessage(), e);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETLocationWakeupService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */