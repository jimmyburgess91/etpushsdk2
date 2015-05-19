/*     */ package com.exacttarget.etpushsdk;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.content.SharedPreferences;
/*     */ import android.content.SharedPreferences.Editor;
/*     */ import android.os.Handler;
/*     */ import android.os.Looper;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.data.AnalyticItem;
/*     */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*     */ import com.exacttarget.etpushsdk.event.AnalyticItemEvent;
/*     */ import com.exacttarget.etpushsdk.event.AnalyticItemEventListener;
/*     */ import com.exacttarget.etpushsdk.event.AnalyticPiItemEvent;
/*     */ import com.exacttarget.etpushsdk.event.BackgroundEvent;
/*     */ import com.exacttarget.etpushsdk.event.BackgroundEventListener;
/*     */ import com.exacttarget.etpushsdk.util.EventBus;
/*     */ import com.j256.ormlite.dao.Dao;
/*     */ import com.j256.ormlite.stmt.QueryBuilder;
/*     */ import com.j256.ormlite.stmt.Where;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
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
/*     */ public class ETAnalytics
/*     */   implements AnalyticItemEventListener, BackgroundEventListener
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@ETAnalytics";
/*     */   public static final String ET_BACKGROUND_TIME_CACHE = "et_background_time_cache";
/*     */   private static ETAnalytics engine;
/*     */   private Context applicationContext;
/*     */   
/*     */   private ETAnalytics(Context applicationContext)
/*     */   {
/*  86 */     Looper looper = Looper.getMainLooper();
/*  87 */     Handler handler = new Handler(looper);
/*  88 */     handler.post(new Runnable() {
/*     */       public void run() {
/*     */         try {
/*  91 */           Class.forName("android.os.AsyncTask");
/*     */         } catch (ClassNotFoundException e) {
/*  93 */           if (ETPush.getLogLevel() <= 6) {
/*  94 */             Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 101 */     });
/* 102 */     this.applicationContext = applicationContext;
/*     */     
/* 104 */     EventBus.getDefault().register(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ETAnalytics engine()
/*     */     throws ETException
/*     */   {
/* 113 */     if ((Config.isETanalyticsActive() | Config.isPIanalyticsActive())) {
/* 114 */       if (engine == null) {
/* 115 */         throw new ETException("You forgot to call readyAimFire first.");
/*     */       }
/*     */       
/* 118 */       return engine;
/*     */     }
/*     */     
/* 121 */     throw new ETException("Analytics are disabled.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void readyAimFire(Context applicationContext)
/*     */     throws ETException
/*     */   {
/* 130 */     if (engine == null) {
/* 131 */       if (ETPush.getLogLevel() <= 3) {
/* 132 */         Log.d("jb4ASDK@ETAnalytics", "readyAimFire()");
/*     */       }
/* 134 */       engine = new ETAnalytics(applicationContext);
/*     */     }
/*     */     else {
/* 137 */       throw new ETException("You must have called readyAimFire more than once.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEvent(AnalyticItemEvent event)
/*     */   {
/* 147 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 149 */       if ((event.getDatabaseIds() != null) && (event.getDatabaseIds().size() > 0)) {
/* 150 */         int rowsUpdated = helper.getAnalyticItemDao().deleteIds(event.getDatabaseIds());
/* 151 */         if (ETPush.getLogLevel() <= 3) {
/* 152 */           Log.e("jb4ASDK@ETAnalytics", "Error: analytic_item rows deleted = " + rowsUpdated);
/*     */         }
/*     */       }
/*     */       else {
/* 156 */         for (AnalyticItem analyticItem : event) {
/* 157 */           int rowsUpdated = helper.getAnalyticItemDao().deleteById(analyticItem.getId());
/*     */           
/* 159 */           if (rowsUpdated == 1) {
/* 160 */             if (ETPush.getLogLevel() <= 3) {
/* 161 */               Log.d("jb4ASDK@ETAnalytics", "removed analytic_item types: " + analyticItem.getAnalyticTypes());
/*     */             }
/*     */             
/*     */           }
/* 165 */           else if (ETPush.getLogLevel() <= 6) {
/* 166 */             Log.e("jb4ASDK@ETAnalytics", "Error: rowsUpdated = " + rowsUpdated);
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 172 */       if (ETPush.getLogLevel() <= 6) {
/* 173 */         Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 177 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 178 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 181 */           if ((helper != null) && (helper.isOpen()))
/* 182 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEvent(AnalyticPiItemEvent event)
/*     */   {
/* 195 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try
/*     */     {
/* 198 */       SharedPreferences prefs = this.applicationContext.getSharedPreferences("ETPush", 0);
/* 199 */       SharedPreferences.Editor prefsEditor = prefs.edit();
/*     */       
/* 201 */       prefsEditor.putString("et_user_id_cache", event.getUserId());
/* 202 */       prefsEditor.putString("et_session_id_cache", event.getSessionId());
/* 203 */       prefsEditor.commit();
/*     */       
/* 205 */       if ((event.getDatabaseIds() != null) && (event.getDatabaseIds().size() > 0)) {
/* 206 */         int rowsDeleted = helper.getAnalyticItemDao().deleteIds(event.getDatabaseIds());
/*     */         
/* 208 */         if (rowsDeleted == event.getDatabaseIds().size()) {
/* 209 */           if (ETPush.getLogLevel() <= 3) {
/* 210 */             Log.e("jb4ASDK@ETAnalytics", "pi_analytic_item rows deleted = " + rowsDeleted);
/*     */           }
/*     */           
/*     */         }
/* 214 */         else if (ETPush.getLogLevel() <= 6) {
/* 215 */           Log.e("jb4ASDK@ETAnalytics", "Error: mis-match on pi_analytic_item rows deleted.  rows deleted:" + rowsDeleted + " expected: " + event.getDatabaseIds().size());
/*     */         }
/*     */       }
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 220 */       if (ETPush.getLogLevel() <= 6) {
/* 221 */         Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 225 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 226 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 229 */           if ((helper != null) && (helper.isOpen()))
/* 230 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void endTimeInAppCounter()
/*     */   {
/* 238 */     long now = System.currentTimeMillis();
/* 239 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 241 */       analyticItemDao = helper.getAnalyticItemDao();
/* 242 */       List<AnalyticItem> analyticItems = analyticItemDao.queryBuilder().orderBy("id", true).where().eq("pi_app_key", "").and().like("analytic_types", "%4%").and().isNull("value").query();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 252 */       for (AnalyticItem analyticItem : analyticItems)
/*     */       {
/* 254 */         int timeInApp = (int)((now - analyticItem.getEventDate().getTime()) / 1000L);
/* 255 */         if (ETPush.getLogLevel() <= 3) {
/* 256 */           Log.d("jb4ASDK@ETAnalytics", "ET Time in app was " + timeInApp + " seconds");
/*     */         }
/* 258 */         analyticItem.setValue(Integer.valueOf(timeInApp));
/* 259 */         analyticItem.setReadyToSend(Boolean.TRUE);
/* 260 */         analyticItemDao.update(analyticItem);
/*     */       }
/*     */       
/* 263 */       analyticItems = analyticItemDao.queryBuilder().orderBy("id", true).where().ne("pi_app_key", "").and().like("analytic_types", "%5%").and().isNull("value").query();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 273 */       for (AnalyticItem analyticItem : analyticItems)
/*     */       {
/* 275 */         int timeInApp = (int)((now - analyticItem.getEventDate().getTime()) / 1000L);
/* 276 */         if (ETPush.getLogLevel() <= 3) {
/* 277 */           Log.d("jb4ASDK@ETAnalytics", "PI Time in app was " + timeInApp + " seconds");
/*     */         }
/* 279 */         analyticItem.setValue(Integer.valueOf(timeInApp));
/* 280 */         analyticItem.setReadyToSend(Boolean.TRUE);
/* 281 */         analyticItemDao.update(analyticItem);
/*     */       }
/*     */     } catch (SQLException e) {
/*     */       Dao<AnalyticItem, Integer> analyticItemDao;
/*     */       Handler handler;
/* 286 */       if (ETPush.getLogLevel() <= 6) {
/* 287 */         Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 291 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 292 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 295 */           if ((helper != null) && (helper.isOpen()))
/* 296 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isCountingTimeInApp()
/*     */   {
/* 304 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 306 */       Dao<AnalyticItem, Integer> analyticItemDao = helper.getAnalyticItemDao();
/* 307 */       List<AnalyticItem> analyticItems = analyticItemDao.queryBuilder().orderBy("id", true).where().eq("pi_app_key", "").and().like("analytic_types", "%4%").and().isNull("value").query();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 317 */       if (ETPush.getLogLevel() <= 3) {
/* 318 */         Log.d("jb4ASDK@ETAnalytics", "isCountingTimeInApp: " + String.valueOf(analyticItems.size() > 0));
/*     */       }
/*     */       Handler handler;
/* 321 */       return analyticItems.size() > 0;
/*     */     }
/*     */     catch (SQLException e) {
/* 324 */       if (ETPush.getLogLevel() <= 6) {
/* 325 */         Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 329 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 330 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 333 */           if ((helper != null) && (helper.isOpen()))
/* 334 */             helper.close(); } }, 10000L);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 340 */     return false;
/*     */   }
/*     */   
/*     */   public void onEvent(BackgroundEvent event) {
/* 344 */     if ((Config.isETanalyticsActive() | Config.isPIanalyticsActive())) {
/* 345 */       SharedPreferences prefs = this.applicationContext.getSharedPreferences("ETPush", 0);
/* 346 */       if (event.isInBackground())
/*     */       {
/*     */ 
/* 349 */         if (ETPush.getLogLevel() <= 3) {
/* 350 */           Log.d("jb4ASDK@ETAnalytics", "App is now in background.");
/*     */         }
/*     */         
/* 353 */         long backgroundTime = System.currentTimeMillis();
/* 354 */         if (ETPush.getLogLevel() <= 3) {
/* 355 */           Log.d("jb4ASDK@ETAnalytics", "Save background time: " + backgroundTime);
/*     */         }
/* 357 */         prefs.edit().putLong("et_background_time_cache", backgroundTime).commit();
/*     */         
/* 359 */         endTimeInAppCounter();
/*     */         
/*     */ 
/* 362 */         if (Config.isETanalyticsActive())
/*     */         {
/* 364 */           Intent sendDataIntent = new Intent(this.applicationContext, ETSendDataReceiver.class);
/* 365 */           sendDataIntent.putExtra("et_send_type_extra", "et_send_type_analytic_events");
/* 366 */           this.applicationContext.sendBroadcast(sendDataIntent);
/*     */         }
/*     */         
/* 369 */         if (Config.isPIanalyticsActive())
/*     */         {
/* 371 */           Intent sendDataIntent = new Intent(this.applicationContext, ETSendDataReceiver.class);
/* 372 */           sendDataIntent.putExtra("et_send_type_extra", "pi_send_type_analytic_events");
/* 373 */           this.applicationContext.sendBroadcast(sendDataIntent);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 378 */         if (ETPush.getLogLevel() <= 3) {
/* 379 */           Log.d("jb4ASDK@ETAnalytics", "App is now in foreground.");
/*     */         }
/*     */         
/*     */ 
/* 383 */         long backgroundTime = prefs.getLong("et_background_time_cache", -1L);
/*     */         
/* 385 */         if (backgroundTime != -1L) {
/* 386 */           if (ETPush.getLogLevel() <= 3) {
/* 387 */             Log.d("jb4ASDK@ETAnalytics", "Found background time.  Checking if app has been in background for over 30 minutes: " + backgroundTime);
/*     */           }
/*     */           
/* 390 */           SharedPreferences.Editor prefsEditor = prefs.edit();
/* 391 */           prefsEditor.remove("et_background_time_cache");
/*     */           
/* 393 */           Calendar timeWentInBackground = Calendar.getInstance();
/* 394 */           timeWentInBackground.setTimeInMillis(backgroundTime);
/*     */           
/* 396 */           Calendar thirtyMinsAgo = Calendar.getInstance();
/* 397 */           thirtyMinsAgo.add(12, -30);
/*     */           
/* 399 */           if (ETPush.getLogLevel() <= 3) {
/* 400 */             Log.d("jb4ASDK@ETAnalytics", String.format("timeWentInBackground: %d and thirtyMinsAgo: %d", new Object[] { Long.valueOf(timeWentInBackground.getTimeInMillis()), Long.valueOf(thirtyMinsAgo.getTimeInMillis()) }));
/*     */           }
/*     */           
/* 403 */           if (timeWentInBackground.before(thirtyMinsAgo)) {
/* 404 */             if (ETPush.getLogLevel() <= 3) {
/* 405 */               Log.d("jb4ASDK@ETAnalytics", "App has been in background for more than 30 minutes, so reset PI session Id.");
/*     */             }
/*     */             
/* 408 */             prefsEditor.remove("et_session_id_cache");
/*     */           }
/*     */           
/* 411 */           prefsEditor.commit();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 416 */         if (!isCountingTimeInApp()) {
/* 417 */           if (ETPush.getLogLevel() <= 3) {
/* 418 */             Log.d("jb4ASDK@ETAnalytics", "Not counting time in App, so start a new counter.");
/*     */           }
/*     */           
/* 421 */           final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */           try {
/* 423 */             Dao<AnalyticItem, Integer> analyticItemDao = helper.getAnalyticItemDao();
/*     */             
/*     */ 
/* 426 */             Date currDate = new Date();
/* 427 */             if (Config.isETanalyticsActive())
/*     */             {
/* 429 */               AnalyticItem analyticItem = new AnalyticItem(this.applicationContext);
/* 430 */               analyticItem.setEventDate(currDate);
/* 431 */               analyticItem.addAnalyticType(4);
/* 432 */               analyticItemDao.create(analyticItem);
/*     */             }
/*     */             
/* 435 */             if (Config.isPIanalyticsActive())
/*     */             {
/* 437 */               AnalyticItem analyticItem = new AnalyticItem(this.applicationContext);
/* 438 */               analyticItem.setEventDate(currDate);
/* 439 */               analyticItem.addAnalyticType(5);
/*     */               
/* 441 */               analyticItem.setPiAppKey("849f26e2-2df6-11e4-ab12-14109fdc48df");
/* 442 */               analyticItemDao.create(analyticItem);
/*     */             }
/*     */           }
/*     */           catch (SQLException e) {
/*     */             Handler handler;
/* 447 */             if (ETPush.getLogLevel() <= 6) {
/* 448 */               Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */             }
/*     */           } finally {
/*     */             Handler handler;
/* 452 */             Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 453 */             handler.postDelayed(new Runnable()
/*     */             {
/*     */               public void run() {
/* 456 */                 if ((helper != null) && (helper.isOpen()))
/* 457 */                   helper.close(); } }, 10000L);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void logFenceOrProximityMessageDisplayed(String regionId, int transitionType, int proximity, List<String> messageIds)
/*     */   {
/* 468 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 470 */       Dao<AnalyticItem, Integer> analyticItemDao = helper.getAnalyticItemDao();
/*     */       
/* 472 */       AnalyticItem analyticItem = new AnalyticItem(this.applicationContext);
/* 473 */       analyticItem.setEventDate(new Date());
/* 474 */       if (transitionType == 1) {
/* 475 */         analyticItem.addAnalyticType(6);
/* 476 */         analyticItem.addObjectId(regionId);
/*     */       }
/* 478 */       else if (transitionType == 2) {
/* 479 */         analyticItem.addAnalyticType(7);
/* 480 */         analyticItem.addObjectId(regionId);
/*     */       }
/*     */       else {
/* 483 */         analyticItem.addAnalyticType(12);
/* 484 */         analyticItem.addObjectId(regionId);
/*     */       }
/*     */       
/* 487 */       analyticItem.addAnalyticType(3);
/* 488 */       for (String messageId : messageIds) {
/* 489 */         analyticItem.addObjectId(messageId);
/*     */       }
/* 491 */       analyticItem.setReadyToSend(Boolean.TRUE);
/* 492 */       analyticItemDao.create(analyticItem);
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 495 */       if (ETPush.getLogLevel() <= 6) {
/* 496 */         Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 500 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 501 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 504 */           if ((helper != null) && (helper.isOpen()))
/* 505 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void startTimeInRegionLog(String regionId, boolean isBeacon)
/*     */   {
/* 513 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 515 */       Dao<AnalyticItem, Integer> analyticItemDao = helper.getAnalyticItemDao();
/*     */       
/* 517 */       AnalyticItem analyticItem = new AnalyticItem(this.applicationContext);
/* 518 */       analyticItem.setEventDate(new Date());
/* 519 */       if (!isBeacon) {
/* 520 */         analyticItem.addAnalyticType(11);
/*     */       }
/*     */       else {
/* 523 */         analyticItem.addAnalyticType(13);
/*     */       }
/* 525 */       analyticItem.addObjectId(regionId);
/* 526 */       analyticItemDao.create(analyticItem);
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 529 */       if (ETPush.getLogLevel() <= 6) {
/* 530 */         Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 534 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 535 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 538 */           if ((helper != null) && (helper.isOpen()))
/* 539 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void stopTimeInRegionLog(String regionId)
/*     */   {
/* 547 */     long now = System.currentTimeMillis();
/* 548 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 550 */       analyticItemDao = helper.getAnalyticItemDao();
/*     */       
/* 552 */       List<AnalyticItem> analyticItems = analyticItemDao.queryBuilder().where().like("object_ids", "%" + regionId + "%").and().isNull("value").query();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 559 */       if ((analyticItems != null) && (analyticItems.size() > 0))
/* 560 */         for (AnalyticItem analyticItem : analyticItems)
/*     */         {
/* 562 */           int timeInRegion = (int)((now - analyticItem.getEventDate().getTime()) / 1000L);
/* 563 */           if (ETPush.getLogLevel() <= 3) {
/* 564 */             Log.d("jb4ASDK@ETAnalytics", "Time in region: " + regionId + " was " + timeInRegion + " seconds");
/*     */           }
/* 566 */           analyticItem.setValue(Integer.valueOf(timeInRegion));
/* 567 */           analyticItem.setReadyToSend(Boolean.TRUE);
/* 568 */           analyticItemDao.update(analyticItem);
/*     */         }
/*     */     } catch (SQLException e) {
/*     */       Dao<AnalyticItem, Integer> analyticItemDao;
/*     */       Handler handler;
/* 573 */       if (ETPush.getLogLevel() <= 6) {
/* 574 */         Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 578 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 579 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 582 */           if ((helper != null) && (helper.isOpen()))
/* 583 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void logMessageReceived(String messageId)
/*     */   {
/* 591 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 593 */       Dao<AnalyticItem, Integer> analyticItemDao = helper.getAnalyticItemDao();
/*     */       
/* 595 */       AnalyticItem analyticItem = new AnalyticItem(this.applicationContext);
/* 596 */       analyticItem.setEventDate(new Date());
/* 597 */       analyticItem.addAnalyticType(10);
/* 598 */       analyticItem.addObjectId(messageId);
/* 599 */       analyticItem.setReadyToSend(Boolean.TRUE);
/* 600 */       analyticItemDao.create(analyticItem);
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 603 */       if (ETPush.getLogLevel() <= 6) {
/* 604 */         Log.e("jb4ASDK@ETAnalytics", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 608 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 609 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 612 */           if ((helper != null) && (helper.isOpen()))
/* 613 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETAnalytics.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */