package com.ah.mis_1;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity {
    WebView webview;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.load_btn);
        final EditText urlText = (EditText)findViewById(R.id.url_txt);
        this.webview = (WebView)findViewById(R.id.web_view);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage(urlText.getText().toString());
            }
        });
    }


    public void loadPage(final String strUrl) {
        final MainActivity context = this;

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(strUrl);
                    URLConnection urlConnection = url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    StringBuilder sb  = new StringBuilder();
                    byte[] contents = new byte[1024];
                    int bytesRead = 0;
                    while( (bytesRead = in.read(contents)) != -1){
                        String str = new String(contents, 0, bytesRead);
                        sb.append(str);
                    }
                    context.showWebViewContent(sb.toString());
                } catch (Exception e) {
                    context.showNotification(e.getMessage());
                }
            }
        });

        this.progressDialog = ProgressDialog.show(this, "Waite!", "Loading page content!!");
        th.start();
    }

    public void showWebViewContent(final String content) {
        final MainActivity ma = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ma.progressDialog.hide();
                ma.webview.loadData(content, "text/html", "utf8");
            }
        });
    }

    public void showNotification(final String n) {
        final MainActivity mc = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mc.progressDialog.hide();
                Toast.makeText(getApplicationContext(), n, Toast.LENGTH_LONG).show();
            }
        });
    }
}
