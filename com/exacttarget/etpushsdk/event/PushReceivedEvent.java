/*    */ package com.exacttarget.etpushsdk.event;
/*    */ 
/*    */ import android.os.Bundle;
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
/*    */ 
/*    */ public class PushReceivedEvent
/*    */ {
/*    */   private Bundle payload;
/*    */   
/*    */   public PushReceivedEvent(Bundle payload)
/*    */   {
/* 30 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public Bundle getPayload() {
/* 34 */     return this.payload;
/*    */   }
/*    */   
/*    */   public void setPayload(Bundle payload) {
/* 38 */     this.payload = payload;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\PushReceivedEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */