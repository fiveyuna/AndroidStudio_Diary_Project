package ddwu.mobile.finalproject.ma01_20190978;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RoutineDBHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "routine_db"; 
	public final static String TABLE_NAME = "routine_table";
	public final static String COL_ID = "_id";
	public final static String COL_DATE = "date";
    public final static String COL_TIME = "time";
    public final static String COL_DID = "did";

//    public final static String COL_IS_JOURNAL = "isJournal";

	public RoutineDBHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + " ( " + COL_ID + " integer primary key autoincrement, " +
				COL_DATE + " TEXT, " + COL_TIME + " TEXT, " + COL_DID + " TEXT);");

	
//		샘플 데이터
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '2021-12-26', '14:00~15:00', '코딩');");
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '2021-12-25', '14:00~15:00', '이렇게 한 일을 써요');");
		db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '2021-12-25', '15:00~16:00', '오늘은 어떤 일이 있었나요?');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
	}

}
