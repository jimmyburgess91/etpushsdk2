/*    */ package com.exacttarget.etpushsdk.data;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
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
/*    */ public class LatLon
/*    */ {
/*    */   @JsonProperty("latitude")
/*    */   private Double latitude;
/*    */   @JsonProperty("longitude")
/*    */   private Double longitude;
/*    */   
/*    */   public Double getLatitude()
/*    */   {
/* 27 */     return this.latitude;
/*    */   }
/*    */   
/*    */   public void setLatitude(Double latitude) {
/* 31 */     this.latitude = latitude;
/*    */   }
/*    */   
/*    */   public Double getLongitude() {
/* 35 */     return this.longitude;
/*    */   }
/*    */   
/*    */   public void setLongitude(Double longitude) {
/* 39 */     this.longitude = longitude;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\LatLon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */