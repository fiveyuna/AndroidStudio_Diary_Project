package ddwu.mobile.finalproject.ma01_20190978;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AllRoutinesActivity extends AppCompatActivity {

	final static String TAG = "AllRoutinesActivity";
	final int UPDATE_CODE = 200;

	long now = System.currentTimeMillis();
	boolean isUpdate = true;

	ListView lvRoutines = null;
	TextView tvDate = null;

	RoutineDBHelper helper;
	Cursor cursor;
	RoutineCursorAdapter adapter;
	RoutineDto routineDto;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_routines);
		lvRoutines = (ListView)findViewById(R.id.lvRoutines);
		tvDate = findViewById(R.id.tvDate);

		tvDate.setText(getDate(0));

		helper = new RoutineDBHelper(this);
		adapter = new RoutineCursorAdapter(this, R.layout.listview_layout_routine, null);

		lvRoutines.setAdapter(adapter);

		Log.d(TAG, "set");

//		리스트 뷰 클릭 처리
        lvRoutines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            	Log.d(TAG, "pos" + position);

				cursor.moveToPosition(position);

				routineDto = new RoutineDto();
				routineDto.setId(cursor.getInt(cursor.getColumnIndex( helper.COL_ID )));
				routineDto.setTime(cursor.getString(cursor.getColumnIndex(helper.COL_TIME)));
				routineDto.setDid(cursor.getString(cursor.getColumnIndex(helper.COL_DID)));

				Log.d(TAG, "update click");
            	Intent intent = new Intent(AllRoutinesActivity.this, UpdateRoutineActivity.class);
				intent.putExtra("routine", routineDto);
            	startActivityForResult(intent, UPDATE_CODE); // 업뎃 했을 수도 안 했을 수도
				
            }
        });

//		리스트 뷰 롱클릭 처리
		lvRoutines.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int pos = position; //위치 기억
				AlertDialog.Builder builder = new AlertDialog.Builder(AllRoutinesActivity.this);
				builder.setTitle(R.string.dialogue_title) // 다이알로그 띄움
						.setMessage(R.string.dialogue_message)
						.setPositiveButton(R.string.dialogue_ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								cursor.moveToPosition(position);
								int _id = cursor.getInt(cursor.getColumnIndex( helper.COL_ID ));

								SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
								String whereClause = helper.COL_ID + "=?";
								String[] whereArgs = new String[] { String.valueOf(_id) };
								int result = sqLiteDatabase.delete(helper.TABLE_NAME, whereClause, whereArgs);

								if(result > 0) {
									Toast.makeText(AllRoutinesActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
									adapter.notifyDataSetChanged();
									onResume();
								} else {
									Toast.makeText(AllRoutinesActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
								}

								helper.close();

							}
						})
						.setNegativeButton(R.string.dialogue_cancle, null)
						.setCancelable(false)
						.show();
				return true;
			}
		});
	}

	protected String getDate(int x) {
		now = now + (1000 * 60 * 60 * 24) * x;
		Date result_date = new Date(now);
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		String returnDate = simpleDate.format(result_date);

		return returnDate;
	}

	protected void changeCursor() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sqlStr = "select * from " + RoutineDBHelper.TABLE_NAME + " where date='" + getDate(0) + "'";
		cursor = db.rawQuery(sqlStr, null);

		Log.d(TAG, sqlStr);

		adapter.changeCursor(cursor);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnDateBack:
				tvDate.setText(getDate(-1));
				changeCursor();
				break;
			case R.id.btnDateForward:
				tvDate.setText(getDate(1));
				changeCursor();
				break;

		}
	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == UPDATE_CODE) {
			switch (resultCode) {
				case RESULT_OK:
					Toast.makeText(this, "루틴 수정 완료", Toast.LENGTH_SHORT).show();
					break;
				case RESULT_CANCELED:
					Toast.makeText(this, "루틴 수정 취소", Toast.LENGTH_SHORT).show();
					isUpdate = false;
					break;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//        DB에서 데이터를 읽어와 Adapter에 설정
		if(isUpdate) {
			Log.d(TAG, "onResume 실행");
			changeCursor();
			helper.close();
		}
		else {
			Log.d(TAG, "onResume 실행 안 함");
			isUpdate = true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//        cursor 사용 종료
		if (cursor != null) cursor.close();
	}

}




