/*      */ package com.exacttarget.etpushsdk;
/*      */ 
/*      */ import android.app.Activity;
/*      */ import android.app.IntentService;
/*      */ import android.content.BroadcastReceiver;
/*      */ import android.content.Context;
/*      */ import android.content.Intent;
/*      */ import android.content.SharedPreferences;
/*      */ import android.content.SharedPreferences.Editor;
/*      */ import android.content.pm.PackageInfo;
/*      */ import android.content.pm.PackageManager;
/*      */ import android.content.pm.PackageManager.NameNotFoundException;
/*      */ import android.content.pm.ResolveInfo;
/*      */ import android.content.res.Resources;
/*      */ import android.database.sqlite.SQLiteDatabase;
/*      */ import android.graphics.Bitmap;
/*      */ import android.graphics.BitmapFactory;
/*      */ import android.net.Uri;
/*      */ import android.os.AsyncTask;
/*      */ import android.os.Build.VERSION;
/*      */ import android.os.Handler;
/*      */ import android.os.PowerManager;
/*      */ import android.os.PowerManager.WakeLock;
/*      */ import android.text.TextUtils;
/*      */ import android.util.Log;
/*      */ import com.exacttarget.etpushsdk.data.Attribute;
/*      */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*      */ import com.exacttarget.etpushsdk.data.Message;
/*      */ import com.exacttarget.etpushsdk.data.Region;
/*      */ import com.exacttarget.etpushsdk.data.RegionMessage;
/*      */ import com.exacttarget.etpushsdk.data.Registration;
/*      */ import com.exacttarget.etpushsdk.event.BackgroundEvent;
/*      */ import com.exacttarget.etpushsdk.event.RegistrationEvent;
/*      */ import com.exacttarget.etpushsdk.event.RegistrationEventListener;
/*      */ import com.exacttarget.etpushsdk.location.receiver.BootReceiver;
/*      */ import com.exacttarget.etpushsdk.location.receiver.LocationChangedReceiver;
/*      */ import com.exacttarget.etpushsdk.location.receiver.PassiveLocationChangedReceiver;
/*      */ import com.exacttarget.etpushsdk.location.receiver.PowerStateChangedReceiver;
/*      */ import com.exacttarget.etpushsdk.util.ETAmazonDeviceMessagingUtil;
/*      */ import com.exacttarget.etpushsdk.util.ETGooglePlayServicesUtil;
/*      */ import com.exacttarget.etpushsdk.util.EventBus;
/*      */ import com.google.android.gms.gcm.GoogleCloudMessaging;
/*      */ import com.j256.ormlite.dao.Dao;
/*      */ import com.j256.ormlite.stmt.DeleteBuilder;
/*      */ import com.j256.ormlite.stmt.Where;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CountDownLatch;
/*      */ import java.util.concurrent.TimeUnit;
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
/*      */ public class ETPush
/*      */   implements RegistrationEventListener
/*      */ {
/*      */   private static final String TAG = "jb4ASDK@ETPush";
/*      */   private static final String PUSH_ENABLED_KEY = "et_push_enabled";
/*      */   private static final String GCM_REG_ID_KEY = "gcm_reg_id_key";
/*      */   private static final String GCM_APP_VERSION_KEY = "gcm_app_version_key";
/*      */   private static final String GCM_SENDER_ID_KEY = "gcm_sender_id_key";
/*      */   private static final String ANDROID_VERSION = "et_android_version";
/*      */   private Class<?> recipentClass;
/*      */   private Class<?> openDirectRecipient;
/*      */   private Class<?> cloudPageRecipient;
/*   86 */   private String notificationAction = null;
/*   87 */   private Uri notificationActionUri = null;
/*      */   
/*   89 */   private Integer notificationResourceId = null;
/*      */   
/*      */   private static String gcmSenderID;
/*      */   
/*      */   private static Context applicationContext;
/*      */   
/*      */   private static SharedPreferences prefs;
/*      */   
/*      */   private static ETPush pushManager;
/*   98 */   private static Set<Integer> activityHashSet = new HashSet();
/*   99 */   private static PauseWaitTask pauseWaitTask = null;
/*      */   
/*      */   private static PowerManager.WakeLock wakeLock;
/*      */   
/*      */   private static Registration registration;
/*  104 */   private static Integer logLevel = Integer.valueOf(5);
/*      */   
/*  106 */   private Object adm = null;
/*      */   
/*  108 */   private static final CountDownLatch latch = new CountDownLatch(1);
/*      */   
/*      */   private ETPush(Context applicationContext) {
/*  111 */     applicationContext = applicationContext;
/*      */     
/*  113 */     prefs = applicationContext.getSharedPreferences("ETPush", 0);
/*      */     
/*  115 */     registration = new Registration(applicationContext);
/*  116 */     registration.setDeviceToken(getRegistrationId());
/*      */     
/*  118 */     PowerManager pm = (PowerManager)applicationContext.getSystemService("power");
/*  119 */     wakeLock = pm.newWakeLock(1, "jb4ASDK@ETPush");
/*      */     
/*  121 */     EventBus.getDefault().register(this);
/*      */   }
/*      */   
/*      */   public static String getSDKVersionName(Context applicationContext) {
/*  125 */     return "3.5.0";
/*      */   }
/*      */   
/*      */   public static int getSDKVersionCode(Context applicationContext) {
/*  129 */     return 3518;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ETPush pushManager()
/*      */     throws ETException
/*      */   {
/*  138 */     initiateLatch(30000L);
/*      */     
/*  140 */     if (pushManager == null) {
/*  141 */       throw new ETException("No ETPush instance available for use. Did you forget to call readyAimFire() first?");
/*      */     }
/*      */     
/*  144 */     return pushManager;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static void initiateLatch(long latchTimeout)
/*      */     throws ETException
/*      */   {
/*  154 */     if (latchTimeout <= 0L) {
/*  155 */       throw new ETException("Latch timeout must be a positive number > 0.");
/*      */     }
/*      */     try
/*      */     {
/*  159 */       if (!latch.await(latchTimeout, TimeUnit.MILLISECONDS)) {
/*  160 */         throw new ETException("Initialization did not complete in a timely fashion.");
/*      */       }
/*      */     }
/*      */     catch (InterruptedException e) {
/*  164 */       throw new ETException("Initialization did not complete in a timely fashion. Our error was: " + e.getMessage());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void readyAimFire(Context applicationContext, String etAppId, String accessToken, String gcmSenderId, boolean enableETanalytics, boolean enableLocationManager, boolean enableCloudPages)
/*      */     throws ETException
/*      */   {
/*  185 */     readyAimFire(applicationContext, etAppId, accessToken, gcmSenderId, enableETanalytics, false, enableLocationManager, enableCloudPages);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void readyAimFire(Context applicationContext, final String etAppId, final String accessToken, final String gcmSenderId, final boolean enableETanalytics, final boolean enablePIanalytics, final boolean enableLocationManager, final boolean enableCloudPages)
/*      */     throws ETException
/*      */   {
/*  208 */     if (pushManager == null) {
/*  209 */       if (getLogLevel() <= 3) {
/*  210 */         Log.d("jb4ASDK@ETPush", "readyAimFire()");
/*      */       }
/*      */       
/*  213 */       if (!etAppId.toLowerCase().matches("[0-9a-f]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}")) {
/*  214 */         if (getLogLevel() <= 6) {
/*  215 */           Log.e("jb4ASDK@ETPush:readyAimFire", "ERROR: The etAppId is not a valid UUID. Did you copy/paste incorrectly?");
/*      */         }
/*  217 */         throw new ETException("The etAppId is not a valid UUID. Did you copy/paste incorrectly?");
/*      */       }
/*      */       
/*  220 */       if (accessToken.length() != 24) {
/*  221 */         if (getLogLevel() <= 6) {
/*  222 */           Log.e("jb4ASDK@ETPush:readyAimFire", "ERROR: The accessToken is not 24 characters. Did you copy/paste incorrectly?");
/*      */         }
/*  224 */         throw new ETException("The accessToken is not 24 characters. Did you copy/paste incorrectly?");
/*      */       }
/*      */       
/*  227 */       new AsyncTask()
/*      */       {
/*      */ 
/*      */         protected Void doInBackground(Void... params)
/*      */         {
/*  232 */           final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.val$applicationContext);
/*      */           
/*  234 */           Handler handler = new Handler(this.val$applicationContext.getMainLooper());
/*  235 */           handler.postDelayed(new Runnable()
/*      */           {
/*      */             public void run() {
/*  238 */               if ((helper != null) && (helper.isOpen()))
/*  239 */                 helper.close(); } }, 10000L);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  244 */           ETPush.access$002(new ETPush(this.val$applicationContext, null));
/*  245 */           ETPush.pushManager.setGcmSenderID(gcmSenderId);
/*      */           
/*  247 */           Config.setEtAppId(etAppId);
/*  248 */           Config.setAccessToken(accessToken);
/*      */           
/*  250 */           Config.setETanalyticsActive(enableETanalytics);
/*  251 */           Config.setPIanalyticsActive(enablePIanalytics);
/*  252 */           Config.setLocationManagerActive(enableLocationManager);
/*  253 */           Config.setCloudPagesActive(enableCloudPages);
/*      */           try
/*      */           {
/*  256 */             if (!Config.isReadOnly(this.val$applicationContext))
/*      */             {
/*      */ 
/*  259 */               ETPush.checkManifestSetup(this.val$applicationContext);
/*      */             }
/*      */             
/*  262 */             if ((enableETanalytics | enablePIanalytics))
/*      */             {
/*  264 */               ETAnalytics.readyAimFire(this.val$applicationContext);
/*      */             }
/*      */             
/*  267 */             if (enableLocationManager)
/*      */             {
/*  269 */               ETLocationManager.readyAimFire(this.val$applicationContext);
/*      */             }
/*      */             
/*  272 */             if (enableCloudPages)
/*      */             {
/*  274 */               ETCloudPageManager.readyAimFire(this.val$applicationContext);
/*      */             }
/*      */           }
/*      */           catch (ETException e) {
/*  278 */             if (ETPush.getLogLevel() <= 6) {
/*  279 */               Log.e("jb4ASDK@ETPush", e.getMessage(), e);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  284 */           ETPush.latch.countDown();
/*  285 */           if (ETPush.getLogLevel() <= 3) {
/*  286 */             Log.d("jb4ASDK@ETPush", "readyAimFire() initialization completed");
/*      */           }
/*      */           
/*      */           try
/*      */           {
/*  291 */             if ((!Config.isReadOnly(this.val$applicationContext)) && (ETPush.pushManager.thirdPartyPushServicesAvailable())) {
/*  292 */               ETPush.pushManager.registerForRemoteNotifications();
/*      */             }
/*      */           } catch (ETException e) {
/*  295 */             if (ETPush.getLogLevel() <= 6) {
/*  296 */               Log.e("jb4ASDK@ETPush", e.getMessage(), e);
/*      */             }
/*      */           }
/*      */           
/*  300 */           return null; } }.execute(new Void[0]);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  305 */       throw new ETException("You must have called readyAimFire more than once. It should only be called from your Application's Application#onCreate() method.");
/*      */     }
/*      */   }
/*      */   
/*      */   private static void checkManifestSetup(Context applicationContext) throws ETException {
/*  310 */     PackageManager packageManager = applicationContext.getPackageManager();
/*  311 */     String packageName = applicationContext.getPackageName();
/*      */     
/*      */ 
/*  314 */     List<String> permissions = new ArrayList();
/*  315 */     permissions.add(packageName + ".permission.C2D_MESSAGE");
/*      */     
/*  317 */     if (ETGooglePlayServicesUtil.isAvailable(applicationContext, false)) {
/*  318 */       permissions.add("com.google.android.c2dm.permission.RECEIVE");
/*      */     }
/*      */     
/*  321 */     if ((ETAmazonDeviceMessagingUtil.isAmazonDevice()) && 
/*  322 */       (!ETAmazonDeviceMessagingUtil.isAvailable(applicationContext, false))) {
/*  323 */       throw new ETException("ApplicationManifest.xml missing required manifest entries for Amazon Device Messaging.");
/*      */     }
/*      */     
/*  326 */     permissions.add("android.permission.INTERNET");
/*  327 */     permissions.add("android.permission.GET_ACCOUNTS");
/*  328 */     permissions.add("android.permission.WAKE_LOCK");
/*  329 */     permissions.add("android.permission.ACCESS_NETWORK_STATE");
/*  330 */     permissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
/*  331 */     permissions.add("android.permission.ACCESS_WIFI_STATE");
/*      */     
/*  333 */     if (Config.isLocationManagerActive()) {
/*  334 */       permissions.add("android.permission.ACCESS_COARSE_LOCATION");
/*  335 */       permissions.add("android.permission.ACCESS_FINE_LOCATION");
/*  336 */       permissions.add("android.permission.RECEIVE_BOOT_COMPLETED");
/*      */     }
/*      */     
/*  339 */     for (String permission : permissions) {
/*  340 */       if (0 != packageManager.checkPermission(permission, packageName)) {
/*  341 */         throw new ETException("ApplicationManifest.xml missing required permission: " + permission);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  347 */     if (ETGooglePlayServicesUtil.isAvailable(applicationContext, false))
/*      */     {
/*  349 */       Intent regIntent = new Intent("com.google.android.c2dm.intent.REGISTRATION");
/*  350 */       regIntent.addCategory(packageName);
/*  351 */       List<ResolveInfo> receiversInfo = packageManager.queryBroadcastReceivers(regIntent, 0);
/*  352 */       if ((receiversInfo == null) || (receiversInfo.size() < 1)) {
/*  353 */         throw new ETException("No BroadcastReceiver defined in ApplicationManifest.xml to handle GCM registration. Did you forget to add ET_GenericReceiver to your manifest or mis-configure it?");
/*      */       }
/*      */       
/*  356 */       Intent pushIntent = new Intent("com.google.android.c2dm.intent.RECEIVE");
/*  357 */       pushIntent.addCategory(packageName);
/*  358 */       receiversInfo = packageManager.queryBroadcastReceivers(pushIntent, 0);
/*  359 */       if ((receiversInfo == null) || (receiversInfo.size() < 1)) {
/*  360 */         throw new ETException("No BroadcastReceiver defined in ApplicationManifest.xml to handle GCM messages. Did you forget to add ET_GenericReceiver to your manifest or mis-configure it?");
/*      */       }
/*      */     }
/*  363 */     else if (ETAmazonDeviceMessagingUtil.isAmazonDevice())
/*      */     {
/*  365 */       Intent regIntent = new Intent("com.amazon.device.messaging.intent.REGISTRATION");
/*  366 */       regIntent.addCategory(packageName);
/*  367 */       List<ResolveInfo> receiversInfo = packageManager.queryBroadcastReceivers(regIntent, 0);
/*  368 */       if ((receiversInfo == null) || (receiversInfo.size() < 1)) {
/*  369 */         throw new ETException("No BroadcastReceiver defined in ApplicationManifest.xml to handle ADM registration. Did you forget to add ET_GenericReceiver to your manifest or mis-configure it?");
/*      */       }
/*      */       
/*  372 */       Intent pushIntent = new Intent("com.amazon.device.messaging.intent.RECEIVE");
/*  373 */       pushIntent.addCategory(packageName);
/*  374 */       receiversInfo = packageManager.queryBroadcastReceivers(pushIntent, 0);
/*  375 */       if ((receiversInfo == null) || (receiversInfo.size() < 1)) {
/*  376 */         throw new ETException("No BroadcastReceiver defined in ApplicationManifest.xml to handle ADM messages. Did you forget to add ET_GenericReceiver to your manifest or mis-configure it?");
/*      */       }
/*      */     }
/*      */     
/*  380 */     List<Class<? extends BroadcastReceiver>> receiverClasses = new ArrayList();
/*  381 */     receiverClasses.add(ETSendDataReceiver.class);
/*  382 */     receiverClasses.add(ETPackageReplacedReceiver.class);
/*  383 */     if (Config.isLocationManagerActive()) {
/*  384 */       receiverClasses.add(LocationChangedReceiver.class);
/*  385 */       receiverClasses.add(PassiveLocationChangedReceiver.class);
/*  386 */       receiverClasses.add(PowerStateChangedReceiver.class);
/*  387 */       receiverClasses.add(ETLocationTimeoutReceiver.class);
/*  388 */       receiverClasses.add(ETLocationWakeupReceiver.class);
/*  389 */       receiverClasses.add(ETLocationProviderChangeReceiver.class);
/*  390 */       receiverClasses.add(ETGeofenceReceiver.class);
/*  391 */       receiverClasses.add(BootReceiver.class);
/*      */     }
/*  393 */     for (Class<? extends BroadcastReceiver> receiverClass : receiverClasses) {
/*  394 */       Intent locIntent = new Intent(applicationContext, receiverClass);
/*  395 */       List<ResolveInfo> receiversInfo = packageManager.queryBroadcastReceivers(locIntent, 0);
/*  396 */       if ((receiversInfo == null) || (receiversInfo.size() < 1)) {
/*  397 */         throw new ETException("ApplicationManifest.xml missing BroadcastReceiver: " + receiverClass.getName());
/*      */       }
/*      */     }
/*      */     
/*  401 */     List<Class<? extends IntentService>> serviceClasses = new ArrayList();
/*  402 */     serviceClasses.add(ETSendDataIntentService.class);
/*  403 */     if (Config.isLocationManagerActive()) {
/*  404 */       serviceClasses.add(ETLocationTimeoutService.class);
/*  405 */       serviceClasses.add(ETLocationWakeupService.class);
/*  406 */       serviceClasses.add(ETLocationProviderChangeService.class);
/*  407 */       serviceClasses.add(ETGeofenceIntentService.class);
/*      */     }
/*  409 */     for (Class<? extends IntentService> serviceClass : serviceClasses) {
/*  410 */       Intent serviceIntent = new Intent(applicationContext, serviceClass);
/*  411 */       List<ResolveInfo> servicesInfo = packageManager.queryIntentServices(serviceIntent, 0);
/*  412 */       if ((servicesInfo == null) || (servicesInfo.size() < 1)) {
/*  413 */         throw new ETException("ApplicationManifest.xml missing Service: " + serviceClass.getName());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  418 */     if ((!checkActivityExistsInManifest(applicationContext, ETLandingPagePresenter.class)) && 
/*  419 */       (getLogLevel() <= 5)) {
/*  420 */       Log.w("jb4ASDK@ETPush", ETLandingPagePresenter.class.getName() + " is not found in AndroidManifest.  This will impact the ability to display CloudPages and OpenDirect URLs.");
/*      */     }
/*      */   }
/*      */   
/*      */   protected static boolean checkActivityExistsInManifest(Context applicationContext, Class<?> cls)
/*      */   {
/*  426 */     Intent recipientIntent = new Intent();
/*  427 */     recipientIntent.setClass(applicationContext, cls);
/*      */     
/*  429 */     List<ResolveInfo> list = applicationContext.getPackageManager().queryIntentActivities(recipientIntent, 65536);
/*  430 */     if (list.size() < 1) {
/*  431 */       return false;
/*      */     }
/*      */     
/*  434 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void registerForRemoteNotifications()
/*      */     throws ETException
/*      */   {
/*  446 */     registerForRemoteNotifications(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void registerForRemoteNotifications(boolean forceRegistration)
/*      */     throws ETException
/*      */   {
/*  457 */     String registeredAndroidVersion = (String)Config.getETSharedPref(applicationContext, applicationContext.getSharedPreferences("jb4ASDK@ETPush", 0), "et_android_version", "");
/*  458 */     String currentAndroidVersion = Build.VERSION.RELEASE;
/*      */     
/*  460 */     boolean newAndroidVersion = !currentAndroidVersion.equals(registeredAndroidVersion);
/*      */     
/*  462 */     if (ETAmazonDeviceMessagingUtil.isAmazonDevice()) {
/*  463 */       if (ETAmazonDeviceMessagingUtil.isAvailable(applicationContext, true))
/*      */       {
/*  465 */         String regid = ADM_getRegistrationId();
/*  466 */         if ((regid.isEmpty()) || (forceRegistration) || (newAndroidVersion))
/*      */         {
/*  468 */           ADM_startRegister();
/*      */         }
/*      */         else
/*      */         {
/*  472 */           registerDeviceToken(regid);
/*      */         }
/*      */       }
/*      */     }
/*  476 */     else if (ETGooglePlayServicesUtil.isAvailable(applicationContext, true)) {
/*  477 */       String regid = getRegistrationId();
/*      */       
/*  479 */       if ((regid.isEmpty()) || (forceRegistration) || (newAndroidVersion)) {
/*  480 */         registerGCM_inBackground();
/*      */       }
/*      */       else
/*      */       {
/*  484 */         registerDeviceToken(regid);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void registerGCM_inBackground()
/*      */   {
/*  496 */     saveAndroidVersion();
/*  497 */     new AsyncTask()
/*      */     {
/*      */       protected String doInBackground(Void... params) {
/*  500 */         String msg = "";
/*      */         try {
/*  502 */           GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(ETPush.applicationContext);
/*      */           
/*  504 */           String regid = gcm.register(new String[] { ETPush.gcmSenderID });
/*  505 */           msg = "Device registered, registration ID=" + regid;
/*      */         }
/*      */         catch (IOException ex) {
/*  508 */           if (ETPush.getLogLevel() <= 6) {
/*  509 */             Log.e("jb4ASDK@ETPush", ex.getMessage(), ex);
/*      */           }
/*      */           
/*  512 */           msg = "Error :" + ex.getMessage();
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  517 */         return msg;
/*      */       }
/*      */       
/*      */       protected void onPostExecute(String msg)
/*      */       {
/*  522 */         if (ETPush.getLogLevel() <= 3)
/*  523 */           Log.d("jb4ASDK@ETPush", msg); } }.execute(new Void[] { null, null, null });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void saveAndroidVersion()
/*      */   {
/*  531 */     SharedPreferences.Editor editor = prefs.edit();
/*  532 */     editor.putString("et_android_version", Build.VERSION.RELEASE);
/*  533 */     editor.commit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void storeRegistrationId(String regId)
/*      */   {
/*  543 */     int appVersion = getAppVersion();
/*  544 */     if (getLogLevel() <= 3) {
/*  545 */       Log.d("jb4ASDK@ETPush", "Saving regId and app version " + appVersion);
/*      */     }
/*  547 */     SharedPreferences.Editor editor = prefs.edit();
/*  548 */     editor.putString("gcm_reg_id_key", regId);
/*  549 */     editor.putInt("gcm_app_version_key", appVersion);
/*  550 */     editor.putString("gcm_sender_id_key", gcmSenderID);
/*  551 */     editor.commit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getRegistrationId()
/*      */   {
/*  563 */     String registrationId = (String)Config.getETSharedPref(applicationContext, applicationContext.getSharedPreferences("jb4ASDK@ETPush", 0), "gcm_reg_id_key", "");
/*  564 */     if (registrationId.isEmpty()) {
/*  565 */       if (getLogLevel() <= 4) {
/*  566 */         Log.i("jb4ASDK@ETPush", "Registration not found.");
/*      */       }
/*  568 */       return "";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  573 */     int registeredVersion = ((Integer)Config.getETSharedPref(applicationContext, applicationContext.getSharedPreferences("jb4ASDK@ETPush", 0), "gcm_app_version_key", Integer.valueOf(Integer.MIN_VALUE))).intValue();
/*  574 */     int currentVersion = getAppVersion();
/*  575 */     if (registeredVersion != currentVersion) {
/*  576 */       if (getLogLevel() <= 4) {
/*  577 */         Log.i("jb4ASDK@ETPush", "App version changed.");
/*      */       }
/*  579 */       return "";
/*      */     }
/*      */     
/*  582 */     String registeredGcmSenderId = (String)Config.getETSharedPref(applicationContext, applicationContext.getSharedPreferences("jb4ASDK@ETPush", 0), "gcm_sender_id_key", "");
/*      */     
/*  584 */     if (!registeredGcmSenderId.equals(gcmSenderID)) {
/*  585 */       if (getLogLevel() <= 4) {
/*  586 */         Log.i("jb4ASDK@ETPush", "GCM Sender Id changed.");
/*      */       }
/*  588 */       return "";
/*      */     }
/*      */     
/*  591 */     return registrationId;
/*      */   }
/*      */   
/*      */ 
/*      */   private int getAppVersion()
/*      */   {
/*      */     try
/*      */     {
/*  599 */       PackageInfo packageInfo = applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
/*      */       
/*  601 */       return packageInfo.versionCode;
/*      */     }
/*      */     catch (PackageManager.NameNotFoundException e)
/*      */     {
/*  605 */       throw new RuntimeException("Could not get package name: " + e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void setPushStateInPreferences(boolean state) {
/*  610 */     prefs.edit().putBoolean("et_push_enabled", state).commit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPushEnabled()
/*      */   {
/*  619 */     return ((Boolean)Config.getETSharedPref(applicationContext, applicationContext.getSharedPreferences("jb4ASDK@ETPush", 0), "et_push_enabled", Boolean.valueOf(true))).booleanValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void enablePush()
/*      */     throws ETException
/*      */   {
/*  630 */     if ((!Config.isReadOnly(applicationContext)) && 
/*  631 */       (thirdPartyPushServicesAvailable()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  640 */       registration = null;
/*  641 */       setPushStateInPreferences(true);
/*  642 */       sendRegistration();
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
/*      */   protected boolean thirdPartyPushServicesAvailable()
/*      */     throws ETException
/*      */   {
/*  657 */     if ((ETGooglePlayServicesUtil.isAvailable(applicationContext, true)) || (ETAmazonDeviceMessagingUtil.isAvailable(applicationContext, true))) {
/*  658 */       return true;
/*      */     }
/*  660 */     return false;
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
/*      */   public void disablePush()
/*      */     throws ETException
/*      */   {
/*  674 */     if ((!Config.isReadOnly(applicationContext)) && 
/*  675 */       (thirdPartyPushServicesAvailable())) {
/*  676 */       setPushStateInPreferences(false);
/*  677 */       registerDeviceToken("");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void activityPaused(Activity activity)
/*      */   {
/*  689 */     if ((!Config.isETanalyticsActive()) && (!Config.isPIanalyticsActive())) {
/*  690 */       if (getLogLevel() <= 5) {
/*  691 */         Log.w("jb4ASDK@ETPush", "--WARNING-- activityPaused() called, but analytics is disabled.");
/*      */       }
/*  693 */       return;
/*      */     }
/*      */     
/*  696 */     Integer hashCode = Integer.valueOf(activity.hashCode());
/*  697 */     if (activityHashSet.contains(hashCode)) {
/*  698 */       if (getLogLevel() <= 3) {
/*  699 */         Log.d("jb4ASDK@ETPush", "paused: " + hashCode);
/*      */       }
/*  701 */       activityHashSet.remove(hashCode);
/*      */ 
/*      */     }
/*  704 */     else if (getLogLevel() <= 5) {
/*  705 */       Log.w("jb4ASDK@ETPush", "unrecognized activity: " + hashCode);
/*      */     }
/*      */     
/*      */ 
/*  709 */     if (activityHashSet.isEmpty())
/*      */     {
/*      */ 
/*  712 */       if (pauseWaitTask == null) {
/*  713 */         if (getLogLevel() <= 3) {
/*  714 */           Log.d("jb4ASDK@ETPush", "start pauseWaitTask");
/*      */         }
/*  716 */         wakeLock.acquire();
/*  717 */         pauseWaitTask = new PauseWaitTask(null);
/*  718 */         pauseWaitTask.execute(new Void[0]);
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
/*      */   public void activityResumed(Activity activity)
/*      */   {
/*  731 */     if ((!Config.isETanalyticsActive()) && (!Config.isPIanalyticsActive())) {
/*  732 */       if (getLogLevel() <= 5) {
/*  733 */         Log.w("jb4ASDK@ETPush", "--WARNING-- activityResumed() called, but analytics is disabled.");
/*      */       }
/*  735 */       return;
/*      */     }
/*      */     
/*  738 */     Integer hashCode = Integer.valueOf(activity.hashCode());
/*  739 */     if ((activityHashSet.isEmpty()) && (pauseWaitTask == null))
/*      */     {
/*  741 */       EventBus.getDefault().postSticky(new BackgroundEvent(false));
/*      */     }
/*      */     
/*  744 */     if (!activityHashSet.contains(hashCode))
/*      */     {
/*  746 */       if (getLogLevel() <= 3) {
/*  747 */         Log.d("jb4ASDK@ETPush", "resumed: " + hashCode);
/*      */       }
/*  749 */       activityHashSet.add(hashCode);
/*  750 */       if ((pauseWaitTask != null) && (!pauseWaitTask.isCancelled()))
/*      */       {
/*      */ 
/*  753 */         pauseWaitTask.cancel(true);
/*      */       }
/*  755 */       pauseWaitTask = null;
/*      */ 
/*      */     }
/*  758 */     else if (getLogLevel() <= 5) {
/*  759 */       Log.w("jb4ASDK@ETPush", "activityResumed() already called for this activity.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNotificationRecipientClass(Class<?> cls)
/*      */     throws ETException
/*      */   {
/*  769 */     if (!checkActivityExistsInManifest(applicationContext, cls)) {
/*  770 */       throw new ETException(cls.getName() + " is not found in AndroidManifest.");
/*      */     }
/*  772 */     this.recipentClass = cls;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> getNotificationRecipientClass()
/*      */   {
/*  781 */     return this.recipentClass;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNotificationResourceId(Integer notificationResourceId)
/*      */     throws ETException
/*      */   {
/*  793 */     if (notificationResourceId != null) {
/*  794 */       String name = applicationContext.getResources().getResourceName(notificationResourceId.intValue());
/*  795 */       if ((name == null) || (!name.startsWith(applicationContext.getPackageName()))) {
/*  796 */         throw new ETException("Notification Icon is not found in Application Resources.");
/*      */       }
/*      */       
/*  799 */       Bitmap bMap = BitmapFactory.decodeResource(applicationContext.getResources(), notificationResourceId.intValue());
/*      */       
/*  801 */       if ((!bMap.hasAlpha()) && 
/*  802 */         (getLogLevel() <= 3)) {
/*  803 */         Log.d("jb4ASDK@ETPush", "Notification icon should have alpha channel colors only to be properly displayed in Android 5.0");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  808 */     this.notificationResourceId = notificationResourceId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Integer getNotificationResourceId()
/*      */   {
/*  817 */     return this.notificationResourceId;
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
/*      */   public void addAttribute(String attribute, String value)
/*      */     throws ETException
/*      */   {
/*  832 */     if (!Config.isReadOnly(applicationContext)) {
/*  833 */       if (registration == null) {
/*  834 */         registration = new Registration(applicationContext);
/*  835 */         registration.setDeviceToken(getRegistrationId());
/*      */       }
/*      */       
/*  838 */       registration.addAttribute(new Attribute(attribute, value));
/*      */       
/*      */ 
/*  841 */       sendRegistration();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeAttribute(String attribute)
/*      */     throws ETException
/*      */   {
/*  853 */     if (!Config.isReadOnly(applicationContext)) {
/*  854 */       if (registration == null) {
/*  855 */         registration = new Registration(applicationContext);
/*  856 */         registration.setDeviceToken(getRegistrationId());
/*      */       }
/*  858 */       registration.removeAttribute(new Attribute(attribute, ""));
/*      */       
/*      */ 
/*  861 */       sendRegistration();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ArrayList<Attribute> getAttributes()
/*      */     throws ETException
/*      */   {
/*  870 */     if (registration == null) {
/*  871 */       registration = new Registration(applicationContext);
/*  872 */       registration.setDeviceToken(getRegistrationId());
/*      */     }
/*  874 */     return registration.getAttributes();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addTag(String tag)
/*      */     throws ETException
/*      */   {
/*  885 */     if (!Config.isReadOnly(applicationContext)) {
/*  886 */       if (registration == null) {
/*  887 */         registration = new Registration(applicationContext);
/*  888 */         registration.setDeviceToken(getRegistrationId());
/*      */       }
/*  890 */       registration.addTag(tag);
/*      */       
/*      */ 
/*  893 */       sendRegistration();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeTag(String tag)
/*      */     throws ETException
/*      */   {
/*  903 */     if (!Config.isReadOnly(applicationContext)) {
/*  904 */       if (registration == null) {
/*  905 */         registration = new Registration(applicationContext);
/*  906 */         registration.setDeviceToken(getRegistrationId());
/*      */       }
/*  908 */       registration.removeTag(tag);
/*      */       
/*      */ 
/*  911 */       sendRegistration();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public HashSet<String> getTags()
/*      */     throws ETException
/*      */   {
/*  920 */     if (registration == null) {
/*  921 */       registration = new Registration(applicationContext);
/*  922 */       registration.setDeviceToken(getRegistrationId());
/*      */     }
/*  924 */     return registration.getTags();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSubscriberKey(String subKey)
/*      */     throws ETException
/*      */   {
/*  933 */     if (!Config.isReadOnly(applicationContext)) {
/*  934 */       if (registration == null) {
/*  935 */         registration = new Registration(applicationContext);
/*  936 */         registration.setDeviceToken(getRegistrationId());
/*      */       }
/*      */       
/*  939 */       registration.setSubscriberKey(subKey);
/*      */       
/*      */ 
/*  942 */       sendRegistration();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setLogLevel(int logLevel)
/*      */     throws ETException
/*      */   {
/*  953 */     if ((logLevel >= 2) && (logLevel <= 7)) {
/*  954 */       logLevel = Integer.valueOf(logLevel);
/*      */     }
/*      */     else {
/*  957 */       throw new ETException("logLevel must be between Log.VERBOSE and Log.ASSERT");
/*      */     }
/*      */     
/*  960 */     if (logLevel.intValue() <= 3) {
/*  961 */       Log.d("jb4ASDK@ETPush", "Logging set to DEBUG.");
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getLogLevel() {
/*  966 */     return logLevel.intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void registerDeviceToken(String deviceToken)
/*      */     throws ETException
/*      */   {
/*  975 */     if (registration == null) {
/*  976 */       registration = new Registration(applicationContext);
/*      */     }
/*  978 */     registration.setDeviceToken(deviceToken);
/*      */     
/*  980 */     if (!deviceToken.isEmpty())
/*      */     {
/*  982 */       storeRegistrationId(deviceToken);
/*      */     }
/*      */     
/*  985 */     sendRegistration();
/*      */   }
/*      */   
/*      */   public void onEvent(RegistrationEvent event) {
/*  989 */     if (getLogLevel() <= 3) {
/*  990 */       Log.d("jb4ASDK@ETPush", "onEventRegistrationEvent() id=" + event.getId());
/*      */     }
/*  992 */     if ((event.getId() != null) && (event.getId().intValue() > 0)) {
/*  993 */       final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(applicationContext);
/*      */       try {
/*  995 */         DeleteBuilder<Registration, Integer> deleteBuilder = helper.getRegistrationDao().deleteBuilder();
/*  996 */         deleteBuilder.where().le("id", event.getId());
/*  997 */         int rowsUpdated = deleteBuilder.delete();
/*      */         
/*  999 */         if (rowsUpdated >= 1) {
/* 1000 */           if (getLogLevel() <= 3) {
/* 1001 */             Log.d("jb4ASDK@ETPush", "success, removed sent registration id from db: " + event.getId());
/*      */           }
/*      */           
/*      */         }
/* 1005 */         else if (getLogLevel() <= 6) {
/* 1006 */           Log.e("jb4ASDK@ETPush", "Error: rowsUpdated = " + rowsUpdated);
/*      */         }
/*      */       }
/*      */       catch (java.sql.SQLException e) {
/*      */         Handler handler;
/* 1011 */         if (getLogLevel() <= 6) {
/* 1012 */           Log.e("jb4ASDK@ETPush", e.getMessage(), e);
/*      */         }
/*      */       } finally {
/*      */         Handler handler;
/* 1016 */         Handler handler = new Handler(applicationContext.getMainLooper());
/* 1017 */         handler.postDelayed(new Runnable()
/*      */         {
/*      */           public void run() {
/* 1020 */             if ((helper != null) && (helper.isOpen()))
/* 1021 */               helper.close(); } }, 10000L);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void sendRegistration()
/*      */   {
/* 1030 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(applicationContext);
/*      */     try {
/* 1032 */       if (!TextUtils.isEmpty(getRegistrationId()))
/*      */       {
/*      */ 
/*      */ 
/* 1036 */         getDeviceToken();
/*      */         
/* 1038 */         registration.setId(null);
/* 1039 */         registration.setEtAppId(Config.getEtAppId());
/* 1040 */         registration.setPushEnabled(Boolean.valueOf(isPushEnabled()));
/* 1041 */         registration.setLocationEnabled(Boolean.valueOf(Config.isLocationManagerActive()));
/* 1042 */         helper.getRegistrationDao().create(registration);
/*      */         
/*      */ 
/* 1045 */         Intent sendDataIntent = new Intent(applicationContext, ETSendDataReceiver.class);
/* 1046 */         sendDataIntent.putExtra("et_send_type_extra", "et_send_type_registration");
/* 1047 */         applicationContext.sendBroadcast(sendDataIntent);
/*      */       }
/*      */     } catch (Exception e) { Handler handler;
/* 1050 */       if (getLogLevel() <= 6)
/* 1051 */         Log.e("jb4ASDK@ETPush", e.getMessage(), e);
/*      */     } finally {
/*      */       Handler handler;
/* 1054 */       Handler handler = new Handler(applicationContext.getMainLooper());
/* 1055 */       handler.postDelayed(new Runnable()
/*      */       {
/*      */         public void run() {
/* 1058 */           if ((helper != null) && (helper.isOpen()))
/* 1059 */             helper.close(); } }, 10000L);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private Object ADM_get()
/*      */     throws ETException
/*      */   {
/* 1067 */     if (this.adm == null) {
/*      */       try {
/* 1069 */         Class<?> clazz = Class.forName("com.amazon.device.messaging.ADM");
/* 1070 */         Constructor<?> ctor = clazz.getConstructor(new Class[] { Context.class });
/* 1071 */         this.adm = ctor.newInstance(new Object[] { applicationContext });
/*      */       }
/*      */       catch (ClassNotFoundException e) {
/* 1074 */         throw new ETException("unable to find com.amazon.device.messaging.ADM");
/*      */       }
/*      */       catch (Exception e) {
/* 1077 */         throw new ETException(e.getCause().getMessage());
/*      */       }
/*      */     }
/* 1080 */     return this.adm;
/*      */   }
/*      */   
/*      */   private String ADM_getRegistrationId() throws ETException {
/* 1084 */     String registrationId = "";
/*      */     try
/*      */     {
/* 1087 */       Class<?> clazz = Class.forName("com.amazon.device.messaging.ADM");
/* 1088 */       registrationId = (String)clazz.getMethod("getRegistrationId", new Class[0]).invoke(ADM_get(), new Object[0]);
/* 1089 */       if (registrationId == null) {
/* 1090 */         registrationId = "";
/*      */       }
/*      */     }
/*      */     catch (ClassNotFoundException e) {
/* 1094 */       throw new ETException("unable to find com.amazon.device.messaging.ADM");
/*      */     }
/*      */     catch (Exception e) {
/* 1097 */       throw new ETException(e.getCause().getMessage());
/*      */     }
/* 1099 */     return registrationId;
/*      */   }
/*      */   
/*      */   private void ADM_startRegister() throws ETException {
/* 1103 */     saveAndroidVersion();
/*      */     try {
/* 1105 */       Class<?> clazz = Class.forName("com.amazon.device.messaging.ADM");
/* 1106 */       clazz.getMethod("startRegister", new Class[0]).invoke(ADM_get(), new Object[0]);
/*      */     }
/*      */     catch (ClassNotFoundException e) {
/* 1109 */       throw new ETException("unable to find com.amazon.device.messaging.ADM");
/*      */     }
/*      */     catch (Exception e) {
/* 1112 */       throw new ETException(e.getCause().getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDeviceToken()
/*      */     throws ETException
/*      */   {
/* 1122 */     if (registration == null) {
/* 1123 */       registration = new Registration(applicationContext);
/* 1124 */       registration.setDeviceToken(getRegistrationId());
/*      */     }
/* 1126 */     return registration.getDeviceToken();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void unregisterDeviceToken()
/*      */     throws ETException
/*      */   {
/* 1133 */     registerDeviceToken("");
/*      */   }
/*      */   
/*      */   private void setGcmSenderID(String gcmSenderID) {
/* 1137 */     if (registration == null) {
/* 1138 */       registration = new Registration(applicationContext);
/* 1139 */       registration.setDeviceToken(getRegistrationId());
/*      */     }
/* 1141 */     registration.setGcmSenderId(gcmSenderID);
/* 1142 */     gcmSenderID = gcmSenderID;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> getOpenDirectRecipient()
/*      */   {
/* 1151 */     return this.openDirectRecipient;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOpenDirectRecipient(Class<?> openDirectRecipient)
/*      */     throws ETException
/*      */   {
/* 1161 */     if (!checkActivityExistsInManifest(applicationContext, openDirectRecipient)) {
/* 1162 */       throw new ETException(openDirectRecipient.getName() + " is not found in AndroidManifest.");
/*      */     }
/*      */     
/* 1165 */     this.openDirectRecipient = openDirectRecipient;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> getCloudPageRecipient()
/*      */   {
/* 1174 */     return this.cloudPageRecipient;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCloudPageRecipient(Class<?> cloudPageRecipient)
/*      */     throws ETException
/*      */   {
/* 1184 */     if (!checkActivityExistsInManifest(applicationContext, cloudPageRecipient)) {
/* 1185 */       throw new ETException(cloudPageRecipient.getName() + " is not found in AndroidManifest.");
/*      */     }
/* 1187 */     this.cloudPageRecipient = cloudPageRecipient;
/*      */   }
/*      */   
/*      */   protected String getNotificationAction() {
/* 1191 */     return this.notificationAction;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNotificationAction(String notificationAction)
/*      */   {
/* 1201 */     this.notificationAction = notificationAction;
/*      */   }
/*      */   
/*      */   protected Uri getNotificationActionUri() {
/* 1205 */     return this.notificationActionUri;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNotificationActionUri(Uri notificationActionUri)
/*      */   {
/* 1215 */     this.notificationActionUri = notificationActionUri;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void showFenceOrProximityMessage(String regionId, int transitionType, int proximity)
/*      */   {
/* 1224 */     if (getLogLevel() <= 3) {
/* 1225 */       Log.d("jb4ASDK@ETPush", "showFenceOrProximityMessage()");
/*      */     }
/*      */     try {
/* 1228 */       if (!isPushEnabled()) {
/* 1229 */         if (getLogLevel() <= 3) {
/* 1230 */           Log.d("jb4ASDK@ETPush", "Push is disabled, no fence or proximity messages will show. Thanks for playing.");
/*      */         }
/* 1232 */         return;
/*      */       }
/*      */       
/* 1235 */       if ((!ETLocationManager.locationManager().isWatchingLocation()) && ((transitionType == 1) || (transitionType == 2))) {
/* 1236 */         if (getLogLevel() <= 3) {
/* 1237 */           Log.d("jb4ASDK@ETPush", "Location is disabled, no fence messages will show. Thanks for playing.");
/*      */         }
/* 1239 */         return;
/*      */       }
/* 1241 */       if ((!ETLocationManager.locationManager().isWatchingProximity()) && (transitionType != 1) && (transitionType != 2)) {
/* 1242 */         if (getLogLevel() <= 3) {
/* 1243 */           Log.d("jb4ASDK@ETPush", "Proximity is disabled, no beacon messages will show. Thanks for playing.");
/*      */         }
/* 1245 */         return;
/*      */       }
/*      */     }
/*      */     catch (ETException e) {
/* 1249 */       if (getLogLevel() <= 6) {
/* 1250 */         Log.e("jb4ASDK@ETPush", e.getMessage(), e);
/*      */       }
/*      */     }
/*      */     
/* 1254 */     Date now = new Date();
/* 1255 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(applicationContext);
/*      */     try
/*      */     {
/* 1258 */       Region region = (Region)helper.getRegionDao().queryForId(regionId);
/* 1259 */       if (getLogLevel() <= 3) {
/* 1260 */         Log.d("jb4ASDK@ETPush", "Geofence Region: " + region.getId());
/*      */       }
/* 1262 */       switch (transitionType) {
/*      */       case 2: 
/* 1264 */         region.setExitCount(Integer.valueOf(region.getExitCount().intValue() + 1));
/* 1265 */         helper.getRegionDao().update(region);
/* 1266 */         if (getLogLevel() <= 3) {
/* 1267 */           Log.d("jb4ASDK@ETPush", "GeofenceExitCount: " + region.getId() + ": " + region.getExitCount());
/*      */         }
/* 1269 */         if (Config.isETanalyticsActive()) {
/* 1270 */           ETAnalytics.engine().stopTimeInRegionLog(regionId);
/*      */         }
/*      */         break;
/*      */       case 1: 
/* 1274 */         region.setEntryCount(Integer.valueOf(region.getEntryCount().intValue() + 1));
/* 1275 */         helper.getRegionDao().update(region);
/* 1276 */         if (getLogLevel() <= 3) {
/* 1277 */           Log.d("jb4ASDK@ETPush", "GeofenceEntryCount: " + region.getId() + ": " + region.getEntryCount());
/*      */         }
/* 1279 */         if (Config.isETanalyticsActive()) {
/* 1280 */           ETAnalytics.engine().startTimeInRegionLog(regionId, false);
/*      */         }
/*      */         break;
/*      */       }
/*      */       
/* 1285 */       List<String> displayedMessageIds = new ArrayList();
/* 1286 */       List<RegionMessage> regionMessages = helper.getRegionMessageDao().queryForEq("region_id", regionId);
/* 1287 */       for (RegionMessage regionMessage : regionMessages) {
/* 1288 */         Message message = (Message)helper.getMessageDao().queryForId(regionMessage.getMessage().getId());
/* 1289 */         if (message != null) {
/* 1290 */           if ((Message.MESSAGE_TYPE_FENCE_ENTRY.equals(message.getMessageType())) && (2 == transitionType)) {
/* 1291 */             if (getLogLevel() <= 3) {
/* 1292 */               Log.d("jb4ASDK@ETPush", "ignoring message " + message.getId() + " because it's an entry and we were triggered by an exit");
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 1297 */           else if ((Message.MESSAGE_TYPE_FENCE_EXIT.equals(message.getMessageType())) && (1 == transitionType)) {
/* 1298 */             if (getLogLevel() <= 3) {
/* 1299 */               Log.d("jb4ASDK@ETPush", "ignoring message " + message.getId() + " because it's an exit and we were triggered by an entry");
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 1304 */           else if ((message.getEndDate() != null) && (message.getEndDate().before(now))) {
/* 1305 */             if (getLogLevel() <= 3) {
/* 1306 */               Log.d("jb4ASDK@ETPush", "fence or proximity message " + message.getId() + " has expired, deleting...");
/*      */             }
/* 1308 */             helper.getMessageDao().delete(message);
/* 1309 */             helper.getRegionMessageDao().delete(regionMessage);
/*      */ 
/*      */ 
/*      */           }
/* 1313 */           else if ((message.getStartDate() != null) && (message.getStartDate().after(now))) {
/* 1314 */             if (getLogLevel() <= 3) {
/* 1315 */               Log.d("jb4ASDK@ETPush", "fence or proximity message " + message.getId() + " hasn't started yet: " + message.getStartDate());
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 1320 */           else if ((message.getMessageLimit().intValue() > -1) && (message.getShowCount().intValue() >= message.getMessageLimit().intValue())) {
/* 1321 */             if (getLogLevel() <= 3) {
/* 1322 */               Log.d("jb4ASDK@ETPush", "fence or proximity message " + message.getId() + " hit its messageLimit, not showing.");
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 1327 */           else if ((message.getMessagesPerPeriod().intValue() > -1) && 
/* 1328 */             (message.getPeriodShowCount().intValue() >= message.getMessagesPerPeriod().intValue()) && (message.getNextAllowedShow().after(now))) {
/* 1329 */             if (getLogLevel() <= 3) {
/* 1330 */               Log.d("jb4ASDK@ETPush", "fence or proximity message " + message.getId() + " hit its messagesPerPeriod Limit, not showing.");
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 1336 */           else if (message.getNextAllowedShow().after(now)) {
/* 1337 */             if (getLogLevel() <= 3) {
/* 1338 */               Log.d("jb4ASDK@ETPush", "fence or proximity message " + message.getId() + " hit before nextAllowedShow, not showing.");
/*      */             }
/*      */             
/*      */           }
/*      */           else
/*      */           {
/* 1344 */             if (2 == transitionType) {
/* 1345 */               if (message.getMinTripped().intValue() > region.getExitCount().intValue()) {
/* 1346 */                 if (getLogLevel() > 3) continue;
/* 1347 */                 Log.d("jb4ASDK@ETPush", "fence message " + message.getId() + " hit before minTripped reached, not showing.");
/*      */ 
/*      */               }
/*      */               
/*      */ 
/*      */             }
/* 1353 */             else if (message.getMinTripped().intValue() > region.getEntryCount().intValue()) {
/* 1354 */               if (getLogLevel() > 3) continue;
/* 1355 */               Log.d("jb4ASDK@ETPush", "fence or proximity message " + message.getId() + " hit before minTripped reached, not showing."); continue;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/* 1363 */               if ((ETLocationManager.locationManager().isWatchingProximity()) && (transitionType != 1) && (transitionType != 2)) {
/* 1364 */                 if ((0 == proximity) || (proximity > message.getProximity())) {
/* 1365 */                   if (getLogLevel() <= 3) {
/* 1366 */                     Log.d("jb4ASDK@ETPush", "Proximity was " + proximity + ", but message.proximity was " + message.getProximity() + ", not showing.");
/*      */                   }
/* 1368 */                   continue;
/*      */                 }
/*      */                 
/* 1371 */                 if (message.getHasEntered().booleanValue()) {
/* 1372 */                   if (getLogLevel() <= 3) {
/* 1373 */                     Log.d("jb4ASDK@ETPush", "We're still inside the region and have never left, not showing.");
/*      */                   }
/* 1375 */                   continue;
/*      */                 }
/*      */               }
/*      */             }
/*      */             catch (ETException e) {
/* 1380 */               if (getLogLevel() <= 6)
/* 1381 */                 Log.e("jb4ASDK@ETPush", e.getMessage(), e);
/*      */             }
/* 1383 */             continue;
/*      */             
/*      */ 
/*      */ 
/* 1387 */             message.setLastShownDate(now);
/* 1388 */             message.setShowCount(Integer.valueOf(message.getShowCount().intValue() + 1));
/*      */             
/* 1390 */             if ((message.getMessagesPerPeriod().intValue() > -1) && (message.getNumberOfPeriods().intValue() > -1) && (!message.getPeriodType().equals(Integer.valueOf(0)))) {
/* 1391 */               message.setPeriodShowCount(Integer.valueOf(message.getPeriodShowCount().intValue() + 1));
/* 1392 */               if (message.getPeriodShowCount().intValue() >= message.getMessagesPerPeriod().intValue()) {
/* 1393 */                 long timeIntervalToAdd = 0L;
/* 1394 */                 switch (message.getPeriodType().intValue()) {
/*      */                 case 5: 
/* 1396 */                   timeIntervalToAdd = message.getNumberOfPeriods().intValue() * 3600000L;
/* 1397 */                   break;
/*      */                 case 4: 
/* 1399 */                   timeIntervalToAdd = message.getNumberOfPeriods().intValue() * 86400000L;
/* 1400 */                   break;
/*      */                 case 3: 
/* 1402 */                   timeIntervalToAdd = message.getNumberOfPeriods().intValue() * 604800000L;
/* 1403 */                   break;
/*      */                 case 2: 
/* 1405 */                   timeIntervalToAdd = message.getNumberOfPeriods().intValue() * 2592000000L;
/* 1406 */                   break;
/*      */                 case 1: 
/* 1408 */                   timeIntervalToAdd = message.getNumberOfPeriods().intValue() * 31536000000L;
/*      */                 }
/*      */                 
/* 1411 */                 message.setNextAllowedShow(new Date(now.getTime() + timeIntervalToAdd));
/*      */                 
/* 1413 */                 if (!message.getIsRollingPeriod().booleanValue()) {
/* 1414 */                   Calendar brokenDate = Calendar.getInstance();
/* 1415 */                   brokenDate.setTimeInMillis(message.getNextAllowedShow().getTime());
/* 1416 */                   brokenDate.set(14, 0);
/* 1417 */                   brokenDate.set(13, 0);
/*      */                   
/* 1419 */                   switch (message.getPeriodType().intValue()) {
/*      */                   case 5: 
/* 1421 */                     brokenDate.set(12, 0);
/* 1422 */                     break;
/*      */                   case 4: 
/* 1424 */                     brokenDate.set(10, 0);
/* 1425 */                     brokenDate.set(12, 0);
/* 1426 */                     break;
/*      */                   case 3: 
/* 1428 */                     brokenDate.set(7, 1);
/* 1429 */                     brokenDate.set(10, 0);
/* 1430 */                     brokenDate.set(12, 0);
/* 1431 */                     break;
/*      */                   case 2: 
/* 1433 */                     brokenDate.set(5, 1);
/* 1434 */                     brokenDate.set(10, 0);
/* 1435 */                     brokenDate.set(12, 0);
/* 1436 */                     break;
/*      */                   case 1: 
/* 1438 */                     brokenDate.set(2, 0);
/* 1439 */                     brokenDate.set(5, 1);
/* 1440 */                     brokenDate.set(10, 0);
/* 1441 */                     brokenDate.set(12, 0);
/*      */                   }
/*      */                   
/*      */                   
/* 1445 */                   message.setNextAllowedShow(brokenDate.getTime());
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 1451 */             if ((message.getPeriodShowCount().intValue() > -1) && (message.getMessagesPerPeriod().intValue() > -1) && (message.getPeriodShowCount().intValue() > message.getMessagesPerPeriod().intValue())) {
/* 1452 */               message.setPeriodShowCount(Integer.valueOf(0));
/*      */             }
/*      */             
/* 1455 */             if ((transitionType != 1) && (transitionType != 2))
/*      */             {
/* 1457 */               long timestamp = System.currentTimeMillis();
/* 1458 */               if (message.getLoiterSeconds().intValue() > 0) {
/* 1459 */                 if (message.getEntryTime().longValue() == 0L) {
/* 1460 */                   message.setEntryTime(Long.valueOf(timestamp));
/* 1461 */                   if (getLogLevel() <= 3) {
/* 1462 */                     Log.d("jb4ASDK@ETPush", "Entered, but loiteringTime has not yet triggered.");
/*      */                   }
/* 1464 */                   helper.getMessageDao().update(message);
/* 1465 */                   continue;
/*      */                 }
/*      */                 
/* 1468 */                 if (timestamp > message.getEntryTime().longValue() + message.getLoiterSeconds().intValue() * 1000L) {
/* 1469 */                   message.setHasEntered(Boolean.TRUE);
/*      */                 }
/*      */                 else {
/* 1472 */                   if (getLogLevel() > 3) continue;
/* 1473 */                   Log.d("jb4ASDK@ETPush", "Entered, but loiteringTime has not yet triggered.");
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/* 1480 */                 message.setHasEntered(Boolean.TRUE);
/*      */               }
/*      */             }
/* 1483 */             helper.getMessageDao().update(message);
/*      */             
/* 1485 */             Intent pushIntent = new Intent("com.google.android.c2dm.intent.RECEIVE");
/* 1486 */             pushIntent.addCategory(applicationContext.getPackageName());
/* 1487 */             if ((message.getCategory() != null) && (message.getCategory().length() > 0)) {
/* 1488 */               pushIntent.putExtra("category", message.getCategory());
/*      */             }
/* 1490 */             if ((message.getOpenDirect() != null) && (message.getOpenDirect().length() > 0)) {
/* 1491 */               pushIntent.putExtra("_od", message.getOpenDirect());
/*      */             }
/* 1493 */             if ((message.getContentType() != null) && (Message.CONTENT_TYPE_CLOUD_PAGE_ALERT.equals(message.getContentType())) && (message.getUrl() != null) && (message.getUrl().length() > 0))
/*      */             {
/* 1495 */               pushIntent.putExtra("_x", message.getUrl());
/*      */             }
/* 1497 */             pushIntent.putExtra("_m", message.getId());
/* 1498 */             if ((message.getSubject() != null) && (message.getSubject().length() > -1))
/*      */             {
/* 1500 */               pushIntent.putExtra("alert", message.getSubject());
/*      */             }
/* 1502 */             if ((message.getSound() != null) && (message.getSound().length() > 0)) {
/* 1503 */               pushIntent.putExtra("sound", message.getSound());
/*      */             }
/* 1505 */             if ((message.getKeys() != null) && (message.getKeys().size() > 0)) {
/* 1506 */               for (Attribute attribute : message.getKeys()) {
/* 1507 */                 pushIntent.putExtra(attribute.getKey(), attribute.getValue());
/*      */               }
/*      */             }
/* 1510 */             if ((message.getCustom() != null) && (message.getCustom().length() > 0)) {
/* 1511 */               pushIntent.putExtra("custom", message.getCustom());
/*      */             }
/*      */             
/*      */ 
/* 1515 */             pushIntent.putExtra("transitionType", transitionType);
/* 1516 */             pushIntent.putExtra("regionId", regionId);
/*      */             
/* 1518 */             displayedMessageIds.add(message.getId());
/*      */             
/* 1520 */             if (getLogLevel() <= 3) {
/* 1521 */               Log.d("jb4ASDK@ETPush", "Sending broadcast Intent to display fence/proximity message: " + message.getId());
/*      */             }
/* 1523 */             applicationContext.sendBroadcast(pushIntent);
/*      */           }
/*      */         }
/*      */       }
/* 1527 */       if ((Config.isETanalyticsActive()) && (displayedMessageIds.size() > 0)) {
/* 1528 */         ETAnalytics.engine().logFenceOrProximityMessageDisplayed(regionId, transitionType, proximity, displayedMessageIds);
/*      */       }
/*      */     } catch (java.sql.SQLException e) {
/*      */       Handler handler;
/* 1532 */       if (getLogLevel() <= 6) {
/* 1533 */         Log.e("jb4ASDK@ETPush", e.getMessage(), e);
/*      */       }
/*      */     } catch (Throwable e) {
/*      */       Handler handler;
/* 1537 */       if (getLogLevel() <= 6) {
/* 1538 */         Log.e("jb4ASDK@ETPush", e.getMessage(), e);
/*      */       }
/*      */     } finally {
/*      */       Handler handler;
/* 1542 */       Handler handler = new Handler(applicationContext.getMainLooper());
/* 1543 */       handler.postDelayed(new Runnable()
/*      */       {
/*      */         public void run() {
/* 1546 */           if ((helper != null) && (helper.isOpen()))
/* 1547 */             helper.close(); } }, 10000L);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class PauseWaitTask
/*      */     extends AsyncTask<Void, Void, Integer>
/*      */   {
/*      */     private PauseWaitTask() {}
/*      */     
/*      */ 
/*      */     protected Integer doInBackground(Void... params)
/*      */     {
/*      */       try
/*      */       {
/* 1562 */         if (ETPush.getLogLevel() <= 3) {
/* 1563 */           Log.d("jb4ASDK@ETPush", "started pauseWaitTask");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1569 */         Thread.sleep(2000L);
/*      */         
/* 1571 */         ETPush.access$902(null);
/*      */         
/* 1573 */         final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(ETPush.applicationContext);
/*      */         try {
/* 1575 */           helper.getWritableDatabase().execSQL("VACUUM");
/* 1576 */           if (ETPush.getLogLevel() <= 3) {
/* 1577 */             Log.d("jb4ASDK@ETPush", "SQLite VACUUM complete");
/*      */           }
/*      */         } catch (android.database.SQLException sqlException) {
/*      */           Handler handler;
/* 1581 */           if (ETPush.getLogLevel() <= 6) {
/* 1582 */             Log.e("jb4ASDK@ETPush", sqlException.getMessage(), sqlException);
/*      */           }
/*      */         } finally {
/*      */           Handler handler;
/* 1586 */           Handler handler = new Handler(ETPush.applicationContext.getMainLooper());
/* 1587 */           handler.postDelayed(new Runnable()
/*      */           {
/*      */             public void run() {
/* 1590 */               if ((helper != null) && (helper.isOpen()))
/* 1591 */                 helper.close(); } }, 10000L);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1597 */         EventBus.getDefault().postSticky(new BackgroundEvent(true));
/*      */       }
/*      */       catch (InterruptedException e)
/*      */       {
/* 1601 */         if (ETPush.getLogLevel() <= 3) {
/* 1602 */           Log.d("jb4ASDK@ETPush", "pauseWaitTask interrupted");
/*      */         }
/*      */       }
/*      */       finally {
/* 1606 */         if (ETPush.wakeLock.isHeld()) {
/* 1607 */           ETPush.wakeLock.release();
/*      */         }
/*      */       }
/*      */       
/* 1611 */       if (ETPush.getLogLevel() <= 3) {
/* 1612 */         Log.d("jb4ASDK@ETPush", "ended pauseWaitTask");
/*      */       }
/*      */       
/* 1615 */       return Integer.valueOf(0);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETPush.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */