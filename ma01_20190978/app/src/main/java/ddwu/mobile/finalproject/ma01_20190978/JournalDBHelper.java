package ddwu.mobile.finalproject.ma01_20190978;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JournalDBHelper extends SQLiteOpenHelper {
	
	private final static String DB_NAME = "journal_db";
	public final static String TABLE_NAME = "journal_table";
	public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";
    public final static String COL_CONTENT = "content";
	public final static String COL_FEELING = "feeling";
	public final static String COL_DATE = "date";
	public final static String COL_LOCATION = "location";
	public final static String COL_IMAGE = "image";

	public JournalDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement, "
				+ COL_TITLE + " TEXT, " + COL_CONTENT + " TEXT, " + COL_FEELING + " TEXT, "
				+ COL_DATE + " TEXT, " + COL_LOCATION + " TEXT, " + COL_IMAGE + " TEXT);");
	
//		샘플 데이터
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '제목1', '내용1', '감정', '2021-12-26', null, null);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
	}

}
