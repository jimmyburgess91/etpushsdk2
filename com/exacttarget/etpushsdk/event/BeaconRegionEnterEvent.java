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
/*    */ 
/*    */ public class BeaconRegionEnterEvent
/*    */ {
/*    */   private Region region;
/*    */   
/*    */   public BeaconRegionEnterEvent(Region region)
/*    */   {
/* 23 */     this.region = region;
/*    */   }
/*    */   
/*    */   public Region getRegion() {
/* 27 */     return this.region;
/*    */   }
/*    */   
/*    */   public void setRegion(Region region) {
/* 31 */     this.region = region;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\BeaconRegionEnterEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */