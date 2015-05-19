/*    */ package com.exacttarget.etpushsdk.location.receiver;
/*    */ 
/*    */ import android.content.BroadcastReceiver;
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.location.Location;
/*    */ import android.os.Bundle;
/*    */ import android.util.Log;
/*    */ import com.exacttarget.etpushsdk.ETPush;
/*    */ import com.exacttarget.etpushsdk.event.LastKnownLocationEvent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocationChangedReceiver
/*    */   extends BroadcastReceiver
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@LocationChangedReceiver";
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 54 */     if (ETPush.getLogLevel() <= 3) {
/* 55 */       Log.d("jb4ASDK@LocationChangedReceiver", "onReceive()");
/*    */     }
/* 57 */     String locationKey = "location";
/* 58 */     String providerEnabledKey = "providerEnabled";
/* 59 */     if ((intent.hasExtra(providerEnabledKey)) && 
/* 60 */       (!intent.getBooleanExtra(providerEnabledKey, true))) {
/* 61 */       Intent providerDisabledIntent = new Intent("com.exacttarget.etpushsdk.active_location_update_provider_disabled");
/* 62 */       context.sendBroadcast(providerDisabledIntent);
/*    */     }
/*    */     
/* 65 */     if (intent.hasExtra(locationKey)) {
/* 66 */       Location location = (Location)intent.getExtras().get(locationKey);
/* 67 */       if (ETPush.getLogLevel() <= 3) {
/* 68 */         Log.d("jb4ASDK@LocationChangedReceiver", "New Active Location: " + location.getLatitude() + ", " + location.getLongitude());
/*    */       }
/* 70 */       EventBus.getDefault().postSticky(new LastKnownLocationEvent(location));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\receiver\LocationChangedReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */