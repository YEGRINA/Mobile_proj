package org.techtown.amatta;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public DBHelper(Context context) {
        super(context, "DB", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery = "create table tb_data " +
                "( id long primary key, " +  // 저장된 시간을 id로 저장하여 데이터를 id로 구별
                "title, " +  // 할 일
                "start_h, " +  // 시작 시간 (hour)
                "start_m, " +  // 시작 시간 (min)
                "end_h, " +  // 종료 시간 (hour)
                "end_m, " +  // 종료 시간 (min)
                "memo, " +  // 입력된 메모
                "emoji, " +  // todo를 대표하는 이모티콘
                "tf, " +  // 체크박스의 체크 여부 (true=1, false=0)
                "year, " +  // 등록된 연도
                "mon, " +  // 등록된 달
                "day );";  // 등록된 일
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String createQuery = "drop table if exists tb_data;";
        sqLiteDatabase.execSQL(createQuery);
        onCreate(sqLiteDatabase);

    }

    void insert(long id, String title, int sh, int sm, int eh, int em, String memo, String emoji,
                int tf, String year, String mon, String day) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("start_h", sh);
        contentValues.put("start_m", sm);
        contentValues.put("end_h", eh);
        contentValues.put("end_m", em);
        contentValues.put("memo", memo);
        contentValues.put("emoji", emoji);
        contentValues.put("tf", tf);
        contentValues.put("year", year);
        contentValues.put("mon", mon);
        contentValues.put("day", day);

        db.insert("tb_data", null, contentValues);
    }

    void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("tb_data", "id="+id, null);
    }

    void update(long id, String title, int sh, int sm, int eh, int em, String memo, String emoji,
                int tf, String year, String mon, String day) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("start_h", sh);
        contentValues.put("start_m", sm);
        contentValues.put("end_h", eh);
        contentValues.put("end_m", em);
        contentValues.put("memo", memo);
        contentValues.put("emoji", emoji);
        contentValues.put("tf", tf);
        contentValues.put("year", year);
        contentValues.put("mon", mon);
        contentValues.put("day", day);

        db.update("tb_data", contentValues, "id="+id, null);
    }
}
