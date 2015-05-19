/*    */ package com.exacttarget.etpushsdk.data;
/*    */ 
/*    */ import com.j256.ormlite.field.DatabaseField;
/*    */ import com.j256.ormlite.table.DatabaseTable;
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
/*    */ 
/*    */ 
/*    */ @DatabaseTable(tableName="region_message")
/*    */ public class RegionMessage
/*    */ {
/*    */   public static final String COLUMN_REGION_ID = "region_id";
/*    */   public static final String COLUMN_MESSAGE_ID = "message_id";
/*    */   @DatabaseField(generatedId=true)
/*    */   private Integer id;
/*    */   @DatabaseField(foreign=true, columnName="region_id")
/*    */   private Region region;
/*    */   @DatabaseField(foreign=true, columnName="message_id")
/*    */   private Message message;
/*    */   
/*    */   public RegionMessage() {}
/*    */   
/*    */   public RegionMessage(Region region, Message message)
/*    */   {
/* 40 */     this.region = region;
/* 41 */     this.message = message;
/*    */   }
/*    */   
/*    */   public Integer getId() {
/* 45 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Integer id) {
/* 49 */     this.id = id;
/*    */   }
/*    */   
/*    */   public Region getRegion() {
/* 53 */     return this.region;
/*    */   }
/*    */   
/*    */   public void setRegion(Region region) {
/* 57 */     this.region = region;
/*    */   }
/*    */   
/*    */   public Message getMessage() {
/* 61 */     return this.message;
/*    */   }
/*    */   
/*    */   public void setMessage(Message message) {
/* 65 */     this.message = message;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\RegionMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */