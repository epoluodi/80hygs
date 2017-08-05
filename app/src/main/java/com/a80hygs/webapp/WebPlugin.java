package com.a80hygs.webapp;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;

/**
 * Created by yangxiaoguang on 2017/8/5.
 */

public class WebPlugin {
    private DWebView dWebView;
    private MainActivity mainActivity;
    private static Map<String, String> classMap = new HashMap<>();

    public WebPlugin(DWebView dWebView, MainActivity mainActivity) {
        this.dWebView = dWebView;
        this.mainActivity = mainActivity;

    }

    /**
     * 新增的插件需要在这个类中添加
     */
    public static void addClassMap(String k, String className) {
        classMap.put(k,className);
    }

    public DWebView getdWebView() {
        return dWebView;
    }

    public MainActivity getBaseActivity() {
        return mainActivity;
    }





    @JavascriptInterface
    public void openScan(JSONObject jsonObject,CompletionHandler completionHandler) {
        mainActivity.openScan(completionHandler);
        return ;
    }





}
