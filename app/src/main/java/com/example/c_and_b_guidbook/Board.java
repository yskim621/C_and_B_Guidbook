package com.example.c_and_b_guidbook;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Stack;


public class Board extends AppCompatActivity {
    ProgressBar ind;

    // 스크롤이 가장 아래에서 수행되었는지 확인할 변수
    boolean lastItemVisibleFlag = false;

    //콤보 박스 역할을 하는 위젯
    private Spinner searchtype;
    private EditText value;

    private Button btnsearch, btnnext;

    private ListView listView;
    private List<Boardtbl> list;
    private ArrayAdapter<Boardtbl> boardAdapter;

    //Spinner에 데이터를 연결할 Adapter
    private ArrayAdapter<CharSequence> adapter;

    //페이지 번호와 페이지 당 데이터 개수를 저장할 변수
    int pageNo = 1;
    int size = 5;

    //조건에 맞는 데이터 개수를 저장할 변수
    int cnt;


    //스레드가 다운로드 받아서 파싱한 결과를 출력할 핸들러
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message){

            // adapter를 이용해서 Listview에 데이터가 수정되면 다시 출력하도록 [신호를 보냄 - notification]
            boardAdapter.notifyDataSetChanged();
            ind.setVisibility(View.INVISIBLE);
        }
    };

    //데이터를 다운로드 받아서 파싱하는 스레드
    class ThreadEx extends Thread{
        //다운로드 받은 문자열을 저장할 객체
        StringBuilder sb = new StringBuilder();

        public void run(){
            try{

                URL url = null;
                //콤보 박스 선택한 항목 번호를 idx에 저장
                int idx = searchtype.getSelectedItemPosition();
                if(idx == 0){
                    url = new URL("http://yskim621.cafe24.com/board/select?" +
                                    "pageno=" + pageNo);
                }else if(idx == 1){
                    url = new URL("http://yskim621.cafe24.com/board/select?" + "searchtype=membernickname&" + "value=" +
                                    value.getText().toString() + "&pageno="
                                    +pageNo
                    );
                }else if(idx == 2){
                    url = new URL("http://yskim621.cafe24.com/board/select?" + "searchtype=boardtitle&" + "value=" +
                                    value.getText().toString() + "&pageno="
                                    +pageNo
                    );
                }else{
                    url = new URL("http://yskim621.cafe24.com/board/select?" + "searchtype=both&" + "value=" +
                                    value.getText().toString() + "&pageno="
                                    +pageNo
                    );
                }

                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
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
                Log.e("다운로드 예외", e.getMessage());
            }

            try{
                //객체로 변환
                JSONObject object = new JSONObject(sb.toString());
                //데이터 개수는 count에 숫자로 저장
                cnt = object.getInt("count");
                //list 키의 데이터를 배열로 가져오기
                JSONArray ar = object.getJSONArray("list");
                for(int i=0; i<ar.length(); i=i+1){
                    JSONObject temp = ar.getJSONObject(i);
                    // result = result + temp.getString(1) + "\n";
                    Boardtbl boardtbl = new Boardtbl();
                    boardtbl.boardnum = temp.getInt("boardnum");
                    boardtbl.boardtitle = temp.getString("boardtitle");
                    boardtbl.boardcontent = temp.getString("boardcontent");
                    boardtbl.boardwritedate = temp.getString("boardwritedate");
                    boardtbl.boardattachment = temp.getString("boardattachment");
                    boardtbl.membernickname = temp.getString("membernickname");

                    list.add(boardtbl);
                    // 이메일과 같이 최신 데이터를 위에서 붙여서 이전 데이터를 밑으로 누르는 방식으로 데이터를 추가하기 위해서는
                    // index 0에 계속 추가를 해서 이전 데이터의 인덱스를 밀어냄
                    // list.add(0, item);
                }
                //핸들러에게 출력을 요청
                handler.sendEmptyMessage(0);

            }catch(Exception e){
                Log.e("파싱에러", e.getMessage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);


        ind = (ProgressBar) findViewById(R.id.progressind);


        searchtype = (Spinner)findViewById(R.id.searchtype);
        adapter = ArrayAdapter.createFromResource(this, R.array.searchtype_array, android.R.layout.simple_spinner_dropdown_item);
        searchtype.setAdapter(adapter);

        value = (EditText)findViewById(R.id.value);
        btnnext = (Button)findViewById(R.id.btnnext);
        btnsearch = (Button)findViewById(R.id.btnsearch);
        // list = (TextView)findViewById(R.id.list);
        listView = (ListView) findViewById(R.id.listview);
        list = new Stack<Boardtbl>();
        boardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(boardAdapter);

        // 옵션 설정
        listView.setDivider(new ColorDrawable(Color.BLUE));
        listView.setDividerHeight(3);

        // 항목을 선택했을 때 호출되는 이벤트 핸들러 작성
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            //adapterView는 이벤트가 발생한 뷰, view는 선택한 항목 뷰, i가 선택한 항목의 인덱스, l은 선택한 항목 뷰의 id
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //선택한 항목의 데이터
                Boardtbl boardtbl = list.get(i);
                //토스트로 itemid를 출력
                //Toast.makeText(MainActivity.this, item.itemid + "", Toast.LENGTH_LONG).show();

                //하위 Activity 출력
                Intent intent = new Intent(Board.this, BoardDetail.class);

                //데이터 전달하기 - boardnum 전달
                intent.putExtra("boardnum", boardtbl.boardnum);

                //액티비티 호출
                startActivity(intent);
            }
        });

        // ListView의 항목 animation 설정
        AnimationSet set = new AnimationSet(true);

        // 이동하는 애니메이션 -> 짝수 매개변수는 좌표애 관한 매개변수
        Animation rtl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        rtl.setDuration(3000);
        set.addAnimation(rtl);

        Animation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(3000);
        set.addAnimation(alpha);

        // 각각의 애니메이션을 설정하고 대기 시작을 추가해서 설정
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);

        // ListView에 애니메이션을 설정
        listView.setLayoutAnimation(controller);


        // listView의 Scroll 이벤트 처리
        listView.setOnScrollListener(new ListView.OnScrollListener() {
            // Scroll이 끝나면 호출되는 메소드
            @Override
            // 첫번째 매개변수는 스크롤이 발생한 뷰
            // 두번째는 현재 스크롤 상태로 OnScrollListener의 상수로 설정
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // 스크롤이 끝나고 가장 하단에서 스크롤을 했다면 데이터 업데이트
                if(scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag == true){
                    pageNo = pageNo + 1;
                    if((pageNo-1) * 3 +1 >= cnt){
                        Toast.makeText(Board.this, "더 이상 데이터가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        new ThreadEx().start();
                    }
                }
            }

            // Scroll 도중에 호출되는 메소드
            @Override
            // 두번째 매개변수 - 첫번째 보이는 아이템의 인덱스
            // 세번째 매개변수 - 현재 보여지고 있는 행의 개수
            // 네번째 매개변수 - 전체 출력된 행의 개수
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 스크롤 하는 위치를 감시하다가 마지막인지 인지해서 값을 변경
                if(totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount){
                    lastItemVisibleFlag = true;
                }

            }
        });

        btnnext.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                pageNo = pageNo + 1;
                new ThreadEx().start();
            }
        });

        btnsearch.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                pageNo = 1;

                // List를 초기화해서 ListView를 초기화
                list.clear();
                new ThreadEx().start();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //데이터가 없을 때만 데이터를 가져오기
        if(list == null || list.size() < 1) {
            new ThreadEx().start();
            ind.setVisibility(View.VISIBLE);
        }
    }
}