package org.techtown.amatta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_ADD = 100;
    public static final int REQUEST_CODE_UPDATE = 200;

    public static final String YEAR = "_year";
    public static final String MON = "_mon";
    public static final String DAY = "_day";

    RecyclerView recyclerView;
    TodoAdapter adapter;
    Toolbar toolbar;
    FloatingActionButton add;
    TextView textView;

    String year = null;
    String month = null;
    String day = null;

    // 받아온 데이터를 저장하는 변수
    long _id = 0;
    String _title = null;
    int _sh = 0;
    int _sm = 0;
    int _eh = 0;
    int _em = 0;
    String _memo = null;
    String _emoji = null;
    String _year = null;
    String _mon = null;
    String _day = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 달력 페이지에서 받은 year, mon, day
        Intent intent = getIntent();

        year = intent.getStringExtra(YEAR); // 년
        month = intent.getStringExtra(MON); // 월
        day = intent.getStringExtra(DAY); // 일
        String intentDate = null;

        // 받은 날짜 == toolbar 타이틀로 쓸 것
        if(day.length() == 1) {
            intentDate = month + "월 0" + day + "일";
        } else {
            intentDate = month + "월 " + day + "일";
        }

        // 툴바 설정 (뒤로 가기)
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 툴바 타이틀(텍스트뷰)
        textView = (TextView)findViewById(R.id.toolbar_Title);
        textView.setText(intentDate);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TodoAdapter();
        recyclerView.setAdapter(adapter);

        add = findViewById(R.id.floatingActionButton);
        add.setOnClickListener(this);

        if(TodoAdapter._id_ != 0) {
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from tb_data where id=" + TodoAdapter._id_, null);

            while (cursor.moveToNext()) {
                _id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                _title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                _sh = cursor.getInt(cursor.getColumnIndexOrThrow("start_h"));
                _sm = cursor.getInt(cursor.getColumnIndexOrThrow("start_m"));
                _eh = cursor.getInt(cursor.getColumnIndexOrThrow("end_h"));
                _em = cursor.getInt(cursor.getColumnIndexOrThrow("end_m"));
                _memo = cursor.getString(cursor.getColumnIndexOrThrow("memo"));
                _emoji = cursor.getString(cursor.getColumnIndexOrThrow("emoji"));
                _year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
                _mon = cursor.getString(cursor.getColumnIndexOrThrow("mon"));
                _day = cursor.getString(cursor.getColumnIndexOrThrow("day"));
            }
            helper.update(TodoAdapter._id_, _title, _sh, _sm, _eh, _em, _memo, _emoji, TodoAdapter.tff, _year, _mon, _day);
            helper.close();
            db.close();
            cursor.close();
        }

        loadListData();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtra(YEAR, year);
        intent.putExtra(MON, month);
        intent.putExtra(DAY, day);
        startActivityForResult(intent, REQUEST_CODE_ADD);  // deprecated 됨
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == REQUEST_CODE_ADD) {
            if(resultCode == RESULT_OK) {
                loadListData();
            }
        } else if(requestCode == REQUEST_CODE_UPDATE) {
            if(resultCode == RESULT_OK) {
                loadListData();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadListData() {
        ArrayList<Todo> items = new ArrayList<>();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_data where year='" + year + "' and mon='" + month + "' and day='" + day + "'", null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            int start_h = cursor.getInt(cursor.getColumnIndexOrThrow("start_h"));
            int start_m = cursor.getInt(cursor.getColumnIndexOrThrow("start_m"));
            int end_h = cursor.getInt(cursor.getColumnIndexOrThrow("end_h"));
            int end_m = cursor.getInt(cursor.getColumnIndexOrThrow("end_m"));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow("memo"));
            String emoji = cursor.getString(cursor.getColumnIndexOrThrow("emoji"));
            int tf = cursor.getInt(cursor.getColumnIndexOrThrow("tf"));
            String year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
            String mon = cursor.getString(cursor.getColumnIndexOrThrow("mon"));
            String day = cursor.getString(cursor.getColumnIndexOrThrow("day"));

            boolean tf_;
            if(tf == 1) {
                tf_ = true;
            } else {
                tf_ = false;
            }

            items.add(new Todo(id, title, start_h, start_m, end_h, end_m, memo, emoji, tf_, year, mon, day));
        }

        helper.close();
        db.close();
        cursor.close();

        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }

}