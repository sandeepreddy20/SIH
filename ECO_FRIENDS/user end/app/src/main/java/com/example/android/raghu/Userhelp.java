package com.example.android.raghu;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class Userhelp extends AppCompatActivity {

    private WebView webView;
    Button btn,btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhelp);
     // btn.setOnClickListener(new View.OnClickListener() {
       //   @Override

      //});

     // btn1.setOnClickListener(new View.OnClickListener() {
      //    @Override

   //   });
        webView = findViewById(R.id.WebView);

    }
    public void openc(View v) {
        /*String url = "http://cpcb.nic.in/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);*/
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://cpcb.nic.in/");
    }

    public void openy(View v) {
        String url = "https://www.youtube.com/watch?v=-ZJmOJf-1gE";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
      /*  webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.youtube.com/watch?v=-ZJmOJf-1gE");*/

    }
}
