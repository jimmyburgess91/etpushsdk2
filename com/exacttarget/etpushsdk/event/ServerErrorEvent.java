/*    */ package com.exacttarget.etpushsdk.event;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class ServerErrorEvent
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String message;
/*    */   private String documentation;
/*    */   private Integer errorcode;
/*    */   
/*    */   public String getMessage()
/*    */   {
/* 35 */     return this.message;
/*    */   }
/*    */   
/*    */   public void setMessage(String message) {
/* 39 */     this.message = message;
/*    */   }
/*    */   
/*    */   public String getDocumentation() {
/* 43 */     return this.documentation;
/*    */   }
/*    */   
/*    */   public void setDocumentation(String documentation) {
/* 47 */     this.documentation = documentation;
/*    */   }
/*    */   
/*    */   public Integer getErrorcode() {
/* 51 */     return this.errorcode;
/*    */   }
/*    */   
/*    */   public void setErrorcode(Integer errorcode) {
/* 55 */     this.errorcode = errorcode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\event\ServerErrorEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */