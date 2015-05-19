/*     */ package com.exacttarget.etpushsdk.data;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.content.SharedPreferences;
/*     */ import android.content.SharedPreferences.Editor;
/*     */ import android.content.pm.ApplicationInfo;
/*     */ import android.content.pm.PackageInfo;
/*     */ import android.content.pm.PackageManager;
/*     */ import android.content.pm.PackageManager.NameNotFoundException;
/*     */ import android.os.Build;
/*     */ import android.os.Build.VERSION;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.Config;
/*     */ import com.exacttarget.etpushsdk.ETPush;
/*     */ import com.exacttarget.etpushsdk.util.ETAmazonDeviceMessagingUtil;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import com.j256.ormlite.field.DataType;
/*     */ import com.j256.ormlite.field.DatabaseField;
/*     */ import com.j256.ormlite.table.DatabaseTable;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ @DatabaseTable(tableName="registration")
/*     */ @JsonIgnoreProperties(ignoreUnknown=false)
/*     */ @JsonInclude(JsonInclude.Include.NON_NULL)
/*     */ public class Registration
/*     */   extends DeviceData
/*     */   implements Serializable
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@Registration";
/*     */   public static final String COLUMN_ID = "id";
/*     */   public static final String COLUMN_LAST_SENT = "last_sent";
/*     */   public static final String COLUMN_HWID = "hwid";
/*     */   public static final String COLUMN_GCM_SENDER_ID = "gcm_sender_id";
/*     */   public static final String COLUMN_LOCALE = "locale";
/*     */   private static final long serialVersionUID = 1L;
/*     */   @DatabaseField(generatedId=true)
/*     */   @JsonIgnore
/*     */   private Integer id;
/*     */   @DatabaseField(columnName="platform")
/*     */   @JsonProperty("platform")
/*     */   private String platform;
/*     */   @DatabaseField(columnName="device_id")
/*     */   @JsonProperty("deviceID")
/*     */   private String deviceId;
/*     */   @DatabaseField(columnName="device_token")
/*     */   @JsonProperty("device_Token")
/*     */   private String deviceToken;
/*     */   @DatabaseField(columnName="subscriber_key")
/*     */   @JsonProperty("subscriberKey")
/*     */   private String subscriberKey;
/*     */   @DatabaseField(columnName="et_app_id")
/*     */   @JsonProperty("etAppId")
/*     */   private String etAppId;
/*     */   @DatabaseField(columnName="email")
/*     */   @JsonProperty("email")
/*     */   private String email;
/*     */   @DatabaseField(columnName="badge")
/*     */   @JsonProperty("badge")
/*     */   private Integer badge;
/*     */   @DatabaseField(columnName="timezone")
/*     */   @JsonProperty("timeZone")
/*     */   private Integer timeZone;
/*     */   @DatabaseField(columnName="dst")
/*     */   @JsonProperty("dST")
/*     */   private Boolean dst;
/*     */   @DatabaseField(columnName="tags", dataType=DataType.SERIALIZABLE)
/*     */   @JsonProperty("tags")
/*     */   private HashSet<String> tags;
/*     */   @DatabaseField(columnName="attributes", dataType=DataType.SERIALIZABLE)
/*     */   @JsonProperty("attributes")
/*     */   private ArrayList<Attribute> attributes;
/*     */   @DatabaseField(columnName="platform_version")
/*     */   @JsonProperty("platform_Version")
/*     */   private String platformVersion;
/*     */   @DatabaseField(columnName="push_enabled")
/*     */   @JsonProperty("push_Enabled")
/*     */   private Boolean pushEnabled;
/*     */   @DatabaseField(columnName="location_enabled")
/*     */   @JsonProperty("location_Enabled")
/*     */   private Boolean locationEnabled;
/*     */   @DatabaseField(columnName="last_sent")
/*     */   @JsonIgnore
/* 116 */   private Long lastSent = Long.valueOf(0L);
/*     */   
/*     */ 
/*     */   @DatabaseField(columnName="hwid")
/*     */   @JsonProperty("hwid")
/*     */   private String hwid;
/*     */   
/*     */   @DatabaseField(columnName="gcm_sender_id")
/*     */   @JsonProperty("gcmSenderId")
/*     */   private String gcmSenderId;
/*     */   
/*     */   @DatabaseField(columnName="locale")
/*     */   @JsonProperty("locale")
/*     */   private String locale;
/*     */   
/*     */   @JsonProperty("language")
/*     */   protected String language;
/*     */   
/*     */   @JsonIgnore
/*     */   private static final String ET_REGISTRATION_CACHE = "et_registration_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   private static final String ET_TAGS_CACHE = "et_tags_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   private static final String ET_ATTRIBUTES_CACHE = "et_attributes_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   public static final String ET_SUBSCRIBER_CACHE = "et_subscriber_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   public static final String ET_DEVICE_ID_CACHE = "et_device_id_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   public static final String ET_MANUFACTURER_CACHE = "et_manufacturer_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   public static final String ET_PLATFORM_CACHE = "et_platform_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   public static final String ET_PLATFORM_VERSION_CACHE = "et_platform_version_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   public static final String ET_MODEL_CACHE = "et_model_cache";
/*     */   
/*     */   @JsonIgnore
/*     */   private static final String ET_SEPARATOR = "^|^";
/*     */   
/*     */   @JsonIgnore
/*     */   private static final String ET_SPLITTER = "\\^\\|\\^";
/*     */   
/*     */   @DatabaseField(persisted=false)
/*     */   @JsonIgnore
/* 169 */   private SharedPreferences prefs = null;
/*     */   
/*     */ 
/*     */   @JsonIgnore
/* 173 */   private Context applicationContext = null;
/*     */   
/*     */ 
/*     */ 
/*     */   public Registration() {}
/*     */   
/*     */ 
/*     */   public Registration(Context context)
/*     */   {
/* 182 */     this.applicationContext = context;
/* 183 */     this.prefs = context.getSharedPreferences("ETPush", 0);
/* 184 */     this.deviceId = uniqueDeviceIdentifier(context);
/* 185 */     this.timeZone = Integer.valueOf(TimeZone.getDefault().getRawOffset() / 1000);
/* 186 */     this.dst = Boolean.valueOf(TimeZone.getDefault().inDaylightTime(new Date()));
/* 187 */     this.locale = Locale.getDefault().toString();
/*     */     
/* 189 */     deserializeTags();
/* 190 */     if (ETAmazonDeviceMessagingUtil.isAmazonDevice()) {
/* 191 */       this.platform = "Amazon";
/* 192 */       this.tags.add("Amazon");
/*     */     }
/*     */     else {
/* 195 */       this.platform = "Android";
/* 196 */       this.tags.add("Android");
/*     */     }
/*     */     
/* 199 */     this.platformVersion = Build.VERSION.RELEASE;
/*     */     
/* 201 */     if (ETPush.getLogLevel() <= 3) {
/* 202 */       Log.d("jb4ASDK@Registration", "Platform: \"" + this.platform + "\", platformVersion: \"" + this.platformVersion + "\"");
/*     */     }
/*     */     
/* 205 */     String manufacturer = Build.MANUFACTURER;
/* 206 */     String model = Build.MODEL;
/* 207 */     this.hwid = (manufacturer + ' ' + model);
/* 208 */     if (ETPush.getLogLevel() <= 3) {
/* 209 */       Log.i("jb4ASDK@Registration", "hwid: " + this.hwid);
/*     */     }
/*     */     
/* 212 */     SharedPreferences.Editor prefsEditor = this.prefs.edit();
/* 213 */     prefsEditor.putString("et_device_id_cache", this.deviceId);
/* 214 */     prefsEditor.putString("et_manufacturer_cache", manufacturer);
/* 215 */     prefsEditor.putString("et_platform_cache", this.platform);
/* 216 */     prefsEditor.putString("et_platform_version_cache", this.platformVersion);
/* 217 */     prefsEditor.putString("et_model_cache", model);
/* 218 */     prefsEditor.commit();
/*     */     
/*     */     try
/*     */     {
/* 222 */       PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
/* 223 */       int flags = packageInfo.applicationInfo.flags;
/* 224 */       if ((flags & 0x2) != 0) {
/* 225 */         this.tags.add("Debug");
/*     */       }
/*     */     }
/*     */     catch (PackageManager.NameNotFoundException e) {
/* 229 */       if (ETPush.getLogLevel() <= 5) {
/* 230 */         Log.w("jb4ASDK@Registration", e.getMessage());
/*     */       }
/*     */     }
/*     */     
/* 234 */     this.tags.add("ALL");
/*     */     
/* 236 */     serializeTags();
/*     */     
/* 238 */     deserializeAttributes();
/* 239 */     addAttribute(new Attribute("_ETSDKVersion", ETPush.getSDKVersionName(this.applicationContext)));
/*     */     
/* 241 */     this.subscriberKey = ((String)Config.getETSharedPref(this.applicationContext, this.applicationContext.getSharedPreferences("et_registration_cache", 0), "et_subscriber_cache", (String)null));
/*     */   }
/*     */   
/*     */   public void addAttribute(Attribute attribute) {
/* 245 */     if (this.attributes.contains(attribute)) {
/* 246 */       this.attributes.remove(attribute);
/*     */     }
/* 248 */     this.attributes.add(attribute);
/* 249 */     serializeAttributes();
/*     */   }
/*     */   
/*     */   public void removeAttribute(Attribute attribute) {
/* 253 */     if (this.attributes.contains(attribute)) {
/* 254 */       this.attributes.remove(attribute);
/*     */     }
/* 256 */     serializeAttributes();
/*     */   }
/*     */   
/*     */   private void serializeAttributes() {
/* 260 */     if (this.prefs != null) {
/* 261 */       StringBuilder out = new StringBuilder();
/* 262 */       for (Attribute attribute : this.attributes) {
/* 263 */         out.append(attribute.getKey());
/* 264 */         out.append("^|^");
/* 265 */         out.append(attribute.getValue());
/* 266 */         out.append("^|^");
/*     */       }
/* 268 */       out.setLength(out.length() - 3);
/* 269 */       this.prefs.edit().putString("et_attributes_cache", out.toString()).commit();
/*     */     }
/*     */   }
/*     */   
/*     */   private void deserializeAttributes() {
/* 274 */     if (this.prefs != null) {
/* 275 */       this.attributes = new ArrayList();
/* 276 */       String cacheString = (String)Config.getETSharedPref(this.applicationContext, this.applicationContext.getSharedPreferences("et_registration_cache", 0), "et_attributes_cache", (String)null);
/* 277 */       if (cacheString == null) {
/* 278 */         return;
/*     */       }
/*     */       
/* 281 */       String[] tokens = cacheString.split("\\^\\|\\^");
/* 282 */       for (int i = 0; i < tokens.length; i += 2) {
/* 283 */         while ((tokens[i] == null) || (tokens[i].isEmpty())) {
/* 284 */           i++;
/*     */         }
/* 286 */         this.attributes.add(new Attribute(tokens[i], tokens[(i + 1)]));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addTag(String tag) {
/* 292 */     this.tags.add(tag);
/* 293 */     serializeTags();
/*     */   }
/*     */   
/*     */   public void removeTag(String tag) {
/* 297 */     this.tags.remove(tag);
/* 298 */     serializeTags();
/*     */   }
/*     */   
/*     */   private void serializeTags() {
/* 302 */     if (this.prefs != null) {
/* 303 */       StringBuilder out = new StringBuilder();
/* 304 */       for (String tag : this.tags) {
/* 305 */         out.append(tag);
/* 306 */         out.append("^|^");
/*     */       }
/* 308 */       out.setLength(out.length() - 3);
/* 309 */       this.prefs.edit().putString("et_tags_cache", out.toString()).commit();
/*     */     }
/*     */   }
/*     */   
/*     */   private void deserializeTags() {
/* 314 */     if (this.prefs != null) {
/* 315 */       this.tags = new HashSet();
/* 316 */       String cacheString = (String)Config.getETSharedPref(this.applicationContext, this.applicationContext.getSharedPreferences("et_registration_cache", 0), "et_tags_cache", (String)null);
/* 317 */       if (cacheString == null) {
/* 318 */         return;
/*     */       }
/*     */       
/* 321 */       String[] tokens = cacheString.split("\\^\\|\\^");
/* 322 */       for (int i = 0; i < tokens.length; i++) {
/* 323 */         if ((tokens[i] != null) && (!tokens[i].isEmpty())) {
/* 324 */           this.tags.add(tokens[i]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Integer getId() {
/* 331 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(Integer id) {
/* 335 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getPlatform() {
/* 339 */     return this.platform;
/*     */   }
/*     */   
/*     */   public void setPlatform(String platform) {
/* 343 */     this.platform = platform;
/*     */   }
/*     */   
/*     */   public String getDeviceId() {
/* 347 */     return this.deviceId;
/*     */   }
/*     */   
/*     */   public void setDeviceId(String deviceId) {
/* 351 */     this.deviceId = deviceId;
/*     */   }
/*     */   
/*     */   public String getEtAppId() {
/* 355 */     return this.etAppId;
/*     */   }
/*     */   
/*     */   public void setEtAppId(String etAppId) {
/* 359 */     this.etAppId = etAppId;
/*     */   }
/*     */   
/*     */   public Integer getBadge() {
/* 363 */     return this.badge;
/*     */   }
/*     */   
/*     */   public void setBadge(Integer badge) {
/* 367 */     this.badge = badge;
/*     */   }
/*     */   
/*     */   public String getEmail() {
/* 371 */     return this.email;
/*     */   }
/*     */   
/*     */   public void setEmail(String email) {
/* 375 */     this.email = email;
/*     */   }
/*     */   
/*     */   public Integer getTimeZone() {
/* 379 */     return this.timeZone;
/*     */   }
/*     */   
/*     */   public void setTimeZone(Integer timeZone) {
/* 383 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */   public Boolean getDst() {
/* 387 */     return this.dst;
/*     */   }
/*     */   
/*     */   public void setDst(Boolean dst) {
/* 391 */     this.dst = dst;
/*     */   }
/*     */   
/*     */   public HashSet<String> getTags() {
/* 395 */     return this.tags;
/*     */   }
/*     */   
/*     */   public void setTags(HashSet<String> tags) {
/* 399 */     this.tags = tags;
/*     */   }
/*     */   
/*     */   public ArrayList<Attribute> getAttributes() {
/* 403 */     return this.attributes;
/*     */   }
/*     */   
/*     */   public void setAttributes(ArrayList<Attribute> attributes) {
/* 407 */     this.attributes = attributes;
/*     */   }
/*     */   
/*     */   public String getPlatformVersion() {
/* 411 */     return this.platformVersion;
/*     */   }
/*     */   
/*     */   public void setPlatformVersion(String platformVersion) {
/* 415 */     this.platformVersion = platformVersion;
/*     */   }
/*     */   
/*     */   public String getDeviceToken() {
/* 419 */     return this.deviceToken != null ? this.deviceToken : "";
/*     */   }
/*     */   
/*     */   public void setDeviceToken(String deviceToken) {
/* 423 */     this.deviceToken = deviceToken;
/*     */   }
/*     */   
/*     */   public String getSubscriberKey() {
/* 427 */     return (this.subscriberKey != null) && (!this.subscriberKey.isEmpty()) ? this.subscriberKey : this.deviceId;
/*     */   }
/*     */   
/*     */   public void setSubscriberKey(String subscriberKey) {
/* 431 */     this.subscriberKey = subscriberKey;
/* 432 */     if (this.prefs != null) {
/* 433 */       this.prefs.edit().putString("et_subscriber_cache", this.subscriberKey).commit();
/*     */     }
/*     */   }
/*     */   
/*     */   public Boolean getPushEnabled() {
/* 438 */     return this.pushEnabled;
/*     */   }
/*     */   
/*     */   public void setPushEnabled(Boolean pushEnabled) {
/* 442 */     this.pushEnabled = pushEnabled;
/*     */   }
/*     */   
/*     */   public Boolean getLocationEnabled() {
/* 446 */     return this.locationEnabled;
/*     */   }
/*     */   
/*     */   public void setLocationEnabled(Boolean locationEnabled) {
/* 450 */     this.locationEnabled = locationEnabled;
/*     */   }
/*     */   
/*     */   public Long getLastSent() {
/* 454 */     return this.lastSent;
/*     */   }
/*     */   
/*     */   public void setLastSent(Long lastSent) {
/* 458 */     this.lastSent = lastSent;
/*     */   }
/*     */   
/*     */   public String getHwid() {
/* 462 */     return this.hwid;
/*     */   }
/*     */   
/*     */   public void setHwid(String hwid) {
/* 466 */     this.hwid = hwid;
/*     */   }
/*     */   
/*     */   public String getGcmSenderId() {
/* 470 */     return this.gcmSenderId;
/*     */   }
/*     */   
/*     */   public void setGcmSenderId(String gcmSenderId) {
/* 474 */     this.gcmSenderId = gcmSenderId;
/*     */   }
/*     */   
/*     */   public String getLocale()
/*     */   {
/* 479 */     if ((this.locale == null) && 
/* 480 */       (this.language != null)) {
/* 481 */       return this.language;
/*     */     }
/*     */     
/* 484 */     return this.locale;
/*     */   }
/*     */   
/*     */   public void setLocale(String locale) {
/* 488 */     this.locale = locale;
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\Registration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */