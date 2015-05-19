/*    */ package com.exacttarget.etpushsdk.event;
/*    */ 
/*    */ import com.exacttarget.etpushsdk.data.Region;
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
/*    */ public class BeaconRegionExitEvent
/*    */ {
/*    */   private Region region;
/*    */   
/*    */   public BeaconRegionExitEvent(Region region)
/*    */   {
/* 22 */     this.region = region;
/*    */   }
/*    */   
/*    */   public Region getRegion() {
/* 26 */     return this.region;
/*    */   }
/*    */   
/*    */   public void setRegion(Region region) {
/* 30 */     this.region = region;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\BeaconRegionExitEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */