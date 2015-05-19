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
/*    */ import com.exacttarget.etpushsdk.location.LegacyLastLocationFinder;
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
/*    */ 
/*    */ public class PassiveLocationChangedReceiver
/*    */   extends BroadcastReceiver
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@PassiveLocationChangedReceiver";
/*    */   
/*    */   public void onReceive(Context context, Intent intent)
/*    */   {
/* 56 */     if (ETPush.getLogLevel() <= 3) {
/* 57 */       Log.d("jb4ASDK@PassiveLocationChangedReceiver", "onReceive()");
/*    */     }
/* 59 */     String key = "location";
/* 60 */     Location location = null;
/*    */     
/* 62 */     if (intent.hasExtra(key))
/*    */     {
/*    */ 
/* 65 */       location = (Location)intent.getExtras().get(key);
/*    */ 
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/*    */ 
/*    */ 
/* 73 */       LegacyLastLocationFinder lastLocationFinder = new LegacyLastLocationFinder(context);
/* 74 */       location = lastLocationFinder.getLastBestLocation(0, System.currentTimeMillis() - 300000L);
/*    */       
/*    */ 
/* 77 */       LastKnownLocationEvent lastKnownLocationEvent = (LastKnownLocationEvent)EventBus.getDefault().getStickyEvent(LastKnownLocationEvent.class);
/* 78 */       if (lastKnownLocationEvent != null) {
/* 79 */         Location lastLocation = lastKnownLocationEvent.getLocation();
/*    */         
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 87 */         if ((lastLocation.getTime() > System.currentTimeMillis() - 300000L) || (lastLocation.distanceTo(location) < 0.0F)) {
/* 88 */           location = null;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 94 */     if (location != null) {
/* 95 */       if (ETPush.getLogLevel() <= 3) {
/* 96 */         Log.d("jb4ASDK@PassiveLocationChangedReceiver", "New Passive Location: " + location.getLatitude() + ", " + location.getLongitude());
/*    */       }
/* 98 */       EventBus.getDefault().postSticky(new LastKnownLocationEvent(location));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\receiver\PassiveLocationChangedReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */