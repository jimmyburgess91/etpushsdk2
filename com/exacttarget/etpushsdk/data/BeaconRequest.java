/*    */ package com.exacttarget.etpushsdk.data;
/*    */ 
/*    */ import android.content.Context;
/*    */ import com.j256.ormlite.field.DatabaseField;
/*    */ import com.j256.ormlite.table.DatabaseTable;
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
/*    */ @DatabaseTable(tableName="beacon_request")
/*    */ public class BeaconRequest
/*    */   extends DeviceData
/*    */ {
/*    */   @DatabaseField(generatedId=true)
/*    */   private Integer id;
/*    */   @DatabaseField(columnName="device_id")
/*    */   private String deviceId;
/*    */   @DatabaseField(columnName="latitude")
/*    */   private Double latitude;
/*    */   @DatabaseField(columnName="longitude")
/*    */   private Double longitude;
/*    */   
/*    */   public BeaconRequest() {}
/*    */   
/*    */   public BeaconRequest(Context context)
/*    */   {
/* 37 */     this.deviceId = uniqueDeviceIdentifier(context);
/*    */   }
/*    */   
/*    */   public Integer getId() {
/* 41 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Integer id) {
/* 45 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getDeviceId() {
/* 49 */     return this.deviceId;
/*    */   }
/*    */   
/*    */   public void setDeviceId(String deviceId) {
/* 53 */     this.deviceId = deviceId;
/*    */   }
/*    */   
/*    */   public Double getLatitude() {
/* 57 */     return this.latitude;
/*    */   }
/*    */   
/*    */   public void setLatitude(Double latitude) {
/* 61 */     this.latitude = latitude;
/*    */   }
/*    */   
/*    */   public Double getLongitude() {
/* 65 */     return this.longitude;
/*    */   }
/*    */   
/*    */   public void setLongitude(Double longitude) {
/* 69 */     this.longitude = longitude;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\BeaconRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */