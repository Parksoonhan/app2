package com.example.hywoman.myapplication;

import android.content.Intent;
import android.database.SQLException;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;
    private Button bt_tab1, bt_tab2, bt_tab3;
    private int frament_no;

    int mainValue = 0;
    int backValue = 0;
    TextView mainText;
    TextView backText;
    Button increaseButton;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bt_tab1 = (Button) bt_tab1.findViewById();
        bt_tab2 = (Button) bt_tab2.findViewById();
        bt_tab3 = (Button) bt_tab3.findViewById();


        // 탭 버튼에 대한 리스너 연결
        bt_tab1.setOnClickListener((View.OnClickListener) this);
        bt_tab2.setOnClickListener((View.OnClickListener) this);
        bt_tab3.setOnClickListener((View.OnClickListener) this);


        // 임의로 액티비티 호출 시점에 어느 프레그먼트를 프레임레이아웃에 띄울 것인지를 정함
        callFragment(frament_no);

        Button button1 = (Button) findViewById(R.id.activityButton); //해당 버튼을 지정합니다.
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //버튼이 눌렸을 때
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent); //액티비티 이동


            }
        });

        mainText = (TextView) findViewById(R.id.mainValue);
        backText = (TextView) findViewById(R.id.backValue);
        increaseButton = (Button) findViewById(R.id.increase);
        backThread thread = new backThread();
        thread.setDaemon(true);
        thread.start();
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mainValue++;
                mainText.setText("MainValue : " + mainValue);
                backText.setText("BackValue : " + backValue);
            }
        });



        setLayout();

        //데이터베이스 생성(파라메터 Context) 및 오픈
        DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //DataBase에 값을 입력
        mDbOpenHelper.insertColumn("박순한", "216230090", "angel@google.com");
        mDbOpenHelper.insertColumn("도경수", "010-43956-1515", "asdffff@emdo.com");
        mDbOpenHelper.insertColumn("조정석", "010-5681-5618", "yaya@hhh.com");
        
        //ArrayList 초기화
        ArrayList<InfoClass> mInfoArr = new ArrayList<InfoClass>();

        doWhileCursorToArray();

        //값이 제대로 입력됬는지 확인하기 위해 로그를 찍어본다
        for (InfoClass i : mInfoArr) {
            Log.i(TAG, "ID = " + i._id);
            Log.i(TAG, "NAME = " + i.name);
            Log.i(TAG, "CONTACT = " + i.contact);
            Log.i(TAG, "EMAIL = " + i.email);


            //리스트뷰에 사용할 어댑터 초기화(파라메터 Context, ArrayList<InfoClass>)
            mAdapter = new CustomAdapter(this, mInfoArr);
            mListView.setAdapter(mAdapter);


    }


    class backThread extends Thread {
        public void run() {
            while (true) {
                backValue++; //backText.setText("BackValue : "+backValue);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    
    mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick Object parent;
        (AdapterView<?> parent,
                View view, int position, long id) {
            Log.i(TAG, "position = " + position);
            //리스트뷰의 position은 0부터 시작하므로 1을 더함
            boolean result = mDbOpenHelper.deleteColumn(position + 1);
            Log.i(TAG, "result = " + result);

            if (result) {
                //정상적인 position을 가져왔을 경우 ArrayList의 position과 일치하는 index 정보를 remove
                mInfoArr.remove(position);
                //어댑터에 ArrayList를 다시 세팅 후 값이 변경됬다고 어댑터에 알림
                mAdapter.setArrayList(mInfoArr);
                mAdapter.notifyDataSetChanged();
            } else {
                //잘못된 position을 가져왔을 경우 다시 확인 요청
                Toast.makeText(MainActivity.this, "INDEX를 확인해 주세요",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

}

    //doWhile문을 이용하여 Cursor에 내용을 다 InfoClass에 입력 후 InfoClass를 ArrayList에 Add
    private void doWhileCursorToArray() {

        mCursor = null;
        //DB에 있는 모든 컬럼을 가져옴
        mCursor = mDbOpenHelper.getAllColumns();
        //컬럼의 갯수 확인
        Log.i(TAG, "Count = " + mCursor.getCount());

        while (mCursor.moveToNext()) {
            //InfoClass에 입력된 값을 압력
            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("name")),
                    mCursor.getString(mCursor.getColumnIndex("contact")),
                    mCursor.getString(mCursor.getColumnIndex("email"))
            );
            //입력된 값을 가지고 있는 InfoClass를 InfoArray에 add
            mInfoArr.add(mInfoClass);
        }
        //Cursor 닫기
        mCursor.close();
    }

    /**
     * 추가 버튼 클릭 메소드.
     *
     * @param v
     */
    public void btnAdd(View v) {
        //추가를 누를 경우 EditText에 있는 String 값을 다 가져옴
        mDbOpenHelper.insertColumn(
                mEditTexts[Constants.NAME].getText().toString().trim(),
                mEditTexts[Constants.CONTACT].getText().toString().trim(),
                mEditTexts[Constants.EMAIL].getText().toString().trim()
        );
        //ArrayList 내용 삭제
        mInfoArr.clear();

        doWhileCursorToArray();

        mAdapter.setArrayList(mInfoArr);
        mAdapter.notifyDataSetChanged();
        //Cursor 닫기
        mCursor.close();
    }

    /**
     * 레이아웃 세팅하는 메소드
     */
    private EditText[] mEditTexts;
    private ListView mListView;

    private void setLayout() {
        mEditTexts = new EditText[]{
                (EditText) findViewById(R.id.etName),
                (EditText) findViewById(R.id.etContact),
                (EditText) findViewById(R.id.etEmail)
        };

        mListView = (ListView) findViewById(R.id.list);
    }

    //액티비티가 종료 될 때 디비를 닫아준다
    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }



    private void callFragment(Object p0) {
        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            case 1:
                // '프래그먼트1' 호출
                Fragment1 fragment1 = new Fragment1();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                Fragment2 fragment2 = new Fragment2();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
            case 3:
                // '프래그먼트1' 호출
                Fragment3 fragment3 = new Fragment3();
                transaction.replace(R.id.fragment_container, fragment3);
                transaction.commit();
                break;
        }
    } 


}