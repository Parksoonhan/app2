package com.example.hywoman.myapplication;

import android.provider.BaseColumns;

/**
 * 데이터베이스 테이블 생성
 * Created by TonyChoi on 2016. 3. 29..
 */
public class DataBases {
    //데이터베이스 호출 시 사용될 생성자
    public static final class CreateDB implements BaseColumns {
        public static final String NAME = "name";
        public static final String CONTACT = "contact";
        public static final String EMAIL = "email";
        public static final String _TABLENAME = "address";
        public static final String _CREATE =
                "create table " + _TABLENAME + "("
                        + _ID + " integer primary key autoincrement, "
                        + NAME + " text not null , "
                        + CONTACT + " text not null , "
                        +EMAIL + " text not null );";
    }
}
