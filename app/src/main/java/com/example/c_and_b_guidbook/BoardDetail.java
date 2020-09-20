package com.example.c_and_b_guidbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class BoardDetail extends AppCompatActivity {

    //화면에 사용할 뷰
    TextView boardtitle, membernickname, boardattachment, boardcontent;

    //텍스트 데이터를 웹에서 다운로드 받아서 출력
    //다운로드 -> 파싱 -> 출력

    //텍스트 데이터를 출력할 핸들러
    Handler textHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message){
            //넘어온 데이터 찾아오기
            Map<String,Object> map = (Map<String,Object>)message.obj;

            //데이터 출력하기
            boardtitle.setText((String)map.get("boardtitle"));
            boardattachment.setText((String)map.get("boardattachment"));
            membernickname.setText((String)map.get("membernickname"));
            boardcontent.setText((String)map.get("boardcontent"));
        }
    };

    //텍스트 데이터를 가져올 스레드 클래스
    class TextThread extends Thread{
        StringBuilder sb = new StringBuilder();
        @Override
        public void run(){
            //텍스트 데이터 다운로드
            try{
                //URL 만들기
                //호출하는 인텐트 가져오기
                Intent intent = getIntent();

                //boardnum의 값을 정수로 가져오고 없을 때 1
                Integer boardnum = intent.getIntExtra("boardnum", 1);
                URL url = new URL("http://yskim621.cafe24.com/board/detail/" + boardnum);

                //Connection 객체 만들기
                HttpURLConnection con = (HttpURLConnection)url.openConnection();

                //옵션 설정
                con.setUseCaches(false);
                con.setConnectTimeout(30000);

                //스트림 객체 생성
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                //문자열 읽기
                while(true){
                    String line = br.readLine();
                    if(line == null){
                        break;
                    }
                    sb.append(line + "\n");
                }

                br.close();
                con.disconnect();

            }catch(Exception e){
                //이 메시지가 보이면 서버가 구동 중인지 확인하고
                //URL은 제대로 입력했는지 확인
                Log.e("다운로드 에러", e.getMessage());
            }
            Log.e("다운로드 받은 문자열", sb.toString());

            try{
                //다운로드 받은 문자열에서 필요한 데이터 추출
                JSONObject object = new JSONObject(sb.toString());
                JSONObject boardtbl = object.getJSONObject("boardtbl");

                String boardtitle = boardtbl.getString("boardtitle");
                String membernickname = boardtbl.getString("membernickname");
                String boardattachment = boardtbl.getString("boardattachment");
                String boardcontent = boardtbl.getString("boardcontent");

                //4개의 데이터를 하나로 묶기
                Map<String, Object> map = new HashMap<>();
                map.put("boardtitle", boardtitle);
                map.put("membernickname", membernickname);
                map.put("boardattachment", boardattachment);
                map.put("boardcontent", boardcontent);


                //핸들러에게 데이터를 전송하고 호출
                Message message = new Message();
                message.obj = map;
                textHandler.sendMessage(message);


            }catch(Exception e){
                Log.e("파싱 에러", e.getMessage());
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        new TextThread().start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_detail);



        //디자인 한 뷰 찾아오기
        boardtitle = (TextView)findViewById(R.id.boardtitle);
        membernickname = (TextView)findViewById(R.id.membernickname);
        boardattachment = (TextView)findViewById(R.id.boardattachment);
        boardcontent = (TextView) findViewById(R.id.boardcontent);

        Button backbtn = (Button)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){

                //현재 Activity 종료
                finish();
            }
        });
    }
}
