/*    */ package com.exacttarget.etpushsdk.util;
/*    */ 
/*    */ import android.graphics.Bitmap;
/*    */ import android.util.Log;
/*    */ import com.exacttarget.etpushsdk.ETPush;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class BitmapUtil
/*    */ {
/*    */   private static final String TAG = "jb4ASDK@BitmapUtil";
/*    */   
/*    */   public static Bitmap getBitmapFromURL(String src)
/*    */   {
/*    */     try
/*    */     {
/* 19 */       URL url = new URL(src);
/* 20 */       HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/* 21 */       connection.setDoInput(true);
/* 22 */       connection.connect();
/* 23 */       InputStream input = connection.getInputStream();
/* 24 */       return android.graphics.BitmapFactory.decodeStream(input);
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 28 */       if (ETPush.getLogLevel() <= 6)
/* 29 */         Log.e("jb4ASDK@BitmapUtil", e.getMessage(), e);
/*    */     }
/* 31 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\util\BitmapUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */