/*     */ package com.exacttarget.etpushsdk.location.receiver;
/*     */ 
/*     */ import android.app.PendingIntent;
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.location.LocationManager;
/*     */ import android.os.Handler;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.Config;
/*     */ import com.exacttarget.etpushsdk.ETException;
/*     */ import com.exacttarget.etpushsdk.ETLocationManager;
/*     */ import com.exacttarget.etpushsdk.ETPush;
/*     */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*     */ import com.exacttarget.etpushsdk.data.Region;
/*     */ import com.exacttarget.etpushsdk.location.ETLocationUpdateRequester;
/*     */ import com.exacttarget.etpushsdk.location.LocationUpdateRequester;
/*     */ import com.j256.ormlite.dao.Dao;
/*     */ import com.j256.ormlite.stmt.UpdateBuilder;
/*     */ import java.sql.SQLException;
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
/*     */ public class BootReceiver
/*     */   extends BroadcastReceiver
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@BootReceiver";
/*     */   
/*     */   public void onReceive(Context context, Intent intent)
/*     */   {
/*  58 */     if (ETPush.getLogLevel() <= 3) {
/*  59 */       Log.d("jb4ASDK@BootReceiver", "onReceive()");
/*     */     }
/*     */     
/*  62 */     Boolean runOnce = (Boolean)Config.getETSharedPref(context, PreferenceManager.getDefaultSharedPreferences(context), "et_key_run_once", Boolean.valueOf(false));
/*     */     
/*  64 */     if (runOnce.booleanValue()) {
/*  65 */       LocationManager locationManager = (LocationManager)context.getSystemService("location");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  70 */       LocationUpdateRequester locationUpdateRequester = new ETLocationUpdateRequester(locationManager);
/*     */       
/*     */ 
/*     */ 
/*  74 */       Boolean followLocationChanges = (Boolean)Config.getETSharedPref(context, PreferenceManager.getDefaultSharedPreferences(context), "et_key_follow_location_changes", Boolean.valueOf(true));
/*     */       
/*     */ 
/*     */ 
/*  78 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(context.getApplicationContext());
/*     */       try {
/*  80 */         UpdateBuilder<Region, String> updateBuilder = helper.getRegionDao().updateBuilder();
/*  81 */         updateBuilder.updateColumnValue("active", Boolean.FALSE);
/*  82 */         updateBuilder.update();
/*     */       } catch (SQLException e) { Handler handler;
/*  84 */         if (ETPush.getLogLevel() <= 6)
/*  85 */           Log.e("jb4ASDK@BootReceiver", e.getMessage(), e);
/*     */       } finally {
/*     */         Handler handler;
/*  88 */         Handler handler = new Handler(context.getApplicationContext().getMainLooper());
/*  89 */         handler.postDelayed(new Runnable()
/*     */         {
/*     */           public void run() {
/*  92 */             if ((helper != null) && (helper.isOpen()))
/*  93 */               helper.close(); } }, 10000L);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  99 */       if (followLocationChanges.booleanValue())
/*     */       {
/*     */ 
/* 102 */         Intent passiveIntent = new Intent(context, PassiveLocationChangedReceiver.class);
/* 103 */         PendingIntent locationListenerPassivePendingIntent = PendingIntent.getActivity(context, 0, passiveIntent, 134217728);
/* 104 */         locationUpdateRequester.requestPassiveLocationUpdates(300000L, 0L, locationListenerPassivePendingIntent);
/*     */       }
/*     */       try
/*     */       {
/* 108 */         if (Config.isLocationManagerActive()) {
/* 109 */           if (ETLocationManager.locationManager().isWatchingLocation()) {
/* 110 */             ETLocationManager.locationManager().setGeofenceInvalidated(true);
/* 111 */             ETLocationManager.locationManager().startWatchingLocation();
/*     */           }
/* 113 */           if (ETLocationManager.locationManager().isWatchingProximity()) {
/* 114 */             ETLocationManager.locationManager().setProximityInvalidated(true);
/* 115 */             ETLocationManager.locationManager().startWatchingProximity();
/*     */           }
/*     */         }
/*     */       } catch (ETException e) {
/* 119 */         if (ETPush.getLogLevel() <= 6) {
/* 120 */           Log.e("jb4ASDK@BootReceiver", e.getMessage(), e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\location\receiver\BootReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */