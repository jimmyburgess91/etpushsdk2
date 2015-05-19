/*      */ package com.exacttarget.etpushsdk;
/*      */ 
/*      */ import android.app.AlarmManager;
/*      */ import android.app.NotificationManager;
/*      */ import android.app.PendingIntent;
/*      */ import android.content.BroadcastReceiver;
/*      */ import android.content.Context;
/*      */ import android.content.Intent;
/*      */ import android.content.IntentFilter;
/*      */ import android.content.ServiceConnection;
/*      */ import android.content.SharedPreferences;
/*      */ import android.content.SharedPreferences.Editor;
/*      */ import android.location.Location;
/*      */ import android.location.LocationManager;
/*      */ import android.os.Bundle;
/*      */ import android.os.Handler;
/*      */ import android.os.Looper;
/*      */ import android.os.RemoteException;
/*      */ import android.os.SystemClock;
/*      */ import android.util.Log;
/*      */ import com.exacttarget.etpushsdk.data.BeaconRequest;
/*      */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*      */ import com.exacttarget.etpushsdk.data.GeofenceRequest;
/*      */ import com.exacttarget.etpushsdk.data.LocationUpdate;
/*      */ import com.exacttarget.etpushsdk.data.Message;
/*      */ import com.exacttarget.etpushsdk.data.RegionMessage;
/*      */ import com.exacttarget.etpushsdk.event.BackgroundEvent;
/*      */ import com.exacttarget.etpushsdk.event.BackgroundEventListener;
/*      */ import com.exacttarget.etpushsdk.event.BeaconRegionEnterEvent;
/*      */ import com.exacttarget.etpushsdk.event.BeaconRegionExitEvent;
/*      */ import com.exacttarget.etpushsdk.event.BeaconRegionRangeEvent;
/*      */ import com.exacttarget.etpushsdk.event.BeaconResponseEvent;
/*      */ import com.exacttarget.etpushsdk.event.GeofenceResponseEvent;
/*      */ import com.exacttarget.etpushsdk.event.GeofenceResponseEventListener;
/*      */ import com.exacttarget.etpushsdk.event.LastKnownLocationEvent;
/*      */ import com.exacttarget.etpushsdk.event.LastKnownLocationEventListener;
/*      */ import com.exacttarget.etpushsdk.event.LocationUpdateEvent;
/*      */ import com.exacttarget.etpushsdk.event.LocationUpdateEventListener;
/*      */ import com.exacttarget.etpushsdk.event.PowerStatusChangedEvent;
/*      */ import com.exacttarget.etpushsdk.location.ETLastLocationFinder;
/*      */ import com.exacttarget.etpushsdk.location.ETLocationUpdateRequester;
/*      */ import com.exacttarget.etpushsdk.location.ILastLocationFinder;
/*      */ import com.exacttarget.etpushsdk.location.LocationUpdateRequester;
/*      */ import com.exacttarget.etpushsdk.location.receiver.PassiveLocationChangedReceiver;
/*      */ import com.exacttarget.etpushsdk.util.ETGooglePlayServicesUtil;
/*      */ import com.exacttarget.etpushsdk.util.EventBus;
/*      */ import com.google.android.gms.common.ConnectionResult;
/*      */ import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
/*      */ import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
/*      */ import com.google.android.gms.location.Geofence;
/*      */ import com.google.android.gms.location.LocationClient;
/*      */ import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
/*      */ import com.google.android.gms.location.LocationClient.OnRemoveGeofencesResultListener;
/*      */ import com.j256.ormlite.dao.Dao;
/*      */ import com.j256.ormlite.stmt.QueryBuilder;
/*      */ import com.j256.ormlite.stmt.UpdateBuilder;
/*      */ import com.j256.ormlite.stmt.Where;
/*      */ import com.radiusnetworks.ibeacon.BleNotAvailableException;
/*      */ import com.radiusnetworks.ibeacon.IBeacon;
/*      */ import com.radiusnetworks.ibeacon.IBeaconConsumer;
/*      */ import com.radiusnetworks.ibeacon.IBeaconManager;
/*      */ import com.radiusnetworks.ibeacon.MonitorNotifier;
/*      */ import com.radiusnetworks.ibeacon.RangeNotifier;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ETLocationManager
/*      */   implements GeofenceResponseEventListener, BackgroundEventListener, LastKnownLocationEventListener, LocationUpdateEventListener
/*      */ {
/*      */   private static final String TAG = "jb4ASDK@ETLocationManager";
/*      */   private static final String GEO_ENABLED_KEY = "et_geo_enabled_key";
/*      */   private static final String GEOFENCE_INVALIDATED_KEY = "et_geofence_invalidated_key";
/*      */   private static final String PROXIMITY_ENABLED_KEY = "et_proximity_enabled_key";
/*      */   private static final String PROXIMITY_INVALIDATED_KEY = "et_proximity_invalidated_key";
/*      */   public static final String ET_LAST_LOCATION_LATITUDE = "et_last_location_latitude";
/*      */   public static final String ET_LAST_LOCATION_LONGITUDE = "et_last_location_longitude";
/*      */   private static ETLocationManager locationManager;
/*      */   private Context applicationContext;
/*      */   private SharedPreferences prefs;
/*      */   private AlarmManager alarmManager;
/*      */   private PendingIntent locationTimeoutPendingIntent;
/*      */   private PendingIntent locationWakeupPendingIntent;
/*      */   protected Intent awokenByIntent;
/*      */   protected ILastLocationFinder lastLocationFinder;
/*      */   private LocationClient locationClient;
/*      */   private RegionMonitor regionMonitor;
/*      */   private IBeaconManager iBeaconManager;
/*      */   private IBeaconMonitor iBeaconMonitor;
/*  115 */   private boolean isWatchingBluetoothChange = false;
/*  116 */   private BroadcastReceiver bluetoothChangeReceiver = new BroadcastReceiver()
/*      */   {
/*      */     public void onReceive(Context context, Intent intent)
/*      */     {
/*  120 */       if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
/*  121 */         if (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1) == 10) {
/*  122 */           if (ETPush.getLogLevel() <= 3) {
/*  123 */             Log.d("jb4ASDK@ETLocationManager", "Bluetooth OFF");
/*      */           }
/*      */           
/*  126 */           if (ETLocationManager.this.isWatchingProximity()) {
/*  127 */             ETLocationManager.this.stopWatchingProximity();
/*  128 */             ETLocationManager.this.setProximityEnabled(true);
/*      */           }
/*      */         }
/*  131 */         else if (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1) == 12) {
/*  132 */           if (ETPush.getLogLevel() <= 3) {
/*  133 */             Log.d("jb4ASDK@ETLocationManager", "Bluetooth ON");
/*      */           }
/*  135 */           if (ETLocationManager.this.isWatchingProximity()) {
/*  136 */             if (!ETLocationManager.this.iBeaconManager.isBound(ETLocationManager.this.iBeaconMonitor)) {
/*  137 */               ETLocationManager.this.setProximityInvalidated(true);
/*  138 */               ETLocationManager.this.iBeaconManager.bind(ETLocationManager.this.iBeaconMonitor);
/*      */             }
/*      */             else
/*      */             {
/*  142 */               ETLocationManager.this.startWatchingProximity();
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   };
/*      */   
/*      */   private ETLocationManager(Context applicationContext) {
/*  151 */     Looper looper = Looper.getMainLooper();
/*  152 */     Handler handler = new Handler(looper);
/*  153 */     handler.post(new Runnable() {
/*      */       public void run() {
/*      */         try {
/*  156 */           Class.forName("android.os.AsyncTask");
/*      */         }
/*      */         catch (ClassNotFoundException e) {
/*  159 */           if (ETPush.getLogLevel() <= 6) {
/*  160 */             Log.e("jb4ASDK@ETLocationManager", e.getMessage(), e);
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/*  168 */     });
/*  169 */     this.applicationContext = applicationContext;
/*      */     
/*  171 */     EventBus.getDefault().register(this);
/*      */     
/*  173 */     this.lastLocationFinder = new ETLastLocationFinder(applicationContext);
/*      */     
/*  175 */     this.prefs = applicationContext.getSharedPreferences("ETPush", 0);
/*      */     
/*  177 */     this.alarmManager = ((AlarmManager)applicationContext.getSystemService("alarm"));
/*      */     
/*  179 */     this.regionMonitor = new RegionMonitor(null);
/*      */     
/*  181 */     this.iBeaconManager = IBeaconManager.getInstanceForApplication(applicationContext);
/*  182 */     this.iBeaconManager.setBackgroundScanPeriod(5000L);
/*  183 */     this.iBeaconManager.setBackgroundBetweenScanPeriod(10000L);
/*  184 */     this.iBeaconMonitor = new IBeaconMonitor(null);
/*      */     
/*  186 */     setGeofenceInvalidated(true);
/*  187 */     setProximityInvalidated(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ETLocationManager locationManager()
/*      */     throws ETException
/*      */   {
/*  196 */     ETPush.initiateLatch(30000L);
/*  197 */     if (Config.isLocationManagerActive()) {
/*  198 */       if (locationManager == null) {
/*  199 */         throw new ETException("You forgot to call readyAimFire first.");
/*      */       }
/*      */       
/*  202 */       return locationManager;
/*      */     }
/*      */     
/*  205 */     throw new ETException("ETLocationManager disabled. Ensure you called readyAimFire and enabled it first.");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static void readyAimFire(Context applicationContext)
/*      */     throws ETException
/*      */   {
/*  215 */     if (locationManager == null) {
/*  216 */       if (ETPush.getLogLevel() <= 3) {
/*  217 */         Log.d("jb4ASDK@ETLocationManager", "readyAimFire()");
/*      */       }
/*  219 */       locationManager = new ETLocationManager(applicationContext);
/*      */       
/*      */ 
/*  222 */       SharedPreferences prefs = applicationContext.getSharedPreferences("ETPush", 0);
/*      */       
/*  224 */       prefs.edit().putBoolean("et_key_run_once", true).commit();
/*      */       
/*      */ 
/*  227 */       LocationManager locationManager = (LocationManager)applicationContext.getSystemService("location");
/*  228 */       LocationUpdateRequester locationUpdateRequester = new ETLocationUpdateRequester(locationManager);
/*  229 */       Intent passiveIntent = new Intent(applicationContext, PassiveLocationChangedReceiver.class);
/*  230 */       PendingIntent locationListenerPassivePendingIntent = PendingIntent.getActivity(applicationContext, 0, passiveIntent, 134217728);
/*  231 */       locationUpdateRequester.requestPassiveLocationUpdates(300000L, 0L, locationListenerPassivePendingIntent);
/*      */       
/*  233 */       if ((!Config.isETanalyticsActive()) && (locationManager.isWatchingLocation()))
/*      */       {
/*  235 */         locationManager.setGeofenceInvalidated(true);
/*  236 */         locationManager.startWatchingLocation();
/*      */       }
/*      */       
/*  239 */       if ((!Config.isETanalyticsActive()) && (locationManager.isWatchingProximity()))
/*      */       {
/*  241 */         locationManager.setProximityInvalidated(true);
/*  242 */         locationManager.startWatchingProximity();
/*      */       }
/*      */     }
/*      */     else {
/*  246 */       throw new ETException("You must have called readyAimFire more than once.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void startWatchingLocation()
/*      */   {
/*  254 */     if (!Config.isLocationManagerActive()) {
/*  255 */       return;
/*      */     }
/*      */     
/*  258 */     if (ETPush.getLogLevel() <= 3) {
/*  259 */       Log.d("jb4ASDK@ETLocationManager", "startWatchingLocation()");
/*      */     }
/*      */     
/*  262 */     setGeoEnabled(true);
/*  263 */     setGeofenceInvalidated(true);
/*      */     
/*  265 */     startLocationBackgroundWatcher();
/*      */   }
/*      */   
/*      */   private void startLocationBackgroundWatcher() {
/*  269 */     if (!areLocationProvidersAvailable())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  281 */       if (ETPush.getLogLevel() <= 5) {
/*  282 */         Log.w("jb4ASDK@ETLocationManager", "No Location Providers available.  ET will wait patiently until they are turned on to start watching location.");
/*      */       }
/*  284 */       return;
/*      */     }
/*      */     
/*      */ 
/*  288 */     PowerStatusChangedEvent powerStatus = (PowerStatusChangedEvent)EventBus.getDefault().getStickyEvent(PowerStatusChangedEvent.class);
/*  289 */     if ((powerStatus == null) || (powerStatus.getStatus() == 1)) {
/*  290 */       this.lastLocationFinder.getLastBestLocation(100, System.currentTimeMillis() - 900000L);
/*      */       
/*      */ 
/*  293 */       Intent locationTimeoutIntent = new Intent(this.applicationContext, ETLocationTimeoutReceiver.class);
/*  294 */       this.locationTimeoutPendingIntent = PendingIntent.getBroadcast(this.applicationContext, 0, locationTimeoutIntent, 134217728);
/*  295 */       this.alarmManager.set(2, SystemClock.elapsedRealtime() + 60000L, this.locationTimeoutPendingIntent);
/*      */       
/*  297 */       if (this.locationWakeupPendingIntent == null)
/*      */       {
/*  299 */         if (ETPush.getLogLevel() <= 3) {
/*  300 */           Log.d("jb4ASDK@ETLocationManager", "Set recurring 15-minute locationWakeup Alarm.");
/*      */         }
/*  302 */         Intent locationWakeupIntent = new Intent(this.applicationContext, ETLocationWakeupReceiver.class);
/*  303 */         this.locationWakeupPendingIntent = PendingIntent.getBroadcast(this.applicationContext, 0, locationWakeupIntent, 134217728);
/*  304 */         this.alarmManager.setInexactRepeating(2, SystemClock.elapsedRealtime() + 900000L, 900000L, this.locationWakeupPendingIntent);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void stopLocationBackgroundWatcher()
/*      */   {
/*  311 */     if ((!isWatchingProximity()) && (!isWatchingLocation())) {
/*  312 */       if (this.lastLocationFinder != null) {
/*  313 */         this.lastLocationFinder.cancel();
/*      */       }
/*  315 */       if (this.locationTimeoutPendingIntent != null) {
/*  316 */         this.alarmManager.cancel(this.locationTimeoutPendingIntent);
/*  317 */         this.locationTimeoutPendingIntent = null;
/*      */       }
/*  319 */       if (this.locationWakeupPendingIntent != null) {
/*  320 */         this.alarmManager.cancel(this.locationWakeupPendingIntent);
/*  321 */         this.locationWakeupPendingIntent = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void completeWakefulIntent() {
/*  327 */     if (this.awokenByIntent != null) {
/*  328 */       ETLocationWakeupReceiver.completeWakefulIntent(this.awokenByIntent);
/*  329 */       this.awokenByIntent = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean startWatchingProximity()
/*      */     throws BleNotAvailableException
/*      */   {
/*  339 */     if (!Config.isLocationManagerActive()) {
/*  340 */       Log.w("jb4ASDK@ETLocationManager", "LocationManager must be set active in readyAimFire to use proximity.");
/*  341 */       return false;
/*      */     }
/*      */     
/*  344 */     if (ETPush.getLogLevel() <= 3) {
/*  345 */       Log.d("jb4ASDK@ETLocationManager", "startWatchingProximity()");
/*      */     }
/*  347 */     setProximityEnabled(true);
/*  348 */     setProximityInvalidated(true);
/*      */     
/*  350 */     if (!this.isWatchingBluetoothChange) {
/*  351 */       IntentFilter filter = new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
/*  352 */       this.applicationContext.registerReceiver(this.bluetoothChangeReceiver, filter);
/*  353 */       this.isWatchingBluetoothChange = true;
/*      */     }
/*      */     
/*  356 */     if (!this.iBeaconManager.checkAvailability()) {
/*  357 */       Log.w("jb4ASDK@ETLocationManager", "Bluetooth LE available, but not currently turned on in settings.");
/*  358 */       return false;
/*      */     }
/*      */     
/*  361 */     if (!this.iBeaconManager.isBound(this.iBeaconMonitor)) {
/*  362 */       this.iBeaconManager.bind(this.iBeaconMonitor);
/*      */     }
/*      */     else {
/*  365 */       startLocationBackgroundWatcher();
/*      */     }
/*      */     
/*  368 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void stopWatchingLocation()
/*      */   {
/*  375 */     if (ETPush.getLogLevel() <= 3) {
/*  376 */       Log.d("jb4ASDK@ETLocationManager", "stopWatchingLocation()");
/*      */     }
/*      */     
/*  379 */     setGeoEnabled(false);
/*      */     
/*  381 */     unmonitorAllGeofences();
/*      */     
/*  383 */     completeWakefulIntent();
/*      */     
/*  385 */     stopLocationBackgroundWatcher();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void stopWatchingProximity()
/*      */   {
/*  392 */     if (ETPush.getLogLevel() <= 3) {
/*  393 */       Log.d("jb4ASDK@ETLocationManager", "stopWatchingProximity()");
/*      */     }
/*  395 */     setProximityEnabled(false);
/*      */     
/*      */ 
/*  398 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*      */     try {
/*  400 */       Dao<com.exacttarget.etpushsdk.data.Region, String> regionDao = helper.getRegionDao();
/*  401 */       Dao<Message, String> messageDao = helper.getMessageDao();
/*      */       
/*      */ 
/*  404 */       UpdateBuilder<com.exacttarget.etpushsdk.data.Region, String> updateBuilder = regionDao.updateBuilder();
/*  405 */       updateBuilder.where().eq("location_type", Integer.valueOf(3));
/*  406 */       updateBuilder.updateColumnValue("active", Boolean.FALSE);
/*  407 */       updateBuilder.updateColumnValue("has_entered", Boolean.FALSE);
/*  408 */       updateBuilder.update();
/*      */       
/*  410 */       UpdateBuilder<Message, String> messageUpdater = messageDao.updateBuilder();
/*  411 */       messageUpdater.updateColumnValue("has_entered", Boolean.FALSE);
/*  412 */       messageUpdater.update();
/*      */       
/*  414 */       for (com.radiusnetworks.ibeacon.Region region : this.iBeaconManager.getMonitoredRegions()) {
/*      */         try {
/*  416 */           if (ETPush.getLogLevel() <= 3) {
/*  417 */             Log.d("jb4ASDK@ETLocationManager", "stopMonitoringBeaconRegion: " + region.getUniqueId());
/*      */           }
/*  419 */           this.iBeaconManager.stopMonitoringBeaconsInRegion(region);
/*      */         }
/*      */         catch (RemoteException e) {
/*  422 */           if (ETPush.getLogLevel() <= 6) {
/*  423 */             Log.e("jb4ASDK@ETLocationManager", e.getMessage());
/*      */           }
/*      */         }
/*      */       }
/*  427 */       for (com.radiusnetworks.ibeacon.Region region : this.iBeaconManager.getRangedRegions()) {
/*      */         try {
/*  429 */           if (ETPush.getLogLevel() <= 3) {
/*  430 */             Log.d("jb4ASDK@ETLocationManager", "stopRangingBeaconRegion: " + region.getUniqueId());
/*      */           }
/*  432 */           this.iBeaconManager.stopRangingBeaconsInRegion(region);
/*      */         }
/*      */         catch (RemoteException e) {
/*  435 */           if (ETPush.getLogLevel() <= 6) {
/*  436 */             Log.e("jb4ASDK@ETLocationManager", e.getMessage());
/*      */           }
/*      */         }
/*      */       }
/*  440 */       if (this.iBeaconManager.getMonitoredRegions().size() > 0) {
/*  441 */         Log.e("jb4ASDK@ETLocationManager", "monitoredRegions SHOULD BE ZERO!!!");
/*      */       }
/*  443 */       if (this.iBeaconManager.getRangedRegions().size() > 0) {
/*  444 */         Log.e("jb4ASDK@ETLocationManager", "rangedRegions SHOULD BE ZERO!!!");
/*      */       }
/*      */       
/*  447 */       if (this.iBeaconManager.isBound(this.iBeaconMonitor)) {
/*  448 */         this.iBeaconManager.unBind(this.iBeaconMonitor);
/*      */       }
/*      */     } catch (SQLException e) {
/*      */       Handler handler;
/*  452 */       if (ETPush.getLogLevel() <= 6) {
/*  453 */         Log.e("jb4ASDK@ETLocationManager", e.getMessage(), e);
/*      */       }
/*      */     } finally {
/*      */       Handler handler;
/*  457 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/*  458 */       handler.postDelayed(new Runnable()
/*      */       {
/*      */         public void run() {
/*  461 */           if ((helper != null) && (helper.isOpen()))
/*  462 */             helper.close(); } }, 10000L);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  468 */     stopLocationBackgroundWatcher();
/*      */   }
/*      */   
/*      */   private void unmonitorAllGeofences() {
/*  472 */     this.regionMonitor.setRemoveAllGeofences(Boolean.TRUE);
/*  473 */     updateRegionMonitoring();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void enterLowPowerMode()
/*      */   {
/*  480 */     if (ETPush.getLogLevel() <= 3) {
/*  481 */       Log.d("jb4ASDK@ETLocationManager", "enterLowPowerMode()");
/*      */     }
/*  483 */     if (this.lastLocationFinder != null) {
/*  484 */       this.lastLocationFinder.cancel();
/*      */     }
/*  486 */     if (this.locationTimeoutPendingIntent != null) {
/*  487 */       this.alarmManager.cancel(this.locationTimeoutPendingIntent);
/*  488 */       this.locationTimeoutPendingIntent = null;
/*      */     }
/*  490 */     if (this.locationWakeupPendingIntent != null) {
/*  491 */       this.alarmManager.cancel(this.locationWakeupPendingIntent);
/*  492 */       this.locationWakeupPendingIntent = null;
/*      */     }
/*  494 */     this.awokenByIntent = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void exitLowPowerMode()
/*      */   {
/*  501 */     startWatchingLocation();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isWatchingLocation()
/*      */   {
/*  508 */     return ((Boolean)Config.getETSharedPref(this.applicationContext, this.applicationContext.getSharedPreferences("jb4ASDK@ETLocationManager", 0), "et_geo_enabled_key", Boolean.valueOf(false))).booleanValue();
/*      */   }
/*      */   
/*      */   public void setGeoEnabled(boolean value) {
/*  512 */     this.prefs.edit().putBoolean("et_geo_enabled_key", value).commit();
/*      */   }
/*      */   
/*      */   protected boolean getGeofenceInvalidated() {
/*  516 */     return ((Boolean)Config.getETSharedPref(this.applicationContext, this.applicationContext.getSharedPreferences("jb4ASDK@ETLocationManager", 0), "et_geofence_invalidated_key", Boolean.valueOf(true))).booleanValue();
/*      */   }
/*      */   
/*      */   public void setGeofenceInvalidated(boolean value) {
/*  520 */     this.prefs.edit().putBoolean("et_geofence_invalidated_key", value).commit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isWatchingProximity()
/*      */   {
/*  527 */     return ((Boolean)Config.getETSharedPref(this.applicationContext, this.applicationContext.getSharedPreferences("jb4ASDK@ETLocationManager", 0), "et_proximity_enabled_key", Boolean.valueOf(false))).booleanValue();
/*      */   }
/*      */   
/*      */   public void setProximityEnabled(boolean value) {
/*  531 */     this.prefs.edit().putBoolean("et_proximity_enabled_key", value).commit();
/*      */   }
/*      */   
/*      */   protected boolean getProximityInvalidated() {
/*  535 */     return ((Boolean)Config.getETSharedPref(this.applicationContext, this.applicationContext.getSharedPreferences("jb4ASDK@ETLocationManager", 0), "et_proximity_invalidated_key", Boolean.valueOf(true))).booleanValue();
/*      */   }
/*      */   
/*      */   public void setProximityInvalidated(boolean value) {
/*  539 */     this.prefs.edit().putBoolean("et_proximity_invalidated_key", value).commit();
/*      */   }
/*      */   
/*      */   public boolean areLocationProvidersAvailable() {
/*  543 */     LocationManager locationManager = (LocationManager)this.applicationContext.getSystemService("location");
/*  544 */     return (locationManager.isProviderEnabled("gps")) || (locationManager.isProviderEnabled("network"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEvent(BackgroundEvent event)
/*      */   {
/*  555 */     if (Config.isLocationManagerActive()) {
/*  556 */       if (!event.isInBackground())
/*      */       {
/*  558 */         if (ETPush.getLogLevel() <= 3) {
/*  559 */           Log.d("jb4ASDK@ETLocationManager", "In FOREGROUND");
/*      */         }
/*  561 */         if (isWatchingLocation())
/*      */         {
/*  563 */           setGeofenceInvalidated(true);
/*  564 */           startWatchingLocation();
/*      */         }
/*      */         
/*  567 */         if (isWatchingProximity()) {
/*  568 */           if (this.iBeaconManager.isBound(this.iBeaconMonitor)) {
/*  569 */             if (ETPush.getLogLevel() <= 3) {
/*  570 */               Log.d("jb4ASDK@ETLocationManager", "BeaconManager: In FOREGROUND");
/*      */             }
/*  572 */             this.iBeaconManager.setBackgroundMode(this.iBeaconMonitor, false);
/*      */           }
/*      */           
/*  575 */           setProximityInvalidated(true);
/*  576 */           startWatchingProximity();
/*      */         }
/*      */         
/*      */       }
/*  580 */       else if ((isWatchingProximity()) && 
/*  581 */         (this.iBeaconManager.isBound(this.iBeaconMonitor))) {
/*  582 */         if (ETPush.getLogLevel() <= 3) {
/*  583 */           Log.d("jb4ASDK@ETLocationManager", "BeaconManager: In BACKGROUND");
/*      */         }
/*  585 */         this.iBeaconManager.setBackgroundMode(this.iBeaconMonitor, true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEvent(LocationUpdateEvent event)
/*      */   {
/*  600 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*      */     try {
/*  602 */       int rowsUpdated = helper.getLocationUpdateDao().deleteById(event.getId());
/*      */       
/*  604 */       if (rowsUpdated == 1) {
/*  605 */         if (ETPush.getLogLevel() <= 3) {
/*  606 */           Log.d("jb4ASDK@ETLocationManager", "removed locationupdate id: " + event.getId());
/*      */         }
/*      */         
/*      */       }
/*  610 */       else if (ETPush.getLogLevel() <= 6) {
/*  611 */         Log.e("jb4ASDK@ETLocationManager", "Error: rowsUpdated = " + rowsUpdated);
/*      */       }
/*      */     }
/*      */     catch (SQLException e) {
/*      */       Handler handler;
/*  616 */       if (ETPush.getLogLevel() <= 6) {
/*  617 */         Log.e("jb4ASDK@ETLocationManager", e.getMessage(), e);
/*      */       }
/*      */     } finally {
/*      */       Handler handler;
/*  621 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/*  622 */       handler.postDelayed(new Runnable()
/*      */       {
/*      */         public void run() {
/*  625 */           if ((helper != null) && (helper.isOpen()))
/*  626 */             helper.close(); } }, 10000L);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onEvent(LastKnownLocationEvent event)
/*      */   {
/*  641 */     if (ETPush.getLogLevel() <= 3) {
/*  642 */       Log.d("jb4ASDK@ETLocationManager", "onEventLocationChanged()");
/*      */     }
/*      */     
/*  645 */     if (System.currentTimeMillis() - event.getLocation().getTime() < 900000L)
/*      */     {
/*  647 */       if (this.locationTimeoutPendingIntent != null) {
/*  648 */         this.alarmManager.cancel(this.locationTimeoutPendingIntent);
/*      */       }
/*  650 */       this.prefs.edit().putString("et_last_location_latitude", String.valueOf(event.getLocation().getLatitude())).commit();
/*  651 */       this.prefs.edit().putString("et_last_location_longitude", String.valueOf(event.getLocation().getLongitude())).commit();
/*      */       
/*      */ 
/*  654 */       if (ETPush.getLogLevel() <= 3) {
/*  655 */         Log.d("jb4ASDK@ETLocationManager", "Provider: " + event.getLocation().getProvider() + ", Lat: " + event.getLocation().getLatitude() + ", Lon: " + event.getLocation().getLongitude() + ", Accuracy: " + event.getLocation().getAccuracy() + ", Timestamp: " + new Date(event.getLocation().getTime()));
/*      */       }
/*      */       
/*  658 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*      */       try {
/*  660 */         Dao<LocationUpdate, Integer> locationDao = helper.getLocationUpdateDao();
/*  661 */         Date timestamp = new Date(event.getLocation().getTime());
/*  662 */         if (locationDao.queryForEq("timestamp", timestamp).isEmpty())
/*      */         {
/*      */ 
/*  665 */           LocationUpdate locationUpdate = new LocationUpdate(this.applicationContext);
/*  666 */           locationUpdate.setLatitude(Double.valueOf(event.getLocation().getLatitude()));
/*  667 */           locationUpdate.setLongitude(Double.valueOf(event.getLocation().getLongitude()));
/*  668 */           locationUpdate.setAccuracy(Integer.valueOf(Math.round(event.getLocation().getAccuracy())));
/*  669 */           locationUpdate.setTimestamp(timestamp);
/*  670 */           locationDao.create(locationUpdate);
/*      */         }
/*      */         
/*      */ 
/*  674 */         Intent sendDataIntent = new Intent(this.applicationContext, ETSendDataReceiver.class);
/*  675 */         sendDataIntent.putExtra("et_send_type_extra", "et_send_type_location");
/*  676 */         this.applicationContext.sendBroadcast(sendDataIntent);
/*      */         
/*  678 */         if ((isWatchingLocation()) && (getGeofenceInvalidated()))
/*      */         {
/*      */ 
/*  681 */           GeofenceRequest geofenceRequest = new GeofenceRequest(this.applicationContext);
/*  682 */           geofenceRequest.setLatitude(Double.valueOf(event.getLocation().getLatitude()));
/*  683 */           geofenceRequest.setLongitude(Double.valueOf(event.getLocation().getLongitude()));
/*      */           
/*  685 */           helper.getGeofenceRequestDao().create(geofenceRequest);
/*      */           
/*      */ 
/*  688 */           Intent sendDataIntent1 = new Intent(this.applicationContext, ETSendDataReceiver.class);
/*  689 */           sendDataIntent1.putExtra("et_send_type_extra", "et_send_type_geofence");
/*  690 */           this.applicationContext.sendBroadcast(sendDataIntent1);
/*      */         }
/*      */         
/*  693 */         if ((isWatchingProximity()) && (getProximityInvalidated())) {
/*  694 */           BeaconRequest beaconRequest = new BeaconRequest(this.applicationContext);
/*  695 */           beaconRequest.setLatitude(Double.valueOf(event.getLocation().getLatitude()));
/*  696 */           beaconRequest.setLongitude(Double.valueOf(event.getLocation().getLongitude()));
/*      */           
/*  698 */           helper.getBeaconRequestDao().create(beaconRequest);
/*      */           
/*      */ 
/*  701 */           Intent sendDataIntent2 = new Intent(this.applicationContext, ETSendDataReceiver.class);
/*  702 */           sendDataIntent2.putExtra("et_send_type_extra", "et_send_type_proximity");
/*  703 */           this.applicationContext.sendBroadcast(sendDataIntent2);
/*      */         }
/*      */       } catch (SQLException e) {
/*      */         Handler handler;
/*  707 */         if (ETPush.getLogLevel() <= 6) {
/*  708 */           Log.e("jb4ASDK@ETLocationManager", e.getMessage(), e);
/*      */         }
/*      */       } finally {
/*      */         Handler handler;
/*  712 */         Handler handler = new Handler(this.applicationContext.getMainLooper());
/*  713 */         handler.postDelayed(new Runnable()
/*      */         {
/*      */           public void run() {
/*  716 */             if ((helper != null) && (helper.isOpen()))
/*  717 */               helper.close(); } }, 10000L);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  724 */       completeWakefulIntent();
/*      */ 
/*      */ 
/*      */     }
/*  728 */     else if (ETPush.getLogLevel() <= 3) {
/*  729 */       Log.d("jb4ASDK@ETLocationManager", "stale location, older than 15 minutes.");
/*      */     }
/*      */   }
/*      */   
/*      */   public void onEvent(GeofenceResponseEvent event)
/*      */   {
/*  735 */     if (ETPush.getLogLevel() <= 3) {
/*  736 */       Log.d("jb4ASDK@ETLocationManager", "onEventGeofenceResponse()");
/*      */     }
/*  738 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*      */     try { Handler handler;
/*  740 */       if (event.getRefreshCenter() == null) {
/*  741 */         if (ETPush.getLogLevel() <= 5) {
/*  742 */           Log.w("jb4ASDK@ETLocationManager", "Got a bad response from retrieving geofences. Try to get them the next time we get a location.");
/*      */         }
/*  744 */         setGeofenceInvalidated(true);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  749 */         Dao<com.exacttarget.etpushsdk.data.Region, String> regionDao = helper.getRegionDao();
/*  750 */         Dao<RegionMessage, Integer> regionMessageDao = helper.getRegionMessageDao();
/*  751 */         Dao<Message, String> messageDao = helper.getMessageDao();
/*  752 */         List<RegionMessage> regionMessages = regionMessageDao.queryForAll();
/*  753 */         List<RegionMessage> regionMessagesToDelete = new ArrayList();
/*  754 */         for (RegionMessage regionMessage : regionMessages) {
/*  755 */           com.exacttarget.etpushsdk.data.Region region = (com.exacttarget.etpushsdk.data.Region)regionDao.queryForId(regionMessage.getRegion().getId());
/*  756 */           if (region.getLocationType().intValue() == 1) {
/*  757 */             regionMessagesToDelete.add(regionMessage);
/*      */           }
/*      */         }
/*  760 */         if (!regionMessagesToDelete.isEmpty()) {
/*  761 */           regionMessageDao.delete(regionMessagesToDelete);
/*      */         }
/*      */         
/*      */ 
/*  765 */         QueryBuilder<com.exacttarget.etpushsdk.data.Region, String> queryBuilder = regionDao.queryBuilder();
/*  766 */         queryBuilder.where().eq("location_type", Integer.valueOf(1)).and().eq("active", Boolean.TRUE);
/*  767 */         List<com.exacttarget.etpushsdk.data.Region> monitoredRegions = queryBuilder.query();
/*  768 */         this.regionMonitor.setMonitoredRegions(monitoredRegions);
/*      */         
/*      */ 
/*  771 */         UpdateBuilder<com.exacttarget.etpushsdk.data.Region, String> updateBuilder = regionDao.updateBuilder();
/*  772 */         updateBuilder.where().eq("location_type", Integer.valueOf(1));
/*  773 */         updateBuilder.updateColumnValue("active", Boolean.FALSE);
/*  774 */         updateBuilder.update();
/*      */         
/*      */ 
/*  777 */         List<com.exacttarget.etpushsdk.data.Region> magicFences = regionDao.queryForEq("id", "~~m@g1c_f3nc3~~");
/*  778 */         com.exacttarget.etpushsdk.data.Region magicFence = null;
/*  779 */         if ((magicFences != null) && (magicFences.size() > 0)) {
/*  780 */           magicFence = (com.exacttarget.etpushsdk.data.Region)magicFences.get(0);
/*      */         }
/*      */         else {
/*  783 */           magicFence = new com.exacttarget.etpushsdk.data.Region();
/*  784 */           magicFence.setId("~~m@g1c_f3nc3~~");
/*      */         }
/*      */         
/*  787 */         magicFence.setActive(Boolean.TRUE);
/*  788 */         magicFence.setCenter(event.getRefreshCenter());
/*  789 */         magicFence.setRadius(event.getRefreshRadius());
/*  790 */         regionDao.createOrUpdate(magicFence);
/*      */         
/*      */ 
/*  793 */         for (Iterator i$ = event.getFences().iterator(); i$.hasNext();) { region = (com.exacttarget.etpushsdk.data.Region)i$.next();
/*  794 */           region.setActive(Boolean.TRUE);
/*  795 */           com.exacttarget.etpushsdk.data.Region dbRegion = (com.exacttarget.etpushsdk.data.Region)regionDao.queryForId(region.getId());
/*  796 */           if (dbRegion != null) {
/*  797 */             region.setEntryCount(dbRegion.getEntryCount());
/*  798 */             region.setExitCount(dbRegion.getExitCount());
/*      */           }
/*  800 */           regionDao.createOrUpdate(region);
/*      */           
/*  802 */           for (Message message : region.getMessages()) {
/*  803 */             List<Message> dbMessages = messageDao.queryForEq("id", message.getId());
/*  804 */             if ((dbMessages != null) && (dbMessages.size() > 0)) {
/*  805 */               Message dbMessage = (Message)dbMessages.get(0);
/*  806 */               message.setLastShownDate(dbMessage.getLastShownDate());
/*  807 */               message.setNextAllowedShow(dbMessage.getNextAllowedShow());
/*  808 */               message.setShowCount(dbMessage.getShowCount());
/*  809 */               if (dbMessage.getPeriodType().equals(message.getPeriodType())) {
/*  810 */                 message.setPeriodShowCount(dbMessage.getPeriodShowCount());
/*      */               }
/*      */               else
/*      */               {
/*  814 */                 message.setPeriodShowCount(Integer.valueOf(0));
/*      */               }
/*      */             }
/*      */             
/*  818 */             if ((message.getMessagesPerPeriod().intValue() <= 0) && (message.getNumberOfPeriods().intValue() > 0) && (!message.getPeriodType().equals(Integer.valueOf(0)))) {
/*  819 */               message.setMessagesPerPeriod(Integer.valueOf(1));
/*      */             }
/*      */             
/*  822 */             messageDao.createOrUpdate(message);
/*      */             
/*      */ 
/*  825 */             regionMessageDao.create(new RegionMessage(region, message));
/*      */           }
/*      */         }
/*      */         com.exacttarget.etpushsdk.data.Region region;
/*  829 */         setGeofenceInvalidated(false);
/*      */         
/*  831 */         updateRegionMonitoring();
/*      */       }
/*      */     } catch (SQLException e) { Handler handler;
/*  834 */       if (ETPush.getLogLevel() <= 6) {
/*  835 */         Log.e("jb4ASDK@ETLocationManager", e.getMessage(), e);
/*      */       }
/*      */     } finally {
/*      */       Handler handler;
/*  839 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/*  840 */       handler.postDelayed(new Runnable()
/*      */       {
/*      */         public void run() {
/*  843 */           if ((helper != null) && (helper.isOpen()))
/*  844 */             helper.close(); } }, 10000L);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void onEvent(BeaconResponseEvent event)
/*      */   {
/*  852 */     if (ETPush.getLogLevel() <= 3) {
/*  853 */       Log.d("jb4ASDK@ETLocationManager", "onEvent(BeaconResponseEvent)");
/*      */     }
/*      */     
/*  856 */     if (this.iBeaconManager.checkAvailability()) {
/*  857 */       if (!this.iBeaconManager.isBound(this.iBeaconMonitor)) {
/*  858 */         this.iBeaconManager.bind(this.iBeaconMonitor);
/*  859 */         if (ETPush.getLogLevel() <= 5) {
/*  860 */           Log.w("jb4ASDK@ETLocationManager", "for some weird reason, iBeaconMonitor wasn't bound.");
/*      */         }
/*  862 */         return;
/*      */       }
/*      */       
/*      */ 
/*  866 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*      */       try
/*      */       {
/*  869 */         Dao<com.exacttarget.etpushsdk.data.Region, String> regionDao = helper.getRegionDao();
/*  870 */         Dao<RegionMessage, Integer> regionMessageDao = helper.getRegionMessageDao();
/*  871 */         Dao<Message, String> messageDao = helper.getMessageDao();
/*      */         
/*  873 */         List<RegionMessage> regionMessages = regionMessageDao.queryForAll();
/*  874 */         List<RegionMessage> regionMessagesToDelete = new ArrayList();
/*  875 */         for (RegionMessage regionMessage : regionMessages) {
/*  876 */           com.exacttarget.etpushsdk.data.Region region = (com.exacttarget.etpushsdk.data.Region)regionDao.queryForId(regionMessage.getRegion().getId());
/*  877 */           if (region.getLocationType().intValue() == 3) {
/*  878 */             regionMessagesToDelete.add(regionMessage);
/*      */           }
/*      */         }
/*  881 */         if (!regionMessagesToDelete.isEmpty()) {
/*  882 */           regionMessageDao.delete(regionMessagesToDelete);
/*      */         }
/*      */         
/*      */ 
/*  886 */         if ((event.getBeacons() != null) && (!event.getBeacons().isEmpty())) {
/*  887 */           for (Iterator i$ = event.getBeacons().iterator(); i$.hasNext();) { region = (com.exacttarget.etpushsdk.data.Region)i$.next();
/*  888 */             for (Message message : region.getMessages()) {
/*  889 */               List<Message> dbMessages = messageDao.queryForEq("id", message.getId());
/*  890 */               if ((dbMessages != null) && (dbMessages.size() > 0)) {
/*  891 */                 Message dbMessage = (Message)dbMessages.get(0);
/*  892 */                 message.setLastShownDate(dbMessage.getLastShownDate());
/*  893 */                 message.setNextAllowedShow(dbMessage.getNextAllowedShow());
/*  894 */                 message.setShowCount(dbMessage.getShowCount());
/*  895 */                 message.setHasEntered(dbMessage.getHasEntered());
/*  896 */                 if (dbMessage.getPeriodType().equals(message.getPeriodType())) {
/*  897 */                   message.setPeriodShowCount(dbMessage.getPeriodShowCount());
/*      */                 }
/*      */                 else
/*      */                 {
/*  901 */                   message.setPeriodShowCount(Integer.valueOf(0));
/*      */                 }
/*      */               }
/*      */               
/*  905 */               if ((message.getMessagesPerPeriod().intValue() <= 0) && (message.getNumberOfPeriods().intValue() > 0) && (!message.getPeriodType().equals(Integer.valueOf(0)))) {
/*  906 */                 message.setMessagesPerPeriod(Integer.valueOf(1));
/*      */               }
/*      */               
/*  909 */               messageDao.createOrUpdate(message);
/*      */               
/*      */ 
/*  912 */               regionMessageDao.create(new RegionMessage(region, message));
/*      */             } }
/*      */           com.exacttarget.etpushsdk.data.Region region;
/*  915 */           QueryBuilder<com.exacttarget.etpushsdk.data.Region, String> queryBuilder = regionDao.queryBuilder();
/*  916 */           List<com.exacttarget.etpushsdk.data.Region> regionsInDb = queryBuilder.where().eq("location_type", Integer.valueOf(3)).and().eq("active", Boolean.TRUE).query();
/*      */           
/*  918 */           for (com.exacttarget.etpushsdk.data.Region regionInDb : regionsInDb) {
/*  919 */             if (!event.getBeacons().contains(regionInDb)) {
/*      */               try
/*      */               {
/*  922 */                 regionInDb.setActive(Boolean.FALSE);
/*  923 */                 regionInDb.setHasEntered(Boolean.FALSE);
/*  924 */                 regionDao.update(regionInDb);
/*      */                 
/*  926 */                 com.radiusnetworks.ibeacon.Region region = new com.radiusnetworks.ibeacon.Region(regionInDb.getId(), regionInDb.getGuid(), regionInDb.getMajor(), regionInDb.getMinor());
/*  927 */                 this.iBeaconManager.stopMonitoringBeaconsInRegion(region);
/*  928 */                 this.iBeaconManager.stopRangingBeaconsInRegion(region);
/*  929 */                 if (ETPush.getLogLevel() <= 3) {
/*  930 */                   Log.d("jb4ASDK@ETLocationManager", "stopRangingBeacon - {id: '" + region.getUniqueId() + "', uuid: '" + region.getProximityUuid() + "', major: " + region.getMajor() + ", minor: " + region.getMinor() + "}");
/*      */                 }
/*      */               }
/*      */               catch (RemoteException e) {
/*  934 */                 if (ETPush.getLogLevel() <= 6) {
/*  935 */                   Log.e("jb4ASDK@ETLocationManager", e.getMessage(), e);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*  941 */           for (com.exacttarget.etpushsdk.data.Region regionToRange : event.getBeacons()) {
/*  942 */             com.exacttarget.etpushsdk.data.Region regionInDb = (com.exacttarget.etpushsdk.data.Region)regionDao.queryForId(regionToRange.getId());
/*  943 */             if (regionInDb == null) {
/*  944 */               regionInDb = regionToRange;
/*  945 */               regionInDb.setActive(Boolean.TRUE);
/*  946 */               regionDao.create(regionInDb);
/*      */             }
/*      */             else {
/*  949 */               if (Boolean.TRUE.equals(regionInDb.getActive())) {
/*  950 */                 com.radiusnetworks.ibeacon.Region region = new com.radiusnetworks.ibeacon.Region(regionInDb.getId(), regionInDb.getGuid(), regionInDb.getMajor(), regionInDb.getMinor());
/*  951 */                 if (this.iBeaconManager.getRangedRegions().contains(region)) {
/*  952 */                   if (ETPush.getLogLevel() > 3) continue;
/*  953 */                   Log.d("jb4ASDK@ETLocationManager", "alreadyRangingBeacon - {id: '" + region.getUniqueId() + "', uuid: '" + region.getProximityUuid() + "', major: " + region.getMajor() + ", minor: " + region.getMinor() + "}"); continue;
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*  959 */               regionInDb.setActive(Boolean.TRUE);
/*  960 */               regionInDb.setGuid(regionToRange.getGuid());
/*  961 */               regionInDb.setMajor(regionToRange.getMajor());
/*  962 */               regionInDb.setMinor(regionToRange.getMinor());
/*  963 */               regionDao.update(regionInDb);
/*      */             }
/*      */             
/*  966 */             com.radiusnetworks.ibeacon.Region region = new com.radiusnetworks.ibeacon.Region(regionInDb.getId(), regionInDb.getGuid(), regionInDb.getMajor(), regionInDb.getMinor());
/*      */             try
/*      */             {
/*  969 */               if (!this.iBeaconManager.getRangedRegions().contains(region)) {
/*  970 */                 if (ETPush.getLogLevel() <= 3) {
/*  971 */                   Log.d("jb4ASDK@ETLocationManager", "startRangingBeacon - {id: '" + region.getUniqueId() + "', uuid: '" + region.getProximityUuid() + "', major: " + region.getMajor() + ", minor: " + region.getMinor() + "}");
/*      */                 }
/*  973 */                 this.iBeaconManager.startMonitoringBeaconsInRegion(region);
/*  974 */                 this.iBeaconManager.startRangingBeaconsInRegion(region);
/*      */ 
/*      */               }
/*  977 */               else if (ETPush.getLogLevel() <= 3) {
/*  978 */                 Log.d("jb4ASDK@ETLocationManager", "alreadyMonitoringBeacon2 - {id: '" + region.getUniqueId() + "', uuid: '" + region.getProximityUuid() + "', major: " + region.getMajor() + ", minor: " + region.getMinor() + "}");
/*      */               }
/*      */             }
/*      */             catch (RemoteException e)
/*      */             {
/*  983 */               if (ETPush.getLogLevel() <= 6) {
/*  984 */                 Log.e("jb4ASDK@ETLocationManager", e.getMessage(), e);
/*      */               }
/*      */               
/*  987 */               regionInDb.setActive(Boolean.FALSE);
/*  988 */               regionDao.update(regionInDb);
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         }
/*  994 */         else if (ETPush.getLogLevel() <= 3) {
/*  995 */           Log.d("jb4ASDK@ETLocationManager", "Empty beacon list from server.");
/*      */         }
/*      */         
/*  998 */         setProximityInvalidated(false);
/*      */       }
/*      */       catch (SQLException e) {
/*      */         Handler handler;
/* 1002 */         if (ETPush.getLogLevel() <= 6) {
/* 1003 */           Log.e("jb4ASDK@ETLocationManager", e.getMessage(), e);
/*      */         }
/*      */       } finally {
/*      */         Handler handler;
/* 1007 */         Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 1008 */         handler.postDelayed(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1011 */             if ((helper != null) && (helper.isOpen()))
/* 1012 */               helper.close(); } }, 10000L);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateRegionMonitoring()
/*      */   {
/* 1022 */     if (ETPush.getLogLevel() <= 3) {
/* 1023 */       Log.d("jb4ASDK@ETLocationManager", "updateRegionMonitoring()");
/*      */     }
/*      */     
/* 1026 */     if (ETGooglePlayServicesUtil.isAvailable(this.applicationContext, false)) {
/* 1027 */       if (this.locationClient == null) {
/* 1028 */         this.locationClient = new LocationClient(this.applicationContext, this.regionMonitor, this.regionMonitor);
/*      */       }
/*      */       
/* 1031 */       if ((!this.locationClient.isConnected()) && (!this.locationClient.isConnecting())) {
/* 1032 */         if (ETPush.getLogLevel() <= 3) {
/* 1033 */           Log.d("jb4ASDK@ETLocationManager", "locationClient.connect() ...");
/*      */         }
/* 1035 */         this.locationClient.connect();
/*      */       }
/*      */       else {
/* 1038 */         if (ETPush.getLogLevel() <= 5) {
/* 1039 */           Log.w("jb4ASDK@ETLocationManager", "locationClientConnecting == true, trying reconnect");
/*      */         }
/* 1041 */         this.locationClient.disconnect();
/* 1042 */         this.locationClient = null;
/* 1043 */         updateRegionMonitoring();
/*      */       }
/*      */       
/*      */     }
/* 1047 */     else if (ETPush.getLogLevel() <= 6) {
/* 1048 */       Log.e("jb4ASDK@ETLocationManager", "Play Services Not available");
/*      */     }
/*      */   }
/*      */   
/*      */   private class RegionMonitor implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationClient.OnAddGeofencesResultListener, LocationClient.OnRemoveGeofencesResultListener { private static final String TAG = "jb4ASDK@RegionMonitor";
/*      */     
/*      */     private RegionMonitor() {}
/*      */     
/* 1056 */     private Boolean removeAllGeofences = Boolean.FALSE;
/* 1057 */     private List<com.exacttarget.etpushsdk.data.Region> monitoredRegions = null;
/* 1058 */     private List<Geofence> geofencesToStartMonitoring = null;
/*      */     
/*      */     public void setMonitoredRegions(List<com.exacttarget.etpushsdk.data.Region> monitoredRegions) {
/* 1061 */       this.monitoredRegions = monitoredRegions;
/*      */     }
/*      */     
/*      */     public void setRemoveAllGeofences(Boolean removeAllGeofences) {
/* 1065 */       this.removeAllGeofences = removeAllGeofences;
/*      */     }
/*      */     
/*      */ 
/*      */     private PendingIntent geofencePendingIntent;
/*      */     private PendingIntent getGeofencePendingIntent()
/*      */     {
/* 1072 */       if (this.geofencePendingIntent == null) {
/* 1073 */         if (ETPush.getLogLevel() <= 3) {
/* 1074 */           Log.d("jb4ASDK@RegionMonitor", "create geofencePendingIntent");
/*      */         }
/* 1076 */         Intent geofenceIntent = new Intent(ETLocationManager.this.applicationContext, ETGeofenceReceiver.class);
/* 1077 */         this.geofencePendingIntent = PendingIntent.getBroadcast(ETLocationManager.this.applicationContext, 0, geofenceIntent, 134217728);
/*      */       }
/*      */       
/* 1080 */       return this.geofencePendingIntent;
/*      */     }
/*      */     
/*      */     private void updateGeofencesFromDatabase() {
/* 1084 */       if (ETPush.getLogLevel() <= 3) {
/* 1085 */         Log.d("jb4ASDK@RegionMonitor", "updateGeofencesFromDatabase()");
/*      */       }
/*      */       
/* 1088 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(ETLocationManager.this.applicationContext);
/*      */       try {
/* 1090 */         Dao<com.exacttarget.etpushsdk.data.Region, String> regionDao = helper.getRegionDao();
/* 1091 */         Dao<RegionMessage, Integer> regionMessageDao = helper.getRegionMessageDao();
/* 1092 */         Dao<Message, String> messageDao = helper.getMessageDao();
/*      */         
/* 1094 */         this.geofencesToStartMonitoring = new ArrayList();
/* 1095 */         List<com.exacttarget.etpushsdk.data.Region> regions = regionDao.queryBuilder().where().eq("location_type", Integer.valueOf(1)).and().eq("active", Boolean.TRUE).query();
/*      */         
/* 1097 */         List<String> regionsToStopMonitoring = new ArrayList();
/* 1098 */         List<com.exacttarget.etpushsdk.data.Region> regionsToStartMonitoring = new ArrayList();
/* 1099 */         for (com.exacttarget.etpushsdk.data.Region monitoredRegion : this.monitoredRegions) {
/* 1100 */           if ((!regions.contains(monitoredRegion)) || ("~~m@g1c_f3nc3~~".equals(monitoredRegion.getId()))) {
/* 1101 */             regionsToStopMonitoring.add(monitoredRegion.getId());
/*      */           }
/*      */         }
/*      */         
/* 1105 */         for (com.exacttarget.etpushsdk.data.Region unmonitoredRegion : regions) {
/* 1106 */           if ((!this.monitoredRegions.contains(unmonitoredRegion)) || ("~~m@g1c_f3nc3~~".equals(unmonitoredRegion.getId()))) {
/* 1107 */             regionsToStartMonitoring.add(unmonitoredRegion);
/*      */           }
/*      */         }
/*      */         
/* 1111 */         for (com.exacttarget.etpushsdk.data.Region region : regionsToStartMonitoring)
/*      */         {
/* 1113 */           List<Message> messages = new ArrayList();
/* 1114 */           List<RegionMessage> regionMessages = regionMessageDao.queryForEq("region_id", region.getId());
/* 1115 */           for (RegionMessage regionMessage : regionMessages) {
/* 1116 */             List<Message> dbMessages = messageDao.queryForEq("id", regionMessage.getMessage().getId());
/* 1117 */             if ((dbMessages != null) && (dbMessages.size() > 0)) {
/* 1118 */               Message message = (Message)dbMessages.get(0);
/* 1119 */               messages.add(message);
/*      */             }
/*      */           }
/* 1122 */           region.setMessages(messages);
/*      */           
/* 1124 */           if (ETPush.getLogLevel() <= 3) {
/* 1125 */             Log.d("jb4ASDK@RegionMonitor", "Will Monitor Region: " + region.getId() + ", " + region.getLatitude() + ", " + region.getLongitude() + ", " + region.getRadius());
/*      */           }
/* 1127 */           this.geofencesToStartMonitoring.add(region.toGeofence());
/*      */         }
/*      */         
/* 1130 */         if (regionsToStopMonitoring.size() > 0) {
/* 1131 */           ETLocationManager.this.locationClient.removeGeofences(regionsToStopMonitoring, this);
/*      */         }
/* 1133 */         else if (this.geofencesToStartMonitoring.size() > 0) {
/* 1134 */           ETLocationManager.this.locationClient.addGeofences(this.geofencesToStartMonitoring, getGeofencePendingIntent(), this);
/*      */         }
/*      */         else {
/* 1137 */           if (ETPush.getLogLevel() <= 3) {
/* 1138 */             Log.d("jb4ASDK@RegionMonitor", "geofence data hasn't changed, disconnecting.");
/*      */           }
/* 1140 */           ETLocationManager.this.locationClient.disconnect();
/*      */         }
/*      */       } catch (SQLException e) {
/*      */         Handler handler;
/* 1144 */         if (ETPush.getLogLevel() <= 6) {
/* 1145 */           Log.e("jb4ASDK@RegionMonitor", e.getMessage(), e);
/*      */         }
/*      */         try {
/* 1148 */           ETLocationManager.this.locationClient.disconnect();
/*      */         }
/*      */         catch (Throwable ex) {}
/*      */       }
/*      */       finally {
/*      */         Handler handler;
/* 1154 */         Handler handler = new Handler(ETLocationManager.this.applicationContext.getMainLooper());
/* 1155 */         handler.postDelayed(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1158 */             if ((helper != null) && (helper.isOpen()))
/* 1159 */               helper.close(); } }, 10000L);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void onRemoveGeofencesByPendingIntentResult(int statusCode, PendingIntent pendingIntent)
/*      */     {
/* 1167 */       if (ETPush.getLogLevel() <= 3) {
/* 1168 */         Log.d("jb4ASDK@RegionMonitor", "onRemoveGeofencesByPendingIntentResult() status: " + getStatusString(statusCode));
/*      */       }
/* 1170 */       this.removeAllGeofences = Boolean.FALSE;
/*      */       
/* 1172 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(ETLocationManager.this.applicationContext);
/*      */       try {
/* 1174 */         UpdateBuilder<com.exacttarget.etpushsdk.data.Region, String> updateBuilder = helper.getRegionDao().updateBuilder();
/* 1175 */         updateBuilder.where().eq("location_type", Integer.valueOf(1));
/* 1176 */         updateBuilder.updateColumnValue("active", Boolean.FALSE);
/* 1177 */         updateBuilder.update();
/*      */       } catch (SQLException e) {
/*      */         Handler handler;
/* 1180 */         if (ETPush.getLogLevel() <= 6) {
/* 1181 */           Log.e("jb4ASDK@RegionMonitor", e.getMessage(), e);
/*      */         }
/*      */       } finally {
/*      */         Handler handler;
/* 1185 */         Handler handler = new Handler(ETLocationManager.this.applicationContext.getMainLooper());
/* 1186 */         handler.postDelayed(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1189 */             if ((helper != null) && (helper.isOpen()))
/* 1190 */               helper.close(); } }, 10000L);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1196 */       ETLocationManager.this.locationClient.disconnect();
/*      */     }
/*      */     
/*      */     public void onRemoveGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
/* 1200 */       if (ETPush.getLogLevel() <= 3) {
/* 1201 */         Log.d("jb4ASDK@RegionMonitor", "onRemoveGeofencesByRequestIdsResult status: " + getStatusString(statusCode));
/*      */       }
/* 1203 */       for (String geofenceRequestId : geofenceRequestIds) {
/* 1204 */         if (ETPush.getLogLevel() <= 3) {
/* 1205 */           Log.d("jb4ASDK@RegionMonitor", "Unmonitor Region: " + geofenceRequestId);
/*      */         }
/*      */       }
/*      */       
/* 1209 */       if (this.geofencesToStartMonitoring.size() > 0) {
/* 1210 */         ETLocationManager.this.locationClient.addGeofences(this.geofencesToStartMonitoring, getGeofencePendingIntent(), this);
/*      */       }
/*      */       else {
/* 1213 */         if (ETPush.getLogLevel() <= 3) {
/* 1214 */           Log.d("jb4ASDK@RegionMonitor", "no new geofences to monitor, disconnecting.");
/*      */         }
/* 1216 */         ETLocationManager.this.locationClient.disconnect();
/*      */       }
/*      */     }
/*      */     
/*      */     public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
/* 1221 */       if (ETPush.getLogLevel() <= 3) {
/* 1222 */         Log.d("jb4ASDK@RegionMonitor", "onAddGeofencesResult() status: " + getStatusString(statusCode));
/*      */       }
/*      */       
/* 1225 */       if (0 != statusCode)
/*      */       {
/* 1227 */         final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(ETLocationManager.this.applicationContext);
/*      */         try {
/* 1229 */           if (ETPush.getLogLevel() <= 3) {
/* 1230 */             Log.d("jb4ASDK@RegionMonitor", "ERROR: Unable to monitor geofences, set them to inactive in db.");
/*      */           }
/* 1232 */           UpdateBuilder<com.exacttarget.etpushsdk.data.Region, String> updateBuilder = helper.getRegionDao().updateBuilder();
/* 1233 */           updateBuilder.where().eq("location_type", Integer.valueOf(1));
/* 1234 */           updateBuilder.updateColumnValue("active", Boolean.FALSE);
/* 1235 */           updateBuilder.update();
/*      */         } catch (SQLException e) {
/*      */           Handler handler;
/* 1238 */           if (ETPush.getLogLevel() <= 6) {
/* 1239 */             Log.e("jb4ASDK@RegionMonitor", e.getMessage(), e);
/*      */           }
/*      */         } finally {
/*      */           Handler handler;
/* 1243 */           Handler handler = new Handler(ETLocationManager.this.applicationContext.getMainLooper());
/* 1244 */           handler.postDelayed(new Runnable()
/*      */           {
/*      */             public void run() {
/* 1247 */               if ((helper != null) && (helper.isOpen()))
/* 1248 */                 helper.close(); } }, 10000L);
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/* 1255 */       else if (ETPush.getLogLevel() <= 3) {
/* 1256 */         for (String requestId : geofenceRequestIds) {
/* 1257 */           Log.d("jb4ASDK@RegionMonitor", "Monitor Region: " + requestId);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1262 */       ETLocationManager.this.locationClient.disconnect();
/*      */     }
/*      */     
/*      */     public void onConnectionFailed(ConnectionResult connectionResult) {
/* 1266 */       if (ETPush.getLogLevel() <= 3) {
/* 1267 */         Log.d("jb4ASDK@RegionMonitor", "onConnectionFailed()");
/*      */       }
/* 1269 */       if (ETPush.getLogLevel() <= 6) {
/* 1270 */         Log.e("jb4ASDK@RegionMonitor", "PlayServices connection failed: " + connectionResult.getErrorCode());
/*      */       }
/*      */     }
/*      */     
/*      */     public void onConnected(Bundle bundle) {
/* 1275 */       if (ETPush.getLogLevel() <= 3) {
/* 1276 */         Log.d("jb4ASDK@RegionMonitor", "onConnected()");
/*      */       }
/*      */       
/*      */ 
/* 1280 */       if (!Config.isLocationManagerActive()) {
/* 1281 */         return;
/*      */       }
/*      */       
/* 1284 */       if (this.removeAllGeofences.booleanValue()) {
/* 1285 */         ETLocationManager.this.locationClient.removeGeofences(getGeofencePendingIntent(), this);
/*      */       }
/*      */       else {
/* 1288 */         updateGeofencesFromDatabase();
/*      */       }
/*      */     }
/*      */     
/*      */     public void onDisconnected() {
/* 1293 */       if (ETPush.getLogLevel() <= 3) {
/* 1294 */         Log.d("jb4ASDK@RegionMonitor", "onDisconnected()");
/*      */       }
/*      */     }
/*      */     
/*      */     private String getStatusString(int statusCode) {
/* 1299 */       String statusString = null;
/* 1300 */       switch (statusCode) {
/*      */       case 0: 
/* 1302 */         statusString = "SUCCESS";
/* 1303 */         break;
/*      */       case 1: 
/* 1305 */         statusString = "ERROR";
/* 1306 */         if (ETPush.getLogLevel() <= 6) {
/* 1307 */           Log.e("jb4ASDK@RegionMonitor", "LocationStatusCodes.ERROR");
/*      */         }
/*      */         break;
/*      */       case 1000: 
/* 1311 */         statusString = "GEOFENCE_NOT_AVAILABLE";
/* 1312 */         if (ETPush.getLogLevel() <= 5) {
/* 1313 */           Log.w("jb4ASDK@RegionMonitor", "LocationStatusCodes.GEOFENCE_NOT_AVAILABLE");
/*      */         }
/*      */         break;
/*      */       case 1001: 
/* 1317 */         statusString = "GEOFENCE_TOO_MANY_GEOFENCES";
/* 1318 */         if (ETPush.getLogLevel() <= 5) {
/* 1319 */           Log.w("jb4ASDK@RegionMonitor", "LocationStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES");
/*      */         }
/*      */         break;
/*      */       case 1002: 
/* 1323 */         statusString = "GEOFENCE_TOO_MANY_PENDING_INTENTS";
/* 1324 */         if (ETPush.getLogLevel() <= 5) {
/* 1325 */           Log.w("jb4ASDK@RegionMonitor", "LocationStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS");
/*      */         }
/*      */         break;
/*      */       }
/* 1329 */       return statusString;
/*      */     }
/*      */   }
/*      */   
/*      */   private class IBeaconMonitor implements IBeaconConsumer, MonitorNotifier, RangeNotifier {
/*      */     private static final String TAG = "jb4ASDK@IBeaconMonitor";
/*      */     
/*      */     private IBeaconMonitor() {}
/*      */     
/*      */     public void onIBeaconServiceConnect() {
/* 1339 */       Log.d("jb4ASDK@IBeaconMonitor", "onIBeaconServiceConnect");
/* 1340 */       ETLocationManager.this.iBeaconManager.setMonitorNotifier(this);
/* 1341 */       ETLocationManager.this.iBeaconManager.setRangeNotifier(this);
/*      */       
/* 1343 */       if ((ETLocationManager.this.isWatchingProximity()) && (ETLocationManager.this.getProximityInvalidated()))
/*      */       {
/* 1345 */         ETLocationManager.this.startWatchingProximity();
/*      */       }
/*      */       
/* 1348 */       BackgroundEvent bge = (BackgroundEvent)EventBus.getDefault().getStickyEvent(BackgroundEvent.class);
/* 1349 */       if ((bge == null) || (bge.isInBackground())) {
/* 1350 */         ETLocationManager.this.iBeaconManager.setBackgroundMode(ETLocationManager.this.iBeaconMonitor, true);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean bindService(Intent intent, ServiceConnection connection, int mode)
/*      */     {
/* 1357 */       Log.d("jb4ASDK@IBeaconMonitor", "bindService");
/* 1358 */       return getApplicationContext().bindService(intent, connection, mode);
/*      */     }
/*      */     
/*      */     public Context getApplicationContext()
/*      */     {
/* 1363 */       return ETLocationManager.this.applicationContext;
/*      */     }
/*      */     
/*      */     public void unbindService(ServiceConnection serviceConnection)
/*      */     {
/* 1368 */       Log.d("jb4ASDK@IBeaconMonitor", "unbindService");
/* 1369 */       getApplicationContext().unbindService(serviceConnection);
/*      */     }
/*      */     
/*      */ 
/*      */     public void didDetermineStateForRegion(int state, com.radiusnetworks.ibeacon.Region region)
/*      */     {
/* 1375 */       if (ETPush.getLogLevel() <= 3) {
/* 1376 */         Log.d("jb4ASDK@IBeaconMonitor", "BeaconState - {state: " + state + ", id: '" + region.getUniqueId() + "', uuid: '" + region.getProximityUuid() + "', major: " + region.getMajor() + ", minor: " + region.getMinor() + "}");
/*      */       }
/*      */     }
/*      */     
/*      */     public void didEnterRegion(com.radiusnetworks.ibeacon.Region region)
/*      */     {
/* 1382 */       if (ETPush.getLogLevel() <= 3) {
/* 1383 */         Log.d("jb4ASDK@IBeaconMonitor", "didEnterRegion - {id: '" + region.getUniqueId() + "', uuid: '" + region.getProximityUuid() + "', major: " + region.getMajor() + ", minor: " + region.getMinor() + "}");
/*      */       }
/* 1385 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(ETLocationManager.this.applicationContext);
/*      */       try {
/* 1387 */         Dao<com.exacttarget.etpushsdk.data.Region, String> regionDao = helper.getRegionDao();
/* 1388 */         com.exacttarget.etpushsdk.data.Region dbRegion = (com.exacttarget.etpushsdk.data.Region)regionDao.queryForId(region.getUniqueId());
/* 1389 */         dbRegion.setEntryCount(Integer.valueOf(dbRegion.getEntryCount().intValue() + 1));
/* 1390 */         dbRegion.setHasEntered(Boolean.TRUE);
/* 1391 */         regionDao.update(dbRegion);
/* 1392 */         if (ETPush.getLogLevel() <= 3) {
/* 1393 */           Log.d("jb4ASDK@IBeaconMonitor", "Beacon: " + dbRegion.getId() + ", EntryCount: " + dbRegion.getEntryCount());
/*      */         }
/*      */         
/* 1396 */         if (Config.isETanalyticsActive()) {
/* 1397 */           ETAnalytics.engine().startTimeInRegionLog(dbRegion.getId(), true);
/*      */         }
/*      */         
/* 1400 */         EventBus.getDefault().post(new BeaconRegionEnterEvent(dbRegion));
/*      */       } catch (SQLException e) {
/*      */         Handler handler;
/* 1403 */         if (ETPush.getLogLevel() <= 6) {
/* 1404 */           Log.e("jb4ASDK@IBeaconMonitor", e.getMessage(), e);
/*      */         }
/*      */       } catch (ETException e) {
/*      */         Handler handler;
/* 1408 */         if (ETPush.getLogLevel() <= 6) {
/* 1409 */           Log.e("jb4ASDK@IBeaconMonitor", e.getMessage(), e);
/*      */         }
/*      */       } finally {
/*      */         Handler handler;
/* 1413 */         Handler handler = new Handler(ETLocationManager.this.applicationContext.getMainLooper());
/* 1414 */         handler.postDelayed(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1417 */             if ((helper != null) && (helper.isOpen()))
/* 1418 */               helper.close(); } }, 10000L);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void didExitRegion(com.radiusnetworks.ibeacon.Region region)
/*      */     {
/* 1427 */       if (ETPush.getLogLevel() <= 3) {
/* 1428 */         Log.d("jb4ASDK@IBeaconMonitor", "didExitRegion - {id: '" + region.getUniqueId() + "', uuid: '" + region.getProximityUuid() + "', major: " + region.getMajor() + ", minor: " + region.getMinor() + "}");
/*      */       }
/* 1430 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(ETLocationManager.this.applicationContext);
/*      */       try {
/* 1432 */         Dao<com.exacttarget.etpushsdk.data.Region, String> regionDao = helper.getRegionDao();
/* 1433 */         Dao<RegionMessage, Integer> regionMessageDao = helper.getRegionMessageDao();
/* 1434 */         Dao<Message, String> messageDao = helper.getMessageDao();
/*      */         
/*      */ 
/* 1437 */         com.exacttarget.etpushsdk.data.Region dbRegion = (com.exacttarget.etpushsdk.data.Region)regionDao.queryForId(region.getUniqueId());
/* 1438 */         dbRegion.setExitCount(Integer.valueOf(dbRegion.getExitCount().intValue() + 1));
/* 1439 */         dbRegion.setHasEntered(Boolean.FALSE);
/* 1440 */         regionDao.update(dbRegion);
/* 1441 */         if (ETPush.getLogLevel() <= 3) {
/* 1442 */           Log.d("jb4ASDK@IBeaconMonitor", "Beacon: " + dbRegion.getId() + ", ExitCount: " + dbRegion.getEntryCount());
/*      */         }
/*      */         
/*      */ 
/* 1446 */         List<RegionMessage> regionMessages = regionMessageDao.queryForEq("region_id", dbRegion.getId());
/* 1447 */         for (RegionMessage regionMessage : regionMessages) {
/* 1448 */           Message message = (Message)messageDao.queryForId(regionMessage.getMessage().getId());
/* 1449 */           message.setHasEntered(Boolean.FALSE);
/* 1450 */           message.setEntryTime(Long.valueOf(0L));
/* 1451 */           if ((message.getEphemeralMessage().booleanValue()) && (message.getNotifyId() != null)) {
/* 1452 */             NotificationManager nm = (NotificationManager)ETLocationManager.this.applicationContext.getSystemService("notification");
/* 1453 */             nm.cancel(message.getNotifyId().intValue());
/* 1454 */             message.setNotifyId(null);
/*      */           }
/* 1456 */           messageDao.update(message);
/*      */         }
/*      */         
/* 1459 */         if (Config.isETanalyticsActive()) {
/* 1460 */           ETAnalytics.engine().stopTimeInRegionLog(dbRegion.getId());
/*      */         }
/*      */         
/* 1463 */         EventBus.getDefault().post(new BeaconRegionExitEvent(dbRegion));
/*      */       } catch (SQLException e) {
/*      */         Handler handler;
/* 1466 */         if (ETPush.getLogLevel() <= 6) {
/* 1467 */           Log.e("jb4ASDK@IBeaconMonitor", e.getMessage(), e);
/*      */         }
/*      */       } catch (ETException e) {
/*      */         Handler handler;
/* 1471 */         if (ETPush.getLogLevel() <= 6) {
/* 1472 */           Log.e("jb4ASDK@IBeaconMonitor", e.getMessage(), e);
/*      */         }
/*      */       } finally {
/*      */         Handler handler;
/* 1476 */         Handler handler = new Handler(ETLocationManager.this.applicationContext.getMainLooper());
/* 1477 */         handler.postDelayed(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1480 */             if ((helper != null) && (helper.isOpen()))
/* 1481 */               helper.close(); } }, 10000L);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void didRangeBeaconsInRegion(Collection<IBeacon> beacons, com.radiusnetworks.ibeacon.Region region)
/*      */     {
/* 1493 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(ETLocationManager.this.applicationContext);
/*      */       try {
/* 1495 */         regionDao = helper.getRegionDao();
/* 1496 */         for (IBeacon beacon : beacons) {
/* 1497 */           if (ETPush.getLogLevel() <= 3) {
/* 1498 */             Log.d("jb4ASDK@IBeaconMonitor", "didRangeBeaconsInRegion - {proximity: " + beacon.getProximity() + ", id: '" + region.getUniqueId() + "', uuid: '" + region.getProximityUuid() + "', major: " + region.getMajor() + ", minor: " + region.getMinor() + "}");
/*      */           }
/*      */           
/* 1501 */           com.exacttarget.etpushsdk.data.Region dbRegion = (com.exacttarget.etpushsdk.data.Region)regionDao.queryForId(region.getUniqueId());
/* 1502 */           if (dbRegion != null) {
/* 1503 */             if (dbRegion.getHasEntered().booleanValue()) {
/* 1504 */               ETPush.pushManager().showFenceOrProximityMessage(region.getUniqueId(), -1, beacon.getProximity());
/* 1505 */               EventBus.getDefault().post(new BeaconRegionRangeEvent(dbRegion, beacon.getProximity(), beacon.getRssi(), beacon.getAccuracy()));
/*      */ 
/*      */             }
/* 1508 */             else if (ETPush.getLogLevel() <= 3) {
/* 1509 */               Log.d("jb4ASDK@IBeaconMonitor", "Ranged region " + dbRegion.getId() + " but monitoring hasn't yet entered");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (ETException e) {
/*      */         Dao<com.exacttarget.etpushsdk.data.Region, String> regionDao;
/*      */         Handler handler;
/* 1517 */         if (ETPush.getLogLevel() <= 6) {
/* 1518 */           Log.e("jb4ASDK@IBeaconMonitor", e.getMessage(), e);
/*      */         }
/*      */       } catch (SQLException e) {
/*      */         Handler handler;
/* 1522 */         if (ETPush.getLogLevel() <= 6) {
/* 1523 */           Log.e("jb4ASDK@IBeaconMonitor", e.getMessage(), e);
/*      */         }
/*      */       } finally {
/*      */         Handler handler;
/* 1527 */         Handler handler = new Handler(ETLocationManager.this.applicationContext.getMainLooper());
/* 1528 */         handler.postDelayed(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1531 */             if ((helper != null) && (helper.isOpen()))
/* 1532 */               helper.close(); } }, 10000L);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETLocationManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */