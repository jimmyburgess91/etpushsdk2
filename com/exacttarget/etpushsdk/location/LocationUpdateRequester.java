/*    */ package com.exacttarget.etpushsdk.location;
/*    */ 
/*    */ import android.app.PendingIntent;
/*    */ import android.location.Criteria;
/*    */ import android.location.LocationManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LocationUpdateRequester
/*    */ {
/*    */   protected LocationManager locationManager;
/*    */   
/*    */   protected LocationUpdateRequester(LocationManager locationManager)
/*    */   {
/* 18 */     this.locationManager = locationManager;
/*    */   }
/*    */   
/*    */   public void requestLocationUpdates(long minTime, long minDistance, Criteria criteria, PendingIntent pendingIntent) {}
/*    */   
/*    */   public void requestPassiveLocationUpdates(long minTime, long minDistance, PendingIntent pendingIntent) {}
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\LocationUpdateRequester.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */