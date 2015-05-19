/*     */ package com.exacttarget.etpushsdk;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.SharedPreferences;
/*     */ import android.content.SharedPreferences.Editor;
/*     */ import android.content.pm.ApplicationInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.os.Bundle;
/*     */ import android.util.Log;
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
/*     */ public class Config
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@Config";
/*     */   public static final String ET_KEY_RUN_ONCE = "et_key_run_once";
/*     */   public static final String ET_KEY_FOLLOW_LOCATION_CHANGES = "et_key_follow_location_changes";
/*     */   public static final String ET_SHARED_PREFS = "ETPush";
/*     */   public static final int ACTIVE_MAX_DISTANCE = 100;
/*     */   public static final long ACTIVE_MAX_TIME = 900000L;
/*     */   public static final int PASSIVE_MAX_DISTANCE = 0;
/*     */   public static final long PASSIVE_MAX_TIME = 300000L;
/*     */   public static final String ACTIVE_LOCATION_UPDATE_PROVIDER_DISABLED = "com.exacttarget.etpushsdk.active_location_update_provider_disabled";
/*     */   private static final String ET_BASE_URL = "https://consumer.exacttargetapis.com";
/*     */   protected static final String ET_REGISTRATION_URL = "https://consumer.exacttargetapis.com/device/v1/registration?access_token={access_token}";
/*     */   protected static final String ET_ANALYTICS_URL = "https://consumer.exacttargetapis.com/device/v1/event/analytic?access_token={access_token}";
/*     */   protected static final String ET_LOCATION_UPDATE_URL = "https://consumer.exacttargetapis.com/device/v1/location/{et_app_id}?access_token={access_token}";
/*     */   protected static final String ET_GEOFENCE_URL = "https://consumer.exacttargetapis.com/device/v1/location/{et_app_id}/fence/?latitude={latitude}&longitude={longitude}&deviceid={device_id}&access_token={access_token}";
/*     */   protected static final String ET_CLOUDPAGE_URL = "https://consumer.exacttargetapis.com/device/v1/{et_app_id}/message/?deviceid={device_id}&messagetype={messagetype}&contenttype={contenttype}&access_token={access_token}";
/*     */   protected static final String ET_PROXIMITY_URL = "https://consumer.exacttargetapis.com/device/v1/location/{et_app_id}/proximity/?latitude={latitude}&longitude={longitude}&deviceid={device_id}&access_token={access_token}";
/*     */   protected static final String PA_ANALYTICS_URL = "https://app.igodigital.com/api/v1/collect/process_batch";
/*     */   private static String etAppId;
/*     */   private static String accessToken;
/*  48 */   private static boolean ETanalyticsActive = false;
/*  49 */   private static boolean PIanalyticsActive = false;
/*  50 */   private static boolean locationManagerActive = false;
/*  51 */   private static boolean cloudPagesActive = false;
/*  52 */   private static Boolean readOnly = null;
/*     */   
/*     */   public static String getEtAppId() {
/*  55 */     return etAppId;
/*     */   }
/*     */   
/*     */   protected static void setEtAppId(String etAppId) {
/*  59 */     etAppId = etAppId;
/*     */   }
/*     */   
/*     */   protected static String getAccessToken() {
/*  63 */     return accessToken;
/*     */   }
/*     */   
/*     */   public static void setAccessToken(String accessToken) {
/*  67 */     accessToken = accessToken;
/*     */   }
/*     */   
/*     */   protected static boolean isETanalyticsActive() {
/*  71 */     return ETanalyticsActive;
/*     */   }
/*     */   
/*     */   protected static void setETanalyticsActive(boolean ETanalyticsActive) {
/*  75 */     ETanalyticsActive = ETanalyticsActive;
/*     */   }
/*     */   
/*     */   public static boolean isPIanalyticsActive() {
/*  79 */     return PIanalyticsActive;
/*     */   }
/*     */   
/*     */   public static void setPIanalyticsActive(boolean PIanalyticsActive) {
/*  83 */     PIanalyticsActive = PIanalyticsActive;
/*     */   }
/*     */   
/*     */   public static boolean isLocationManagerActive() {
/*  87 */     return locationManagerActive;
/*     */   }
/*     */   
/*     */   protected static void setLocationManagerActive(boolean locationManagerActive) {
/*  91 */     locationManagerActive = locationManagerActive;
/*     */   }
/*     */   
/*     */   protected static boolean isCloudPagesActive() {
/*  95 */     return cloudPagesActive;
/*     */   }
/*     */   
/*     */   protected static void setCloudPagesActive(boolean cloudPagesActive) {
/*  99 */     cloudPagesActive = cloudPagesActive;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object getETSharedPref(Context context, SharedPreferences oldPrefs, String key, Object defaultValue)
/*     */   {
/* 109 */     SharedPreferences prefs = context.getSharedPreferences("ETPush", 0);
/*     */     Object returnValue;
/* 111 */     Object returnValue; if (prefs.contains(key)) {
/*     */       Object returnValue;
/* 113 */       if ((defaultValue instanceof Boolean)) {
/* 114 */         returnValue = Boolean.valueOf(prefs.getBoolean(key, ((Boolean)defaultValue).booleanValue())); } else { Object returnValue;
/* 115 */         if ((defaultValue instanceof Float)) {
/* 116 */           returnValue = Float.valueOf(prefs.getFloat(key, ((Float)defaultValue).floatValue())); } else { Object returnValue;
/* 117 */           if ((defaultValue instanceof Integer)) {
/* 118 */             returnValue = Integer.valueOf(prefs.getInt(key, ((Integer)defaultValue).intValue())); } else { Object returnValue;
/* 119 */             if ((defaultValue instanceof Long)) {
/* 120 */               returnValue = Long.valueOf(prefs.getLong(key, ((Long)defaultValue).longValue()));
/*     */             } else
/* 122 */               returnValue = prefs.getString(key, (String)defaultValue);
/*     */           }
/*     */         }
/*     */       }
/* 126 */     } else if (oldPrefs.contains(key)) { Object returnValue;
/*     */       Object returnValue;
/* 128 */       if ((defaultValue instanceof Boolean)) {
/* 129 */         returnValue = Boolean.valueOf(oldPrefs.getBoolean(key, ((Boolean)defaultValue).booleanValue())); } else { Object returnValue;
/* 130 */         if ((defaultValue instanceof Float)) {
/* 131 */           returnValue = Float.valueOf(oldPrefs.getFloat(key, ((Float)defaultValue).floatValue())); } else { Object returnValue;
/* 132 */           if ((defaultValue instanceof Integer)) {
/* 133 */             returnValue = Integer.valueOf(oldPrefs.getInt(key, ((Integer)defaultValue).intValue())); } else { Object returnValue;
/* 134 */             if ((defaultValue instanceof Long)) {
/* 135 */               returnValue = Long.valueOf(oldPrefs.getLong(key, ((Long)defaultValue).longValue()));
/*     */             } else
/* 137 */               returnValue = oldPrefs.getString(key, (String)defaultValue);
/*     */           }
/*     */         } }
/* 140 */       oldPrefs.edit().remove(key).commit();
/*     */     }
/*     */     else
/*     */     {
/* 144 */       returnValue = defaultValue;
/*     */     }
/*     */     
/* 147 */     return returnValue;
/*     */   }
/*     */   
/*     */   protected static boolean isReadOnly(Context context) {
/* 151 */     if (readOnly == null) {
/*     */       try {
/* 153 */         ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
/* 154 */         Bundle bundle = ai.metaData;
/* 155 */         readOnly = Boolean.valueOf(bundle.getBoolean("etpush_readonly", false));
/*     */       }
/*     */       catch (Exception e) {
/* 158 */         if (ETPush.getLogLevel() <= 6) {
/* 159 */           Log.e("jb4ASDK@Config", e.getMessage(), e);
/*     */         }
/* 161 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 165 */     return readOnly.booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\Config.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */