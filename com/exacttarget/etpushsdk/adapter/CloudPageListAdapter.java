/*     */ package com.exacttarget.etpushsdk.adapter;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.os.Handler;
/*     */ import android.util.Log;
/*     */ import android.widget.BaseAdapter;
/*     */ import android.widget.ListAdapter;
/*     */ import com.exacttarget.etpushsdk.ETPush;
/*     */ import com.exacttarget.etpushsdk.data.ETSqliteOpenHelper;
/*     */ import com.exacttarget.etpushsdk.data.Message;
/*     */ import com.exacttarget.etpushsdk.event.CloudPagesChangedEvent;
/*     */ import com.exacttarget.etpushsdk.util.EventBus;
/*     */ import com.j256.ormlite.dao.Dao;
/*     */ import com.j256.ormlite.stmt.QueryBuilder;
/*     */ import com.j256.ormlite.stmt.Where;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
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
/*     */ public abstract class CloudPageListAdapter
/*     */   extends BaseAdapter
/*     */   implements ListAdapter
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@CloudPageListAdapter";
/*     */   private Context applicationContext;
/*     */   public static final int DISPLAY_ALL = 0;
/*     */   public static final int DISPLAY_UNREAD = 1;
/*     */   public static final int DISPLAY_READ = 2;
/*     */   private int display;
/*  49 */   private List<Message> allMessages = new ArrayList();
/*  50 */   private List<Message> unreadMessages = new ArrayList();
/*  51 */   private List<Message> readMessages = new ArrayList();
/*     */   
/*     */   private Handler uiHandler;
/*     */   
/*     */   public CloudPageListAdapter(Context appContext)
/*     */   {
/*  57 */     this.applicationContext = appContext;
/*  58 */     this.uiHandler = new Handler();
/*  59 */     EventBus.getDefault().register(this);
/*  60 */     reloadData();
/*     */   }
/*     */   
/*     */   protected void finalize() throws Throwable
/*     */   {
/*  65 */     EventBus.getDefault().unregister(this);
/*  66 */     super.finalize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEvent(CloudPagesChangedEvent event)
/*     */   {
/*  75 */     this.uiHandler.post(new Runnable() {
/*     */       public void run() {
/*  77 */         CloudPageListAdapter.this.notifyDataSetChanged();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getCount()
/*     */   {
/*  86 */     int count = 0;
/*     */     
/*  88 */     switch (this.display) {
/*     */     case 0: 
/*  90 */       count = this.allMessages.size();
/*  91 */       break;
/*     */     case 1: 
/*  93 */       count = this.unreadMessages.size();
/*  94 */       break;
/*     */     case 2: 
/*  96 */       count = this.readMessages.size();
/*     */     }
/*     */     
/*     */     
/* 100 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getItem(int position)
/*     */   {
/* 107 */     Message message = null;
/* 108 */     switch (this.display) {
/*     */     case 0: 
/* 110 */       message = (Message)this.allMessages.get(position);
/* 111 */       break;
/*     */     case 1: 
/* 113 */       message = (Message)this.unreadMessages.get(position);
/* 114 */       break;
/*     */     case 2: 
/* 116 */       message = (Message)this.readMessages.get(position);
/*     */     }
/*     */     
/*     */     
/* 120 */     return message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getItemId(int position)
/*     */   {
/* 128 */     return position;
/*     */   }
/*     */   
/*     */   public void notifyDataSetChanged()
/*     */   {
/* 133 */     reloadData();
/*     */     
/* 135 */     super.notifyDataSetChanged();
/*     */   }
/*     */   
/*     */   private void reloadData() {
/* 139 */     this.allMessages.clear();
/* 140 */     this.unreadMessages.clear();
/* 141 */     this.readMessages.clear();
/*     */     
/* 143 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 145 */       QueryBuilder<Message, String> queryBuilder = helper.getMessageDao().queryBuilder();
/* 146 */       queryBuilder.where().eq("message_type", Message.MESSAGE_TYPE_BASIC).and().eq("content_type", Message.CONTENT_TYPE_CLOUD_PAGE_ONLY).and().eq("message_deleted", Boolean.FALSE);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */       queryBuilder.orderBy("start_date", false);
/*     */       
/* 154 */       List<Message> messages = helper.getMessageDao().query(queryBuilder.prepare());
/* 155 */       this.allMessages.addAll(messages);
/* 156 */       now = new Date();
/* 157 */       for (Message message : messages) {
/* 158 */         if ((Message.MESSAGE_TYPE_BASIC.equals(message.getMessageType())) && (Message.CONTENT_TYPE_CLOUD_PAGE_ONLY.equals(message.getContentType())) && ((message.getEndDate() == null) || (message.getEndDate().after(now)))) {
/* 159 */           if (message.getRead().booleanValue()) {
/* 160 */             this.readMessages.add(message);
/*     */           }
/*     */           else
/* 163 */             this.unreadMessages.add(message);
/*     */         }
/*     */       }
/*     */     } catch (SQLException e) {
/*     */       Date now;
/*     */       Handler handler;
/* 169 */       if (ETPush.getLogLevel() <= 6) {
/* 170 */         Log.e("jb4ASDK@CloudPageListAdapter", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 174 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 175 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 178 */           if ((helper != null) && (helper.isOpen()))
/* 179 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getDisplay()
/*     */   {
/* 187 */     return this.display;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDisplay(int display)
/*     */   {
/* 196 */     if (this.display != display) {
/* 197 */       this.display = display;
/* 198 */       notifyDataSetChanged();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageRead(Message message)
/*     */   {
/* 207 */     message.setRead(Boolean.TRUE);
/* 208 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 210 */       helper.getMessageDao().update(message);
/* 211 */       notifyDataSetChanged();
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 214 */       if (ETPush.getLogLevel() <= 6) {
/* 215 */         Log.e("jb4ASDK@CloudPageListAdapter", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 219 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 220 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 223 */           if ((helper != null) && (helper.isOpen()))
/* 224 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageUnread(Message message)
/*     */   {
/* 236 */     message.setRead(Boolean.FALSE);
/* 237 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 239 */       helper.getMessageDao().update(message);
/* 240 */       notifyDataSetChanged();
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 243 */       if (ETPush.getLogLevel() <= 6) {
/* 244 */         Log.e("jb4ASDK@CloudPageListAdapter", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 248 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 249 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 252 */           if ((helper != null) && (helper.isOpen()))
/* 253 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deleteMessage(Message message)
/*     */   {
/* 265 */     final ETSqliteOpenHelper helper = ETSqliteOpenHelper.getHelper(this.applicationContext);
/*     */     try {
/* 267 */       message.setMessageDeleted(Boolean.TRUE);
/* 268 */       helper.getMessageDao().update(message);
/* 269 */       notifyDataSetChanged();
/*     */     } catch (SQLException e) {
/*     */       Handler handler;
/* 272 */       if (ETPush.getLogLevel() <= 6) {
/* 273 */         Log.e("jb4ASDK@CloudPageListAdapter", e.getMessage(), e);
/*     */       }
/*     */     } finally {
/*     */       Handler handler;
/* 277 */       Handler handler = new Handler(this.applicationContext.getMainLooper());
/* 278 */       handler.postDelayed(new Runnable()
/*     */       {
/*     */         public void run() {
/* 281 */           if ((helper != null) && (helper.isOpen()))
/* 282 */             helper.close(); } }, 10000L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\adapter\CloudPageListAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */