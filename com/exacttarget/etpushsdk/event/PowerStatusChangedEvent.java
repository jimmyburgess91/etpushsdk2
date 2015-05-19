/*    */ package com.exacttarget.etpushsdk.event;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PowerStatusChangedEvent
/*    */ {
/*    */   public static final int BATTERY_LOW = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int BATTERY_OK = 1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private int status;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PowerStatusChangedEvent(int status)
/*    */   {
/* 29 */     this.status = status;
/*    */   }
/*    */   
/*    */   public int getStatus() {
/* 33 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setStatus(int status) {
/* 37 */     this.status = status;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\PowerStatusChangedEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */