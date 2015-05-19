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
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ @JsonInclude(JsonInclude.Include.NON_NULL)
/*    */ public class GeofenceResponse
/*    */ {
/*    */   @JsonProperty("refreshCenter")
/*    */   private LatLon refreshCenter;
/*    */   @JsonProperty("refreshRadius")
/*    */   private Integer refreshRadius;
/*    */   @JsonProperty("fences")
/*    */   private List<Region> fences;
/*    */   
/*    */   public LatLon getRefreshCenter()
/*    */   {
/* 32 */     return this.refreshCenter;
/*    */   }
/*    */   
/*    */   public void setRefreshCenter(LatLon refreshCenter) {
/* 36 */     this.refreshCenter = refreshCenter;
/*    */   }
/*    */   
/*    */   public Integer getRefreshRadius() {
/* 40 */     return this.refreshRadius;
/*    */   }
/*    */   
/*    */   public void setRefreshRadius(Integer refreshRadius) {
/* 44 */     this.refreshRadius = refreshRadius;
/*    */   }
/*    */   
/*    */   public List<Region> getFences() {
/* 48 */     return this.fences;
/*    */   }
/*    */   
/*    */   public void setFences(List<Region> fences) {
/* 52 */     this.fences = fences;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\GeofenceResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */