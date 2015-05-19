/*     */ package com.exacttarget.etpushsdk.data;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import com.j256.ormlite.field.DataType;
/*     */ import com.j256.ormlite.field.DatabaseField;
/*     */ import com.j256.ormlite.table.DatabaseTable;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @DatabaseTable(tableName="location_update")
/*     */ @JsonIgnoreProperties(ignoreUnknown=true)
/*     */ public class LocationUpdate
/*     */   extends DeviceData
/*     */ {
/*     */   public static final String COLUMN_LAST_SENT = "last_sent";
/*     */   @DatabaseField(generatedId=true)
/*     */   @JsonIgnore
/*     */   private Integer id;
/*     */   @DatabaseField(columnName="device_id")
/*     */   @JsonProperty("deviceId")
/*     */   private String deviceId;
/*     */   @DatabaseField(columnName="latitude")
/*     */   @JsonProperty("latitude")
/*     */   private Double latitude;
/*     */   @DatabaseField(columnName="longitude")
/*     */   @JsonProperty("longitude")
/*     */   private Double longitude;
/*     */   @DatabaseField(columnName="accuracy")
/*     */   @JsonProperty("accuracy")
/*     */   private Integer accuracy;
/*     */   @DatabaseField(columnName="timestamp", dataType=DataType.DATE_STRING, format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
/*     */   @JsonProperty("location_DateTime_Utc")
/*     */   @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale="ENGLISH", timezone="UTC")
/*     */   private Date timestamp;
/*     */   @DatabaseField(columnName="last_sent")
/*     */   @JsonIgnore
/*  54 */   private Long lastSent = Long.valueOf(0L);
/*     */   
/*     */ 
/*     */ 
/*     */   public LocationUpdate() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public LocationUpdate(Context context)
/*     */   {
/*  64 */     this.deviceId = uniqueDeviceIdentifier(context);
/*     */   }
/*     */   
/*     */   public Integer getId() {
/*  68 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(Integer id) {
/*  72 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getDeviceId() {
/*  76 */     return this.deviceId;
/*     */   }
/*     */   
/*     */   public void setDeviceId(String deviceId) {
/*  80 */     this.deviceId = deviceId;
/*     */   }
/*     */   
/*     */   public Double getLatitude() {
/*  84 */     return this.latitude;
/*     */   }
/*     */   
/*     */   public void setLatitude(Double latitude) {
/*  88 */     this.latitude = latitude;
/*     */   }
/*     */   
/*     */   public Double getLongitude() {
/*  92 */     return this.longitude;
/*     */   }
/*     */   
/*     */   public void setLongitude(Double longitude) {
/*  96 */     this.longitude = longitude;
/*     */   }
/*     */   
/*     */   public Integer getAccuracy() {
/* 100 */     return this.accuracy;
/*     */   }
/*     */   
/*     */   public void setAccuracy(Integer accuracy) {
/* 104 */     this.accuracy = accuracy;
/*     */   }
/*     */   
/*     */   public Date getTimestamp() {
/* 108 */     return this.timestamp;
/*     */   }
/*     */   
/*     */   public void setTimestamp(Date timestamp) {
/* 112 */     this.timestamp = timestamp;
/*     */   }
/*     */   
/*     */   public Long getLastSent() {
/* 116 */     return this.lastSent;
/*     */   }
/*     */   
/*     */   public void setLastSent(Long lastSent) {
/* 120 */     this.lastSent = lastSent;
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\LocationUpdate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */