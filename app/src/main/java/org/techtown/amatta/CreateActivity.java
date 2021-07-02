package org.techtown.amatta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ID = "_id";
    public static final String YEAR = "_year";
    public static final String MON = "_mon";
    public static final String DAY = "_day";

    DBHelper dbHelper = new DBHelper(this);

    TextView memoNum;
    EditText memo, todo, emoji;
    Button startTime, endTime, save, delete;
    Toolbar toolbar;

    int sh = 8;
    int sm = 0;
    int eh = 9;
    int em = 0;
    int tf = 0;
    String year = null;
    String mon = null;
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
        setContentView(R.layout.activity_create);

        // 툴바 설정 (뒤로 가기)
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        emoji = findViewById(R.id.emoji);
        memoNum = findViewById(R.id.memoNum);
        memo = findViewById(R.id.memo);
        todo = findViewById(R.id.todo);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        save.setOnClickListener(this);
        delete.setOnClickListener(this);
        emoji.setFilters(new InputFilter[]{specialCharFilter});

        // 메모 글자수 표시
        memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String input = memo.getText().toString();
                memoNum.setText(input.length()+"/90");
            }
        });

        Intent intent = getIntent();
        long id = intent.getLongExtra(ID, 0);
        if(id != 0) {  // 리스트 아이템을 클리해서 intent로 넘어온 경우
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from tb_data where id=" + id, null);

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

            sh = _sh;
            sm = _sm;
            eh = _eh;
            em = _em;
            tf = TodoAdapter.tff;
            year = _year;
            mon = _mon;
            day = _day;

            // 시작시간
            if (_sm < 10) {
                startTime.setText(_sh + ":0" + _sm);
            } else {
                startTime.setText(_sh + ":" + _sm);
            }
            // 종료시간
            if (_em < 10) {
                endTime.setText(_eh + ":0" + _em);
            } else {
                endTime.setText(_eh + ":" + _em);
            }
            todo.setText(_title);
            memo.setText(_memo);
            emoji.setText(_emoji);

            helper.close();
            db.close();
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == startTime) {
            TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // 시작시간이 종료시간보다 느릴 경우
                    if(hourOfDay > eh || (hourOfDay == eh && minute >= em)) {
                        eh = hourOfDay + 1;
                        em = minute;

                        // 24시 이상이 나오지 않도록 함
                        if(eh >= 24) {
                            eh = 23;
                            em = 59;
                        }

                        if(minute < 10) {
                            endTime.setText(eh + ":0" + minute);
                        } else {
                            endTime.setText(eh + ":" + minute);
                        }
                    }

                    sh = hourOfDay;
                    sm = minute;

                    if(minute < 10) {
                        startTime.setText(hourOfDay + ":0" + minute);
                    } else {
                        startTime.setText(hourOfDay + ":" + minute);
                    }
                }
            }, sh, sm, false);
            timePicker.show();
        } else if(v == endTime) {
            TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // 종료시간이 시작시간보다 빠를 경우
                    if(hourOfDay < sh || (hourOfDay == sh && minute <= sm)) {
                        sh = hourOfDay - 1;
                        sm = minute;

                        // 0시 이하가 나오지 않도록 함
                        if(sh <= 0) {
                            sh = 0;
                            sm = 0;
                        }

                        if(minute < 10) {
                            startTime.setText(sh + ":0" + minute);
                        } else {
                            startTime.setText(sh + ":" + minute);
                        }
                    }

                    eh = hourOfDay;
                    em = minute;

                    if(minute < 10) {
                        endTime.setText(hourOfDay + ":0" + minute);
                    } else {
                        endTime.setText(hourOfDay + ":" + minute);
                    }
                }
            }, eh, em, false);
            timePicker.show();
        } else if(v == save) {
            Intent intent = getIntent();

            if(year == null) {
                year = intent.getStringExtra(YEAR);
                mon = intent.getStringExtra(MON);
                day = intent.getStringExtra(DAY);
            }
            String title = todo.getText().toString();
            String _memo = memo.getText().toString();
            String _emoji = emoji.getText().toString();
            long id = System.currentTimeMillis();

            // title을 입력하지 않으면 저장되지 않음
            if(title.length() == 0) {
                Toast.makeText(this, "할 일을 1글자 이상 입력하세요", Toast.LENGTH_LONG).show();
                return;
            }

            dbHelper = new DBHelper(this);

            if(_id != 0) {
                dbHelper.update(_id, title, sh, sm, eh, em, _memo, _emoji, tf, year, mon, day);
            } else {
                dbHelper.insert(id, title, sh, sm, eh, em, _memo, _emoji, tf, year, mon, day);
            }
            dbHelper.close();

            setResult(RESULT_OK, intent);
            finish();
        } else if(v == delete) {
            Intent intent = new Intent();

            dbHelper = new DBHelper(this);
            dbHelper.delete(_id);
            dbHelper.close();

            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
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

    private InputFilter specialCharFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for(int i=start;i<end;i++) {
                Pattern unicodeOutliers = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
                if(unicodeOutliers.matcher(source).matches()) {
                    return null;
                }
            }
            return "";
        }
    };

}