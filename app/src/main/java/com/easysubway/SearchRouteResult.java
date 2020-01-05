package com.easysubway;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


import kr.go.seoul.trafficsubway.Common.BaseActivity;

public class SearchRouteResult extends BaseActivity {
    private String openAPIKey = "70575677706d696333327152507752";
    private String subwayLocationAPIKey = "70575677706d696333327152507752";
    private String startStation = "";
    private String finalStation = "";
    private TextView result;
    public String startX = "";
    public String startY = "";
    public String finalX = "";
    public String finalY = "";
    public String startCode = "";
    public String finalCode = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras=getIntent().getExtras();
        boolean is_theme_white;
        is_theme_white = extras.getBoolean("is_theme_white");
        if(extras.getBoolean("is_theme_white")==false)
        {
            setContentView(R.layout.search_route_result_black);
            is_theme_white = false;
        }
        else
        {
            setContentView(R.layout.search_route_result);
            is_theme_white = true;
        }
        result = (TextView)findViewById(R.id.result);

        if(this.getIntent() != null && this.getIntent().getStringExtra("OpenAPIKey") != null) {
            this.openAPIKey = this.getIntent().getStringExtra("OpenAPIKey");
        }

        if(this.getIntent() != null && this.getIntent().getStringExtra("SubwayLocationAPIKey") != null) {
            this.subwayLocationAPIKey = this.getIntent().getStringExtra("SubwayLocationAPIKey");
        }

        if(this.getIntent() != null && this.getIntent().getStringExtra("startStation") != null) {
            this.startStation = this.getIntent().getStringExtra("startStation");
        }

        if(this.getIntent() != null && this.getIntent().getStringExtra("finalStation") != null) {
            this.finalStation = this.getIntent().getStringExtra("finalStation");
        }

        System.out.println("--------------------------");
        System.out.println(this.startStation);
        System.out.println(this.finalStation);

        openAddressAPI addressAPI = new openAddressAPI(); //역에 따른 주소를 넘겨주는 api
        try {
            String str = addressAPI.execute(startStation,finalStation).get();
            System.out.println("--------------역코드------------------");
            String[] array = str.split("/");
            startCode = array[0];
            finalCode = array[1];
            System.out.println(startCode);
            System.out.println(finalCode);
        }catch(Exception e){
            e.printStackTrace();
        }

        wgsAddressAPI wgsApi = new wgsAddressAPI();
        try {
            String str = wgsApi.execute(startCode,finalCode).get();
            System.out.println("-----------------좌표--------------------");
            String[] array = str.split("/");
            startX = array[0];
            startY = array[1];
            finalX = array[2];
            finalY = array[3];
            System.out.println(startX);
            System.out.println(startY);
            System.out.println(finalX);
            System.out.println(finalY);
        }catch(Exception e){
            e.printStackTrace();
        }

        openAPI subwayApi = new openAPI(); //받은 주소로 경로를 탐색하는 api
        subwayApi.execute(startX,startY,finalX,finalY);

    }

    class wgsAddressAPI extends AsyncTask<String, Void, String> {
        URL startUrl = null;
        URL finalUrl = null;
        protected String doInBackground(String... strings) {
            String s = this.executeClient(strings);
            System.out.println(s);
            return s;
        }

        String executeClient(String[] str) {

            StringBuffer buffer = new StringBuffer();
            try {
                StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088/70575677706d696333327152507752/xml/SearchLocationOfSTNByIDService/1/5/");
                urlBuilder.append(URLEncoder.encode(str[0], "UTF-8") + "/"); //출발지
                startUrl = new URL(urlBuilder.toString());
                InputStream is= startUrl.openStream(); //url위치로 입력스트림 연결
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();
                xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
                String tag;
                xpp.next();
                int eventType= xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) { ;
                    switch( eventType ){
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();//테그 이름 얻어오기
                            if (tag.equals("row")) ;// 첫번째 검색결과
                            else if (tag.equals("XPOINT_WGS")) {
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                break;
                            }else if (tag.equals("YPOINT_WGS")) {
                                xpp.next();
                                buffer.append("/");
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                break;
                            }
                    }
                    eventType = xpp.next();
                }

                urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088/70575677706d696333327152507752/xml/SearchLocationOfSTNByIDService/1/5/");
                urlBuilder.append(URLEncoder.encode(str[1], "UTF-8") + "/"); //출발지
                startUrl = new URL(urlBuilder.toString());
                is= startUrl.openStream(); //url위치로 입력스트림 연결
                factory= XmlPullParserFactory.newInstance();
                xpp= factory.newPullParser();
                xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
                xpp.next();
                eventType= xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) { ;
                    switch( eventType ){
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();//테그 이름 얻어오기
                            if (tag.equals("row")) ;// 첫번째 검색결과
                            else if (tag.equals("XPOINT_WGS")) {
                                xpp.next();
                                buffer.append("/");
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                break;
                            }else if (tag.equals("YPOINT_WGS")) {
                                xpp.next();
                                buffer.append("/");
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                break;
                            }
                            break;
                    }
                    eventType = xpp.next();
                }
                return buffer.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }
    }

    public class openAddressAPI extends AsyncTask<String, String, String> {
        URL startUrl = null;
        URL finalUrl = null;
        protected String doInBackground(String... strings) {
            String s = this.executeClient(strings);
            System.out.println(s);
            return s;
        }

        String executeClient(String[] str) {

            StringBuffer buffer = new StringBuffer();

            try {
                StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088/70575677706d696333327152507752/xml/SearchSTNBySubwayLineInfo/1/5/ /");
                urlBuilder.append(URLEncoder.encode(str[0], "UTF-8")); //출발지
                startUrl = new URL(urlBuilder.toString());
                InputStream is= startUrl.openStream(); //url위치로 입력스트림 연결
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();
                xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
                String tag;
                xpp.next();
                int eventType= xpp.getEventType();
                int start = 1;
                while (eventType != XmlPullParser.END_DOCUMENT) { ;
                    switch( eventType ){
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();//테그 이름 얻어오기
                            if (tag.equals("row")) ;// 첫번째 검색결과
                            else if (tag.equals("STATION_CD")) {
                                buffer = new StringBuffer();
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                break;
                            }
                            break;
                    }
                    eventType = xpp.next();
                }

                StringBuffer finalbuffer = new StringBuffer();
                urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088/70575677706d696333327152507752/xml/SearchSTNBySubwayLineInfo/1/5/ /");
                urlBuilder.append(URLEncoder.encode(str[1], "UTF-8")); //출발지
                startUrl = new URL(urlBuilder.toString());
                is= startUrl.openStream(); //url위치로 입력스트림 연결
                factory= XmlPullParserFactory.newInstance();
                xpp= factory.newPullParser();
                xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
                xpp.next();
                eventType= xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) { ;
                    switch( eventType ){
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();//테그 이름 얻어오기
                            if (tag.equals("row")) ;// 첫번째 검색결과
                            else if (tag.equals("STATION_CD")) {
                                finalbuffer = new StringBuffer();
                                xpp.next();
                                finalbuffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                break;
                            }
                            break;
                    }
                    eventType = xpp.next();
                }
                buffer.append("/");
                buffer.append(finalbuffer.toString());
                return buffer.toString();
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }
    }

    public class openAPI extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result.setText(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            URL url;
            try {
                //url = new URL("http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway?ServiceKey=FgYWtyQY6EGgb5Yl4%2B1jT5cmRUYrK1Ht%2BetulrZ0YCKnSCoh%2FzgAXkh8r3ukrvo6Qpheo7ruYP5TMNJE5XA8PA%3D%3D&startX=126.83948388112836&startY=37.558210971753226&endX=127.01460762172958&endY=37.57250");
                ////
                StringBuilder urlBuilder = new StringBuilder("http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway");
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=FgYWtyQY6EGgb5Yl4%2B1jT5cmRUYrK1Ht%2BetulrZ0YCKnSCoh%2FzgAXkh8r3ukrvo6Qpheo7ruYP5TMNJE5XA8PA%3D%3D"); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("startX","UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8")); /*출발지X좌표*/
                urlBuilder.append("&" + URLEncoder.encode("startY","UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8")); /*출발지Y좌표*/
                urlBuilder.append("&" + URLEncoder.encode("endX","UTF-8") + "=" + URLEncoder.encode(strings[3], "UTF-8")); /*목적지X좌표*/
                urlBuilder.append("&" + URLEncoder.encode("endY","UTF-8") + "=" + URLEncoder.encode(strings[2], "UTF-8")); /*목적지Y좌표*/
                url = new URL(urlBuilder.toString());
                ////
                InputStream is = url.openStream(); //url위치로 입력스트림 연결
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기
                String tag;
                xpp.next();
                int eventType = xpp.getEventType();
                int find = 0;
                int start = 0;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (find == 1) {
                        break;
                    }
                    ;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            buffer.append("파싱 시작...\n\n");
                            break;
                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();//테그 이름 얻어오기
                            if (tag.equals("pathList")) ;// 첫번째 검색결과
                            else if (start == 0 && tag.equals("fname")) {
                                buffer.append("출발역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                start = 1;
                                break;
                            } else if (start == 1 && tag.equals("fname")) {
                                buffer.append("환승역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                break;
                            } else if (tag.equals("tname")) {
                                buffer.append("도착역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                break;
                            } else if (tag.equals("time")) {
                                buffer.append("소요시간 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                find = 1;
                                break;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag = xpp.getName(); //테그 이름 얻어오기
                            if (tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                            break;
                    }
                    eventType = xpp.next();
                }
                return buffer.toString();
            } catch (Exception e) {
                System.out.println("failed");
            }
            return "";
        }
    }
}
