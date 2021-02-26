package com.example.c_and_b_guidbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Content extends AppCompatActivity {
    ImageView gameimg;
    WebView webView;
    TextView gameinfo;
    Map<String, Object> map = new HashMap<>();

    //WebView webView = new WebView();

    class ThreadEx extends Thread {


        public void run() {
            try {
                //URL 만들기
                //호출하는 인텐트 가져오기
                Intent intent = getIntent();
                StringBuilder sb = new StringBuilder();
                //gamecode 값을 정수로 가져오고 없을 때 1
                String gamecode = intent.getStringExtra("gamecode");
                String gametype = intent.getStringExtra("gametype");
                URL url;
                if(gamecode.equals("poker")){
                    url = new URL("http://yskim621.cafe24.com/game/" + gametype);
                } else{
                    url = new URL("http://yskim621.cafe24.com/game/" + gametype + "/" + gamecode);
                }

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setConnectTimeout(30000);

                //문자열 읽는 객체를 생성
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line + "\n");
                }
                br.close();
                con.disconnect();


                switch (gamecode) {
                    case "poker":
                        try {
                            //HTML parsing - html을 전부 펼쳐서 DOM 객체로 생성
                            Document document = Jsoup.parse(sb.toString());

                            //원하는 항목 가져오기
                            Elements elements1 = document.getElementsByClass("thumb no6");
                            Elements elements2 = document.getElementsByClass("game-guide");
                            String result = "";

                            for (Element element : elements1) {
                                result += element.text();
                                result += "\n";
                            }
                            for (Element element : elements2) {
                                result += element.text();
                                result += "\n";
                            }


                            //출력하기 위해서 핸들러를 호출
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);

                        } catch (Exception e){
                            Log.e("poker", e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case "black_jack":
                        try {
                            JSONObject object = new JSONObject(sb.toString());
                            //Log.e("JsonData", object.toString());

                            JSONArray data = object.getJSONArray("result");
                            map.put("result", data.getString(0));

                            String temp = (String) map.get("result");
                            Log.e("temp", temp);


                            //HTML parsing - html을 전부 펼쳐서 DOM 객체로 생성
                            Document document = Jsoup.parse(temp);

                            //원하는 항목 가져오기
                            Elements elements1 = document.getElementsByClass("thumb no1");
                            Elements elements2 = document.getElementsByClass("game-guide first");
                            String result = "";

                            for (Element element : elements1) {
                                result += element.text();
                                result += "\n";
                            }
                            for (Element element : elements2) {
                                result += element.text();
                                result += "\n";
                            }


                            //출력하기 위해서 핸들러를 호출
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);

                        } catch (Exception e){
                            Log.e("black_jack", e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case "bacara":
                        try {
                            JSONObject object = new JSONObject(sb.toString());

                            JSONArray data = object.getJSONArray("result");
                            map.put("result", data.getString(0));

                            String temp = (String) map.get("result");
                            Log.e("temp", temp);

                            Document document = (Document) Jsoup.parse(temp);
                            Elements elements1 = document.getElementsByClass("thumb no2");
                            Elements elements2 = document.getElementsByClass("game-guide");
                            String result = "";

                            for (Element element : elements1) {
                                result += element.text();
                                result += "\n";
                            }
                            for (Element element : elements2) {
                                result += element.text();
                                result += "\n";
                            }

                            //출력하기 위해서 핸들러를 호출
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);

                        }catch (Exception e){
                            Log.e("bacara", e.getMessage());
                            e.printStackTrace();
                        }
                        break;

                    case "hoola":
                    case "gostop":
                    case "seosda":
                        try{
                            JSONObject object = new JSONObject(sb.toString());

                            JSONArray data = object.getJSONArray("result");
                            map.put("result", data.getString(0));

                            String temp = (String) map.get("result");
                            //Log.e("temp", temp);

                            Document document = (Document) Jsoup.parse(temp);
                            Elements elements1 = document.getElementsByClass("entry-content");
                            String result = "";

                            for (Element element : elements1) {
                                result += element.text();
                                result += "\n";
                            }

                            //출력하기 위해서 핸들러를 호출
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);


                        } catch (Exception e){
                            Log.e("error", gamecode + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case "chess":
                    case "omok":
                        try{
                            JSONObject object = new JSONObject(sb.toString());

                            JSONArray data = object.getJSONArray("result");
                            map.put("result", data.getString(0));

                            String temp = (String) map.get("result");
                            //Log.e("temp", temp);

                            Document document = (Document) Jsoup.parse(temp);
                            Elements elements1 = document.getElementsByClass("tt_article_useless_p_margin");
                            String result = "";

                            for (Element element : elements1) {
                                result += element.text();
                                result += "\n";
                            }

                            //출력하기 위해서 핸들러를 호출
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);

                        } catch (Exception e){
                            Log.e("error", gamecode + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case "go":
                        try{
                            JSONObject object = new JSONObject(sb.toString());

                            JSONArray data = object.getJSONArray("result");
                            map.put("result", data.getString(0));

                            String temp = (String) map.get("result");
                            //Log.e("temp", temp);

                            Document document = (Document) Jsoup.parse(temp);
                            Elements elements1 = document.getElementsByClass("scroll type3");
                            String result = "";

                            for (Element element : elements1) {
                                result += element.text();
                                result += "\n";
                            }

                            //출력하기 위해서 핸들러를 호출
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);

                        } catch (Exception e){
                            Log.e("error", gamecode + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case "janggi":
                        try{
                            JSONObject object = new JSONObject(sb.toString());

                            JSONArray data = object.getJSONArray("result");
                            map.put("result", data.getString(0));

                            String temp = (String) map.get("result");
                            //Log.e("temp", temp);

                            Document document = (Document) Jsoup.parse(temp);
                            Elements elements1 = document.getElementsByClass("mw-body-content");
                            String result = "";

                            for (Element element : elements1) {
                                result += element.text();
                                result += "\n";
                            }

                            //출력하기 위해서 핸들러를 호출
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);

                        } catch (Exception e){
                            Log.e("error", gamecode + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                }

            } catch (Exception e) {
                Log.e("다운로드 예외", e.getMessage());
            }
        }
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            String result = (String) message.obj;
            //Log.e("message.obj:", result);
            webView.loadData(result, "text/html", "UTF-8");

            Intent intent = getIntent();
            String gamecode = intent.getStringExtra("gamecode");
            switch (gamecode) {
                case "poker":
                    gameimg.setImageResource(R.drawable.poker);
                    break;
                case "black_jack":
                    gameimg.setImageResource(R.drawable.black_jack);
                    break;
                case "bacara":
                    gameimg.setImageResource(R.drawable.bacara);
                    break;
                case "hoola":
                    gameimg.setImageResource(R.drawable.hoola);
                    break;
                case "gostop":
                    gameimg.setImageResource(R.drawable.gostop);
                    break;
                case "seosda":
                    gameimg.setImageResource(R.drawable.seosda);
                    break;
                case "chess":
                    gameimg.setImageResource(R.drawable.chess);
                    break;
                case "go":
                    gameimg.setImageResource(R.drawable.go);
                    break;
                case "janggi":
                    gameimg.setImageResource(R.drawable.janggi);
                    break;
                case "omok":
                    gameimg.setImageResource(R.drawable.omok);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        webView = (WebView) findViewById(R.id.webview);
        gameinfo = (TextView) findViewById(R.id.gameinfo);
        gameimg = (ImageView) findViewById(R.id.gameimg);

        //redirect 되는 웹 사이트도 웹 뷰로 출력하기 위한 설정
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("about:blank");
    }

    @Override
    public void onResume() {
        super.onResume();
        new ThreadEx().start();
    }

}