package ddwu.mobile.finalproject.ma01_20190978;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateRoutineActivity extends AppCompatActivity {

    final static String TAG = "UpdateActivity";

    EditText etTime;
    EditText etDid;

    RoutineDto routineDto;

    RoutineDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_routine);

        routineDto = (RoutineDto) getIntent().getSerializableExtra("routine");

        etTime = findViewById(R.id.etUpdateTime);
        etDid = findViewById(R.id.etUpdateDid);

        etTime.setText(routineDto.getTime());
        etDid.setText(routineDto.getDid());

        helper = new RoutineDBHelper(this);

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateRoutine:
//                DB 데이터 업데이트 작업 수행
                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

                ContentValues row = new ContentValues();
                row.put(helper.COL_TIME, etTime.getText().toString());
                row.put(helper.COL_DID, etDid.getText().toString());

                String whereClause = helper.COL_ID + "=?";
                String[] whereArgs = new String[] { String.valueOf(routineDto.getId()) };

                int result = sqLiteDatabase.update(helper.TABLE_NAME, row, whereClause, whereArgs);
                helper.close();

                if(result > 0) {
                    Log.d(TAG, "업데이트 성공");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Log.d(TAG, "업데이트 실패");
                    setResult(RESULT_CANCELED);
                }
                break;
            case R.id.btnUpdateRoutineClose:
//                DB 데이터 업데이트 작업 취소
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }


}
