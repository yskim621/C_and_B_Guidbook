package com.example.c_and_b_guidbook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class BoardInsert extends AppCompatActivity {
    EditText boardtitle, boardcontent;
    Button btninsert;

    class InsertThread extends Thread{
        public void run(){
            try {
                // upload할 주소 만들기
                URL url = new URL("http://yskim621.cafe24.com/board/write");
                // 서버에게 넘겨줄 문자열 파라미터를 생성
                String [] data = {boardtitle.getText().toString().trim(), boardcontent.getText().toString().trim()};


                String [] dataName = {"boardtitle", "boardcontent"};

                // 파라미터 전송에 필요한 변수를 생성
                String lineEnd = "\r\n";

                // 파일 업로드 시 boundary값이 있어야 함
                // 랜덤하게 생성할 것을 권장 -> String boundary = "androidinsert";
                String boundary = UUID.randomUUID().toString();

                // 업로드 옵션을 설정
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setReadTimeout(30000);
                con.setUseCaches(false);
                con.setConnectTimeout(30000);
                con.setDoInput(true);
                con.setDoOutput(true);

                // 파일 업로드 옵션 설정
                con.setRequestProperty("ENCTYPE", "multipart/form-data");
                con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                // 문자열 파라미터 전송
                String delimiter = "--" + boundary + lineEnd;
                StringBuffer postDataBuilder = new StringBuffer();
                for(int i=0; i<data.length; i=i+1){
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append("Content-Disposition: form-data; name=\"" + dataName[i] + "\"" + lineEnd + lineEnd + data[i] + lineEnd);
                }

                // 업로드할 파일이 있는 경우에만 작성
                String fileName = "profile.jpg";
                if(fileName != null){
                    postDataBuilder.append(delimiter);
                    postDataBuilder.append("Content-Disposition: form-data; name=\"" + "boardattachment" + "\";filename=\"" + fileName + "\"" + lineEnd);

                }

                // 파라미터 전송
                DataOutputStream ds = new DataOutputStream(con.getOutputStream());
                ds.write(postDataBuilder.toString().getBytes());

                // 파일 업로드
                if(fileName != null){
                    ds.writeBytes(lineEnd);

                    // 파일 읽어오기 - id에 해당하는 파일을 raw 디렉토리에 복사
                    InputStream fres = getResources().openRawResource(R.raw.profile);
                    byte [] buffer = new byte[fres.available()];
                    int length = -1;

                    // 파일의 내용을 읽어서 읽은 내용이 있으면 그 내용을 ds에 기록
                    while((length = fres.read(buffer)) != -1){
                        ds.write(buffer, 0, length);

                    }
                    ds.writeBytes(lineEnd);
                    ds.writeBytes(lineEnd);
                    ds.writeBytes("--" + boundary + "--" + lineEnd);
                    fres.close();
                } else {
                    ds.writeBytes(lineEnd);
                    ds.writeBytes("--" + boundary + "--" + lineEnd);
                }
                ds.flush();
                ds.close();

                // 서버로 부터의 응답 가져오기
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuilder sb = new StringBuilder();
                while(true){
                    String line = br.readLine();
                    if(line == null){
                        break;
                    }
                    sb.append(line + "\n");
                }
                br.close();
                con.disconnect();

                // JSON 파싱
                JSONObject object = new JSONObject(sb.toString());
                boolean result = object.getBoolean("result");

                // 핸들러에게 결과를 전송
                Message message = new Message();
                message.obj = result;
                insertHandler.sendMessage(message);




            }catch (Exception e){
                Log.e("업로드 예외 발생", e.getMessage());

            }

        }
    }

    Handler insertHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message){
            boolean result = (Boolean) message.obj;
            if(result == true){
                Toast.makeText(BoardInsert.this, "삽입 성공", Toast.LENGTH_LONG).show();

                // 키보드 내리기
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(boardtitle.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(boardcontent.getWindowToken(), 0);
            }else {
                Toast.makeText(BoardInsert.this, "삽입 실패", Toast.LENGTH_LONG).show();
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_insert);

        boardtitle = (EditText) findViewById(R.id.boardtitle);
        boardcontent = (EditText) findViewById(R.id.boardcontent);


        btninsert = (Button) findViewById(R.id.btninsert);
        btninsert.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                // 유효성 검사
                if(boardtitle.getText().toString().trim().length() < 1){
                    Toast.makeText(BoardInsert.this, "제목은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(boardcontent.getText().toString().trim().length() < 1){
                    Toast.makeText(BoardInsert.this, "건의 사항은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                new InsertThread().start();
            }

        });
    }
}