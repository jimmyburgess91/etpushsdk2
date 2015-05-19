/*    */ package com.exacttarget.etpushsdk;
/*    */ 
/*    */ import android.app.IntentService;
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
/*    */ 
/*    */ 
/*    */ public class ETGeofenceIntentService
/*    */   extends IntentService
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETGeofenceIntentService";
/*    */   protected static final String PARAM_DATABASE_ID = "et_param_database_id";
/*    */   protected static final String PARAM_TRANSITION_TYPE = "et_param_transition_type";
/*    */   
/*    */   public ETGeofenceIntentService()
/*    */   {
/* 46 */     super("jb4ASDK@ETGeofenceIntentService");
/*    */   }
/*    */   
/*    */   protected void onHandleIntent(Intent intent)
/*    */   {
/* 51 */     if (ETPush.getLogLevel() <= 3) {
/* 52 */       Log.d("jb4ASDK@ETGeofenceIntentService", "onHandleIntent()");
/*    */     }
/*    */     
/* 55 */     String regionId = intent.getStringExtra("et_param_database_id");
/* 56 */     int transitionType = intent.getIntExtra("et_param_transition_type", -1);
/*    */     try
/*    */     {
/* 59 */       if (("~~m@g1c_f3nc3~~".equals(regionId)) && (2 == transitionType)) {
/* 60 */         if (ETPush.getLogLevel() <= 3) {
/* 61 */           Log.d("jb4ASDK@ETGeofenceIntentService", "Magic fence was exited, get new fence data");
/*    */         }
/* 63 */         ETLocationManager.locationManager().setGeofenceInvalidated(true);
/* 64 */         if (ETLocationManager.locationManager().isWatchingLocation()) {
/* 65 */           ETLocationManager.locationManager().startWatchingLocation();
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 70 */         ETPush.pushManager().showFenceOrProximityMessage(regionId, transitionType, -1);
/*    */       }
/*    */     }
/*    */     catch (ETException e) {
/* 74 */       if (ETPush.getLogLevel() <= 6) {
/* 75 */         Log.e("jb4ASDK@ETGeofenceIntentService", e.getMessage(), e);
/*    */       }
/*    */     }
/*    */     
/* 79 */     WakefulBroadcastReceiver.completeWakefulIntent(intent);
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETGeofenceIntentService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */