package com.example.c_and_b_guidbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class Adapter extends BaseAdapter {

    Context context;
    List<Map<String, Object>> list;

    //생성자
    public Adapter(
            Context context,
            List<Map<String, Object>> list){
        this.context = context;
        this.list = list;

    }

    //행의 개수를 설정하는 메소드
    //이 메소드에서 리턴한 값만큼 아래 메소드들을 호출
    @Override
    public int getCount() {
        return list.size();
    }

    //행의 항목을 만들어주는 메소드
    @Override
    public Object getItem(int i) {
        Map<String, Object> map = list.get(i);
        return map.get("title").toString();
    }
    //각 행의 아이디를 설정하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //중요한 메소드!
    //첫번째 매개변수는 행 번호
    //두번째 매개변수가 출력할 뷰
    //세번째 매개변수는 항목이 출력되는 AdapterView
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final int pos = i;

        if(view == null){
            //icontext.xml 파일을 전개해서 뷰로 생성
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_cell, viewGroup, false);
        }
        /*
        if(i % 2 == 0){
            view.setBackgroundColor(Color.RED);
        }else{
            view.setBackgroundColor(Color.BLUE);
        }
         */

        //이미지 출력
        ImageView image = (ImageView)view.findViewById(R.id.image);
        int imageid = (Integer)list.get(i).get("image");
        image.setImageResource(imageid);

        //텍스트 뷰
        TextView title = (TextView)view.findViewById(R.id.gamecode);
        final String gamecode = (String)list.get(i).get("gamecode");
        title.setText(gamecode);

        //Parameter send
        final String gametype = (String)list.get(i).get("gametype");

        //버튼
        Button btn = (Button)view.findViewById(R.id.content);
        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                //context.startActivities(new Intent[]{new Intent(context, Content.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)});

                Intent intent = new Intent(context, Content.class);
                intent.putExtra("gamecode", gamecode);
                intent.putExtra("gametype", gametype);
                ((Activity) context).startActivity(intent);
                //((Activity) context).startActivityForResult(intent, (Integer) list.get(i).get("code"));
            }
        });


        return view;
    }
}
