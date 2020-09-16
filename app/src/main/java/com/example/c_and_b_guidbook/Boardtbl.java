package com.example.c_and_b_guidbook;

public class Boardtbl {
    // Domain class
    public Integer boardnum;
    public String boardtitle;
    public String boardcontent;
    public String boardwritedate;
    public Integer boardreadcnt;
    public String boardattachment;
    public String membernickname;

    @Override
    //ListView는 객체를 데이터로 주입하면 toString의 결과를 셀에 출력하기 때문
    public String toString() {
        return boardtitle;
    }
}
