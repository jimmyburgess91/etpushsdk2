/*     */ package com.exacttarget.etpushsdk;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.os.Handler;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.data.CloudPagesResponse;
/*     */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*     */ import com.exacttarget.etpushsdk.data.Message;
/*     */ import com.exacttarget.etpushsdk.event.BackgroundEvent;
/*     */ import com.exacttarget.etpushsdk.event.CloudPagesChangedEvent;
/*     */ import com.exacttarget.etpushsdk.util.EventBus;
/*     */ import com.j256.ormlite.dao.Dao;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
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
/*     */ public class ETCloudPageManager
/*     */ {
/*  34 */   private static final String TAG = ETCloudPageManager.class.getSimpleName();
/*     */   
/*     */   private static ETCloudPageManager cloudPageManager;
/*     */   private Context applicationContext;
/*     */   
/*     */   private ETCloudPageManager(Context applicationContext)
/*     */   {
/*  41 */     this.applicationContext = applicationContext;
/*     */     
/*  43 */     EventBus.getDefault().register(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ETCloudPageManager cloudPageManager()
/*     */     throws ETException
/*     */   {
/*  51 */     if (Config.isCloudPagesActive()) {
/*  52 */       if (cloudPageManager == null) {
/*  53 */         throw new ETException("You forgot to call readyAimFire first.");
/*     */       }
/*     */       
/*  56 */       return cloudPageManager;
/*     */     }
/*     */     
/*  59 */     throw new ETException("ETCloudPageManager disabled.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void readyAimFire(Context applicationContext)
/*     */     throws ETException
/*     */   {
/*  68 */     if (cloudPageManager == null) {
/*  69 */       if (ETPush.getLogLevel() <= 3) {
/*  70 */         Log.d(TAG, "readyAimFire()");
/*     */       }
/*  72 */       cloudPageManager = new ETCloudPageManager(applicationContext);
/*     */     }
/*     */     else {
/*  75 */       throw new ETException("You must have called readyAimFire more than once.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEventBackgroundEvent(BackgroundEvent event)
/*     */   {
/*  86 */     if ((Config.isCloudPagesActive()) && 
/*  87 */       (!event.isInBackground()))
/*     */     {
/*  89 */       if (ETPush.getLogLevel() <= 3) {
/*  90 */         Log.d(TAG, "In FOREGROUND");
/*     */       }
/*  92 */       refreshData();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEventCloudPagesResponse(CloudPagesResponse response)
/*     */   {
/* 104 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 106 */       Dao<Message, String> messageDao = helper.getMessageDao();
/* 107 */       if ((response != null) && (response.getMessages() != null) && (!response.getMessages().isEmpty())) {
/* 108 */         for (Message message : response.getMessages()) {
/* 109 */           Message dbMessage = (Message)messageDao.queryForId(message.getId());
/* 110 */           if (dbMessage != null)
/*     */           {
/* 112 */             message.setRead(dbMessage.getRead());
/*     */             
/* 114 */             message.setMessageDeleted(dbMessage.getMessageDeleted());
/*     */             
/* 116 */             messageDao.update(message);
/*     */           }
/*     */           else {
/* 119 */             messageDao.create(message);
/*     */           }
/*     */         }
/*     */         
/* 123 */         EventBus.getDefault().post(new CloudPagesChangedEvent());
/*     */       }
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 127 */       if (ETPush.getLogLevel() <= 6) {
/* 128 */         Log.e(TAG, e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 132 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 133 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 136 */           if ((helper != null) && (helper.isOpen()))
/* 137 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refreshData()
/*     */   {
/* 148 */     Intent sendDataIntent = new Intent(this.applicationContext, ETSendDataReceiver.class);
/* 149 */     sendDataIntent.putExtra("et_send_type_extra", "et_send_type_cloudpage");
/* 150 */     this.applicationContext.sendBroadcast(sendDataIntent);
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETCloudPageManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */