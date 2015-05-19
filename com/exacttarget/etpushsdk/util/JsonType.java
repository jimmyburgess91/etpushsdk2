/*    */ package com.exacttarget.etpushsdk.util;
/*    */ 
/*    */ import com.j256.ormlite.field.FieldType;
/*    */ import com.j256.ormlite.field.types.StringType;
/*    */ import java.lang.reflect.Field;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ public class JsonType
/*    */   extends StringType
/*    */ {
/*    */   private static JsonType singleton;
/*    */   
/* 14 */   public JsonType() { super(com.j256.ormlite.field.SqlType.STRING, new Class[] { String.class }); }
/*    */   
/*    */   public static JsonType getSingleton() {
/* 17 */     if (singleton == null) {
/* 18 */       singleton = new JsonType();
/*    */     }
/* 20 */     return singleton;
/*    */   }
/*    */   
/*    */   public boolean isValidForField(Field field) {
/* 24 */     return java.util.Collection.class.isAssignableFrom(field.getType());
/*    */   }
/*    */   
/*    */   public Object javaToSqlArg(FieldType fieldType, Object obj) throws SQLException
/*    */   {
/* 29 */     Object sqlArg = null;
/* 30 */     if ("java.lang.String".equals(obj.getClass().getName())) {
/* 31 */       sqlArg = super.javaToSqlArg(fieldType, obj);
/*    */     }
/*    */     else {
/* 34 */       String json = JSONUtil.objectToJson(obj);
/* 35 */       sqlArg = super.javaToSqlArg(fieldType, json);
/*    */     }
/*    */     
/* 38 */     return sqlArg;
/*    */   }
/*    */   
/*    */   public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException
/*    */   {
/* 43 */     String json = (String)super.sqlArgToJava(fieldType, sqlArg, columnPos);
/* 44 */     return JSONUtil.jsonToObject(json, Object.class);
/*    */   }
/*    */   
/*    */   public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos) throws SQLException
/*    */   {
/* 49 */     String json = (String)super.resultStringToJava(fieldType, stringValue, columnPos);
/* 50 */     return JSONUtil.jsonToObject(json, Object.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\util\JsonType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */