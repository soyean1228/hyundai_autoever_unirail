package com.unirail;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by eunhye Lee on 2017-08-08.
 */

public class WebViewInterface {
     WebView mAppView;
     boolean is_theme_white;
     static Activity mContext;
     static String openAPIKey;
     static String subwayLocationAPIKey;
    
     public WebViewInterface(Activity activity, WebView view, String openAPIKey, String subwayLocationAPIKey) {
        this.mAppView = view;
        this.mContext = activity;
        this.openAPIKey = openAPIKey;
        this.subwayLocationAPIKey = subwayLocationAPIKey;
    }

    public void changeTheme(boolean is_white){
         this.is_theme_white = is_white;
    }

    @JavascriptInterface
    public void showSubwayInfo(String station) {
        Intent intent = new Intent(this.mContext, TrafficSubwayInfo.class);
        intent.putExtra("OpenAPIKey", this.openAPIKey);
        intent.putExtra("SubwayLocationAPIKey", this.subwayLocationAPIKey);
        intent.putExtra("StationNM", station);
        intent.putExtra("is_theme_white", is_theme_white);
        if(is_theme_white==false)
        {
            System.out.println("false=하양");
        }
        else {
            System.out.println("true=검정");
        }
        this.mContext.startActivity(intent);
    }
}
