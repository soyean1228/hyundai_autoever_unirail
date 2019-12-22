package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    //private static final String dataPath = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway?ServiceKey=FgYWtyQY6EGgb5Yl4%2B1jT5cmRUYrK1Ht%2BetulrZ0YCKnSCoh%2FzgAXkh8r3ukrvo6Qpheo7ruYP5TMNJE5XA8PA%3D%3D&startX=126.83948388112836&startY=37.558210971753226&endX=127.01460762172958&endY=37.57250";
    TextView finalStation;
    TextView startStation;
    TextView time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        finalStation = (TextView)findViewById(R.id.finalStation);
        startStation = (TextView)findViewById(R.id.startStation);
        time = (TextView)findViewById(R.id.time);
        openApi subwayApi = new openApi();
        subwayApi.execute();
    }

    public class openApi extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //finalStation.setText(s);
            startStation.setText(s);
            //time.setText(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            URL url;
            try {
//                StringBuilder urlBuilder = new StringBuilder("http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway"); /*URL*/
//                urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=FgYWtyQY6EGgb5Yl4%2B1jT5cmRUYrK1Ht%2BetulrZ0YCKnSCoh%2FzgAXkh8r3ukrvo6Qpheo7ruYP5TMNJE5XA8PA%3D%3D"); /*Service Key*/
//                urlBuilder.append("&" + URLEncoder.encode("startX", "UTF-8") + "=" + URLEncoder.encode("126.83948388112836", "UTF-8")); /*출발지X좌표*/
//                urlBuilder.append("&" + URLEncoder.encode("startY", "UTF-8") + "=" + URLEncoder.encode("37.558210971753226", "UTF-8")); /*출발지Y좌표*/
//                urlBuilder.append("&" + URLEncoder.encode("endX", "UTF-8") + "=" + URLEncoder.encode("127.01460762172958", "UTF-8")); /*목적지X좌표*/
//                urlBuilder.append("&" + URLEncoder.encode("endY", "UTF-8") + "=" + URLEncoder.encode("37.57250", "UTF-8")); /*목적지Y좌표*/
//                URL url = new URL(urlBuilder.toString());
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("Content-type", "application/json");
//                System.out.println("Response code: " + conn.getResponseCode());
//                BufferedReader rd;
//                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                } else {
//                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                }
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    sb.append(line);
//                }
//                rd.close();
//                conn.disconnect();
//                System.out.println(sb.toString());
//                s = sb.toString();
//                return s;
                url = new URL("http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway?ServiceKey=FgYWtyQY6EGgb5Yl4%2B1jT5cmRUYrK1Ht%2BetulrZ0YCKnSCoh%2FzgAXkh8r3ukrvo6Qpheo7ruYP5TMNJE5XA8PA%3D%3D&startX=126.83948388112836&startY=37.558210971753226&endX=127.01460762172958&endY=37.57250");
                InputStream is= url.openStream(); //url위치로 입력스트림 연결
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();
                xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
                String tag;
                xpp.next();
                int eventType= xpp.getEventType();
                int find = 0;
                int start = 0;
                while( eventType != XmlPullParser.END_DOCUMENT ){
                    if(find == 1){break;};
                    switch( eventType ){
                        case XmlPullParser.START_DOCUMENT:
                            buffer.append("파싱 시작...\n\n");
                            break;
                        case XmlPullParser.START_TAG:
                            tag= xpp.getName();//테그 이름 얻어오기
                            if(tag.equals("pathList")) ;// 첫번째 검색결과
                            else if(start == 0 && tag.equals("fname")){
                                buffer.append("출발역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                start = 1;
                                break;
                            }
                            else if(start == 1 && tag.equals("fname")){
                                buffer.append("환승역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                break;
                            }
                            else if(tag.equals("tname")){
                                buffer.append("도착역 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                buffer.append("\n"); //줄바꿈 문자 추가
                                break;
                            }
                            else if(tag.equals("time")){
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
                            tag= xpp.getName(); //테그 이름 얻어오기

                            if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                            break;
                    }
                    eventType= xpp.next();
                }
                return buffer.toString();
            } catch (Exception e) {
                System.out.println("failed");
            }
            return "";
        }
    }

//    public class getXMLTask extends AsyncTask<String,Void,Document>{
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Document document) {
//            super.onPostExecute(document);
//        }
//
//        @Override
//        protected Document doInBackground(String... strings) {
//            return null;
//        }
//    }

}
