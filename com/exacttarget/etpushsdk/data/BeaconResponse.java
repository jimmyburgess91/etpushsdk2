/*    */ package com.exacttarget.etpushsdk.data;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
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
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ @JsonInclude(JsonInclude.Include.NON_NULL)
/*    */ public class BeaconResponse
/*    */ {
/*    */   @JsonProperty("beacons")
/*    */   private List<Region> beacons;
/*    */   
/*    */   public List<Region> getBeacons()
/*    */   {
/* 26 */     return this.beacons;
/*    */   }
/*    */   
/*    */   public void setBeacons(List<Region> beacons) {
/* 30 */     this.beacons = beacons;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\BeaconResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */