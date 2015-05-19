/*    */ package com.exacttarget.etpushsdk.location;
/*    */ 
/*    */ import android.app.PendingIntent;
/*    */ import android.location.Criteria;
/*    */ import android.location.LocationManager;
/*    */ import android.util.Log;
/*    */ import com.exacttarget.etpushsdk.ETPush;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ETLocationUpdateRequester
/*    */   extends LocationUpdateRequester
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@ETLocationUpdateRequester";
/*    */   
/*    */   public ETLocationUpdateRequester(LocationManager locationManager)
/*    */   {
/* 21 */     super(locationManager);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void requestPassiveLocationUpdates(long minTime, long minDistance, PendingIntent pendingIntent)
/*    */   {
/* 29 */     if (ETPush.getLogLevel() <= 3) {
/* 30 */       Log.d("jb4ASDK@ETLocationUpdateRequester", "requestPassiveLocationUpdates");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 35 */     this.locationManager.requestLocationUpdates("passive", minTime, (float)minDistance, pendingIntent);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void requestLocationUpdates(long minTime, long minDistance, Criteria criteria, PendingIntent pendingIntent)
/*    */   {
/* 43 */     if (ETPush.getLogLevel() <= 3) {
/* 44 */       Log.d("jb4ASDK@ETLocationUpdateRequester", "requestLocationUpdates");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 50 */     this.locationManager.requestLocationUpdates(minTime, (float)minDistance, criteria, pendingIntent);
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\ETLocationUpdateRequester.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */