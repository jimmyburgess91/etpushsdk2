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
/*    */ public class BeaconRegionRangeEvent
/*    */ {
/*    */   private Region region;
/*    */   private int proximity;
/*    */   private int rssi;
/*    */   private double accuracy;
/*    */   
/*    */   public BeaconRegionRangeEvent(Region region, int proximity, int rssi, double accuracy)
/*    */   {
/* 26 */     this.region = region;
/* 27 */     this.proximity = proximity;
/* 28 */     this.rssi = rssi;
/* 29 */     this.accuracy = accuracy;
/*    */   }
/*    */   
/*    */   public Region getRegion() {
/* 33 */     return this.region;
/*    */   }
/*    */   
/*    */   public void setRegion(Region region) {
/* 37 */     this.region = region;
/*    */   }
/*    */   
/*    */   public int getProximity() {
/* 41 */     return this.proximity;
/*    */   }
/*    */   
/*    */   public void setProximity(int proximity) {
/* 45 */     this.proximity = proximity;
/*    */   }
/*    */   
/*    */   public int getRssi() {
/* 49 */     return this.rssi;
/*    */   }
/*    */   
/*    */   public void setRssi(int rssi) {
/* 53 */     this.rssi = rssi;
/*    */   }
/*    */   
/*    */   public double getAccuracy() {
/* 57 */     return this.accuracy;
/*    */   }
/*    */   
/*    */   public void setAccuracy(double accuracy) {
/* 61 */     this.accuracy = accuracy;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\BeaconRegionRangeEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */