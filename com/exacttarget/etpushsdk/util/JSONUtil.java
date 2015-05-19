/*     */ package com.exacttarget.etpushsdk.util;
/*     */ 
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.ETPush;
/*     */ import com.exacttarget.etpushsdk.event.ServerErrorEvent;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import java.io.IOException;
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
/*     */ public class JSONUtil
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@JSONUtil";
/*  35 */   private static final ObjectMapper mapper = new ObjectMapper();
/*     */   
/*     */   public static <T> T jsonToObject(String json, Class<T> clazz) {
/*  38 */     Object response = null;
/*     */     try {
/*  40 */       response = mapper.readValue(json, clazz);
/*     */     }
/*     */     catch (Throwable e) {
/*  43 */       Log.e("jb4ASDK@JSONUtil", e.getMessage(), e);
/*     */       try
/*     */       {
/*  46 */         ServerErrorEvent errorMessage = (ServerErrorEvent)mapper.readValue(json, ServerErrorEvent.class);
/*  47 */         if (ETPush.getLogLevel() <= 6) {
/*  48 */           Log.e("jb4ASDK@JSONUtil", "SERVER ERROR: " + errorMessage.getMessage());
/*     */         }
/*  50 */         EventBus.getDefault().post(errorMessage);
/*     */       }
/*     */       catch (Throwable e1)
/*     */       {
/*     */         try {
/*  55 */           List<ServerErrorEvent> errorMessages = (List)mapper.readValue(json, new TypeReference() {});
/*  56 */           for (ServerErrorEvent errorMessage : errorMessages) {
/*  57 */             if (ETPush.getLogLevel() <= 6) {
/*  58 */               Log.e("jb4ASDK@JSONUtil", "SERVER ERROR: " + errorMessage.getMessage());
/*     */             }
/*  60 */             EventBus.getDefault().post(errorMessage);
/*     */           }
/*     */         }
/*     */         catch (Throwable e2) {
/*  64 */           if (ETPush.getLogLevel() <= 6) {
/*  65 */             Log.e("jb4ASDK@JSONUtil", e2.getMessage(), e2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  71 */     return (T)clazz.cast(response);
/*     */   }
/*     */   
/*     */   public static String objectToJson(Object jsonObject) {
/*  75 */     String response = null;
/*     */     try {
/*  77 */       response = mapper.writeValueAsString(jsonObject);
/*     */     }
/*     */     catch (JsonProcessingException e) {
/*  80 */       if (ETPush.getLogLevel() <= 6) {
/*  81 */         Log.e("jb4ASDK@JSONUtil", e.getMessage(), e);
/*     */       }
/*     */     } catch (IOException e) {
/*  84 */       if (ETPush.getLogLevel() <= 6) {
/*  85 */         Log.e("jb4ASDK@JSONUtil", e.getMessage(), e);
/*     */       }
/*     */     }
/*  88 */     return response;
/*     */   }
/*     */   
/*     */   public static String jsonMessToPrettyString(String json) {
/*     */     try {
/*  93 */       return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(mapper.readValue(json, Object.class));
/*     */     }
/*     */     catch (JsonParseException e) {
/*  96 */       if (ETPush.getLogLevel() <= 6) {
/*  97 */         Log.e("jb4ASDK@JSONUtil", e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     catch (JsonMappingException e) {
/* 101 */       if (ETPush.getLogLevel() <= 6) {
/* 102 */         Log.e("jb4ASDK@JSONUtil", e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     catch (JsonProcessingException e) {
/* 106 */       if (ETPush.getLogLevel() <= 6) {
/* 107 */         Log.e("jb4ASDK@JSONUtil", e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 111 */       if (ETPush.getLogLevel() <= 6) {
/* 112 */         Log.e("jb4ASDK@JSONUtil", e.getMessage(), e);
/*     */       }
/*     */     }
/* 115 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\util\JSONUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */