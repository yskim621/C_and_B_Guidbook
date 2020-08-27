package com.example.c_and_b_guidbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TabHost 찾아오기
        TabHost host = (TabHost)findViewById(R.id.host);
        //탭 설정 시작
        host.setup();

        //첫번째 탭 설정
        TabHost.TabSpec spec = host.newTabSpec("탭1");
        spec.setIndicator(null, ResourcesCompat.getDrawable(getResources(), R.drawable.trumpcard, null));
        spec.setContent(R.id.tab1);
        host.addTab(spec);

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