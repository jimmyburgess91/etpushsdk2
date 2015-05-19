/*    */ package com.exacttarget.etpushsdk.event;
/*    */ 
/*    */ import com.exacttarget.etpushsdk.data.AnalyticItem;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class AnalyticItemEvent
/*    */   extends ArrayList<AnalyticItem>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private List<Integer> databaseIds;
/*    */   
/*    */   public List<Integer> getDatabaseIds()
/*    */   {
/* 24 */     return this.databaseIds;
/*    */   }
/*    */   
/*    */   public void setDatabaseIds(List<Integer> databaseIds) {
/* 28 */     this.databaseIds = databaseIds;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\AnalyticItemEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */