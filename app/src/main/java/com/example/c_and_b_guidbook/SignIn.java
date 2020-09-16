package com.example.c_and_b_guidbook;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {

    // 화면에 보여지는 버튼과 텍스트 뷰에 대한 변수
    Button loginbtn;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // 뷰 찾아오기
        result = (TextView) findViewById(R.id.result);
        loginbtn = (Button) findViewById(R.id.login);

        // 버튼을 눌렀을 때 처리
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Annonymous class나 Lambda식에서는 자신이 속한 메소드의 지역변수를 사용할 수 없음
                // 사용하고자 하는 변수가 있으면 이 변수를 인스턴스 변수로 만들거나 final변수로 생성해야 함
                // layout 파일에 만든 뷰를 전개하기
                final LinearLayout linear = (LinearLayout) View.inflate(SignIn.this, R.layout.login, null);

                // 디자인한 뷰를 출력하는 대화상자를 생성
                new AlertDialog.Builder(SignIn.this)
                        .setTitle("로그인")
                        .setView(linear)
                        .setCancelable(false)
                        .setNegativeButton("취소", null)
                        .setPositiveButton("로그인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 뷰의 참조를 가져올 때는 부모 뷰에서 호출해야 함
                                // id를 입력하는 EditText는 Linear안에 있으므로 Linear가 호출해야 함
                                EditText id = (EditText) linear.findViewById(R.id.id);
                                EditText password = (EditText) linear.findViewById(R.id.password);

                                // 입력한 내용을 가지고 로그인을 시도
                                // EditText나 TextView에서 가져온 문자열은 자료형이 String이 아니므로 toString()을 호출해서 문자열로 변환해야 함
                                result.setText("아이디: " + id.getText().toString() + " 비밀번호: " + password.getText().toString());
                            }
                        })
                        .show();
            }
        });
    }
}