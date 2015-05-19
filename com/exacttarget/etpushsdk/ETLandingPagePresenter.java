/*     */ package com.exacttarget.etpushsdk;
/*     */ 
/*     */ import android.app.Activity;
/*     */ import android.content.Intent;
/*     */ import android.os.Bundle;
/*     */ import android.view.View;
/*     */ import android.view.View.OnClickListener;
/*     */ import android.webkit.WebSettings;
/*     */ import android.webkit.WebView;
/*     */ import android.webkit.WebViewClient;
/*     */ import android.widget.Button;
/*     */ import android.widget.LinearLayout;
/*     */ import android.widget.LinearLayout.LayoutParams;
/*     */ import com.exacttarget.etpushsdk.util.ETAmazonDeviceMessagingUtil;
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
/*     */ public class ETLandingPagePresenter
/*     */   extends Activity
/*     */ {
/*     */   public void onCreate(Bundle savedInstanceState)
/*     */   {
/*  49 */     super.onCreate(savedInstanceState);
/*  50 */     setTitle("Loading...");
/*     */     
/*  52 */     LinearLayout ll = new LinearLayout(this);
/*  53 */     ll.setOrientation(1);
/*  54 */     ll.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
/*  55 */     ll.setGravity(17);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  60 */     if (ETAmazonDeviceMessagingUtil.isAmazonDevice()) {
/*  61 */       addDoneButton(ll);
/*     */     }
/*     */     
/*     */     String website;
/*     */     String website;
/*  66 */     if (getIntent().getExtras().containsKey("_x")) {
/*  67 */       website = getIntent().getExtras().getString("_x"); } else { String website;
/*  68 */       if (getIntent().getExtras().containsKey("_od")) {
/*  69 */         website = getIntent().getExtras().getString("_od");
/*     */       } else {
/*  71 */         website = null;
/*  72 */         setTitle("No website URL found in payload.");
/*     */       }
/*     */     }
/*  75 */     if (website != null) {
/*  76 */       WebView webView = new WebView(this);
/*  77 */       LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
/*     */       
/*     */ 
/*     */ 
/*  81 */       layoutParams.weight = 0.9F;
/*  82 */       webView.setLayoutParams(layoutParams);
/*  83 */       webView.loadUrl(website);
/*  84 */       webView.getSettings().setJavaScriptEnabled(true);
/*  85 */       ll.addView(webView);
/*     */       
/*  87 */       webView.setWebViewClient(new WebViewClient()
/*     */       {
/*     */         public void onPageFinished(WebView view, String url)
/*     */         {
/*  91 */           ETLandingPagePresenter.this.setTitle(view.getTitle());
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*  96 */     setContentView(ll);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Button addDoneButton(LinearLayout linearLayout)
/*     */   {
/* 107 */     if (linearLayout == null)
/*     */     {
/* 109 */       return null;
/*     */     }
/* 111 */     Button doneButton = new Button(this);
/* 112 */     doneButton.setText("DONE");
/* 113 */     LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
/*     */     
/*     */ 
/*     */ 
/* 117 */     layoutParams.gravity = 3;
/* 118 */     layoutParams.weight = 0.1F;
/* 119 */     doneButton.setLayoutParams(layoutParams);
/* 120 */     doneButton.setOnClickListener(new View.OnClickListener()
/*     */     {
/*     */       public void onClick(View v) {
/* 123 */         ETLandingPagePresenter.this.finish();
/*     */       }
/* 125 */     });
/* 126 */     linearLayout.addView(doneButton);
/* 127 */     return doneButton;
/*     */   }
/*     */ }


/* Location:              C:\Users\Jimmy\Downloads\etsdk-3.5.0.jar!\com\exacttarget\etpushsdk\ETLandingPagePresenter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */