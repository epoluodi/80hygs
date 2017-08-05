package com.a80hygs.webapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.a80hygs.webapp.Scan.ScanActivity;

import org.json.JSONObject;

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
        dWebView.setJavascriptInterface(new WebPlugin(dWebView,this));
        dWebView.setWebViewClient(webViewClient);//设置自己webviewclient
//        file:///android_asset/www/index.html
//        dWebView.loadUrl("http://80hygs.com");
        dWebView.loadUrl("file:///android_asset/www/index.html");
    }


    /**
     * 自定义web client
     */
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
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
