/*    */ package com.exacttarget.etpushsdk.data;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.io.Serializable;
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
/*    */ public class Attribute
/*    */   implements Serializable, Comparable<Attribute>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   @JsonProperty("key")
/*    */   private String key;
/*    */   @JsonProperty("value")
/*    */   private String value;
/*    */   
/*    */   public Attribute() {}
/*    */   
/*    */   public Attribute(String key, String value)
/*    */   {
/* 46 */     this.key = key;
/* 47 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 51 */     return this.key;
/*    */   }
/*    */   
/* 54 */   public void setKey(String key) { this.key = key; }
/*    */   
/*    */   public String getValue() {
/* 57 */     return this.value;
/*    */   }
/*    */   
/* 60 */   public void setValue(String value) { this.value = value; }
/*    */   
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 65 */     if (!(o instanceof Attribute)) {
/* 66 */       return false;
/*    */     }
/* 68 */     Attribute another = (Attribute)o;
/* 69 */     if ((this.key == null) && (another.key == null)) {
/* 70 */       return true;
/*    */     }
/* 72 */     return this.key.equalsIgnoreCase(another.key);
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 77 */     if (this.key == null) {
/* 78 */       return 0;
/*    */     }
/* 80 */     return this.key.hashCode();
/*    */   }
/*    */   
/*    */   public int compareTo(Attribute another)
/*    */   {
/* 85 */     if ((this.key == null) || (another == null) || (another.key == null)) {
/* 86 */       return 0;
/*    */     }
/*    */     
/* 89 */     return this.key.compareToIgnoreCase(another.key);
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\Attribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */