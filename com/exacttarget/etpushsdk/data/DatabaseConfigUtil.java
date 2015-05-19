/*    */ package com.exacttarget.etpushsdk.data;
/*    */ 
/*    */ import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
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
/*    */ public class DatabaseConfigUtil
/*    */   extends OrmLiteConfigUtil
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 26 */       System.out.println("Starting DatabaseConfigUtil...");
/* 27 */       System.out.println("ClassPath: " + System.getProperty("java.class.path"));
/*    */       
/* 29 */       File configFile = new File(args[0]);
/* 30 */       Class<?>[] classes = new Class[args.length - 1];
/* 31 */       for (int i = 1; i < args.length; i++) {
/* 32 */         String className = args[i];
/* 33 */         System.out.println("Generating DB Config for: " + className);
/* 34 */         classes[(i - 1)] = Class.forName(className);
/*    */       }
/*    */       
/* 37 */       writeConfigFile(configFile, classes);
/*    */     }
/*    */     catch (Throwable e) {
/* 40 */       System.err.println("ERROR Generating ORMLite config");
/* 41 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\DatabaseConfigUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */