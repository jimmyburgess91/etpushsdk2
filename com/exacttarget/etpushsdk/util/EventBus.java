/*     */ package com.exacttarget.etpushsdk.util;
/*     */ 
/*     */ import android.util.Log;
/*     */ import com.exacttarget.etpushsdk.ETPush;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ public class EventBus
/*     */ {
/*     */   private static final String TAG = "jb4ASDK@EventBus";
/*  61 */   private static final EventBus eventBus = new EventBus();
/*     */   
/*  63 */   private List<Object> listenerRegistry = new ArrayList();
/*  64 */   private Map<String, List<ListenerMethod>> registry = new HashMap();
/*  65 */   private Map<String, Object> stickyEvents = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EventBus getDefault()
/*     */   {
/*  76 */     return eventBus;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void register(Object listener)
/*     */   {
/*  84 */     synchronized ("jb4ASDK@EventBus") {
/*  85 */       if (!this.listenerRegistry.contains(listener)) {
/*  86 */         this.listenerRegistry.add(listener);
/*  87 */         Class<?> clazz = listener.getClass();
/*  88 */         Method[] methods = clazz.getMethods();
/*  89 */         for (Method method : methods) {
/*  90 */           if (method.getName().startsWith("onEvent")) {
/*  91 */             Class<?>[] paramTypes = method.getParameterTypes();
/*  92 */             Class<?> paramType = paramTypes[0];
/*  93 */             String eventType = paramType.getName();
/*  94 */             if (!this.registry.containsKey(eventType)) {
/*  95 */               this.registry.put(eventType, new ArrayList());
/*     */             }
/*  97 */             List<ListenerMethod> listeners = (List)this.registry.get(eventType);
/*  98 */             listeners.add(new ListenerMethod(listener, method, null));
/*     */             
/* 100 */             if (this.stickyEvents.containsKey(eventType)) {
/* 101 */               Object stickyEvent = this.stickyEvents.get(eventType);
/*     */               try {
/* 103 */                 if (ETPush.getLogLevel() <= 3) {
/* 104 */                   Log.d("jb4ASDK@EventBus", "Calling: " + listener.getClass().getName() + " - " + method.getName());
/*     */                 }
/* 106 */                 method.invoke(listener, new Object[] { stickyEvent });
/*     */               }
/*     */               catch (Throwable e) {
/* 109 */                 if (ETPush.getLogLevel() <= 6) {
/* 110 */                   Log.e("jb4ASDK@EventBus", e.getMessage(), e);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unregister(Object listener)
/*     */   {
/* 125 */     synchronized ("jb4ASDK@EventBus") {
/* 126 */       if (this.listenerRegistry.contains(listener)) {
/* 127 */         this.listenerRegistry.remove(listener);
/* 128 */         Class<?> clazz = listener.getClass();
/* 129 */         Method[] methods = clazz.getMethods();
/* 130 */         for (Method method : methods) {
/* 131 */           if (method.getName().startsWith("onEvent")) {
/* 132 */             Class<?>[] paramTypes = method.getParameterTypes();
/* 133 */             Class<?> paramType = paramTypes[0];
/* 134 */             String eventType = paramType.getName();
/* 135 */             if (this.registry.containsKey(eventType)) {
/* 136 */               List<ListenerMethod> listeners = (List)this.registry.get(eventType);
/* 137 */               List<ListenerMethod> toRemove = new ArrayList();
/* 138 */               for (ListenerMethod listenerMethod : listeners) {
/* 139 */                 if (listenerMethod.listener.equals(listener)) {
/* 140 */                   toRemove.add(listenerMethod);
/*     */                 }
/*     */               }
/* 143 */               listeners.removeAll(toRemove);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void post(Object event)
/*     */   {
/* 156 */     if (event == null) {
/* 157 */       if (ETPush.getLogLevel() <= 5) {
/* 158 */         Log.w("jb4ASDK@EventBus", "Null Object passed to EventBus.post()");
/*     */       }
/* 160 */       return;
/*     */     }
/* 162 */     synchronized ("jb4ASDK@EventBus") {
/* 163 */       List<ListenerMethod> listeners = (List)this.registry.get(event.getClass().getName());
/* 164 */       if (listeners != null) {
/* 165 */         for (ListenerMethod listener : listeners) {
/*     */           try {
/* 167 */             if (ETPush.getLogLevel() <= 3) {
/* 168 */               Log.d("jb4ASDK@EventBus", "Calling: " + listener.listener.getClass().getName() + " - " + listener.method.getName());
/*     */             }
/* 170 */             listener.method.invoke(listener.listener, new Object[] { event });
/*     */           }
/*     */           catch (Throwable e) {
/* 173 */             if (ETPush.getLogLevel() <= 6) {
/* 174 */               Log.e("jb4ASDK@EventBus", e.getMessage(), e);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void postSticky(Object event)
/*     */   {
/* 188 */     if (event == null) {
/* 189 */       if (ETPush.getLogLevel() <= 5) {
/* 190 */         Log.w("jb4ASDK@EventBus", "Null Object passed to EventBus.postSticky()");
/*     */       }
/* 192 */       return;
/*     */     }
/*     */     
/* 195 */     post(event);
/*     */     
/*     */ 
/* 198 */     this.stickyEvents.put(event.getClass().getName(), event);
/*     */   }
/*     */   
/*     */   private class ListenerMethod {
/*     */     public Object listener;
/*     */     public Method method;
/*     */     
/* 205 */     private ListenerMethod(Object listener, Method method) { this.listener = listener;
/* 206 */       this.method = method;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getStickyEvent(Class<?> clazz)
/*     */   {
/* 216 */     return this.stickyEvents.get(clazz.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\util\EventBus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */