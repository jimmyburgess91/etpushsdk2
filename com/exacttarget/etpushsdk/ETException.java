/*    */ package com.exacttarget.etpushsdk;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ETException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ETException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ETException(String detailMessage, Throwable throwable)
/*    */   {
/* 26 */     super(detailMessage, throwable);
/*    */   }
/*    */   
/*    */   public ETException(String detailMessage) {
/* 30 */     super(detailMessage);
/*    */   }
/*    */   
/*    */   public ETException(Throwable throwable) {
/* 34 */     super(throwable);
/*    */   }
/*    */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */