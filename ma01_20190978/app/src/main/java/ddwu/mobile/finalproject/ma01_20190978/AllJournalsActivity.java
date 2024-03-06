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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AllJournalsActivity extends AppCompatActivity {

	final static String TAG = "AllJournalsActivity";
	final int UPDATE_CODE = 200;

	boolean isUpdate = true;

	ListView lvJournals = null;

	JournalDBHelper helper;
	Cursor cursor;
	JournalCursorAdapter adapter;
	JournalDto journalDto;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_journals);
		lvJournals = (ListView)findViewById(R.id.lvJournals);

		helper = new JournalDBHelper(this);
		adapter = new JournalCursorAdapter(this, R.layout.listview_layout_journal, null);

		lvJournals.setAdapter(adapter);

		Log.d(TAG, "set");

//		리스트 뷰 클릭 처리
        lvJournals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            	Log.d(TAG, "pos" + position);

				cursor.moveToPosition(position);

				journalDto = new JournalDto();
				journalDto.setId(cursor.getInt(cursor.getColumnIndex( helper.COL_ID )));
				journalDto.setTitle(cursor.getString(cursor.getColumnIndex( helper.COL_TITLE )));
				journalDto.setContent(cursor.getString(cursor.getColumnIndex( helper.COL_CONTENT )));
				journalDto.setFeeling(cursor.getString(cursor.getColumnIndex( helper.COL_FEELING )));
				journalDto.setImage(cursor.getString(cursor.getColumnIndex( helper.COL_IMAGE )));
				journalDto.setLocation(cursor.getString(cursor.getColumnIndex( helper.COL_LOCATION )));

				Log.d(TAG, "update click");
            	Intent intent = new Intent(AllJournalsActivity.this, UpdateJournalActivity.class);
				intent.putExtra("journal", journalDto);
				startActivityForResult(intent, UPDATE_CODE); // 업뎃 했을 수도 안 했을 수도
				
            }
        });

//		리스트 뷰 롱클릭 처리
		lvJournals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int pos = position; //위치 기억
				AlertDialog.Builder builder = new AlertDialog.Builder(AllJournalsActivity.this);
				builder.setTitle(R.string.dialogue_title_journal) // 다이알로그 띄움
						.setMessage(R.string.dialogue_message_journal)
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
									Toast.makeText(AllJournalsActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
									adapter.notifyDataSetChanged();
									onResume();
								} else {
									Toast.makeText(AllJournalsActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
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
			SQLiteDatabase db = helper.getReadableDatabase();
			cursor = db.rawQuery("select * from " + JournalDBHelper.TABLE_NAME, null);

			adapter.changeCursor(cursor);
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




