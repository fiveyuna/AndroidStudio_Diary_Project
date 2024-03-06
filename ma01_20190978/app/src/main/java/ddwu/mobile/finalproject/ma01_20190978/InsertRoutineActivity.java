package ddwu.mobile.finalproject.ma01_20190978;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertRoutineActivity extends AppCompatActivity {

	EditText etTime;
	EditText etDid;

	RoutineDBHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_routine);

		etTime = findViewById(R.id.etTime);
		etDid = findViewById(R.id.etDid);

		helper = new RoutineDBHelper(this);
	}

	protected String getDate() {
		long now = System.currentTimeMillis();
		Date result_date = new Date(now);
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		String returnDate = simpleDate.format(result_date);

		return returnDate;
	}
	
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAddNewRoutine:
//			DB 데이터 삽입 작업 수행
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues value = new ContentValues();
			value.put(helper.COL_TIME, etTime.getText().toString());
			value.put(helper.COL_DID, etDid.getText().toString());
			value.put(helper.COL_DATE, getDate());

			long count = db.insert(helper.TABLE_NAME, null, value);

			if (count > 0) {    // 정상수행에 따른 처리
				etTime.setText("");
				etDid.setText("");
				Toast.makeText(this, "새로운 루틴이 추가되었습니다", Toast.LENGTH_SHORT).show();
			} else {        // 이상에 따른 처리
				Toast.makeText(this, "새로운 루틴 추가에 실패하였습니다", Toast.LENGTH_SHORT).show();
			}

			helper.close();
			break;
		case R.id.btnAddNewRoutineClose:
//			DB 데이터 삽입 취소 수행
			finish();
			break;
		}
	}
	
	
	
	
	
}
