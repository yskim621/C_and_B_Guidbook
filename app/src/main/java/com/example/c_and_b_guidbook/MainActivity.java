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
    ListView trumpcardlistview;
    //출력할 데이터
    ArrayList<Map<String, Object>> data;
    ArrayAdapter<String> adapter;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trumpcardlistview = (ListView)findViewById(R.id.trumpcardlistview);
        data = new ArrayList<>();

        //TabHost 찾아오기
        TabHost host = (TabHost)findViewById(R.id.host);

        //탭 설정 시작
        host.setup();

        //첫번째 탭 설정
        TabHost.TabSpec spec = host.newTabSpec("탭1");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.trumpcard, null));
        spec.setContent(R.id.trumpcardlistview);
        host.addTab(spec);

        Map<String, Object> map = new HashMap<>();
        map.put("image", R.drawable.poker);
        map.put("title", "Poker");
        map.put("content", "5개 패의 족보가 가장 높은 사람이 승리하는 게임");
        data.add(map);

        map = new HashMap<>();
        map.put("image", R.drawable.black_jack);
        map.put("title", "Black_jack");
        map.put("content", "21에 가장 근접한 자가 이기는 게임");
        data.add(map);

        map = new HashMap<>();
        map.put("image", R.drawable.bacara);
        map.put("title", "Bacara");
        map.put("content", "2팀으로 나누어 베팅하며 낮은 수를 가진 팀이 승리");
        data.add(map);

        map = new HashMap<>();
        map.put("image", R.drawable.hoola);
        map.put("title", "Hoola");
        map.put("content", "모든 카드를 다 드랍하면 이기는 게임");
        data.add(map);

        //어댑터 생성
        CardAdapter adapter = new CardAdapter(this, data);


        trumpcardlistview.setAdapter(adapter);

        //새로운 탭 생성
        spec = host.newTabSpec("탭2");
        //탭 아이콘 설정
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.hwatu, null));
        //탭에 보여질 내용 설정
        spec.setContent(R.id.tab2);
        //탭 추가
        host.addTab(spec);

        spec = host.newTabSpec("탭3");
        //탭 아이콘 설정
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.boardgame, null));
        //탭에 보여질 내용 설정
        spec.setContent(R.id.tab3);
        //탭 추가
        host.addTab(spec);
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