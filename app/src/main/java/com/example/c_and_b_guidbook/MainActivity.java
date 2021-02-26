package com.example.c_and_b_guidbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView trumpcardlistview, hwatulistview, boardgamelistview;
    //출력할 데이터
    ArrayList<Map<String, Object>> trumpcard, hwatu, boardgame;
    ArrayAdapter<String> adapter;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trumpcardlistview = (ListView)findViewById(R.id.trumpcardlistview);
        hwatulistview = (ListView)findViewById(R.id.hwatulistview);
        boardgamelistview = (ListView)findViewById(R.id.boardgamelistview);

        //TabHost 찾아오기
        TabHost host = (TabHost)findViewById(R.id.host);

        //탭 설정 시작
        host.setup();

        //Trumpcard 탭 설정
        TabHost.TabSpec spec = host.newTabSpec("trumpcard");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.trumpcard, null));
        spec.setContent(R.id.trumpcardlistview);
        host.addTab(spec);

        trumpcard = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("gametype", "trumpcard");
        map.put("image", R.drawable.poker);
        map.put("gamecode", "poker");
        map.put("content", "5개 패의 족보가 가장 높은 사람이 승리하는 게임");
        trumpcard.add(map);

        map = new HashMap<>();
        map.put("gametype", "trumpcard");
        map.put("image", R.drawable.black_jack);
        map.put("gamecode", "black_jack");
        map.put("content", "21에 가장 근접한 자가 이기는 게임");
        trumpcard.add(map);

        map = new HashMap<>();
        map.put("gametype", "trumpcard");
        map.put("image", R.drawable.bacara);
        map.put("gamecode", "bacara");
        map.put("content", "플레이어와 뱅커 중 어느 쪽이 이길지 매회 예상을 하고 배팅하는 게임");
        trumpcard.add(map);

        map = new HashMap<>();
        map.put("gametype", "trumpcard");
        map.put("image", R.drawable.hoola);
        map.put("gamecode", "hoola");
        map.put("content", "모든 카드를 다 드랍하면 이기는 게임");
        trumpcard.add(map);

        //어댑터 생성
        Adapter adapter = new Adapter(this, trumpcard);


        trumpcardlistview.setAdapter(adapter);



        //Hwatu 탭 생성
        spec = host.newTabSpec("hwatu");
        //탭 아이콘 설정
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.hwatu, null));
        //탭에 보여질 내용 설정
        spec.setContent(R.id.hwatulistview);
        //탭 추가
        host.addTab(spec);

        hwatu = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("gametype", "hwatu");
        map1.put("image", R.drawable.gostop);
        map1.put("gamecode", "gostop");
        map1.put("content", "광/그림/피 등의 점수를 계산에서 2인 7점, 3인 3점을 먼저 내는 사람이 승리");
        hwatu.add(map1);

        map1 = new HashMap<>();
        map1.put("gametype", "hwatu");
        map1.put("image", R.drawable.seosda);
        map1.put("gamecode", "seosda");
        map1.put("content", "2장 혹은 3장의 패로 족보에 의거하여 베팅");
        hwatu.add(map1);

        //어댑터 생성
        adapter = new Adapter(this, hwatu);

        hwatulistview.setAdapter(adapter);




        spec = host.newTabSpec("boardgame");
        //탭 아이콘 설정
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.boardgame, null));
        //탭에 보여질 내용 설정
        spec.setContent(R.id.boardgamelistview);
        //탭 추가
        host.addTab(spec);

        boardgame = new ArrayList<>();

        Map<String, Object> map2 = new HashMap<>();
        map2.put("gametype", "boardgame");
        map2.put("image", R.drawable.chess);
        map2.put("gamecode", "chess");
        map2.put("content", "체스는 가로와 세로가 각각 8줄씩 64칸으로 격자로 배열 된 체스보드에서 두 명의 플레이어가 피스들을 규칙에 따라 움직여 싸우는 보드 게임");
        boardgame.add(map2);

        map2 = new HashMap<>();
        map2.put("gametype", "boardgame");
        map2.put("image", R.drawable.go);
        map2.put("gamecode", "go");
        map2.put("content", "바둑 혹은 오로, 혁기, 는 두 사람이 흑과 백의 돌을 사각의 판 위에 번갈아 놓으며 집을 차지하는 것을 겨루는 놀이");
        boardgame.add(map2);

        map2 = new HashMap<>();
        map2.put("gametype", "boardgame");
        map2.put("image", R.drawable.janggi);
        map2.put("gamecode", "janggi");
        map2.put("content", "장기는 청과 홍 두 편으로 나뉘어 각 16개의 기물을 가지고 군대를 지휘하는 총사령관의 입장에서 작전을 구상, 수행하여 상대편의 왕을 잡는 추상 전략 보드 게임");
        boardgame.add(map2);

        map2 = new HashMap<>();
        map2.put("gametype", "boardgame");
        map2.put("image", R.drawable.omok);
        map2.put("gamecode", "omok");
        map2.put("content", "오목은 바둑판에 두 사람이 번갈아 돌을 놓아 가로나 세로, 대각선으로 다섯 개의 연속된 돌을 놓으면 이기는 놀이");
        boardgame.add(map2);

        //어댑터 생성
        adapter = new Adapter(this, boardgame);

        boardgamelistview.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.board:
                intent = new Intent(MainActivity.this, Board.class);
                startActivity(intent);
                break;
            case R.id.signUp:
                intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
                break;
            case R.id.signIn:
                intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}