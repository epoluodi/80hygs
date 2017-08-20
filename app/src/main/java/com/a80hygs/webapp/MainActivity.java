package com.a80hygs.webapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.a80hygs.webapp.Scan.ScanActivity;

import org.json.JSONObject;

import java.security.Permission;

import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;

public class MainActivity extends AppCompatActivity {

    protected DWebView dWebView;

    private CompletionHandler completionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dWebView = (DWebView) findViewById(R.id.dwebview);
        dWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        dWebView.setDownloadListener(downloadListener);
        dWebView.setJavascriptInterface(new WebPlugin(dWebView,this));
        dWebView.setWebViewClient(webViewClient);//设置自己webviewclient
        dWebView.getSettings().setAllowFileAccess(true);


        if (Build.VERSION.SDK_INT>23) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)
            {}
            else
            {
//                this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                this.requestPermissions(new String[]{Manifest.permission.CAMERA},1);
            }
        }else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                //
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        1);
            }
        }



//        file:///android_asset/www/index.html
        dWebView.loadUrl("http://80hygs.com");
//        dWebView.loadUrl("file:///android_asset/www/index.html");
    }



    private DownloadListener downloadListener=new DownloadListener() {
        @Override
        public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
            Uri uri = Uri.parse(s);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(MainActivity.this,"你无法使用扫码功能",Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    /**
     * 自定义web client
     */
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

            String url=  request.getUrl().toString();
            return super.shouldInterceptRequest(view, request);
        }




        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            Log.i("url 地址",view.getUrl());
            if (Build.VERSION.SDK_INT >= 21) {
                view.loadUrl(request.getUrl().toString());
            }else
                view.loadUrl(view.getUrl().toString());

            return super.shouldOverrideUrlLoading(view, request);
        }




    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4)
        {
            if (dWebView.canGoBack())
                dWebView.goBack();
            else
                finish();
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void openScan(CompletionHandler completionHandler)
    {
        Intent intent = new Intent(this, ScanActivity.class);
        this.completionHandler = completionHandler;
        startActivityForResult(intent, ScanActivity.SCANRESULTREQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==ScanActivity.SCANRESULTREQUEST)
        {
            if (resultCode ==1)
            {

                String code =data.getExtras().getString("code");
                Log.i("onActivityResult 回调 " ,code);
                completionHandler.complete(code);
                completionHandler=null;
            }
            return;
        }



    }
}
