/*     */ package com.exacttarget.etpushsdk;
/*     */ 
/*     */ import android.app.IntentService;
/*     */ import android.content.Context;
/*     */ import android.content.Intent;
/*     */ import android.net.ConnectivityManager;
/*     */ import android.net.NetworkInfo;
/*     */ import android.os.SystemClock;
/*     */ import android.support.v4.content.WakefulBroadcastReceiver;
/*     */ import android.util.Base64;
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.event.AnalyticPiItemEvent;
/*     */ import com.exacttarget.etpushsdk.event.GeofenceResponseEvent;
/*     */ import com.exacttarget.etpushsdk.util.EventBus;
/*     */ import com.exacttarget.etpushsdk.util.JSONUtil;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.HttpClient;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ import org.apache.http.impl.client.DefaultHttpClient;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ import org.json.JSONObject;
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
/*     */ public class ETSendDataIntentService
/*     */   extends IntentService
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@ETSendDataIntentService";
/*     */   public static final String PARAM_DATABASE_ID = "param_database_id";
/*     */   public static final String PARAM_DATABASE_IDS = "param_database_ids";
/*     */   public static final String PARAM_HTTP_METHOD = "param_http_method";
/*     */   public static final String PARAM_HTTP_URL = "param_http_url";
/*     */   public static final String PARAM_HTTP_RESPONSE_TYPE = "param_http_response_type";
/*     */   public static final String PARAM_DATA_JSON = "param_data_json";
/*     */   public static final String PARAM_BASIC_USER = "param_basic_user";
/*     */   public static final String PARAM_BASIC_PASS = "param_basic_pass";
/*     */   public static final String PARAM_AUTH_TOKEN = "param_auth_token";
/*     */   
/*     */   public ETSendDataIntentService()
/*     */   {
/*  79 */     super("ETSendDataIntentService");
/*     */   }
/*     */   
/*     */   protected void onHandleIntent(Intent intent)
/*     */   {
/*  84 */     if (ETPush.getLogLevel() <= 3) {
/*  85 */       Log.d("jb4ASDK@ETSendDataIntentService", "onHandleIntent()");
/*     */     }
/*     */     
/*  88 */     Integer databaseId = Integer.valueOf(intent.getIntExtra("param_database_id", -1));
/*     */     
/*     */ 
/*  91 */     List<Integer> databaseIds = (List)intent.getSerializableExtra("param_database_ids");
/*     */     
/*  93 */     String httpMethod = intent.getStringExtra("param_http_method");
/*  94 */     String httpUrl = intent.getStringExtra("param_http_url");
/*  95 */     String httpResponseType = intent.getStringExtra("param_http_response_type");
/*  96 */     String dataJson = intent.getStringExtra("param_data_json");
/*  97 */     String basicUser = intent.getStringExtra("param_basic_user");
/*  98 */     String basicPass = intent.getStringExtra("param_basic_pass");
/*  99 */     String authToken = intent.getStringExtra("param_auth_token");
/* 100 */     Context applicationContext = getApplicationContext();
/* 101 */     if (isOnline(applicationContext)) {
/* 102 */       sendUpdate(applicationContext, httpMethod, httpUrl, httpResponseType, basicUser, basicPass, authToken, databaseId, databaseIds, dataJson);
/*     */     }
/* 104 */     else if (ETPush.getLogLevel() <= 5) {
/* 105 */       Log.w("jb4ASDK@ETSendDataIntentService", "SendUpdate: Network not available");
/*     */     }
/*     */     
/*     */ 
/* 109 */     WakefulBroadcastReceiver.completeWakefulIntent(intent);
/*     */   }
/*     */   
/*     */   private boolean isOnline(Context c) {
/* 113 */     boolean isConnected = false;
/* 114 */     ConnectivityManager cm = (ConnectivityManager)c.getSystemService("connectivity");
/* 115 */     NetworkInfo netInfo = cm.getActiveNetworkInfo();
/* 116 */     if ((netInfo != null) && (netInfo.isConnected())) {
/* 117 */       isConnected = true;
/*     */     }
/* 119 */     return isConnected;
/*     */   }
/*     */   
/*     */   private void sendUpdate(Context applicationContext, String httpMethod, String in_httpUrl, String httpResponseType, String basicUser, String basicPass, String authToken, Integer databaseId, List<Integer> databaseIds, String httpData)
/*     */   {
/* 124 */     int sCode = 0;
/*     */     try {
/* 126 */       if (ETPush.getLogLevel() <= 3) {
/* 127 */         Log.d("jb4ASDK@ETSendDataIntentService", "Sending data...");
/*     */       }
/*     */       
/*     */ 
/* 131 */       String httpUrl = in_httpUrl.replaceAll("\\{et_app_id\\}", Config.getEtAppId()).replaceAll("\\{access_token\\}", Config.getAccessToken());
/*     */       
/*     */ 
/*     */ 
/* 135 */       HttpClient httpClient = new DefaultHttpClient();
/*     */       
/* 137 */       HttpUriRequest etRequest = null;
/* 138 */       if ("GET".equals(httpMethod))
/*     */       {
/* 140 */         etRequest = new HttpGet(httpUrl);
/*     */       }
/* 142 */       else if ("POST".equals(httpMethod))
/*     */       {
/* 144 */         etRequest = new HttpPost(httpUrl);
/* 145 */         ((HttpPost)etRequest).setEntity(new StringEntity(httpData));
/* 146 */         etRequest.setHeader("Content-type", "application/json");
/*     */       }
/* 148 */       else if ("PUT".equals(httpMethod)) {
/* 149 */         etRequest = new HttpPut(httpUrl);
/* 150 */         ((HttpPut)etRequest).setEntity(new StringEntity(httpData));
/* 151 */         etRequest.setHeader("Content-type", "application/json");
/*     */       }
/*     */       else {
/* 154 */         throw new ETException("Invalid Request Method: " + httpMethod + ", only GET, POST, PUT supported.");
/*     */       }
/*     */       
/* 157 */       if ((basicUser != null) && (basicPass != null) && (!basicUser.isEmpty()) && (!basicPass.isEmpty())) {
/* 158 */         String creds = "Basic " + Base64.encodeToString(new String(new StringBuilder().append(basicUser).append(':').append(basicPass).toString()).getBytes(), 2);
/*     */         
/* 160 */         etRequest.setHeader("Authorization", creds);
/*     */       }
/* 162 */       else if ((authToken != null) && (!authToken.isEmpty())) {
/* 163 */         String creds = "Token token=\"" + authToken + "\"";
/*     */         
/* 165 */         etRequest.setHeader("Authorization", creds);
/*     */       }
/*     */       
/* 168 */       etRequest.setHeader("Accept", "application/json");
/* 169 */       etRequest.setHeader("User-Agent", generateUserAgent());
/* 170 */       etRequest.setHeader("X-ET-TOKEN", Config.getAccessToken());
/*     */       
/* 172 */       if (ETPush.getLogLevel() <= 3) {
/* 173 */         Log.d("jb4ASDK@ETSendDataIntentService", "Request Url: " + in_httpUrl);
/* 174 */         Log.d("jb4ASDK@ETSendDataIntentService", "Request data: " + httpData);
/*     */       }
/* 176 */       long start = SystemClock.elapsedRealtime();
/* 177 */       HttpResponse etPostResponse = httpClient.execute(etRequest);
/* 178 */       if (ETPush.getLogLevel() <= 3) {
/* 179 */         Log.d("jb4ASDK@ETSendDataIntentService", "Request took: " + (SystemClock.elapsedRealtime() - start) + "ms");
/*     */       }
/* 181 */       sCode = etPostResponse.getStatusLine().getStatusCode();
/*     */       
/* 183 */       if ((sCode >= 200) && (sCode <= 299)) {
/* 184 */         if (ETPush.getLogLevel() <= 3) {
/* 185 */           Log.d("jb4ASDK@ETSendDataIntentService", "Success with StatusCode: " + String.valueOf(sCode));
/*     */         }
/*     */       }
/* 188 */       else if ((sCode >= 400) && (sCode <= 499)) {
/* 189 */         if (ETPush.getLogLevel() <= 5) {
/* 190 */           Log.w("jb4ASDK@ETSendDataIntentService", "Warning with StatusCode: " + String.valueOf(sCode));
/* 191 */           if (sCode == 402) {
/* 192 */             Log.w("jb4ASDK@ETSendDataIntentService", "You are attempting to use a feature that is not enabled in your account. If you believe this is incorrect, please contact Global Support.");
/*     */           }
/*     */           else {
/* 195 */             Log.w("jb4ASDK@ETSendDataIntentService", "A client error occurred while communicating with ExactTarget. Please verify that you have everything configured correctly.");
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 200 */       else if (ETPush.getLogLevel() <= 6) {
/* 201 */         Log.e("jb4ASDK@ETSendDataIntentService", "Error with StatusCode: " + String.valueOf(sCode));
/*     */       }
/*     */       
/*     */ 
/* 205 */       HttpEntity etResponseEntity = etPostResponse.getEntity();
/* 206 */       String jsonResponse = null;
/* 207 */       if (etResponseEntity != null) {
/* 208 */         jsonResponse = EntityUtils.toString(etResponseEntity);
/* 209 */         if ((sCode >= 200) && (sCode <= 299)) {
/* 210 */           if (ETPush.getLogLevel() <= 3) {
/* 211 */             Log.d("jb4ASDK@ETSendDataIntentService", "Success Response: " + jsonResponse);
/*     */           }
/*     */         }
/* 214 */         else if ((sCode >= 400) && (sCode <= 499)) {
/* 215 */           if (ETPush.getLogLevel() <= 5) {
/* 216 */             Log.w("jb4ASDK@ETSendDataIntentService", "Warning Response: " + jsonResponse);
/*     */           }
/*     */         }
/*     */         else {
/* 220 */           if (ETPush.getLogLevel() <= 6) {
/* 221 */             Log.e("jb4ASDK@ETSendDataIntentService", "Error Response: " + jsonResponse);
/*     */           }
/*     */           
/*     */ 
/* 225 */           if (!httpResponseType.equals(GeofenceResponseEvent.class.getName()))
/*     */           {
/* 227 */             databaseId = Integer.valueOf(0);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 235 */       if ((httpResponseType != null) && (!httpResponseType.isEmpty())) {
/* 236 */         Object responseObject = null;
/*     */         
/* 238 */         if (httpResponseType.equals("pi_analytics")) {
/*     */           try {
/* 240 */             JSONObject piResponseJSON = new JSONObject(jsonResponse);
/* 241 */             AnalyticPiItemEvent event = new AnalyticPiItemEvent();
/*     */             
/* 243 */             event.setUserId(piResponseJSON.getString("user_id"));
/* 244 */             event.setSessionId(piResponseJSON.getString("session_id"));
/* 245 */             event.setDatabaseIds(databaseIds);
/* 246 */             responseObject = event;
/*     */           }
/*     */           catch (Exception e) {
/* 249 */             if (ETPush.getLogLevel() <= 6) {
/* 250 */               Log.e("jb4ASDK@ETSendDataIntentService", e.getMessage());
/*     */             }
/* 252 */             responseObject = null;
/*     */           }
/*     */         }
/*     */         else {
/* 256 */           responseObject = JSONUtil.jsonToObject(jsonResponse, Class.forName(httpResponseType));
/* 257 */           if (responseObject == null)
/*     */           {
/*     */ 
/* 260 */             responseObject = Class.forName(httpResponseType).newInstance();
/* 261 */             if ((databaseIds != null) && (!databaseIds.isEmpty())) {
/* 262 */               Method setDatabaseIds = responseObject.getClass().getMethod("setDatabaseIds", new Class[] { List.class });
/* 263 */               if (setDatabaseIds != null) {
/* 264 */                 setDatabaseIds.invoke(responseObject, new Object[] { databaseIds });
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 270 */           Method setId = null;
/*     */           
/* 272 */           if ((databaseIds != null) && (!databaseIds.isEmpty())) {
/* 273 */             if (!((ArrayList)responseObject).isEmpty()) {
/* 274 */               for (int i = 0; i < databaseIds.size(); i++) {
/* 275 */                 Object responseObjectItem = ((ArrayList)responseObject).get(i);
/* 276 */                 if (setId == null) {
/* 277 */                   setId = responseObjectItem.getClass().getMethod("setId", new Class[] { Integer.class });
/*     */                 }
/* 279 */                 setId.invoke(responseObjectItem, new Object[] { databaseIds.get(i) });
/*     */               }
/*     */             }
/* 282 */           } else if (databaseId.intValue() > 0) {
/* 283 */             setId = responseObject.getClass().getMethod("setId", new Class[] { Integer.class });
/* 284 */             setId.invoke(responseObject, new Object[] { databaseId });
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 289 */         if (responseObject != null) {
/* 290 */           EventBus.getDefault().post(responseObject);
/*     */         }
/*     */       }
/*     */       
/* 294 */       if (ETPush.getLogLevel() <= 3) {
/* 295 */         Log.d("jb4ASDK@ETSendDataIntentService", "Sending data done.");
/*     */       }
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/* 300 */       if (ETPush.getLogLevel() <= 6) {
/* 301 */         Log.e("jb4ASDK@ETSendDataIntentService", e.getMessage(), e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private String generateUserAgent() {
/* 307 */     return "ETPushSDK/" + ETPush.getSDKVersionName(getApplicationContext()) + " (Android)";
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETSendDataIntentService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */