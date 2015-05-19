/*     */ package com.exacttarget.etpushsdk.data;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import com.fasterxml.jackson.annotation.JsonRawValue;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.j256.ormlite.field.DataType;
/*     */ import com.j256.ormlite.field.DatabaseField;
/*     */ import com.j256.ormlite.table.DatabaseTable;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ @DatabaseTable(tableName="messages")
/*     */ @JsonIgnoreProperties(ignoreUnknown=true)
/*     */ @JsonInclude(JsonInclude.Include.NON_NULL)
/*     */ public class Message
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String COLUMN_ID = "id";
/*     */   public static final String COLUMN_ALERT = "alert";
/*     */   public static final String COLUMN_SOUND = "sound";
/*     */   public static final String COLUMN_BADGE = "badge";
/*     */   public static final String COLUMN_OPEN_DIRECT = "open_direct";
/*     */   public static final String COLUMN_CATEGORY = "category";
/*     */   public static final String COLUMN_START_DATE = "start_date";
/*     */   public static final String COLUMN_END_DATE = "end_date";
/*     */   public static final String COLUMN_MESSAGE_TYPE = "message_type";
/*     */   public static final String COLUMN_CONTENT_TYPE = "content_type";
/*     */   public static final String COLUMN_PAGE_ID = "page_id";
/*     */   public static final String COLUMN_URL = "url";
/*     */   public static final String COLUMN_SUBJECT = "subject";
/*     */   public static final String COLUMN_SITE_ID = "site_id";
/*     */   public static final String COLUMN_READ = "read";
/*     */   public static final String COLUMN_CUSTOM = "custom";
/*     */   public static final String COLUMN_KEYS = "keys";
/*     */   public static final String COLUMN_PERIOD_SHOW_COUNT = "period_show_count";
/*     */   public static final String COLUMN_SHOW_COUNT = "show_count";
/*     */   public static final String COLUMN_LAST_SHOWN_DATE = "last_shown_date";
/*     */   public static final String COLUMN_NEXT_ALLOWED_SHOW = "next_allowed_show";
/*     */   public static final String COLUMN_MESSAGE_LIMIT = "message_limit";
/*     */   public static final String COLUMN_ROLLING_PERIOD = "rolling_period";
/*     */   public static final String COLUMN_PERIOD_TYPE = "period_type";
/*     */   public static final String COLUMN_NUMBER_OF_PERIODS = "number_of_periods";
/*     */   public static final String COLUMN_MESSAGES_PER_PERIOD = "messages_per_period";
/*     */   public static final String COLUMN_MESSAGE_DELETED = "message_deleted";
/*     */   public static final String COLUMN_MIN_TRIPPED = "min_tripped";
/*     */   public static final String COLUMN_PROXIMITY = "proximity";
/*     */   public static final String COLUMN_EPHEMERAL_MESSAGE = "ephemeral_message";
/*     */   public static final String COLUMN_HAS_ENTERED = "has_entered";
/*     */   public static final String COLUMN_NOTIFY_ID = "notify_id";
/*     */   public static final String COLUMN_LOITER_SECONDS = "loiter_seconds";
/*     */   public static final String COLUMN_ENTRY_TIME = "entry_time";
/*  75 */   public static final Integer MESSAGE_TYPE_UNKNOWN = Integer.valueOf(0);
/*  76 */   public static final Integer MESSAGE_TYPE_BASIC = Integer.valueOf(1);
/*  77 */   public static final Integer MESSAGE_TYPE_ENHANCED = Integer.valueOf(2);
/*  78 */   public static final Integer MESSAGE_TYPE_FENCE_ENTRY = Integer.valueOf(3);
/*  79 */   public static final Integer MESSAGE_TYPE_FENCE_EXIT = Integer.valueOf(4);
/*  80 */   public static final Integer MESSAGE_TYPE_PROXIMITY_ENTRY = Integer.valueOf(5);
/*     */   
/*  82 */   public static final Integer CONTENT_TYPE_NONE = Integer.valueOf(0);
/*  83 */   public static final Integer CONTENT_TYPE_ALERT = Integer.valueOf(1);
/*  84 */   public static final Integer CONTENT_TYPE_CLOUD_PAGE_ONLY = Integer.valueOf(2);
/*  85 */   public static final Integer CONTENT_TYPE_CLOUD_PAGE_ALERT = Integer.valueOf(3);
/*     */   
/*     */   public static final int PERIOD_TYPE_UNIT_NONE = 0;
/*     */   
/*     */   public static final int PERIOD_TYPE_UNIT_YEAR = 1;
/*     */   
/*     */   public static final int PERIOD_TYPE_UNIT_MONTH = 2;
/*     */   public static final int PERIOD_TYPE_UNIT_WEEK = 3;
/*     */   public static final int PERIOD_TYPE_UNIT_DAY = 4;
/*     */   public static final int PERIOD_TYPE_UNIT_HOUR = 5;
/*     */   public static final long UNIT_HOUR = 3600000L;
/*     */   public static final long UNIT_DAY = 86400000L;
/*     */   public static final long UNIT_WEEK = 604800000L;
/*     */   public static final long UNIT_MONTH = 2592000000L;
/*     */   public static final long UNIT_YEAR = 31536000000L;
/*     */   public static final int PROXIMITY_UNKNOWN = 0;
/*     */   public static final int PROXIMITY_IMMEDIATE = 1;
/*     */   public static final int PROXIMITY_NEAR = 2;
/*     */   public static final int PROXIMITY_FAR = 3;
/*     */   @DatabaseField(id=true, columnName="id")
/*     */   @JsonProperty("id")
/* 106 */   private String id = null;
/*     */   
/*     */   @DatabaseField(columnName="alert")
/*     */   @JsonProperty("alert")
/* 110 */   private String alert = null;
/*     */   
/*     */   @DatabaseField(columnName="sound")
/*     */   @JsonProperty("sound")
/* 114 */   private String sound = null;
/*     */   
/*     */   @DatabaseField(columnName="badge")
/*     */   @JsonProperty("badge")
/* 118 */   private String badge = null;
/*     */   
/*     */   @DatabaseField(columnName="open_direct")
/*     */   @JsonProperty("openDirect")
/* 122 */   private String openDirect = null;
/*     */   
/*     */   @DatabaseField(columnName="category")
/*     */   @JsonProperty("category")
/* 126 */   private String category = null;
/*     */   @DatabaseField(columnName="start_date", dataType=DataType.DATE_STRING, format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
/*     */   @JsonProperty("startDateUtc")
/*     */   @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale="ENGLISH", timezone="UTC")
/* 130 */   private Date startDate = new Date(System.currentTimeMillis() - 1000L);
/*     */   
/*     */   @DatabaseField(columnName="end_date", dataType=DataType.DATE_STRING, format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
/*     */   @JsonProperty("endDateUtc")
/*     */   @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale="ENGLISH", timezone="UTC")
/* 135 */   private Date endDate = null;
/*     */   
/*     */ 
/*     */   @DatabaseField(columnName="message_type")
/*     */   @JsonProperty("messageType")
/* 140 */   private Integer messageType = null;
/*     */   
/*     */   @DatabaseField(columnName="content_type")
/*     */   @JsonProperty("contentType")
/* 144 */   private Integer contentType = null;
/*     */   
/*     */   @DatabaseField(columnName="messages_per_period")
/*     */   @JsonProperty("messagesPerPeriod")
/* 148 */   private Integer messagesPerPeriod = Integer.valueOf(-1);
/*     */   
/*     */   @DatabaseField(columnName="number_of_periods")
/*     */   @JsonProperty("numberOfPeriods")
/* 152 */   private Integer numberOfPeriods = Integer.valueOf(-1);
/*     */   
/*     */   @DatabaseField(columnName="period_type")
/*     */   @JsonProperty("periodType")
/* 156 */   private Integer periodType = Integer.valueOf(0);
/*     */   
/*     */   @DatabaseField(columnName="rolling_period")
/*     */   @JsonProperty("isRollingPeriod")
/* 160 */   private Boolean isRollingPeriod = Boolean.TRUE;
/*     */   
/*     */   @DatabaseField(columnName="message_limit")
/*     */   @JsonProperty("messageLimit")
/* 164 */   private Integer messageLimit = Integer.valueOf(-1);
/*     */   
/*     */   @DatabaseField(columnName="next_allowed_show", dataType=DataType.DATE_STRING, format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
/*     */   @JsonIgnore
/* 168 */   private Date nextAllowedShow = new Date(System.currentTimeMillis() - 1000L);
/*     */   
/*     */   @DatabaseField(columnName="last_shown_date", dataType=DataType.DATE_STRING, format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
/*     */   @JsonIgnore
/* 172 */   private Date lastShownDate = null;
/*     */   
/*     */   @DatabaseField(columnName="show_count")
/*     */   @JsonIgnore
/* 176 */   private Integer showCount = Integer.valueOf(0);
/*     */   
/*     */   @DatabaseField(columnName="period_show_count")
/*     */   @JsonIgnore
/* 180 */   private Integer periodShowCount = Integer.valueOf(0);
/*     */   
/*     */   @DatabaseField(columnName="min_tripped")
/*     */   @JsonProperty("minTripped")
/* 184 */   private Integer minTripped = Integer.valueOf(0);
/*     */   
/*     */   @DatabaseField(columnName="keys", dataType=DataType.SERIALIZABLE)
/*     */   @JsonProperty("keys")
/* 188 */   private ArrayList<Attribute> keys = null;
/*     */   
/*     */   @DatabaseField(columnName="custom", dataType=DataType.STRING)
/*     */   @JsonIgnore
/*     */   private String custom;
/*     */   @DatabaseField(persisted=false)
/*     */   @JsonRawValue
/*     */   @JsonProperty("custom")
/* 196 */   private JsonNode customObj = null;
/*     */   
/*     */ 
/*     */ 
/*     */   @DatabaseField(columnName="read")
/* 201 */   private Boolean read = Boolean.FALSE;
/*     */   
/*     */   @DatabaseField(columnName="site_id")
/*     */   @JsonProperty("siteId")
/*     */   private String siteId;
/*     */   
/*     */   @DatabaseField(columnName="subject")
/*     */   @JsonProperty("subject")
/*     */   private String subject;
/*     */   
/*     */   @DatabaseField(columnName="url")
/*     */   @JsonProperty("url")
/*     */   private String url;
/*     */   
/*     */   @DatabaseField(columnName="page_id")
/*     */   @JsonProperty("pageId")
/*     */   private String pageId;
/*     */   @DatabaseField(columnName="message_deleted")
/*     */   @JsonIgnore
/* 220 */   private Boolean messageDeleted = Boolean.FALSE;
/*     */   
/*     */   @DatabaseField(columnName="proximity")
/*     */   @JsonProperty("proximity")
/* 224 */   private int proximity = 2;
/*     */   
/*     */   @DatabaseField(columnName="ephemeral_message")
/*     */   @JsonProperty("ephemeralMessage")
/* 228 */   private Boolean ephemeralMessage = Boolean.FALSE;
/*     */   
/*     */   @DatabaseField(columnName="has_entered")
/*     */   @JsonIgnore
/* 232 */   private Boolean hasEntered = Boolean.FALSE;
/*     */   
/*     */   @DatabaseField(columnName="notify_id")
/*     */   @JsonIgnore
/*     */   private Integer notifyId;
/*     */   
/*     */   @DatabaseField(columnName="loiter_seconds")
/*     */   @JsonProperty("loiterSeconds")
/* 240 */   private Integer loiterSeconds = Integer.valueOf(0);
/*     */   
/*     */   @DatabaseField(columnName="entry_time")
/*     */   @JsonIgnore
/* 244 */   private Long entryTime = Long.valueOf(0L);
/*     */   
/*     */ 
/*     */   public String getId()
/*     */   {
/* 249 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 253 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getAlert() {
/* 257 */     return this.alert;
/*     */   }
/*     */   
/*     */   public void setAlert(String alert) {
/* 261 */     this.alert = alert;
/*     */   }
/*     */   
/*     */   public String getSound() {
/* 265 */     return this.sound;
/*     */   }
/*     */   
/*     */   public void setSound(String sound) {
/* 269 */     this.sound = sound;
/*     */   }
/*     */   
/*     */   public String getBadge() {
/* 273 */     return this.badge;
/*     */   }
/*     */   
/*     */   public void setBadge(String badge) {
/* 277 */     this.badge = badge;
/*     */   }
/*     */   
/*     */   public String getOpenDirect() {
/* 281 */     return this.openDirect;
/*     */   }
/*     */   
/*     */   public void setOpenDirect(String openDirect) {
/* 285 */     this.openDirect = openDirect;
/*     */   }
/*     */   
/*     */   public String getCategory() {
/* 289 */     return this.category;
/*     */   }
/*     */   
/*     */   public void setCategory(String category) {
/* 293 */     this.category = category;
/*     */   }
/*     */   
/*     */   public Date getStartDate() {
/* 297 */     return this.startDate;
/*     */   }
/*     */   
/*     */   public void setStartDate(Date startDate) {
/* 301 */     this.startDate = startDate;
/*     */   }
/*     */   
/*     */   public Date getEndDate() {
/* 305 */     return this.endDate;
/*     */   }
/*     */   
/*     */   public void setEndDate(Date endDate) {
/* 309 */     this.endDate = endDate;
/*     */   }
/*     */   
/*     */   public Integer getMessageType() {
/* 313 */     return this.messageType;
/*     */   }
/*     */   
/*     */   public void setMessageType(Integer messageType) {
/* 317 */     this.messageType = messageType;
/*     */   }
/*     */   
/*     */   public Integer getMessagesPerPeriod() {
/* 321 */     return this.messagesPerPeriod;
/*     */   }
/*     */   
/*     */   public void setMessagesPerPeriod(Integer messagesPerPeriod) {
/* 325 */     this.messagesPerPeriod = messagesPerPeriod;
/*     */   }
/*     */   
/*     */   public Integer getNumberOfPeriods() {
/* 329 */     return this.numberOfPeriods;
/*     */   }
/*     */   
/*     */   public void setNumberOfPeriods(Integer numberOfPeriods) {
/* 333 */     this.numberOfPeriods = numberOfPeriods;
/*     */   }
/*     */   
/*     */   public Integer getPeriodType() {
/* 337 */     return this.periodType;
/*     */   }
/*     */   
/*     */   public void setPeriodType(Integer periodType) {
/* 341 */     this.periodType = periodType;
/*     */   }
/*     */   
/*     */   public Boolean getIsRollingPeriod() {
/* 345 */     return this.isRollingPeriod;
/*     */   }
/*     */   
/*     */   public void setIsRollingPeriod(Boolean isRollingPeriod) {
/* 349 */     this.isRollingPeriod = isRollingPeriod;
/*     */   }
/*     */   
/*     */   public Integer getMessageLimit() {
/* 353 */     return this.messageLimit;
/*     */   }
/*     */   
/*     */   public void setMessageLimit(Integer messageLimit) {
/* 357 */     this.messageLimit = messageLimit;
/*     */   }
/*     */   
/*     */   public ArrayList<Attribute> getKeys() {
/* 361 */     return this.keys;
/*     */   }
/*     */   
/*     */   public void setKeys(ArrayList<Attribute> keys) {
/* 365 */     this.keys = keys;
/*     */   }
/*     */   
/*     */   public Date getNextAllowedShow() {
/* 369 */     return this.nextAllowedShow;
/*     */   }
/*     */   
/*     */   public void setNextAllowedShow(Date nextAllowedShow) {
/* 373 */     this.nextAllowedShow = nextAllowedShow;
/*     */   }
/*     */   
/*     */   public Date getLastShownDate() {
/* 377 */     return this.lastShownDate;
/*     */   }
/*     */   
/*     */   public void setLastShownDate(Date lastShownDate) {
/* 381 */     this.lastShownDate = lastShownDate;
/*     */   }
/*     */   
/*     */   public Integer getShowCount() {
/* 385 */     return this.showCount;
/*     */   }
/*     */   
/*     */   public void setShowCount(Integer showCount) {
/* 389 */     this.showCount = showCount;
/*     */   }
/*     */   
/*     */   public Integer getPeriodShowCount() {
/* 393 */     return this.periodShowCount;
/*     */   }
/*     */   
/*     */   public void setPeriodShowCount(Integer periodShowCount) {
/* 397 */     this.periodShowCount = periodShowCount;
/*     */   }
/*     */   
/*     */   public String getCustom() {
/* 401 */     return this.customObj == null ? this.custom : this.customObj.toString();
/*     */   }
/*     */   
/*     */   public void setCustom(String custom) {
/* 405 */     this.custom = custom;
/*     */   }
/*     */   
/*     */   public JsonNode getCustomObj()
/*     */   {
/* 410 */     return this.customObj;
/*     */   }
/*     */   
/*     */   public void setCustomObj(JsonNode customObj) {
/* 414 */     this.customObj = customObj;
/*     */   }
/*     */   
/*     */   public Integer getContentType() {
/* 418 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public void setContentType(Integer contentType) {
/* 422 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   public Boolean getRead() {
/* 426 */     return this.read;
/*     */   }
/*     */   
/*     */   public void setRead(Boolean read) {
/* 430 */     this.read = read;
/*     */   }
/*     */   
/*     */   public String getSiteId() {
/* 434 */     return this.siteId;
/*     */   }
/*     */   
/*     */   public void setSiteId(String siteId) {
/* 438 */     this.siteId = siteId;
/*     */   }
/*     */   
/*     */   public String getSubject() {
/* 442 */     if ((this.subject == null) && (this.alert == null)) {
/* 443 */       return "A new message";
/*     */     }
/* 445 */     if (this.subject == null) {
/* 446 */       return this.alert;
/*     */     }
/* 448 */     return this.subject;
/*     */   }
/*     */   
/*     */   public void setSubject(String subject) {
/* 452 */     this.subject = subject;
/*     */   }
/*     */   
/*     */   public String getUrl() {
/* 456 */     return this.url;
/*     */   }
/*     */   
/*     */   public void setUrl(String url) {
/* 460 */     this.url = url;
/*     */   }
/*     */   
/*     */   public String getPageId() {
/* 464 */     return this.pageId;
/*     */   }
/*     */   
/*     */   public void setPageId(String pageId) {
/* 468 */     this.pageId = pageId;
/*     */   }
/*     */   
/*     */   public Boolean getMessageDeleted() {
/* 472 */     return this.messageDeleted;
/*     */   }
/*     */   
/*     */   public void setMessageDeleted(Boolean messageDeleted) {
/* 476 */     this.messageDeleted = messageDeleted;
/*     */   }
/*     */   
/*     */   public Integer getMinTripped() {
/* 480 */     return this.minTripped;
/*     */   }
/*     */   
/*     */   public void setMinTripped(Integer minTripped) {
/* 484 */     this.minTripped = minTripped;
/*     */   }
/*     */   
/*     */   public int getProximity() {
/* 488 */     return this.proximity;
/*     */   }
/*     */   
/*     */   public void setProximity(int proximity) {
/* 492 */     this.proximity = proximity;
/*     */   }
/*     */   
/*     */   public Boolean getEphemeralMessage() {
/* 496 */     return this.ephemeralMessage;
/*     */   }
/*     */   
/*     */   public void setEphemeralMessage(Boolean ephemeralMessage) {
/* 500 */     this.ephemeralMessage = ephemeralMessage;
/*     */   }
/*     */   
/*     */   public Boolean getHasEntered() {
/* 504 */     return this.hasEntered;
/*     */   }
/*     */   
/*     */   public void setHasEntered(Boolean hasEntered) {
/* 508 */     this.hasEntered = hasEntered;
/*     */   }
/*     */   
/*     */   public Integer getNotifyId() {
/* 512 */     return this.notifyId;
/*     */   }
/*     */   
/*     */   public void setNotifyId(Integer notifyId) {
/* 516 */     this.notifyId = notifyId;
/*     */   }
/*     */   
/*     */   public Integer getLoiterSeconds() {
/* 520 */     return this.loiterSeconds;
/*     */   }
/*     */   
/*     */   public void setLoiterSeconds(Integer loiterSeconds) {
/* 524 */     this.loiterSeconds = loiterSeconds;
/*     */   }
/*     */   
/*     */   public Long getEntryTime() {
/* 528 */     return this.entryTime;
/*     */   }
/*     */   
/*     */   public void setEntryTime(Long entryTime) {
/* 532 */     this.entryTime = entryTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\Message.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */