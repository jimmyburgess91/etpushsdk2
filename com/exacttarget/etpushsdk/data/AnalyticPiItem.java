/*     */ package com.exacttarget.etpushsdk.data;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.ETPush;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnalyticPiItem
/*     */ {
/*  17 */   private String api_key = null;
/*  18 */   private String app_id = null;
/*  19 */   private String user_id = null;
/*  20 */   private String session_id = null;
/*  21 */   private String app_name = null;
/*  22 */   private String device_id = null;
/*  23 */   private Boolean push_enabled = null;
/*  24 */   private Double latitude = null;
/*  25 */   private Double longitude = null;
/*  26 */   private String manufacturer = null;
/*  27 */   private String platform = null;
/*  28 */   private String platform_version = null;
/*  29 */   private String device_type = null;
/*     */   private ArrayList<JSON_Event> mEvents;
/*     */   private static final String JF_API_KEY = "api_key";
/*     */   
/*  33 */   private class JSON_Event { public String api_endpoint = "";
/*  34 */     public Date timestamp = null;
/*  35 */     public String event_name = "";
/*  36 */     public Boolean open_from_push = null;
/*     */     
/*     */ 
/*     */     private JSON_Event() {}
/*     */   }
/*     */   
/*     */ 
/*     */   private static final String JF_APP_ID = "app_id";
/*     */   
/*     */   public static final String JF_USER_ID = "user_id";
/*     */   public static final String JF_SESSION_ID = "session_id";
/*     */   private static final String JF_PAYLOAD = "payload";
/*     */   private static final String JF_APP_NAME = "app_name";
/*     */   private static final String JF_USER_INFO = "user_info";
/*     */   private static final String JF_DEVICE_ID = "device_id";
/*     */   private static final String JF_PUSH_ENABLED = "push_enabled";
/*     */   private static final String JF_LOCATION = "location";
/*     */   private static final String JF_LATITUDE = "latitude";
/*     */   private static final String JF_LONGITUDE = "longitude";
/*     */   private static final String JF_DEVICE = "device";
/*     */   private static final String JF_MANUFACTURER = "manufacturer";
/*     */   private static final String JF_PLATFORM = "platform";
/*     */   private static final String JF_PLATFORM_VERSION = "platform_version";
/*     */   private static final String JF_DEVICE_TYPE = "device_type";
/*     */   private static final String JF_EVENTS = "events";
/*     */   private static final String JF_API_ENDPOINT = "api_endpoint";
/*     */   private static final String JF_TIMESTAMP = "timestamp";
/*     */   private static final String JF_EVENT_NAME = "event_name";
/*     */   private static final String JF_DETAILS = "details";
/*     */   private static final String JF_OPEN_FROM_PUSH = "open_from_push";
/*     */   private static final String API_ENDPOINT_TRACK_EVENT = "track_event";
/*     */   private static final String EVENT_APP_OPEN = "app_open";
/*     */   private static final String EVENT_APP_CLOSE = "app_close";
/*     */   public static final String ET_USER_ID_CACHE = "et_user_id_cache";
/*     */   public static final String ET_SESSION_ID_CACHE = "et_session_id_cache";
/*     */   public static final String HTTP_RESPONSE_TYPE = "pi_analytics";
/*     */   public static final String MOBILE_TEAM_API_KEY = "849f26e2-2df6-11e4-ab12-14109fdc48df";
/*     */   public static final int SESSION_TIMEOUT_MINUTES = 30;
/*     */   private Context applicationContext;
/*     */   private static final String TAG = "jb4ASDK@AnalyticPIItem";
/*     */   public AnalyticPiItem(Context context)
/*     */   {
/*  78 */     this.applicationContext = context;
/*  79 */     this.app_name = this.applicationContext.getPackageName();
/*  80 */     this.mEvents = new ArrayList();
/*     */   }
/*     */   
/*     */   public String getApiKey() {
/*  84 */     return this.api_key;
/*     */   }
/*     */   
/*     */   public void setApiKey(String api_key) {
/*  88 */     this.api_key = api_key;
/*     */   }
/*     */   
/*     */   public String getAppId() {
/*  92 */     return this.app_id;
/*     */   }
/*     */   
/*     */   public void setAppId(String app_id) {
/*  96 */     this.app_id = app_id;
/*     */   }
/*     */   
/*     */   public String getUserId() {
/* 100 */     return this.user_id;
/*     */   }
/*     */   
/*     */   public void setUserId(String user_id) {
/* 104 */     this.user_id = user_id;
/*     */   }
/*     */   
/*     */   public String getSessionId() {
/* 108 */     return this.session_id;
/*     */   }
/*     */   
/*     */   public void setSessionId(String session_id) {
/* 112 */     this.session_id = session_id;
/*     */   }
/*     */   
/*     */   public String getDeviceId() {
/* 116 */     return this.device_id;
/*     */   }
/*     */   
/*     */   public String setDeviceId(String device_id) {
/* 120 */     return this.device_id = device_id;
/*     */   }
/*     */   
/*     */   public Boolean getPushEnabled() {
/* 124 */     return this.push_enabled;
/*     */   }
/*     */   
/*     */   public void setPushEnabled(Boolean push_enabled) {
/* 128 */     this.push_enabled = push_enabled;
/*     */   }
/*     */   
/*     */   public String getManufacturer() {
/* 132 */     return this.manufacturer;
/*     */   }
/*     */   
/*     */   public void setManufacturer(String manufacturer) {
/* 136 */     this.manufacturer = manufacturer;
/*     */   }
/*     */   
/*     */   public String getPlatform() {
/* 140 */     return this.platform;
/*     */   }
/*     */   
/*     */   public void setPlatform(String platform) {
/* 144 */     this.platform = platform;
/*     */   }
/*     */   
/*     */   public String getPlatformVersion() {
/* 148 */     return this.platform_version;
/*     */   }
/*     */   
/*     */   public void setPlatformVersion(String platform_version) {
/* 152 */     this.platform_version = platform_version;
/*     */   }
/*     */   
/*     */   public String getDeviceType() {
/* 156 */     return this.device_type;
/*     */   }
/*     */   
/*     */   public void setDeviceType(String device_type) {
/* 160 */     this.device_type = device_type;
/*     */   }
/*     */   
/*     */   public double getLatitude() {
/* 164 */     return this.latitude.doubleValue();
/*     */   }
/*     */   
/*     */   public void setLatitude(double latitude) {
/* 168 */     this.latitude = Double.valueOf(latitude);
/*     */   }
/*     */   
/*     */   public double getLongitude() {
/* 172 */     return this.longitude.doubleValue();
/*     */   }
/*     */   
/*     */   public void setLongitude(double longitude) {
/* 176 */     this.longitude = Double.valueOf(longitude);
/*     */   }
/*     */   
/*     */   public void addTimeInApp(Date openTime, Date closeTime, boolean fromPush) {
/* 180 */     addOpenEvent(openTime, fromPush);
/* 181 */     addCloseEvent(closeTime);
/*     */   }
/*     */   
/*     */   private void addOpenEvent(Date openTime, boolean fromPush) {
/* 185 */     JSON_Event event = new JSON_Event(null);
/* 186 */     event.api_endpoint = "track_event";
/* 187 */     event.event_name = "app_open";
/* 188 */     event.timestamp = new Date(openTime.getTime());
/* 189 */     event.open_from_push = Boolean.valueOf(fromPush);
/*     */     
/* 191 */     this.mEvents.add(event);
/*     */   }
/*     */   
/*     */   private void addCloseEvent(Date closeTime) {
/* 195 */     JSON_Event event = new JSON_Event(null);
/* 196 */     event.api_endpoint = "track_event";
/* 197 */     event.event_name = "app_close";
/* 198 */     event.timestamp = new Date(closeTime.getTime());
/*     */     
/* 200 */     this.mEvents.add(event);
/*     */   }
/*     */   
/*     */   public JSONObject getJSONPayload() {
/* 204 */     JSONObject analyticPayload = new JSONObject();
/*     */     try
/*     */     {
/* 207 */       analyticPayload.put("api_key", this.api_key);
/* 208 */       analyticPayload.put("app_id", this.app_id);
/*     */       
/* 210 */       if (!this.user_id.equals("")) {
/* 211 */         analyticPayload.put("user_id", this.user_id);
/*     */       }
/*     */       
/* 214 */       if (!this.session_id.equals("")) {
/* 215 */         analyticPayload.put("session_id", this.session_id);
/*     */       }
/*     */       
/* 218 */       JSONObject payload = new JSONObject();
/* 219 */       payload.put("app_name", this.app_name);
/*     */       
/*     */ 
/* 222 */       JSONObject userInfo = new JSONObject();
/* 223 */       userInfo.put("device_id", this.device_id);
/*     */       
/*     */ 
/* 226 */       JSONObject details = new JSONObject();
/* 227 */       details.put("push_enabled", this.push_enabled);
/* 228 */       userInfo.put("details", details);
/*     */       
/* 230 */       if ((this.latitude != null) && (this.longitude != null))
/*     */       {
/* 232 */         JSONObject location = new JSONObject();
/* 233 */         location.put("latitude", this.latitude);
/* 234 */         location.put("longitude", this.longitude);
/* 235 */         userInfo.put("location", location);
/*     */       }
/*     */       
/*     */ 
/* 239 */       JSONObject device = new JSONObject();
/* 240 */       device.put("manufacturer", this.manufacturer);
/* 241 */       device.put("platform", this.platform);
/* 242 */       device.put("platform_version", this.platform_version);
/* 243 */       device.put("device_type", this.device_type);
/* 244 */       userInfo.put("device", device);
/*     */       
/* 246 */       payload.put("user_info", userInfo);
/*     */       
/*     */ 
/* 249 */       JSONArray events = new JSONArray();
/*     */       
/* 251 */       SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
/* 252 */       formatDate.setTimeZone(TimeZone.getTimeZone("UTC"));
/* 253 */       for (JSON_Event analyticEvent : this.mEvents) {
/* 254 */         JSONObject event = new JSONObject();
/* 255 */         event.put("api_endpoint", analyticEvent.api_endpoint);
/* 256 */         event.put("timestamp", formatDate.format(analyticEvent.timestamp));
/* 257 */         event.put("event_name", analyticEvent.event_name);
/*     */         
/* 259 */         if (analyticEvent.event_name.equals("app_open")) {
/* 260 */           details = new JSONObject();
/* 261 */           details.put("open_from_push", analyticEvent.open_from_push);
/* 262 */           event.put("details", details);
/*     */         }
/*     */         
/* 265 */         events.put(event);
/*     */       }
/*     */       
/* 268 */       payload.put("events", events);
/*     */       
/* 270 */       analyticPayload.put("payload", payload);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 274 */       if (ETPush.getLogLevel() <= 6) {
/* 275 */         Log.e("jb4ASDK@AnalyticPIItem", e.getMessage());
/* 276 */         analyticPayload = null;
/*     */       }
/*     */     }
/*     */     
/* 280 */     return analyticPayload;
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\AnalyticPiItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */