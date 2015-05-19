/*     */ package com.exacttarget.etpushsdk.location;
/*     */ 
/*     */ import android.app.PendingIntent;
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.IntentFilter;
/*     */ import android.location.Criteria;
/*     */ import android.location.Location;
/*     */ import android.location.LocationListener;
/*     */ import android.location.LocationManager;
/*     */ import android.os.Bundle;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.ETException;
/*     */ import com.exacttarget.etpushsdk.ETLocationManager;
/*     */ import com.exacttarget.etpushsdk.ETPush;
/*     */ import com.exacttarget.etpushsdk.event.LastKnownLocationEvent;
/*     */ import com.exacttarget.etpushsdk.util.EventBus;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ETLastLocationFinder
/*     */   implements ILastLocationFinder
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@LastLocationFinder";
/*  59 */   protected static String SINGLE_LOCATION_UPDATE_ACTION = "com.exacttarget.etpushsdk.SINGLE_LOCATION_UPDATE_ACTION";
/*     */   
/*     */   protected PendingIntent singleUpatePI;
/*     */   
/*     */   protected LocationListener locationListener;
/*     */   
/*     */   protected LocationManager locationManager;
/*     */   
/*     */   protected Context context;
/*     */   
/*     */   protected Criteria criteria;
/*     */   
/*     */   public ETLastLocationFinder(Context context)
/*     */   {
/*  73 */     this.context = context;
/*  74 */     this.locationManager = ((LocationManager)context.getSystemService("location"));
/*  75 */     this.criteria = new Criteria();
/*  76 */     this.criteria.setAccuracy(1);
/*     */     
/*     */ 
/*     */ 
/*  80 */     Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);
/*  81 */     this.singleUpatePI = PendingIntent.getBroadcast(context, 0, updateIntent, 134217728);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Location getLastBestLocation(int minDistance, long minTime)
/*     */   {
/*  95 */     Location bestResult = null;
/*  96 */     float bestAccuracy = Float.MAX_VALUE;
/*  97 */     long bestTime = Long.MIN_VALUE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 102 */     List<String> matchingProviders = this.locationManager.getAllProviders();
/* 103 */     for (String provider : matchingProviders) {
/* 104 */       Location location = this.locationManager.getLastKnownLocation(provider);
/* 105 */       if (location != null) {
/* 106 */         float accuracy = location.getAccuracy();
/* 107 */         long time = location.getTime();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */         if ((time > minTime) && (accuracy <= bestAccuracy) && (time > bestTime)) {
/* 114 */           bestResult = location;
/* 115 */           bestAccuracy = accuracy;
/* 116 */           bestTime = time;
/*     */         }
/* 118 */         else if ((time < minTime) && (bestAccuracy == Float.MAX_VALUE) && (time > bestTime)) {
/* 119 */           bestResult = location;
/* 120 */           bestTime = time;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */     if ((bestTime < minTime) || (bestAccuracy > minDistance)) {
/* 132 */       if (ETPush.getLogLevel() <= 3) {
/* 133 */         Log.d("jb4ASDK@LastLocationFinder", "starting singleUpdateReceiver");
/*     */       }
/* 135 */       IntentFilter locIntentFilter = new IntentFilter(SINGLE_LOCATION_UPDATE_ACTION);
/* 136 */       this.context.registerReceiver(this.singleUpdateReceiver, locIntentFilter);
/*     */       try {
/* 138 */         this.locationManager.requestSingleUpdate(this.criteria, this.singleUpatePI);
/*     */       }
/*     */       catch (IllegalArgumentException e) {
/* 141 */         if (ETPush.getLogLevel() <= 6) {
/* 142 */           Log.e("jb4ASDK@LastLocationFinder", e.getMessage(), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 148 */       EventBus.getDefault().postSticky(new LastKnownLocationEvent(bestResult));
/*     */     }
/*     */     
/* 151 */     return bestResult;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */   protected BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver()
/*     */   {
/*     */     public void onReceive(Context context, Intent intent) {
/* 162 */       if (ETPush.getLogLevel() <= 3) {
/* 163 */         Log.d("jb4ASDK@LastLocationFinder", "onReceive()");
/*     */       }
/*     */       try
/*     */       {
/* 167 */         context.unregisterReceiver(ETLastLocationFinder.this.singleUpdateReceiver);
/*     */       }
/*     */       catch (IllegalArgumentException ex) {
/* 170 */         if (ETPush.getLogLevel() <= 6) {
/* 171 */           Log.e("jb4ASDK@LastLocationFinder", ex.getMessage());
/*     */         }
/* 173 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 177 */         if (ETLocationManager.locationManager().areLocationProvidersAvailable())
/*     */         {
/* 179 */           String key = "location";
/* 180 */           Location location = (Location)intent.getExtras().get(key);
/*     */           
/* 182 */           if ((ETLastLocationFinder.this.locationListener != null) && (location != null)) {
/* 183 */             ETLastLocationFinder.this.locationListener.onLocationChanged(location);
/*     */           }
/* 185 */           EventBus.getDefault().postSticky(new LastKnownLocationEvent(location));
/*     */         }
/*     */         
/* 188 */         ETLastLocationFinder.this.locationManager.removeUpdates(ETLastLocationFinder.this.singleUpatePI);
/*     */       }
/*     */       catch (ETException e) {
/* 191 */         if (ETPush.getLogLevel() <= 6) {
/* 192 */           Log.e("jb4ASDK@LastLocationFinder", e.getMessage());
/*     */         }
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   public void setChangedLocationListener(LocationListener l)
/*     */   {
/* 202 */     this.locationListener = l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void cancel()
/*     */   {
/* 209 */     this.locationManager.removeUpdates(this.singleUpatePI);
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\ETLastLocationFinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */