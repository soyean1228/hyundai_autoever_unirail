package com.easysubway;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.go.seoul.trafficsubway.Common.BaseActivity;

public class MainActivity extends BaseActivity {

    boolean is_theme_white = false;

    private Button black_theme;
    private Button white_theme;
    //private Button search;
    private Button search_route;
    private Button start_activity;
    private RelativeLayout Rlayout;
    static  final int GET_STRING = 1;

    private TextView textmain;
    public static Context mContext;

    public MainActivity()
    {
        openAPIKey = "71536a5044736861373274514e706b";
        subwayLocationAPIKey = "71536a5044736861373274514e706b";

    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Rlayout = findViewById(R.id.Rlayout);
        black_theme = findViewById(R.id.black_theme);
        white_theme = findViewById(R.id.white_theme);
        //search_route = findViewById(R.id.start_search1);
        search_route = findViewById(R.id.search_route);
        textmain = findViewById(R.id.textmain);
        start_activity=findViewById(R.id.start_activity);

        search_route.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent=new Intent(MainActivity.this,searchRoute.class);
                        intent.putExtra("is_theme_white", is_theme_white);
                        startActivity(intent);
                    }
            }
        );

        black_theme.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                is_theme_white = false;
                                black_theme.setBackgroundColor(Color.BLACK);
                                white_theme.setBackgroundColor(Color.BLACK);
                                textmain.setTextColor(Color.WHITE);
                                textmain.setBackgroundColor(Color.BLACK);
                                search_route.setBackgroundColor(Color.BLACK);
                                search_route.setTextColor(Color.WHITE);
                                Rlayout.setBackgroundColor(Color.BLACK);
                                black_theme.setTextColor(Color.WHITE);
                                white_theme.setTextColor(Color.WHITE);
                                start_activity.setTextColor(Color.WHITE);
                                start_activity.setBackgroundColor(Color.BLACK);
                            }
                        }
                );
        white_theme.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                is_theme_white = true;
                                black_theme.setBackgroundColor(Color.WHITE);
                                white_theme.setBackgroundColor(Color.WHITE);
                                textmain.setTextColor(Color.BLACK);
                                textmain.setBackgroundColor(Color.WHITE);
                                search_route.setBackgroundColor(Color.WHITE);
                                search_route.setTextColor(Color.BLACK);
                                Rlayout.setBackgroundColor(Color.WHITE);
                                black_theme.setTextColor(Color.BLACK);
                                white_theme.setTextColor(Color.BLACK);
                                start_activity.setTextColor(Color.BLACK);
                                start_activity.setBackgroundColor(Color.WHITE);
                            }
                        }
                );

        mContext = this;
        textmain = findViewById(R.id.textmain);
        Button button = (Button)findViewById(R.id.search_route);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, searchRoute.class);
                intent.putExtra("is_theme_white", is_theme_white);
                startActivityForResult(intent, GET_STRING);
            }
        });

        if(getIntent() != null && getIntent().getStringExtra("OpenAPIKey") != null)
            openAPIKey = getIntent().getStringExtra("OpenAPIKey");
        if(getIntent() != null && getIntent().getStringExtra("SubwayLocationAPIKey") != null)
            subwayLocationAPIKey = getIntent().getStringExtra("SubwayLocationAPIKey");
        initView();
        start_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("boolean-keyword", true);

                startActivity(intent);


            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == GET_STRING){
            if(resultCode == RESULT_OK){
                textmain.setText(data.getStringExtra("INPUT_TEXT"));
            }
        }
    }

    private void initView()
    {
        btnBackSubway = (ImageView)findViewById(R.id.btn_back_subway);
        btnBackSubway.setOnClickListener(new android.view.View.OnClickListener()
                                         {

                                             public void onClick(View view)
                                             {
                                                 finish();
                                             }

                                             final MainActivity this$0;

                                             {
                                                 this.this$0 = MainActivity.this;
                                                 // super();
                                             }
                                         }
        );
        lineMapWebview = (WebView)findViewById(R.id.line_map_webview);
        lineMapWebview.setWebViewClient(new WebViewClient());
        lineMapWebview.getSettings().setJavaScriptEnabled(true);
        lineMapWebview.getSettings().setBuiltInZoomControls(true);
        lineMapWebview.getSettings().setSupportZoom(true);
        lineMapWebview.getSettings().setDisplayZoomControls(false);
        lineMapWebview.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebViewInterface = new WebViewInterface(this, lineMapWebview, openAPIKey, subwayLocationAPIKey);
        lineMapWebview.addJavascriptInterface(mWebViewInterface, "Android");
        lineMapWebview.loadUrl("file:///android_asset/mSeoul_Subway.html");
    }


    private String openAPIKey;
    private String subwayLocationAPIKey;
    private ImageView btnBackSubway;
    private WebView lineMapWebview;
    private WebViewInterface mWebViewInterface;
}

