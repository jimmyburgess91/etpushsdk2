/*    */ package com.exacttarget.etpushsdk.data;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.ArrayList;
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
/*    */ public class CloudPagesResponse
/*    */ {
/*    */   @JsonProperty("messages")
/*    */   private ArrayList<Message> messages;
/*    */   
/*    */   public ArrayList<Message> getMessages()
/*    */   {
/* 23 */     return this.messages;
/*    */   }
/*    */   
/*    */   public void setMessages(ArrayList<Message> messages) {
/* 27 */     this.messages = messages;
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\data\CloudPagesResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */