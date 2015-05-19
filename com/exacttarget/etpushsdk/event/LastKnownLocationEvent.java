/*    */ package com.exacttarget.etpushsdk.event;
/*    */ 
/*    */ import android.location.Location;
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
/*    */ public class LastKnownLocationEvent
/*    */ {
/*    */   private Location location;
/*    */   
/*    */   public LastKnownLocationEvent(Location location)
/*    */   {
/* 27 */     this.location = location;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 31 */     return this.location;
/*    */   }
/*    */   
/*    */   public void setLocation(Location location) {
/* 35 */     this.location = location;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\LastKnownLocationEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */