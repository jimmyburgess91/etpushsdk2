/*     */ package com.exacttarget.etpushsdk.data;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.database.sqlite.SQLiteDatabase;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.ETPush;
/*     */ import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
/*     */ import com.j256.ormlite.dao.Dao;
/*     */ import com.j256.ormlite.support.ConnectionSource;
/*     */ import com.j256.ormlite.table.TableUtils;
/*     */ import java.sql.SQLException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public class ETSqliteOpenHelper
/*     */   extends OrmLiteSqliteOpenHelper
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@ETSqliteOpenHelper";
/*     */   private static final String DATABASE_NAME = "etdb.db";
/*     */   private static final int DATABASE_VERSION = 6;
/*  33 */   private static ETSqliteOpenHelper helper = null;
/*  34 */   private static final AtomicInteger usageCounter = new AtomicInteger(0);
/*     */   
/*  36 */   private Context appContext = null;
/*     */   
/*     */   private static Dao<BeaconRequest, Integer> beaconRequestDao;
/*     */   
/*     */   private static Dao<GeofenceRequest, Integer> geofenceRequestDao;
/*     */   
/*     */   private static Dao<LocationUpdate, Integer> locationUpdateDao;
/*     */   private static Dao<Message, String> messageDao;
/*     */   private static Dao<RegionMessage, Integer> regionMessageDao;
/*     */   private static Dao<Region, String> regionDao;
/*     */   private static Dao<Registration, Integer> registrationDao;
/*     */   private static Dao<AnalyticItem, Integer> analyticItemDao;
/*     */   
/*     */   private ETSqliteOpenHelper(Context context)
/*     */   {
/*  51 */     super(context, "etdb.db", null, 6, context.getApplicationContext().getClassLoader().getResourceAsStream("com/exacttarget/etpushsdk/data/ormlite_config.txt"));
/*  52 */     this.appContext = context.getApplicationContext();
/*     */   }
/*     */   
/*     */   public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
/*     */   {
/*     */     try {
/*  58 */       if (ETPush.getLogLevel() <= 3) {
/*  59 */         Log.d("jb4ASDK@ETSqliteOpenHelper", "onCreate");
/*     */       }
/*  61 */       TableUtils.createTable(connectionSource, BeaconRequest.class);
/*  62 */       TableUtils.createTable(connectionSource, GeofenceRequest.class);
/*  63 */       TableUtils.createTable(connectionSource, LocationUpdate.class);
/*  64 */       TableUtils.createTable(connectionSource, Message.class);
/*  65 */       TableUtils.createTable(connectionSource, Region.class);
/*  66 */       TableUtils.createTable(connectionSource, RegionMessage.class);
/*  67 */       TableUtils.createTable(connectionSource, Registration.class);
/*  68 */       TableUtils.createTable(connectionSource, AnalyticItem.class);
/*     */     }
/*     */     catch (SQLException e) {
/*  71 */       if (ETPush.getLogLevel() <= 6) {
/*  72 */         Log.e("jb4ASDK@ETSqliteOpenHelper", "Can't create database", e);
/*     */       }
/*  74 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ETSqliteOpenHelper getHelper(Context context)
/*     */   {
/*  83 */     synchronized ("jb4ASDK@ETSqliteOpenHelper") {
/*  84 */       if (helper == null) {
/*  85 */         helper = new ETSqliteOpenHelper(context);
/*     */       }
/*  87 */       usageCounter.incrementAndGet();
/*     */     }
/*  89 */     return helper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/*  99 */     synchronized ("jb4ASDK@ETSqliteOpenHelper") {
/* 100 */       if (usageCounter.decrementAndGet() == 0) {
/* 101 */         super.close();
/* 102 */         beaconRequestDao = null;
/* 103 */         geofenceRequestDao = null;
/* 104 */         locationUpdateDao = null;
/* 105 */         messageDao = null;
/* 106 */         regionMessageDao = null;
/* 107 */         regionDao = null;
/* 108 */         registrationDao = null;
/* 109 */         analyticItemDao = null;
/* 110 */         helper = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset(Context applicationContext)
/*     */   {
/* 119 */     synchronized ("jb4ASDK@ETSqliteOpenHelper") {
/* 120 */       close();
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
/* 133 */       applicationContext.deleteDatabase("etdb.db");
/*     */     }
/*     */   }
/*     */   
/*     */   public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
/*     */   {
/*     */     try {
/* 140 */       if (oldVersion < 2) {
/* 141 */         Log.d("jb4ASDK@ETSqliteOpenHelper", "Updating DB from " + oldVersion + " to 2");
/* 142 */         getRegistrationDao().executeRaw("ALTER TABLE `registration` ADD COLUMN last_sent INTEGER;", new String[0]);
/* 143 */         getRegistrationDao().executeRaw("ALTER TABLE `registration` ADD COLUMN hwid TEXT;", new String[0]);
/* 144 */         getRegistrationDao().executeRaw("ALTER TABLE `registration` ADD COLUMN gcm_sender_id TEXT;", new String[0]);
/* 145 */         getLocationUpdateDao().executeRaw("ALTER TABLE `location_update` ADD COLUMN last_sent INTEGER;", new String[0]);
/* 146 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN message_deleted BOOLEAN;", new String[0]);
/*     */       }
/*     */       
/* 149 */       if (oldVersion < 3) {
/* 150 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN beacon_guid TEXT;", new String[0]);
/* 151 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN beacon_major INTEGER;", new String[0]);
/* 152 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN beacon_minor INTEGER;", new String[0]);
/* 153 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN entry_count INTEGER;", new String[0]);
/* 154 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN exit_count INTEGER;", new String[0]);
/* 155 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN description TEXT;", new String[0]);
/* 156 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN location_type INTEGER;", new String[0]);
/* 157 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN name TEXT;", new String[0]);
/* 158 */         getRegionDao().executeRaw("ALTER TABLE `regions` ADD COLUMN has_entered BOOLEAN;", new String[0]);
/* 159 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN min_tripped INTEGER;", new String[0]);
/* 160 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN proximity INTEGER;", new String[0]);
/* 161 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN ephemeral_message BOOLEAN;", new String[0]);
/* 162 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN has_entered BOOLEAN;", new String[0]);
/* 163 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN notify_id INTEGER;", new String[0]);
/* 164 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN loiter_seconds INTEGER;", new String[0]);
/* 165 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN entry_time INTEGER;", new String[0]);
/* 166 */         TableUtils.createTable(connectionSource, AnalyticItem.class);
/* 167 */         TableUtils.createTable(connectionSource, BeaconRequest.class);
/*     */       }
/*     */       
/* 170 */       if (oldVersion < 4) {
/* 171 */         getRegistrationDao().executeRaw("ALTER TABLE `registration` ADD COLUMN locale TEXT;", new String[0]);
/*     */       }
/*     */       
/* 174 */       if (oldVersion < 5) {
/* 175 */         getMessageDao().executeRaw("ALTER TABLE `messages` ADD COLUMN category TEXT;", new String[0]);
/*     */       }
/*     */       
/* 178 */       if (oldVersion < 6) {
/* 179 */         getRegistrationDao().executeRaw("ALTER TABLE `analytic_item` ADD COLUMN pi_app_key TEXT;", new String[0]);
/*     */       }
/*     */     }
/*     */     catch (SQLException e) {
/* 183 */       Log.e("jb4ASDK@ETSqliteOpenHelper", "Can't update database. blow it away and re-create", e);
/* 184 */       this.appContext.deleteDatabase("etdb.db");
/* 185 */       onCreate(database, connectionSource);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dao<BeaconRequest, Integer> getBeaconRequestDao()
/*     */     throws SQLException
/*     */   {
/* 194 */     if (beaconRequestDao == null) {
/* 195 */       beaconRequestDao = getDao(BeaconRequest.class);
/*     */     }
/* 197 */     return beaconRequestDao;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dao<GeofenceRequest, Integer> getGeofenceRequestDao()
/*     */     throws SQLException
/*     */   {
/* 205 */     if (geofenceRequestDao == null) {
/* 206 */       geofenceRequestDao = getDao(GeofenceRequest.class);
/*     */     }
/* 208 */     return geofenceRequestDao;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dao<LocationUpdate, Integer> getLocationUpdateDao()
/*     */     throws SQLException
/*     */   {
/* 216 */     if (locationUpdateDao == null) {
/* 217 */       locationUpdateDao = getDao(LocationUpdate.class);
/*     */     }
/* 219 */     return locationUpdateDao;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dao<Message, String> getMessageDao()
/*     */     throws SQLException
/*     */   {
/* 227 */     if (messageDao == null) {
/* 228 */       messageDao = getDao(Message.class);
/*     */     }
/* 230 */     return messageDao;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dao<Region, String> getRegionDao()
/*     */     throws SQLException
/*     */   {
/* 238 */     if (regionDao == null) {
/* 239 */       regionDao = getDao(Region.class);
/*     */     }
/* 241 */     return regionDao;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dao<RegionMessage, Integer> getRegionMessageDao()
/*     */     throws SQLException
/*     */   {
/* 249 */     if (regionMessageDao == null) {
/* 250 */       regionMessageDao = getDao(RegionMessage.class);
/*     */     }
/* 252 */     return regionMessageDao;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dao<Registration, Integer> getRegistrationDao()
/*     */     throws SQLException
/*     */   {
/* 260 */     if (registrationDao == null) {
/* 261 */       registrationDao = getDao(Registration.class);
/*     */     }
/* 263 */     return registrationDao;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dao<AnalyticItem, Integer> getAnalyticItemDao()
/*     */     throws SQLException
/*     */   {
/* 271 */     if (analyticItemDao == null) {
/* 272 */       analyticItemDao = getDao(AnalyticItem.class);
/*     */     }
/* 274 */     return analyticItemDao;
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\ETSqliteOpenHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */