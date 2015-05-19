/*     */ package com.exacttarget.etpushsdk;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.SharedPreferences;
/*     */ import android.os.Handler;
/*     */ import android.support.v4.content.WakefulBroadcastReceiver;
/*     */ import android.text.TextUtils;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.data.AnalyticItem;
/*     */ import com.exacttarget.etpushsdk.data.AnalyticPiItem;
/*     */ import com.exacttarget.etpushsdk.data.BeaconRequest;
/*     */ import com.exacttarget.etpushsdk.data.CloudPagesResponse;
/*     */ import com.exacttarget.etpushsdk.data.DeviceData;
/*     */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*     */ import com.exacttarget.etpushsdk.data.GeofenceRequest;
/*     */ import com.exacttarget.etpushsdk.data.LocationUpdate;
/*     */ import com.exacttarget.etpushsdk.data.Message;
/*     */ import com.exacttarget.etpushsdk.data.Registration;
/*     */ import com.exacttarget.etpushsdk.event.AnalyticItemEvent;
/*     */ import com.exacttarget.etpushsdk.event.BeaconResponseEvent;
/*     */ import com.exacttarget.etpushsdk.event.GeofenceResponseEvent;
/*     */ import com.exacttarget.etpushsdk.event.LocationUpdateEvent;
/*     */ import com.exacttarget.etpushsdk.event.RegistrationEvent;
/*     */ import com.exacttarget.etpushsdk.util.JSONUtil;
/*     */ import com.j256.ormlite.dao.Dao;
/*     */ import com.j256.ormlite.stmt.QueryBuilder;
/*     */ import com.j256.ormlite.stmt.Where;
/*     */ import java.io.Serializable;
/*     */ import java.sql.SQLException;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
/*     */ import org.json.JSONObject;
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
/*     */ public class ETSendDataReceiver
/*     */   extends WakefulBroadcastReceiver
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@ETSendDataReceiver";
/*     */   public static final String SEND_TYPE_EXTRA = "et_send_type_extra";
/*     */   protected static final String SEND_TYPE_ET_ANALYTIC_EVENTS = "et_send_type_analytic_events";
/*     */   protected static final String SEND_TYPE_PI_ANALYTIC_EVENTS = "pi_send_type_analytic_events";
/*     */   protected static final String SEND_TYPE_REGISTRATION = "et_send_type_registration";
/*     */   protected static final String SEND_TYPE_LOCATION = "et_send_type_location";
/*     */   protected static final String SEND_TYPE_GEOFENCE_REQUEST = "et_send_type_geofence";
/*     */   protected static final String SEND_TYPE_PROXIMITY_REQUEST = "et_send_type_proximity";
/*     */   protected static final String SEND_TYPE_CLOUDPAGE_REQUEST = "et_send_type_cloudpage";
/*     */   public static final String SEND_TYPE_CUSTOM_APP_REQUEST = "et_send_type_custom_app_request";
/*  83 */   private static final Long FIVE_MINUTES_IN_MILLIS = Long.valueOf(300000L);
/*     */   
/*  85 */   private final DecimalFormat latLngFormat = new DecimalFormat("#.######");
/*     */   
/*  87 */   private static SharedPreferences sp = null;
/*     */   
/*     */   public void onReceive(Context context, Intent intent)
/*     */   {
/*  91 */     String sendType = intent.getStringExtra("et_send_type_extra");
/*     */     
/*  93 */     if (ETPush.getLogLevel() <= 3) {
/*  94 */       Log.d("jb4ASDK@ETSendDataReceiver", "onReceive()");
/*     */     }
/*     */     
/*  97 */     Long now = Long.valueOf(System.currentTimeMillis());
/*     */     
/*  99 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(context.getApplicationContext());
/*     */     try {
/* 101 */       if (ETPush.getLogLevel() <= 3) {
/* 102 */         Log.d("jb4ASDK@ETSendDataReceiver", "Request: " + sendType);
/*     */       }
/* 104 */       if (("et_send_type_analytic_events".equals(sendType)) && (!Config.isReadOnly(context)))
/*     */       {
/* 106 */         Dao<AnalyticItem, Integer> analyticItemDao = helper.getAnalyticItemDao();
/* 107 */         List<AnalyticItem> analyticEventList = analyticItemDao.queryBuilder().orderBy("id", true).where().eq("pi_app_key", "").and().eq("ready_to_send", Boolean.TRUE).and().lt("last_sent", Long.valueOf(now.longValue() - FIVE_MINUTES_IN_MILLIS.longValue())).query();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */         List<Integer> analyticEventIds = new ArrayList();
/* 118 */         for (AnalyticItem analyticEvent : analyticEventList) {
/* 119 */           analyticEvent.setLastSent(now);
/* 120 */           analyticItemDao.update(analyticEvent);
/* 121 */           analyticEventIds.add(analyticEvent.getId());
/*     */         }
/*     */         
/* 124 */         if (ETPush.getLogLevel() <= 3) {
/* 125 */           Log.d("jb4ASDK@ETSendDataReceiver", "ET Items: " + JSONUtil.objectToJson(analyticEventList));
/*     */         }
/*     */         
/* 128 */         Intent intentService = new Intent(context, ETSendDataIntentService.class);
/* 129 */         intentService.putExtra("param_database_ids", (Serializable)analyticEventIds);
/* 130 */         intentService.putExtra("param_http_method", "POST");
/* 131 */         intentService.putExtra("param_http_url", "https://consumer.exacttargetapis.com/device/v1/event/analytic?access_token={access_token}");
/* 132 */         intentService.putExtra("param_http_response_type", AnalyticItemEvent.class.getName());
/* 133 */         intentService.putExtra("param_data_json", JSONUtil.objectToJson(analyticEventList));
/* 134 */         startWakefulService(context, intentService);
/*     */       }
/* 136 */       else if (("pi_send_type_analytic_events".equals(sendType)) && (!Config.isReadOnly(context)))
/*     */       {
/*     */         try
/*     */         {
/* 140 */           Dao<AnalyticItem, Integer> analyticItemDao = helper.getAnalyticItemDao();
/* 141 */           List<AnalyticItem> analyticEventList = analyticItemDao.queryBuilder().orderBy("id", true).where().ne("pi_app_key", "").and().eq("ready_to_send", Boolean.TRUE).and().lt("last_sent", Long.valueOf(now.longValue() - FIVE_MINUTES_IN_MILLIS.longValue())).query();
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */           SharedPreferences prefs = context.getSharedPreferences("ETPush", 0);
/*     */           
/*     */ 
/* 154 */           String piUserId = prefs.getString("et_user_id_cache", "");
/* 155 */           String piSessionId = prefs.getString("et_session_id_cache", "");
/*     */           
/* 157 */           AnalyticPiItem analyticPiItem = new AnalyticPiItem(context);
/*     */           
/* 159 */           analyticPiItem.setApiKey("849f26e2-2df6-11e4-ab12-14109fdc48df");
/* 160 */           analyticPiItem.setAppId(Config.getEtAppId());
/* 161 */           analyticPiItem.setUserId(piUserId);
/* 162 */           analyticPiItem.setSessionId(piSessionId);
/*     */           
/* 164 */           analyticPiItem.setPushEnabled(Boolean.valueOf(ETPush.pushManager().isPushEnabled()));
/* 165 */           analyticPiItem.setManufacturer(prefs.getString("et_manufacturer_cache", ""));
/* 166 */           analyticPiItem.setPlatform(prefs.getString("et_platform_cache", ""));
/* 167 */           analyticPiItem.setPlatformVersion(prefs.getString("et_platform_version_cache", ""));
/* 168 */           analyticPiItem.setDeviceType(prefs.getString("et_model_cache", ""));
/* 169 */           analyticPiItem.setDeviceId(prefs.getString("et_device_id_cache", ""));
/*     */           
/* 171 */           if (Config.isLocationManagerActive()) {
/* 172 */             ETLocationManager locationManager = ETLocationManager.locationManager();
/* 173 */             if (locationManager.isWatchingLocation()) {
/* 174 */               String latitude = prefs.getString("et_last_location_latitude", "");
/* 175 */               String longitude = prefs.getString("et_last_location_longitude", "");
/* 176 */               if (((!TextUtils.isEmpty(latitude) ? 1 : 0) & (!TextUtils.isEmpty(longitude) ? 1 : 0)) != 0) {
/* 177 */                 analyticPiItem.setLatitude(Double.valueOf(latitude).doubleValue());
/* 178 */                 analyticPiItem.setLongitude(Double.valueOf(longitude).doubleValue());
/*     */               }
/*     */             }
/*     */           }
/*     */           
/* 183 */           List<Integer> analyticEventIds = new ArrayList();
/* 184 */           for (AnalyticItem analyticEvent : analyticEventList) {
/* 185 */             analyticEvent.setLastSent(now);
/* 186 */             analyticItemDao.update(analyticEvent);
/* 187 */             analyticEventIds.add(analyticEvent.getId());
/*     */             
/* 189 */             Calendar startTime = Calendar.getInstance();
/* 190 */             startTime.setTime(analyticEvent.getEventDate());
/*     */             
/* 192 */             Calendar endTime = Calendar.getInstance();
/* 193 */             endTime.setTimeInMillis(startTime.getTimeInMillis());
/* 194 */             endTime.add(13, analyticEvent.getValue().intValue());
/*     */             
/* 196 */             analyticPiItem.addTimeInApp(startTime.getTime(), endTime.getTime(), analyticEvent.getAnalyticTypes().contains(Integer.valueOf(2)));
/*     */             
/* 198 */             if (ETPush.getLogLevel() <= 3) {
/* 199 */               SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
/* 200 */               formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
/* 201 */               Log.d("jb4ASDK@ETSendDataReceiver", "Time in app start time: " + formatDate.format(startTime.getTime()) + " end time: " + formatDate.format(endTime.getTime()) + " from push: " + analyticEvent.getAnalyticTypes().contains(Integer.valueOf(2)));
/*     */             }
/*     */           }
/*     */           
/* 205 */           if (ETPush.getLogLevel() <= 3) {
/* 206 */             Log.d("jb4ASDK@ETSendDataReceiver", "PI Items:" + analyticPiItem.getJSONPayload().toString());
/*     */           }
/*     */           
/* 209 */           Intent intentService = new Intent(context, ETSendDataIntentService.class);
/* 210 */           intentService.putExtra("param_database_ids", (Serializable)analyticEventIds);
/* 211 */           intentService.putExtra("param_http_method", "POST");
/* 212 */           intentService.putExtra("param_http_url", "https://app.igodigital.com/api/v1/collect/process_batch");
/* 213 */           intentService.putExtra("param_http_response_type", "pi_analytics");
/* 214 */           intentService.putExtra("param_data_json", analyticPiItem.getJSONPayload().toString());
/* 215 */           startWakefulService(context, intentService);
/*     */         }
/*     */         catch (Exception e) {
/* 218 */           if (ETPush.getLogLevel() <= 6) {
/* 219 */             Log.e("jb4ASDK@ETSendDataReceiver", e.getMessage(), e);
/*     */           }
/*     */         }
/*     */       }
/* 223 */       else if (("et_send_type_registration".equals(sendType)) && (!Config.isReadOnly(context))) {
/* 224 */         Dao<Registration, Integer> registrationDao = helper.getRegistrationDao();
/*     */         
/* 226 */         Registration registration = (Registration)registrationDao.queryBuilder().orderBy("id", false).queryForFirst();
/*     */         
/*     */ 
/*     */ 
/* 230 */         if ((registration != null) && (registration.getLastSent().longValue() < now.longValue() - FIVE_MINUTES_IN_MILLIS.longValue())) {
/* 231 */           registration.setLastSent(now);
/* 232 */           registrationDao.update(registration);
/*     */           
/* 234 */           Intent intentService = new Intent(context, ETSendDataIntentService.class);
/* 235 */           intentService.putExtra("param_database_id", registration.getId());
/* 236 */           if (ETPush.getLogLevel() <= 3) {
/* 237 */             Log.d("jb4ASDK@ETSendDataReceiver", "REGISTRATION ID: " + registration.getId());
/*     */           }
/* 239 */           intentService.putExtra("param_http_method", "POST");
/* 240 */           intentService.putExtra("param_http_url", "https://consumer.exacttargetapis.com/device/v1/registration?access_token={access_token}");
/* 241 */           intentService.putExtra("param_http_response_type", RegistrationEvent.class.getName());
/* 242 */           intentService.putExtra("param_data_json", JSONUtil.objectToJson(registration));
/* 243 */           startWakefulService(context, intentService);
/*     */ 
/*     */         }
/* 246 */         else if (ETPush.getLogLevel() <= 3) {
/* 247 */           Log.d("jb4ASDK@ETSendDataReceiver", "SKIP registration send.");
/*     */         }
/*     */       }
/*     */       else {
/*     */         Dao<LocationUpdate, Integer> locationDao;
/* 252 */         if (("et_send_type_location".equals(sendType)) && (!Config.isReadOnly(context))) {
/* 253 */           locationDao = helper.getLocationUpdateDao();
/* 254 */           List<LocationUpdate> locationList = locationDao.queryBuilder().orderBy("id", true).where().lt("last_sent", Long.valueOf(now.longValue() - FIVE_MINUTES_IN_MILLIS.longValue())).query();
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 260 */           for (LocationUpdate locationUpdate : locationList) {
/* 261 */             locationUpdate.setLastSent(now);
/* 262 */             locationDao.update(locationUpdate);
/*     */             
/* 264 */             if (ETPush.getLogLevel() <= 3) {
/* 265 */               Log.d("jb4ASDK@ETSendDataReceiver", "Send Location Update for: " + locationUpdate.getId());
/*     */             }
/* 267 */             Intent intentService = new Intent(context, ETSendDataIntentService.class);
/* 268 */             intentService.putExtra("param_database_id", locationUpdate.getId());
/* 269 */             intentService.putExtra("param_http_method", "POST");
/* 270 */             intentService.putExtra("param_http_url", "https://consumer.exacttargetapis.com/device/v1/location/{et_app_id}?access_token={access_token}");
/* 271 */             intentService.putExtra("param_http_response_type", LocationUpdateEvent.class.getName());
/* 272 */             intentService.putExtra("param_data_json", JSONUtil.objectToJson(locationUpdate));
/* 273 */             startWakefulService(context, intentService);
/*     */           }
/*     */         }
/* 276 */         else if ("et_send_type_geofence".equals(sendType)) {
/* 277 */           Dao<GeofenceRequest, Integer> geofenceDao = helper.getGeofenceRequestDao();
/* 278 */           List<GeofenceRequest> geofenceRequestList = geofenceDao.queryForAll();
/* 279 */           if ((geofenceRequestList != null) && (geofenceRequestList.size() > 0)) {
/* 280 */             GeofenceRequest lastRequest = (GeofenceRequest)geofenceRequestList.get(geofenceRequestList.size() - 1);
/* 281 */             geofenceDao.delete(geofenceRequestList);
/*     */             
/* 283 */             String url = "https://consumer.exacttargetapis.com/device/v1/location/{et_app_id}/fence/?latitude={latitude}&longitude={longitude}&deviceid={device_id}&access_token={access_token}".replaceAll("\\{device_id\\}", lastRequest.getDeviceId()).replaceAll("\\{latitude\\}", this.latLngFormat.format(lastRequest.getLatitude())).replaceAll("\\{longitude\\}", this.latLngFormat.format(lastRequest.getLongitude()));
/*     */             
/*     */ 
/*     */ 
/* 287 */             if (Config.isReadOnly(context.getApplicationContext())) {
/* 288 */               url = url + "&all=true";
/*     */             }
/* 290 */             Intent intentService = new Intent(context, ETSendDataIntentService.class);
/* 291 */             intentService.putExtra("param_http_method", "GET");
/* 292 */             intentService.putExtra("param_http_url", url);
/* 293 */             intentService.putExtra("param_http_response_type", GeofenceResponseEvent.class.getName());
/* 294 */             startWakefulService(context, intentService);
/*     */           }
/*     */         }
/* 297 */         else if ("et_send_type_proximity".equals(sendType)) {
/* 298 */           Dao<BeaconRequest, Integer> beaconDao = helper.getBeaconRequestDao();
/* 299 */           List<BeaconRequest> beaconRequestList = beaconDao.queryForAll();
/* 300 */           if ((beaconRequestList != null) && (beaconRequestList.size() > 0)) {
/* 301 */             BeaconRequest lastRequest = (BeaconRequest)beaconRequestList.get(beaconRequestList.size() - 1);
/* 302 */             beaconDao.delete(beaconRequestList);
/*     */             
/* 304 */             String url = "https://consumer.exacttargetapis.com/device/v1/location/{et_app_id}/proximity/?latitude={latitude}&longitude={longitude}&deviceid={device_id}&access_token={access_token}".replaceAll("\\{device_id\\}", lastRequest.getDeviceId()).replaceAll("\\{latitude\\}", this.latLngFormat.format(lastRequest.getLatitude())).replaceAll("\\{longitude\\}", this.latLngFormat.format(lastRequest.getLongitude()));
/*     */             
/*     */ 
/*     */ 
/* 308 */             if (Config.isReadOnly(context.getApplicationContext())) {
/* 309 */               url = url + "&all=true";
/*     */             }
/* 311 */             Intent intentService = new Intent(context, ETSendDataIntentService.class);
/* 312 */             intentService.putExtra("param_http_method", "GET");
/* 313 */             intentService.putExtra("param_http_url", url);
/* 314 */             intentService.putExtra("param_http_response_type", BeaconResponseEvent.class.getName());
/* 315 */             startWakefulService(context, intentService);
/*     */           }
/*     */         }
/* 318 */         else if ("et_send_type_cloudpage".equals(sendType)) {
/* 319 */           if (sp == null) {
/* 320 */             sp = context.getSharedPreferences("ETPush", 0);
/*     */           }
/* 322 */           String url = "https://consumer.exacttargetapis.com/device/v1/{et_app_id}/message/?deviceid={device_id}&messagetype={messagetype}&contenttype={contenttype}&access_token={access_token}".replaceAll("\\{device_id\\}", new DeviceData().uniqueDeviceIdentifier(context)).replaceAll("\\{messagetype\\}", Message.MESSAGE_TYPE_BASIC.toString()).replaceAll("\\{contenttype\\}", Message.CONTENT_TYPE_CLOUD_PAGE_ONLY.toString());
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 327 */           Intent intentService = new Intent(context, ETSendDataIntentService.class);
/* 328 */           intentService.putExtra("param_http_method", "GET");
/* 329 */           intentService.putExtra("param_http_url", url);
/* 330 */           intentService.putExtra("param_http_response_type", CloudPagesResponse.class.getName());
/* 331 */           startWakefulService(context, intentService);
/*     */         }
/* 333 */         else if ("et_send_type_custom_app_request".equals(sendType))
/*     */         {
/* 335 */           Intent intentService = new Intent(context, ETSendDataIntentService.class);
/* 336 */           intentService.putExtra("param_http_method", intent.getStringExtra("param_http_method"));
/* 337 */           intentService.putExtra("param_http_url", intent.getStringExtra("param_http_url"));
/* 338 */           intentService.putExtra("param_http_response_type", intent.getStringExtra("param_http_response_type"));
/* 339 */           String json = intent.getStringExtra("param_data_json");
/* 340 */           if ((json != null) && (json.length() > 0)) {
/* 341 */             intentService.putExtra("param_data_json", json);
/*     */           }
/* 343 */           startWakefulService(context, intentService);
/*     */ 
/*     */         }
/* 346 */         else if ((!Config.isReadOnly(context)) && 
/* 347 */           (ETPush.getLogLevel() <= 6)) {
/* 348 */           Log.e("jb4ASDK@ETSendDataReceiver", "Unknown SEND_TYPE for ETSendDataReceiver: " + sendType);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SQLException e) {
/*     */       Handler handler;
/* 354 */       if (ETPush.getLogLevel() <= 6) {
/* 355 */         Log.e("jb4ASDK@ETSendDataReceiver", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 359 */       Handler handler = new Handler(context.getMainLooper());
/* 360 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 363 */           if ((helper != null) && (helper.isOpen()))
/* 364 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETSendDataReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */