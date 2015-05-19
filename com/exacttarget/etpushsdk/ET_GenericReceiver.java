/*     */ package com.exacttarget.etpushsdk;
/*     */ 
/*     */ import android.app.Notification;
/*     */ import android.app.NotificationManager;
/*     */ import android.app.PendingIntent;
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.SharedPreferences;
/*     */ import android.content.SharedPreferences.Editor;
/*     */ import android.content.pm.ApplicationInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.graphics.Bitmap;
/*     */ import android.graphics.BitmapFactory;
/*     */ import android.net.Uri;
/*     */ import android.os.AsyncTask;
/*     */ import android.os.Bundle;
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import android.preference.PreferenceManager;
/*     */ import android.provider.Settings.System;
/*     */ import android.support.v4.app.NotificationCompat.BigPictureStyle;
/*     */ import android.support.v4.app.NotificationCompat.BigTextStyle;
/*     */ import android.support.v4.app.NotificationCompat.Builder;
/*     */ import android.util.Log;
/*     */ import android.webkit.URLUtil;
/*     */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*     */ import com.exacttarget.etpushsdk.data.Message;
/*     */ import com.exacttarget.etpushsdk.event.PushReceivedEvent;
/*     */ import com.exacttarget.etpushsdk.event.ServerErrorEvent;
/*     */ import com.exacttarget.etpushsdk.util.BitmapUtil;
/*     */ import com.exacttarget.etpushsdk.util.EventBus;
/*     */ import com.j256.ormlite.dao.Dao;
/*     */ import java.lang.reflect.Field;
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
/*     */ public class ET_GenericReceiver
/*     */   extends BroadcastReceiver
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@ET_GenericReceiver";
/*     */   private SharedPreferences sp;
/*     */   private static final String NOTIFICATION_REQUEST_CODE = "et_notification_request_code_key";
/*     */   protected static final String NOTIFICATION_ID = "et_notification_id_key";
/*     */   
/*     */   public ET_GenericReceiver()
/*     */   {
/*  89 */     this.sp = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     Looper looper = Looper.getMainLooper();
/*  97 */     Handler handler = new Handler(looper);
/*  98 */     handler.post(new Runnable() {
/*     */       public void run() {
/*     */         try {
/* 101 */           Class.forName("android.os.AsyncTask");
/*     */         } catch (ClassNotFoundException e) {
/* 103 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public final void onReceive(final Context context, Intent intent)
/*     */   {
/*     */     try {
/* 112 */       if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION"))
/*     */       {
/* 114 */         if (ETPush.getLogLevel() <= 3) {
/* 115 */           Log.d("jb4ASDK@ET_GenericReceiver", "Received a registration event from Google.");
/*     */         }
/*     */         
/* 118 */         String newRegistration = intent.getStringExtra("registration_id");
/* 119 */         String newError = intent.getStringExtra("error");
/* 120 */         String newUnregistered = intent.getStringExtra("unregistered");
/*     */         
/* 122 */         if (newError != null) {
/* 123 */           if (ETPush.getLogLevel() <= 6) {
/* 124 */             Log.e("jb4ASDK@ET_GenericReceiver", "GCM Registration error: " + newError);
/*     */           }
/* 126 */           ServerErrorEvent errorEvent = new ServerErrorEvent();
/* 127 */           errorEvent.setMessage("GCM Registration error: " + newError);
/* 128 */           EventBus.getDefault().post(errorEvent);
/*     */         }
/* 130 */         else if (newUnregistered != null) {
/* 131 */           if (ETPush.getLogLevel() <= 3) {
/* 132 */             Log.d("jb4ASDK@ET_GenericReceiver", "GCM Unregistered: " + newUnregistered);
/*     */           }
/* 134 */           ETPush.pushManager().unregisterDeviceToken();
/*     */         }
/* 136 */         else if (newRegistration != null) {
/* 137 */           if (ETPush.getLogLevel() <= 3) {
/* 138 */             Log.d("jb4ASDK@ET_GenericReceiver", "GCM Registration complete. ID: " + newRegistration);
/*     */           }
/* 140 */           ETPush.pushManager().registerDeviceToken(newRegistration);
/*     */         }
/*     */         
/*     */       }
/* 144 */       else if (intent.getAction().equals("com.amazon.device.messaging.intent.REGISTRATION"))
/*     */       {
/* 146 */         if (ETPush.getLogLevel() <= 3) {
/* 147 */           Log.d("jb4ASDK@ET_GenericReceiver", "Received a registration event from Amazon.");
/*     */         }
/* 149 */         String newRegistration = intent.getStringExtra("registration_id");
/* 150 */         String newError = intent.getStringExtra("error");
/* 151 */         boolean newUnregistered = intent.getBooleanExtra("unregistered", false);
/*     */         
/* 153 */         if (newError != null) {
/* 154 */           if (ETPush.getLogLevel() <= 6) {
/* 155 */             Log.e("jb4ASDK@ET_GenericReceiver", "ADM Registration error: " + newError);
/*     */           }
/*     */         }
/* 158 */         else if (newUnregistered) {
/* 159 */           if (ETPush.getLogLevel() <= 3) {
/* 160 */             Log.d("jb4ASDK@ET_GenericReceiver", "ADM Unregistered: " + newUnregistered);
/*     */           }
/* 162 */           ETPush.pushManager().unregisterDeviceToken();
/*     */         }
/* 164 */         else if (newRegistration != null) {
/* 165 */           if (ETPush.getLogLevel() <= 3) {
/* 166 */             Log.d("jb4ASDK@ET_GenericReceiver", "ADM Registration complete. ID: " + newRegistration);
/*     */           }
/* 168 */           ETPush.pushManager().registerDeviceToken(newRegistration);
/*     */         }
/*     */       }
/* 171 */       else if ((intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) || (intent.getAction().equals("com.amazon.device.messaging.intent.RECEIVE")))
/*     */       {
/*     */ 
/* 174 */         if (ETPush.getLogLevel() <= 3) {
/* 175 */           Log.d("jb4ASDK@ET_GenericReceiver", "Hello from ExactTarget! Push Message received.");
/*     */         }
/*     */         
/* 178 */         if (!ETPush.pushManager().isPushEnabled())
/*     */         {
/* 180 */           if (ETPush.getLogLevel() <= 3) {
/* 181 */             Log.d("jb4ASDK@ET_GenericReceiver", "Push is disabled. Thanks for playing.");
/*     */           }
/* 183 */           return;
/*     */         }
/*     */         
/*     */ 
/* 187 */         Bundle payload = intent.getExtras();
/*     */         
/* 189 */         if (ETPush.getLogLevel() <= 3) {
/* 190 */           String payloadStr = "";
/* 191 */           for (String key : payload.keySet()) {
/* 192 */             payloadStr = payloadStr + "[" + key + ":" + payload.get(key) + "] ";
/*     */           }
/*     */           
/* 195 */           Log.d("jb4ASDK@ET_GenericReceiver", "Payload: " + payloadStr);
/*     */         }
/*     */         
/* 198 */         if ((!payload.containsKey("regionId")) && (payload.containsKey("_m")))
/*     */         {
/* 200 */           if (Config.isETanalyticsActive()) {
/* 201 */             ETAnalytics.engine().logMessageReceived(payload.getString("_m"));
/*     */           }
/*     */         }
/*     */         
/* 205 */         new AsyncTask()
/*     */         {
/*     */ 
/*     */           protected Void doInBackground(Bundle... bundle)
/*     */           {
/* 210 */             Bundle payload = bundle[0];
/*     */             
/* 212 */             Intent launchIntent = ET_GenericReceiver.this.setupLaunchIntent(context, payload);
/*     */             
/* 214 */             Intent openIntent = new Intent(context.getApplicationContext().getPackageName() + ".MESSAGE_OPENED");
/* 215 */             openIntent.putExtra("et_open_intent", launchIntent);
/*     */             
/* 217 */             if (ET_GenericReceiver.this.sp == null) {
/* 218 */               ET_GenericReceiver.this.sp = context.getSharedPreferences("ETPush", 0);
/*     */             }
/*     */             
/* 221 */             int notifyId = -1;
/* 222 */             synchronized ("et_notification_id_key") {
/* 223 */               notifyId = ((Integer)Config.getETSharedPref(context, PreferenceManager.getDefaultSharedPreferences(context), "et_notification_id_key", Integer.valueOf(0))).intValue();
/* 224 */               if (ETPush.getLogLevel() <= 3) {
/* 225 */                 Log.d("jb4ASDK@ET_GenericReceiver", "NOTIFICATION_ID: " + notifyId);
/*     */               }
/* 227 */               openIntent.putExtra("et_notification_id", notifyId);
/*     */               
/* 229 */               if (payload.getString("_m") != null) {
/* 230 */                 final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(context.getApplicationContext());
/*     */                 try {
/* 232 */                   Dao<Message, String> messageDao = helper.getMessageDao();
/* 233 */                   Message message = (Message)messageDao.queryForId(payload.getString("_m"));
/* 234 */                   if (message != null) {
/* 235 */                     message.setNotifyId(Integer.valueOf(notifyId));
/* 236 */                     messageDao.update(message);
/*     */                   }
/*     */                 } catch (SQLException e) {
/*     */                   Handler handler;
/* 240 */                   if (ETPush.getLogLevel() <= 6) {
/* 241 */                     Log.e("jb4ASDK@ET_GenericReceiver", e.getMessage(), e);
/*     */                   }
/*     */                 } finally {
/*     */                   Handler handler;
/* 245 */                   Handler handler = new Handler(context.getApplicationContext().getMainLooper());
/* 246 */                   handler.postDelayed(new Runnable()
/*     */                   {
/*     */                     public void run() {
/* 249 */                       if ((helper != null) && (helper.isOpen()))
/* 250 */                         helper.close(); } }, 10000L);
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 256 */               notifyId++;
/* 257 */               ET_GenericReceiver.this.sp.edit().putInt("et_notification_id_key", notifyId).commit();
/*     */             }
/*     */             
/* 260 */             NotificationCompat.Builder builder = ET_GenericReceiver.this.setupNotificationBuilder(context, payload);
/*     */             
/* 262 */             if ((builder != null) && (launchIntent != null)) {
/* 263 */               PendingIntent pendingIntent = ET_GenericReceiver.this.setupLaunchPendingIntent(context, openIntent, false);
/* 264 */               builder.setContentIntent(pendingIntent);
/*     */             }
/*     */             
/* 267 */             if (builder != null) {
/* 268 */               Notification notification = builder.build();
/* 269 */               NotificationManager nm = (NotificationManager)context.getApplicationContext().getSystemService("notification");
/* 270 */               nm.notify(notifyId, notification);
/*     */             }
/*     */             
/* 273 */             EventBus.getDefault().post(new PushReceivedEvent(payload));
/*     */             
/* 275 */             return null; } }.execute(new Bundle[] { payload });
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (ETException e)
/*     */     {
/* 282 */       if (ETPush.getLogLevel() <= 6) {
/* 283 */         Log.e("jb4ASDK@ET_GenericReceiver", e.getMessage(), e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PendingIntent setupLaunchPendingIntent(Context context, Intent launchIntent, boolean autoCancel)
/*     */   {
/* 296 */     PendingIntent pendingIntent = null;
/* 297 */     if (this.sp == null) {
/* 298 */       this.sp = context.getSharedPreferences("ETPush", 0);
/*     */     }
/*     */     
/* 301 */     synchronized ("et_notification_request_code_key") {
/* 302 */       int notificationId = ((Integer)Config.getETSharedPref(context, PreferenceManager.getDefaultSharedPreferences(context), "et_notification_request_code_key", Integer.valueOf(0))).intValue();
/*     */       
/*     */ 
/* 305 */       launchIntent.putExtra("et_auto_cancel", autoCancel);
/* 306 */       if (ETPush.getLogLevel() <= 3) {
/* 307 */         Log.d("jb4ASDK@ET_GenericReceiver", "AUTOCANCEL: " + autoCancel);
/*     */       }
/* 309 */       if (autoCancel) {
/* 310 */         launchIntent.putExtra("et_notification_id", notificationId);
/* 311 */         if (ETPush.getLogLevel() <= 3) {
/* 312 */           Log.d("jb4ASDK@ET_GenericReceiver", "NOTIFICATION_ID: " + notificationId);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 319 */       pendingIntent = PendingIntent.getBroadcast(context, notificationId, launchIntent, 268435456);
/* 320 */       notificationId++;
/* 321 */       this.sp.edit().putInt("et_notification_request_code_key", notificationId).commit();
/*     */     }
/*     */     
/* 324 */     return pendingIntent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Intent setupLaunchIntent(Context context, Bundle payload)
/*     */   {
/* 335 */     Intent launchIntent = null;
/*     */     try {
/* 337 */       if ((ETPush.pushManager().getNotificationAction() != null) && (ETPush.pushManager().getNotificationActionUri() != null)) {
/* 338 */         if (ETPush.getLogLevel() <= 3) {
/* 339 */           Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to NotificationUri: " + ETPush.pushManager().getNotificationActionUri());
/*     */         }
/* 341 */         launchIntent = new Intent(ETPush.pushManager().getNotificationAction(), ETPush.pushManager().getNotificationActionUri());
/* 342 */         launchIntent.putExtras(payload);
/*     */ 
/*     */       }
/* 345 */       else if ((ETPush.pushManager().getCloudPageRecipient() != null) && (payload.getString("_x") != null))
/*     */       {
/* 347 */         if (ETPush.getLogLevel() <= 3) {
/* 348 */           Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to Cloud Page: " + ETPush.pushManager().getCloudPageRecipient());
/*     */         }
/*     */         
/*     */ 
/* 352 */         launchIntent = new Intent(context, ETPush.pushManager().getCloudPageRecipient());
/* 353 */         launchIntent.putExtra("loadURL", payload.getString("_x"));
/* 354 */         launchIntent.putExtras(payload);
/*     */       }
/* 356 */       else if ((ETPush.pushManager().getCloudPageRecipient() == null) && (payload.getString("_x") != null))
/*     */       {
/* 358 */         String url = payload.getString("_x");
/* 359 */         if ((URLUtil.isValidUrl(url)) && ((URLUtil.isHttpUrl(url)) || (URLUtil.isHttpsUrl(url))))
/*     */         {
/* 361 */           if (ETPush.getLogLevel() <= 3) {
/* 362 */             Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to Cloud Page: " + url);
/*     */           }
/*     */           
/* 365 */           launchIntent = new Intent(context, ETLandingPagePresenter.class);
/* 366 */           launchIntent.putExtras(payload);
/*     */         }
/*     */         else {
/* 369 */           if (ETPush.getLogLevel() <= 3) {
/* 370 */             Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to Launch Package");
/*     */           }
/*     */           
/* 373 */           launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
/* 374 */           launchIntent.putExtras(payload);
/*     */         }
/*     */         
/*     */       }
/* 378 */       else if ((ETPush.pushManager().getOpenDirectRecipient() != null) && (payload.getString("_od") != null))
/*     */       {
/* 380 */         if (ETPush.getLogLevel() <= 3) {
/* 381 */           Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to Open Direct: " + ETPush.pushManager().getOpenDirectRecipient());
/*     */         }
/*     */         
/* 384 */         launchIntent = new Intent(context, ETPush.pushManager().getOpenDirectRecipient());
/* 385 */         launchIntent.putExtra("open_direct_payload", payload.getString("_od"));
/* 386 */         launchIntent.putExtras(payload);
/*     */       }
/* 388 */       else if ((ETPush.pushManager().getOpenDirectRecipient() == null) && (payload.getString("_od") != null))
/*     */       {
/* 390 */         String url = payload.getString("_od");
/* 391 */         if ((URLUtil.isValidUrl(url)) && ((URLUtil.isHttpUrl(url)) || (URLUtil.isHttpsUrl(url))))
/*     */         {
/* 393 */           if (ETPush.getLogLevel() <= 3) {
/* 394 */             Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to Open Direct: " + url);
/*     */           }
/*     */           
/* 397 */           launchIntent = new Intent(context, ETLandingPagePresenter.class);
/* 398 */           launchIntent.putExtras(payload);
/*     */         }
/*     */         else {
/* 401 */           if (ETPush.getLogLevel() <= 3) {
/* 402 */             Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to Launch Package");
/*     */           }
/*     */           
/* 405 */           launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
/* 406 */           launchIntent.putExtras(payload);
/*     */         }
/*     */       }
/* 409 */       else if (ETPush.pushManager().getNotificationRecipientClass() != null) {
/* 410 */         if (ETPush.getLogLevel() <= 3) {
/* 411 */           Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to Nofification Recipient: " + ETPush.pushManager().getNotificationRecipientClass());
/*     */         }
/*     */         
/*     */ 
/* 415 */         launchIntent = new Intent(context, ETPush.pushManager().getNotificationRecipientClass());
/* 416 */         launchIntent.putExtras(payload);
/*     */       }
/*     */       else
/*     */       {
/* 420 */         if (ETPush.getLogLevel() <= 3) {
/* 421 */           Log.d("jb4ASDK@ET_GenericReceiver", "Launch Intent set to Launch Package");
/*     */         }
/* 423 */         launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
/* 424 */         launchIntent.putExtras(payload);
/*     */       }
/*     */     }
/*     */     catch (ETException e) {
/* 428 */       if (ETPush.getLogLevel() <= 6) {
/* 429 */         Log.e("jb4ASDK@ET_GenericReceiver", e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/* 433 */     return launchIntent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected NotificationCompat.Builder setupNotificationBuilder(Context context, Bundle payload)
/*     */   {
/* 445 */     NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
/*     */     
/* 447 */     int appIconResourceId = context.getApplicationInfo().icon;
/* 448 */     int notiIconResourceId = appIconResourceId;
/*     */     try {
/* 450 */       if (ETPush.pushManager().getNotificationResourceId() != null) {
/* 451 */         notiIconResourceId = ETPush.pushManager().getNotificationResourceId().intValue();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 455 */       if (ETPush.getLogLevel() <= 6) {
/* 456 */         Log.e("jb4ASDK@ET_GenericReceiver", "Error getting notification icon: " + e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/* 460 */     builder.setSmallIcon(notiIconResourceId);
/* 461 */     Bitmap largeIconBitmap = BitmapFactory.decodeResource(context.getResources(), appIconResourceId);
/* 462 */     builder.setLargeIcon(largeIconBitmap);
/* 463 */     builder.setAutoCancel(true);
/*     */     
/* 465 */     int appLabelResourceId = context.getApplicationInfo().labelRes;
/* 466 */     String app_name = context.getString(appLabelResourceId);
/* 467 */     builder.setContentTitle(app_name);
/* 468 */     String alert = payload.getString("alert");
/*     */     
/* 470 */     if (alert != null)
/*     */     {
/* 472 */       builder.setTicker(alert);
/* 473 */       builder.setContentText(alert);
/*     */       
/* 475 */       String bigPicUrl = payload.getString("et_big_pic");
/* 476 */       if ((bigPicUrl != null) && (!bigPicUrl.isEmpty())) {
/* 477 */         builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapUtil.getBitmapFromURL(bigPicUrl)).setSummaryText(alert));
/*     */       }
/*     */       else {
/* 480 */         builder.setStyle(new NotificationCompat.BigTextStyle().bigText(alert).setBigContentTitle(app_name));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 485 */     if (payload.getString("sound") != null)
/*     */     {
/* 487 */       if (payload.getString("sound").equals("custom.caf")) {
/*     */         try
/*     */         {
/* 490 */           String className = context.getPackageName() + ".R$raw";
/* 491 */           Class<?> raw = Class.forName(className);
/* 492 */           Field custom = raw.getDeclaredField("custom");
/* 493 */           Uri customSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + custom.getInt(null));
/* 494 */           builder.setSound(customSound);
/*     */         }
/*     */         catch (ClassNotFoundException e) {
/* 497 */           if (ETPush.getLogLevel() <= 5) {
/* 498 */             Log.w("jb4ASDK@ET_GenericReceiver", "R.raw.custom sound requested but not defined, reverting to default notification sound.", e);
/*     */           }
/* 500 */           builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
/*     */         }
/*     */         catch (NoSuchFieldException e) {
/* 503 */           if (ETPush.getLogLevel() <= 5) {
/* 504 */             Log.w("jb4ASDK@ET_GenericReceiver", "R.raw.custom sound requested but not defined, reverting to default notification sound.", e);
/*     */           }
/* 506 */           builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
/*     */         } catch (IllegalArgumentException e) {
/* 508 */           if (ETPush.getLogLevel() <= 5) {
/* 509 */             Log.w("jb4ASDK@ET_GenericReceiver", "R.raw.custom sound requested but not defined, reverting to default notification sound.", e);
/*     */           }
/* 511 */           builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
/*     */         } catch (IllegalAccessException e) {
/* 513 */           if (ETPush.getLogLevel() <= 5) {
/* 514 */             Log.w("jb4ASDK@ET_GenericReceiver", "R.raw.custom sound requested but not defined, reverting to default notification sound.", e);
/*     */           }
/* 516 */           builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
/*     */         }
/*     */         
/*     */       } else {
/* 520 */         builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
/*     */       }
/*     */     }
/*     */     
/* 524 */     return builder;
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
/*     */   protected PendingIntent createPendingIntentWithOpenAnalytics(Context context, Intent launchIntent, boolean autoCancel)
/*     */   {
/* 537 */     Intent openIntent = new Intent(context.getApplicationContext().getPackageName() + ".MESSAGE_OPENED");
/* 538 */     openIntent.putExtra("et_open_intent", launchIntent);
/*     */     
/* 540 */     PendingIntent pendingIntent = setupLaunchPendingIntent(context, openIntent, autoCancel);
/*     */     
/* 542 */     return pendingIntent;
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ET_GenericReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */