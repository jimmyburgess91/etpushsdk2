/*    */ package com.exacttarget.etpushsdk.event;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnalyticPiItemEvent
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String userId;
/*    */   private String sessionId;
/*    */   private List<Integer> databaseIds;
/*    */   
/*    */   public String getUserId()
/*    */   {
/* 22 */     return this.userId;
/*    */   }
/*    */   
/*    */   public void setUserId(String userId) {
/* 26 */     this.userId = userId;
/*    */   }
/*    */   
/*    */   public String getSessionId() {
/* 30 */     return this.sessionId;
/*    */   }
/*    */   
/*    */   public void setSessionId(String sessionId) {
/* 34 */     this.sessionId = sessionId;
/*    */   }
/*    */   
/*    */   public List<Integer> getDatabaseIds() {
/* 38 */     return this.databaseIds;
/*    */   }
/*    */   
/*    */   public void setDatabaseIds(List<Integer> databaseIds) {
/* 42 */     this.databaseIds = databaseIds;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\AnalyticPiItemEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */