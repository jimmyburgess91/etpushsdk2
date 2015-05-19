/*     */ package com.exacttarget.etpushsdk;
/*     */ 
/*     */ import android.app.NotificationManager;
/*     */ import android.content.BroadcastReceiver;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.os.Handler;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.data.AnalyticItem;
/*     */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*     */ import com.j256.ormlite.dao.Dao;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Date;
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
/*     */ public class ETOpenReceiver
/*     */   extends BroadcastReceiver
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@ETOpenReceiver";
/*     */   protected static final String OPEN_INTENT = "et_open_intent";
/*     */   protected static final String MESSAGE_OPENED = ".MESSAGE_OPENED";
/*     */   protected static final String AUTO_CANCEL = "et_auto_cancel";
/*     */   protected static final String NOTIFICATION_ID = "et_notification_id";
/*     */   
/*     */   public void onReceive(Context context, Intent intent)
/*     */   {
/*  63 */     if (ETPush.getLogLevel() <= 3) {
/*  64 */       Log.d("jb4ASDK@ETOpenReceiver", "onReceive");
/*     */     }
/*  66 */     Bundle payload = intent.getExtras();
/*  67 */     Intent launchIntent = (Intent)payload.getParcelable("et_open_intent");
/*     */     
/*  69 */     boolean autoCancel = payload.getBoolean("et_auto_cancel", false);
/*  70 */     if (ETPush.getLogLevel() <= 3) {
/*  71 */       Log.d("jb4ASDK@ETOpenReceiver", "AUTOCANCEL: " + autoCancel);
/*     */     }
/*  73 */     int notificationId = payload.getInt("et_notification_id", -1);
/*  74 */     if (ETPush.getLogLevel() <= 3) {
/*  75 */       Log.d("jb4ASDK@ETOpenReceiver", "NOTIFICATION_ID: " + notificationId);
/*     */     }
/*  77 */     if ((autoCancel) && (notificationId > -1)) {
/*  78 */       NotificationManager nm = (NotificationManager)context.getApplicationContext().getSystemService("notification");
/*  79 */       nm.cancel(notificationId);
/*     */       
/*     */ 
/*  82 */       context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
/*     */     }
/*     */     
/*  85 */     if (launchIntent != null) {
/*  86 */       launchIntent.addFlags(268435456);
/*  87 */       context.getApplicationContext().startActivity(launchIntent);
/*     */       
/*  89 */       if ((Config.isETanalyticsActive() | Config.isPIanalyticsActive())) {
/*  90 */         String messageId = launchIntent.getExtras().getString("_m");
/*  91 */         int transitionType = launchIntent.getExtras().getInt("transitionType", -1);
/*  92 */         String regionId = launchIntent.getExtras().getString("regionId");
/*  93 */         if ((messageId != null) && (messageId.length() > 0)) {
/*  94 */           final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(context.getApplicationContext());
/*     */           try {
/*  96 */             Dao<AnalyticItem, Integer> analyticItemDao = helper.getAnalyticItemDao();
/*     */             
/*     */ 
/*  99 */             ETAnalytics.engine().endTimeInAppCounter();
/*     */             
/*     */ 
/* 102 */             AnalyticItem event = new AnalyticItem(context.getApplicationContext());
/* 103 */             event.setEventDate(new Date());
/* 104 */             event.addAnalyticType(2);
/* 105 */             event.addObjectId(messageId);
/* 106 */             if (regionId != null)
/*     */             {
/* 108 */               if (transitionType == 1) {
/* 109 */                 event.addAnalyticType(6);
/*     */               }
/* 111 */               else if (transitionType == 2) {
/* 112 */                 event.addAnalyticType(7);
/*     */               }
/*     */               else {
/* 115 */                 event.addAnalyticType(12);
/*     */               }
/* 117 */               event.addObjectId(regionId);
/*     */             }
/*     */             
/* 120 */             if (Config.isETanalyticsActive())
/*     */             {
/* 122 */               AnalyticItem etEvent = new AnalyticItem(event);
/* 123 */               etEvent.addAnalyticType(4);
/* 124 */               analyticItemDao.create(etEvent);
/*     */             }
/*     */             
/* 127 */             if (Config.isPIanalyticsActive())
/*     */             {
/* 129 */               AnalyticItem piEvent = new AnalyticItem(event);
/* 130 */               piEvent.addAnalyticType(5);
/* 131 */               piEvent.setPiAppKey("849f26e2-2df6-11e4-ab12-14109fdc48df");
/* 132 */               analyticItemDao.create(piEvent);
/*     */             }
/*     */           } catch (SQLException e) {
/*     */             Handler handler;
/* 136 */             if (ETPush.getLogLevel() <= 6) {
/* 137 */               Log.e("jb4ASDK@ETOpenReceiver", e.getMessage(), e);
/*     */             }
/*     */           } catch (ETException e) {
/*     */             Handler handler;
/* 141 */             if (ETPush.getLogLevel() <= 6) {
/* 142 */               Log.e("jb4ASDK@ETOpenReceiver", e.getMessage(), e);
/*     */             }
/*     */           } finally {
/*     */             Handler handler;
/* 146 */             Handler handler = new Handler(context.getApplicationContext().getMainLooper());
/* 147 */             handler.postDelayed(new Runnable()
/*     */             {
/*     */               public void run() {
/* 150 */                 if ((helper != null) && (helper.isOpen()))
/* 151 */                   helper.close(); } }, 10000L);
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 160 */     else if (ETPush.getLogLevel() <= 6) {
/* 161 */       Log.e("jb4ASDK@ETOpenReceiver", "Received invalid Intent.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void startActivity(Context context, Intent launchIntent)
/*     */   {
/* 167 */     context.startActivity(launchIntent);
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETOpenReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */