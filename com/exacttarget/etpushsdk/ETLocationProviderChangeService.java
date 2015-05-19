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
/*    */ public class ETLocationProviderChangeService
/*    */   extends IntentService
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETLocationProviderChangeService";
/*    */   
/*    */   public ETLocationProviderChangeService()
/*    */   {
/* 32 */     super("jb4ASDK@ETLocationProviderChangeService");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void onHandleIntent(Intent intent)
/*    */   {
/* 38 */     if (ETPush.getLogLevel() <= 3) {
/* 39 */       Log.d("jb4ASDK@ETLocationProviderChangeService", "onHandleIntent()");
/*    */     }
/*    */     try {
/* 42 */       if (ETLocationManager.locationManager().areLocationProvidersAvailable()) {
/* 43 */         if (ETPush.getLogLevel() <= 3) {
/* 44 */           Log.d("jb4ASDK@ETLocationProviderChangeService", "Location Provider enabled.");
/*    */         }
/* 46 */         if (ETLocationManager.locationManager().isWatchingLocation()) {
/* 47 */           ETLocationManager.locationManager().startWatchingLocation();
/*    */         }
/*    */       }
/*    */       else {
/* 51 */         if (ETPush.getLogLevel() <= 3) {
/* 52 */           Log.d("jb4ASDK@ETLocationProviderChangeService", "Location Provider disabled.");
/*    */         }
/* 54 */         if (ETLocationManager.locationManager().isWatchingLocation()) {
/* 55 */           ETLocationManager.locationManager().stopWatchingLocation();
/*    */           
/*    */ 
/* 58 */           ETLocationManager.locationManager().setGeoEnabled(true);
/*    */         }
/*    */       }
/*    */       
/* 62 */       ETLocationProviderChangeReceiver.completeWakefulIntent(intent);
/*    */     }
/*    */     catch (ETException e) {
/* 65 */       if (ETPush.getLogLevel() <= 6) {
/* 66 */         Log.e("jb4ASDK@ETLocationProviderChangeService", e.getMessage(), e);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETLocationProviderChangeService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */