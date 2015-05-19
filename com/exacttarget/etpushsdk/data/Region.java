/*     */ package com.exacttarget.etpushsdk.data;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import com.google.android.gms.location.Geofence;
/*     */ import com.google.android.gms.location.Geofence.Builder;
/*     */ import com.j256.ormlite.field.DatabaseField;
/*     */ import com.j256.ormlite.table.DatabaseTable;
/*     */ import java.util.Date;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @DatabaseTable(tableName="regions")
/*     */ @JsonIgnoreProperties(ignoreUnknown=true)
/*     */ @JsonInclude(JsonInclude.Include.NON_NULL)
/*     */ public class Region
/*     */ {
/*     */   public static final String TAG = "jb4ASDK@Region";
/*     */   public static final String COLUMN_ID = "id";
/*     */   public static final String COLUMN_LATITUDE = "latitude";
/*     */   public static final String COLUMN_LONGITUDE = "longitude";
/*     */   public static final String COLUMN_RADIUS = "radius";
/*     */   public static final String COLUMN_ACTIVE = "active";
/*     */   public static final String COLUMN_BEACON_GUID = "beacon_guid";
/*     */   public static final String COLUMN_BEACON_MAJOR = "beacon_major";
/*     */   public static final String COLUMN_BEACON_MINOR = "beacon_minor";
/*     */   public static final String COLUMN_ENTRY_COUNT = "entry_count";
/*     */   public static final String COLUMN_EXIT_COUNT = "exit_count";
/*     */   public static final String COLUMN_DESCRIPTION = "description";
/*     */   public static final String COLUMN_NAME = "name";
/*     */   public static final String COLUMN_LOCATION_TYPE = "location_type";
/*     */   public static final String COLUMN_HAS_ENTERED = "has_entered";
/*     */   public static final int LOCATION_TYPE_FENCE = 1;
/*     */   public static final int LOCATION_TYPE_BEACON = 3;
/*     */   public static final String MAGIC_FENCE_ID = "~~m@g1c_f3nc3~~";
/*     */   @DatabaseField(id=true, columnName="id")
/*     */   @JsonProperty("id")
/*     */   private String id;
/*     */   @DatabaseField(columnName="name")
/*     */   @JsonProperty("name")
/*     */   private String name;
/*     */   @DatabaseField(persisted=false)
/*     */   @JsonProperty("center")
/*     */   private LatLon center;
/*     */   @DatabaseField(columnName="location_type")
/*     */   @JsonProperty("locationType")
/*  71 */   private Integer locationType = Integer.valueOf(1);
/*     */   
/*     */   @DatabaseField(columnName="latitude")
/*     */   @JsonIgnore
/*     */   private Double latitude;
/*     */   
/*     */   @DatabaseField(columnName="longitude")
/*     */   @JsonIgnore
/*     */   private Double longitude;
/*     */   
/*     */   @DatabaseField(columnName="radius")
/*     */   @JsonProperty("radius")
/*     */   private Integer radius;
/*     */   
/*     */   @DatabaseField(persisted=false)
/*     */   @JsonProperty("messages")
/*     */   private List<Message> messages;
/*     */   
/*     */   @DatabaseField(columnName="active")
/*     */   @JsonIgnore
/*     */   private Boolean active;
/*     */   
/*     */   @DatabaseField(columnName="beacon_guid")
/*     */   @JsonProperty("proximityUuid")
/*     */   private String guid;
/*     */   
/*     */   @DatabaseField(columnName="beacon_major")
/*     */   @JsonProperty("major")
/*     */   private Integer major;
/*     */   
/*     */   @DatabaseField(columnName="beacon_minor")
/*     */   @JsonProperty("minor")
/*     */   private Integer minor;
/*     */   
/*     */   @DatabaseField(columnName="entry_count")
/*     */   @JsonIgnore
/* 107 */   private Integer entryCount = Integer.valueOf(0);
/*     */   
/*     */   @DatabaseField(columnName="exit_count")
/*     */   @JsonIgnore
/* 111 */   private Integer exitCount = Integer.valueOf(0);
/*     */   
/*     */   @DatabaseField(columnName="description")
/*     */   @JsonProperty("description")
/*     */   private String description;
/*     */   
/*     */   @DatabaseField(columnName="has_entered")
/*     */   @JsonIgnore
/* 119 */   private Boolean hasEntered = Boolean.FALSE;
/*     */   
/*     */ 
/*     */   public String getId()
/*     */   {
/* 124 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/* 128 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 132 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 136 */     this.name = name;
/*     */   }
/*     */   
/*     */   public Integer getLocationType() {
/* 140 */     if (this.locationType == null) {
/* 141 */       this.locationType = Integer.valueOf(1);
/*     */     }
/* 143 */     return this.locationType;
/*     */   }
/*     */   
/*     */   public void setLocationType(Integer locationType) {
/* 147 */     this.locationType = locationType;
/*     */   }
/*     */   
/*     */   public LatLon getCenter() {
/* 151 */     return this.center;
/*     */   }
/*     */   
/*     */   public void setCenter(LatLon center) {
/* 155 */     this.center = center;
/* 156 */     setLatitude(center.getLatitude());
/* 157 */     setLongitude(center.getLongitude());
/*     */   }
/*     */   
/*     */   public Double getLatitude() {
/* 161 */     return this.latitude;
/*     */   }
/*     */   
/*     */   public void setLatitude(Double latitude) {
/* 165 */     this.latitude = latitude;
/*     */   }
/*     */   
/*     */   public Double getLongitude() {
/* 169 */     return this.longitude;
/*     */   }
/*     */   
/*     */   public void setLongitude(Double longitude) {
/* 173 */     this.longitude = longitude;
/*     */   }
/*     */   
/*     */   public Integer getRadius() {
/* 177 */     return this.radius;
/*     */   }
/*     */   
/*     */   public void setRadius(Integer radius) {
/* 181 */     this.radius = radius;
/*     */   }
/*     */   
/*     */   public List<Message> getMessages() {
/* 185 */     return this.messages;
/*     */   }
/*     */   
/*     */   public void setMessages(List<Message> messages) {
/* 189 */     this.messages = messages;
/*     */   }
/*     */   
/*     */   public Boolean getActive() {
/* 193 */     return this.active;
/*     */   }
/*     */   
/*     */   public void setActive(Boolean active) {
/* 197 */     this.active = active;
/*     */   }
/*     */   
/*     */   public String getGuid() {
/* 201 */     return this.guid;
/*     */   }
/*     */   
/*     */   public void setGuid(String guid) {
/* 205 */     this.guid = guid;
/*     */   }
/*     */   
/*     */   public Integer getMajor() {
/* 209 */     return this.major;
/*     */   }
/*     */   
/*     */   public void setMajor(Integer major) {
/* 213 */     this.major = major;
/*     */   }
/*     */   
/*     */   public Integer getMinor() {
/* 217 */     return this.minor;
/*     */   }
/*     */   
/*     */   public void setMinor(Integer minor) {
/* 221 */     this.minor = minor;
/*     */   }
/*     */   
/*     */   public Integer getEntryCount() {
/* 225 */     return this.entryCount;
/*     */   }
/*     */   
/*     */   public void setEntryCount(Integer entryCount) {
/* 229 */     this.entryCount = entryCount;
/*     */   }
/*     */   
/*     */   public Integer getExitCount() {
/* 233 */     return this.exitCount;
/*     */   }
/*     */   
/*     */   public void setExitCount(Integer exitCount) {
/* 237 */     this.exitCount = exitCount;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/* 241 */     return this.description;
/*     */   }
/*     */   
/*     */   public void setDescription(String description) {
/* 245 */     this.description = description;
/*     */   }
/*     */   
/*     */   public Boolean getHasEntered() {
/* 249 */     return this.hasEntered;
/*     */   }
/*     */   
/*     */   public void setHasEntered(Boolean hasEntered) {
/* 253 */     this.hasEntered = hasEntered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Geofence toGeofence()
/*     */   {
/* 263 */     int transitionTypes = 0;
/* 264 */     long lastEnding = 0L;
/* 265 */     for (Message message : this.messages) {
/* 266 */       if (lastEnding != -1L) {
/* 267 */         if (message.getEndDate() != null) {
/* 268 */           if (lastEnding < message.getEndDate().getTime()) {
/* 269 */             lastEnding = message.getEndDate().getTime();
/*     */           }
/*     */         }
/*     */         else {
/* 273 */           lastEnding = -1L;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 278 */     if ("~~m@g1c_f3nc3~~".equals(getId()))
/*     */     {
/* 280 */       transitionTypes = 2;
/*     */     }
/*     */     else
/*     */     {
/* 284 */       transitionTypes = 3;
/*     */     }
/* 286 */     return new Geofence.Builder().setRequestId(this.id).setCircularRegion(this.latitude.doubleValue(), this.longitude.doubleValue(), this.radius.intValue()).setTransitionTypes(transitionTypes).setExpirationDuration(lastEnding == -1L ? -1L : lastEnding - System.currentTimeMillis()).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 296 */     if ((o instanceof Region)) {
/* 297 */       if ((this.id == null) && (((Region)o).id == null)) {
/* 298 */         return true;
/*     */       }
/* 300 */       if (this.id != null) {
/* 301 */         return this.id.equals(((Region)o).id);
/*     */       }
/*     */     }
/* 304 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 309 */     return this.id.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\Region.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */