/*     */ package com.exacttarget.etpushsdk.location;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.location.Criteria;
/*     */ import android.location.Location;
/*     */ import android.location.LocationListener;
/*     */ import android.location.LocationManager;
/*     */ import android.os.Bundle;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.ETPush;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LegacyLastLocationFinder
/*     */   implements ILastLocationFinder
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@PreGingerbreadLastLocationFinder";
/*     */   protected LocationListener locationListener;
/*     */   protected LocationManager locationManager;
/*     */   protected Criteria criteria;
/*     */   protected Context context;
/*     */   
/*     */   public LegacyLastLocationFinder(Context context)
/*     */   {
/*  64 */     this.locationManager = ((LocationManager)context.getSystemService("location"));
/*  65 */     this.criteria = new Criteria();
/*     */     
/*     */ 
/*     */ 
/*  69 */     this.criteria.setAccuracy(2);
/*  70 */     this.context = context;
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
/*  84 */     Location bestResult = null;
/*  85 */     float bestAccuracy = Float.MAX_VALUE;
/*  86 */     long bestTime = Long.MAX_VALUE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  91 */     List<String> matchingProviders = this.locationManager.getAllProviders();
/*  92 */     for (String provider : matchingProviders) {
/*  93 */       Location location = this.locationManager.getLastKnownLocation(provider);
/*  94 */       if (location != null) {
/*  95 */         float accuracy = location.getAccuracy();
/*  96 */         long time = location.getTime();
/*     */         
/*  98 */         if ((time < minTime) && (accuracy < bestAccuracy)) {
/*  99 */           bestResult = location;
/* 100 */           bestAccuracy = accuracy;
/* 101 */           bestTime = time;
/*     */         }
/* 103 */         else if ((time > minTime) && (bestAccuracy == Float.MAX_VALUE) && (time < bestTime)) {
/* 104 */           bestResult = location;
/* 105 */           bestTime = time;
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
/*     */ 
/*     */ 
/*     */ 
/* 119 */     if ((this.locationListener != null) && ((bestTime > minTime) || (bestAccuracy > minDistance))) {
/* 120 */       String provider = this.locationManager.getBestProvider(this.criteria, true);
/* 121 */       if (provider != null) {
/* 122 */         this.locationManager.requestLocationUpdates(provider, 0L, 0.0F, this.singeUpdateListener, this.context.getMainLooper());
/*     */       }
/*     */     }
/* 125 */     return bestResult;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */   protected LocationListener singeUpdateListener = new LocationListener() {
/*     */     public void onLocationChanged(Location location) {
/* 135 */       if (ETPush.getLogLevel() <= 3) {
/* 136 */         Log.d("jb4ASDK@PreGingerbreadLastLocationFinder", "Single Location Update Received: " + location.getLatitude() + "," + location.getLongitude());
/*     */       }
/* 138 */       if ((LegacyLastLocationFinder.this.locationListener != null) && (location != null))
/* 139 */         LegacyLastLocationFinder.this.locationListener.onLocationChanged(location);
/* 140 */       LegacyLastLocationFinder.this.locationManager.removeUpdates(LegacyLastLocationFinder.this.singeUpdateListener);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void onStatusChanged(String provider, int status, Bundle extras) {}
/*     */     
/*     */ 
/*     */     public void onProviderEnabled(String provider) {}
/*     */     
/*     */ 
/*     */     public void onProviderDisabled(String provider) {}
/*     */   };
/*     */   
/*     */ 
/*     */   public void setChangedLocationListener(LocationListener l)
/*     */   {
/* 157 */     this.locationListener = l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void cancel()
/*     */   {
/* 164 */     this.locationManager.removeUpdates(this.singeUpdateListener);
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\LegacyLastLocationFinder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */