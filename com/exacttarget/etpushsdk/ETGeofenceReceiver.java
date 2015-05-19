/*    */ package com.exacttarget.etpushsdk;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.support.v4.content.WakefulBroadcastReceiver;
/*    */ import android.util.Log;
/*    */ import com.google.android.gms.location.Geofence;
/*    */ import com.google.android.gms.location.LocationClient;
/*    */ import java.util.List;
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
/*    */ public class ETGeofenceReceiver
/*    */   extends WakefulBroadcastReceiver
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETGeofenceReceiver";
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 47 */     if (ETPush.getLogLevel() <= 3) {
/* 48 */       Log.d("jb4ASDK@ETGeofenceReceiver", "onReceive()");
/*    */     }
/* 50 */     if (LocationClient.hasError(intent)) {
/* 51 */       handleError(intent);
/* 52 */       WakefulBroadcastReceiver.completeWakefulIntent(intent);
/*    */     }
/*    */     else {
/* 55 */       handleEnterExit(context, intent);
/*    */     }
/*    */   }
/*    */   
/*    */   private void handleEnterExit(Context context, Intent intent) {
/* 60 */     if (ETPush.getLogLevel() <= 3) {
/* 61 */       Log.d("jb4ASDK@ETGeofenceReceiver", "handleEnterExit()");
/*    */     }
/*    */     
/* 64 */     int transition = LocationClient.getGeofenceTransition(intent);
/*    */     
/*    */     int i;
/* 67 */     if ((transition == 1) || (transition == 2))
/*    */     {
/*    */ 
/* 70 */       List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
/* 71 */       i = 0;
/* 72 */       for (Geofence geofence : geofences) {
/* 73 */         if (ETPush.getLogLevel() <= 3) {
/* 74 */           Log.d("jb4ASDK@ETGeofenceReceiver", "FenceTripped: " + i + ", " + geofence.getRequestId());
/* 75 */           i++;
/*    */         }
/* 77 */         Intent intentService = new Intent(context, ETGeofenceIntentService.class);
/* 78 */         intentService.putExtra("et_param_database_id", geofence.getRequestId());
/* 79 */         intentService.putExtra("et_param_transition_type", transition);
/* 80 */         startWakefulService(context, intentService);
/*    */       }
/*    */       
/*    */     }
/*    */     else
/*    */     {
/* 86 */       if (ETPush.getLogLevel() <= 6) {
/* 87 */         Log.e("jb4ASDK@ETGeofenceReceiver", "Invalid Geofence Transition Type: " + transition);
/*    */       }
/* 89 */       WakefulBroadcastReceiver.completeWakefulIntent(intent);
/*    */     }
/*    */   }
/*    */   
/*    */   private void handleError(Intent intent) {
/* 94 */     int errorCode = LocationClient.getErrorCode(intent);
/*    */     
/* 96 */     if (ETPush.getLogLevel() <= 6) {
/* 97 */       Log.e("jb4ASDK@ETGeofenceReceiver", "ERROR, LocationStatusCode: " + errorCode);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETGeofenceReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */