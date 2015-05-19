/*    */ package com.exacttarget.etpushsdk.util;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.os.Build;
/*    */ import android.util.Log;
/*    */ import com.exacttarget.etpushsdk.ETException;
/*    */ import com.exacttarget.etpushsdk.ETPush;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ 
/*    */ public class ETAmazonDeviceMessagingUtil
/*    */ {
/* 13 */   private static Boolean admAvailable = Boolean.valueOf(false);
/*    */   private static final String TAG = "jb4ASDK@ETAmazonDeviceMessagingUtil";
/*    */   
/*    */   public static boolean isAvailable(Context applicationContext, boolean enablingPush)
/*    */   {
/* 18 */     if (!isAmazonDevice())
/*    */     {
/* 20 */       admAvailable = Boolean.valueOf(false);
/*    */     }
/*    */     else {
/*    */       try {
/* 24 */         Class.forName("com.amazon.device.messaging.ADM");
/* 25 */         ADMManifest_checkManifestAuthoredProperly(applicationContext);
/* 26 */         admAvailable = Boolean.TRUE;
/*    */       }
/*    */       catch (ClassNotFoundException e) {
/* 29 */         if (ETPush.getLogLevel() <= 3) {
/* 30 */           Log.e("jb4ASDK@ETAmazonDeviceMessagingUtil", "Amazon ADM API's not found.");
/*    */         }
/* 32 */         admAvailable = Boolean.FALSE;
/*    */       }
/*    */       catch (Exception e) {
/* 35 */         if (ETPush.getLogLevel() <= 6) {
/* 36 */           Log.e("jb4ASDK@ETAmazonDeviceMessagingUtil", e.getMessage());
/*    */         }
/* 38 */         admAvailable = Boolean.FALSE;
/*    */       }
/*    */       
/* 41 */       if (!admAvailable.booleanValue()) {
/* 42 */         ETGooglePlayServicesUtil.showErrorNotification(applicationContext, "Amazon Device Messaging not available.", false, enablingPush);
/*    */       }
/*    */     }
/*    */     
/* 46 */     return admAvailable.booleanValue();
/*    */   }
/*    */   
/*    */   public static boolean isAmazonDevice() {
/* 50 */     return Build.MANUFACTURER.equals("Amazon");
/*    */   }
/*    */   
/*    */   private static void ADMManifest_checkManifestAuthoredProperly(Context applicationContext) throws ETException {
/*    */     try {
/* 55 */       Class<?> clazz = Class.forName("com.amazon.device.messaging.development.ADMManifest");
/* 56 */       Method m = clazz.getDeclaredMethod("checkManifestAuthoredProperly", new Class[] { Context.class });
/* 57 */       m.invoke(null, new Object[] { applicationContext });
/*    */     }
/*    */     catch (ClassNotFoundException e) {
/* 60 */       throw new ETException("jb4ASDK@ETAmazonDeviceMessagingUtil unable to find com.amazon.device.messaging.development.ADMManifest");
/*    */     }
/*    */     catch (Exception e) {
/* 63 */       throw new ETException(e.getCause().getMessage());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\util\ETAmazonDeviceMessagingUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */