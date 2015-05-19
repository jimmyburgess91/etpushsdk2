/*    */ package com.exacttarget.etpushsdk.data;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.provider.Settings.Secure;
/*    */ import android.util.Log;
/*    */ import com.exacttarget.etpushsdk.ETPush;
/*    */ import java.math.BigInteger;
/*    */ import java.security.MessageDigest;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeviceData
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@DeviceData";
/* 29 */   private static String hashedId = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String uniqueDeviceIdentifier(Context context)
/*    */   {
/* 37 */     if (hashedId == null) {
/* 38 */       hashedId = "";
/*    */       try {
/* 40 */         String preHashString = Settings.Secure.getString(context.getContentResolver(), "android_id") + "-" + context.getPackageName();
/* 41 */         hashedId = md5(preHashString);
/*    */       }
/*    */       catch (Throwable e) {
/* 44 */         if (ETPush.getLogLevel() <= 6) {
/* 45 */           Log.e("jb4ASDK@DeviceData", e.getMessage(), e);
/*    */         }
/*    */       }
/*    */     }
/* 49 */     return hashedId;
/*    */   }
/*    */   
/*    */   private String md5(String id) {
/* 53 */     String idHash = "";
/*    */     try {
/* 55 */       MessageDigest idDigest = MessageDigest.getInstance("MD5");
/* 56 */       byte[] idBytes = id.getBytes();
/* 57 */       idDigest.update(idBytes, 0, idBytes.length);
/* 58 */       idHash = new BigInteger(1, idDigest.digest()).toString(16);
/*    */     }
/*    */     catch (Throwable e) {
/* 61 */       if (ETPush.getLogLevel() <= 6) {
/* 62 */         Log.e("jb4ASDK@DeviceData", e.getMessage(), e);
/*    */       }
/*    */     }
/* 65 */     return idHash;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\DeviceData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */