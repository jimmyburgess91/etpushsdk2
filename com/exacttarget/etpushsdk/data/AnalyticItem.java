/*     */ package com.exacttarget.etpushsdk.data;
/*     */ 
/*     */ import android.content.Context;
/*     */ import com.exacttarget.etpushsdk.Config;
/*     */ import com.exacttarget.etpushsdk.util.JsonType;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import com.j256.ormlite.field.DataType;
/*     */ import com.j256.ormlite.field.DatabaseField;
/*     */ import com.j256.ormlite.table.DatabaseTable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @DatabaseTable(tableName="analytic_item")
/*     */ @JsonIgnoreProperties(ignoreUnknown=false)
/*     */ @JsonInclude(JsonInclude.Include.NON_NULL)
/*     */ public class AnalyticItem
/*     */   extends DeviceData
/*     */ {
/*     */   public static final int ANALYTIC_TYPE_OPEN = 2;
/*     */   public static final int ANALYTIC_TYPE_DISPLAY = 3;
/*     */   public static final int ANALYTIC_TYPE_ET_TIMEINAPP = 4;
/*     */   public static final int ANALYTIC_TYPE_PI_TIMEINAPP = 5;
/*     */   public static final int ANALYTIC_TYPE_FENCE_ENTRY = 6;
/*     */   public static final int ANALYTIC_TYPE_FENCE_EXIT = 7;
/*     */   public static final int ANALYTIC_TYPE_RECEIVED = 10;
/*     */   public static final int ANALYTIC_TYPE_TIME_IN_LOCATION = 11;
/*     */   public static final int ANALYTIC_TYPE_BEACON_IN_RANGE = 12;
/*     */   public static final int ANALYTIC_TYPE_TIME_WITH_BEACON_IN_RANGE = 13;
/*     */   public static final String COLUMN_ET_APP_ID = "et_app_id";
/*     */   public static final String COLUMN_DEVICE_ID = "device_id";
/*     */   public static final String COLUMN_EVENT_DATE = "event_date";
/*     */   public static final String COLUMN_ANALYTIC_TYPES = "analytic_types";
/*     */   public static final String COLUMN_OBJECT_IDS = "object_ids";
/*     */   public static final String COLUMN_VALUE = "value";
/*     */   public static final String COLUMN_LAST_SENT = "last_sent";
/*     */   public static final String COLUMN_READY_TO_SEND = "ready_to_send";
/*     */   public static final String COLUMN_PI_APP_KEY = "pi_app_key";
/*     */   @DatabaseField(generatedId=true)
/*     */   @JsonIgnore
/*     */   private Integer id;
/*     */   @DatabaseField(columnName="et_app_id")
/*     */   @JsonProperty(required=true)
/*     */   private String etAppId;
/*     */   @DatabaseField(columnName="device_id")
/*     */   @JsonProperty(required=true)
/*     */   private String deviceId;
/*     */   @DatabaseField(columnName="event_date", dataType=DataType.DATE_STRING, format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
/*     */   @JsonProperty(required=true)
/*     */   @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale="ENGLISH", timezone="UTC")
/*     */   private Date eventDate;
/*     */   @DatabaseField(columnName="analytic_types", persisterClass=JsonType.class)
/*     */   @JsonProperty(required=true)
/*  64 */   private List<Integer> analyticTypes = new ArrayList();
/*     */   
/*     */   @DatabaseField(columnName="object_ids", persisterClass=JsonType.class)
/*     */   @JsonProperty(required=true)
/*  68 */   private List<String> objectIds = new ArrayList();
/*     */   
/*     */   @DatabaseField(columnName="value")
/*     */   @JsonProperty
/*     */   private Integer value;
/*     */   
/*     */   @DatabaseField(columnName="last_sent")
/*     */   @JsonIgnore
/*  76 */   private Long lastSent = Long.valueOf(0L);
/*     */   
/*     */   @DatabaseField(columnName="ready_to_send")
/*     */   @JsonIgnore
/*  80 */   private Boolean readyToSend = Boolean.FALSE;
/*     */   
/*     */   @DatabaseField(columnName="pi_app_key")
/*     */   @JsonProperty(required=true)
/*  84 */   private String piAppKey = "";
/*     */   
/*     */ 
/*     */ 
/*     */   public AnalyticItem()
/*     */   {
/*  90 */     this.etAppId = Config.getEtAppId();
/*     */   }
/*     */   
/*     */   public AnalyticItem(AnalyticItem inCopyFromAI)
/*     */   {
/*  95 */     copyFromAI(inCopyFromAI);
/*     */   }
/*     */   
/*     */   public AnalyticItem(Context context)
/*     */   {
/* 100 */     this.deviceId = uniqueDeviceIdentifier(context);
/* 101 */     this.etAppId = Config.getEtAppId();
/*     */   }
/*     */   
/*     */   public AnalyticItem(Context context, AnalyticItem inCopyFromAI)
/*     */   {
/* 106 */     copyFromAI(inCopyFromAI);
/*     */   }
/*     */   
/*     */   private void copyFromAI(AnalyticItem inCopyFromAI) {
/* 110 */     setEtAppId(inCopyFromAI.getEtAppId());
/* 111 */     setDeviceId(inCopyFromAI.getDeviceId());
/* 112 */     setEventDate(inCopyFromAI.getEventDate());
/* 113 */     setAnalyticTypes(inCopyFromAI.getAnalyticTypes());
/* 114 */     setObjectIds(inCopyFromAI.getObjectIds());
/* 115 */     setValue(inCopyFromAI.getValue());
/* 116 */     setLastSent(inCopyFromAI.getLastSent());
/* 117 */     setReadyToSend(inCopyFromAI.getReadyToSend());
/* 118 */     setPiAppKey(inCopyFromAI.getPiAppKey());
/*     */   }
/*     */   
/*     */   public Integer getId() {
/* 122 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(Integer id) {
/* 126 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getEtAppId() {
/* 130 */     return this.etAppId;
/*     */   }
/*     */   
/*     */   public void setEtAppId(String etAppId) {
/* 134 */     this.etAppId = etAppId;
/*     */   }
/*     */   
/*     */   public String getDeviceId() {
/* 138 */     return this.deviceId;
/*     */   }
/*     */   
/*     */   public void setDeviceId(String deviceId) {
/* 142 */     this.deviceId = deviceId;
/*     */   }
/*     */   
/*     */   public Date getEventDate() {
/* 146 */     return this.eventDate;
/*     */   }
/*     */   
/*     */   public void setEventDate(Date eventDate) {
/* 150 */     this.eventDate = eventDate;
/*     */   }
/*     */   
/*     */   public List<Integer> getAnalyticTypes() {
/* 154 */     return this.analyticTypes;
/*     */   }
/*     */   
/*     */   public void setAnalyticTypes(List<Integer> analyticTypes) {
/* 158 */     this.analyticTypes = new ArrayList(analyticTypes);
/*     */   }
/*     */   
/*     */   public List<String> getObjectIds() {
/* 162 */     return this.objectIds;
/*     */   }
/*     */   
/*     */   public void setObjectIds(List<String> objectIds) {
/* 166 */     this.objectIds = new ArrayList(objectIds);
/*     */   }
/*     */   
/*     */   public Integer getValue() {
/* 170 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(Integer value) {
/* 174 */     this.value = value;
/*     */   }
/*     */   
/*     */   public Long getLastSent() {
/* 178 */     return this.lastSent;
/*     */   }
/*     */   
/*     */   public void setLastSent(Long lastSent) {
/* 182 */     this.lastSent = lastSent;
/*     */   }
/*     */   
/*     */   public Boolean getReadyToSend() {
/* 186 */     return this.readyToSend;
/*     */   }
/*     */   
/*     */   public void setReadyToSend(Boolean readyToSend) {
/* 190 */     this.readyToSend = readyToSend;
/*     */   }
/*     */   
/*     */   public String getPiAppKey() {
/* 194 */     return this.piAppKey;
/*     */   }
/*     */   
/*     */   public void setPiAppKey(String piAppKey) {
/* 198 */     this.piAppKey = piAppKey;
/*     */   }
/*     */   
/*     */   public void addAnalyticType(int analyticType) {
/* 202 */     this.analyticTypes.add(Integer.valueOf(analyticType));
/*     */   }
/*     */   
/*     */   public void addObjectId(String objectId) {
/* 206 */     this.objectIds.add(objectId);
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\AnalyticItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */