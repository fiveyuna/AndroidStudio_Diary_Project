package ddwu.mobile.finalproject.ma01_20190978;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class InsertJournalActivity extends AppCompatActivity {

	final static String TAG = "InsertJournalActivity";

	EditText etTitle;
	EditText etContent;
	EditText etFeeling;

	TextView tvNowDate;
	TextView tvLocation;

	JournalDBHelper helper;

	private static final int REQUEST_TAKE_PHOTO = 100;
	private static final int REQUEST_GET_LOCATION = 200;

	private ImageView ivInsertJournal;
	private String mCurrentPhotoPath = null;

	private String selectLocation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_journal);

		etTitle = findViewById(R.id.etTitle);
		etContent = findViewById(R.id.etContent);
		etFeeling = findViewById(R.id.etFeeling);

		tvNowDate = findViewById(R.id.tvNowDate);
		tvNowDate.setText(getDate(0));
		tvLocation = findViewById(R.id.tvLocation);

		ivInsertJournal = (ImageView) findViewById(R.id.ivInsertJournal);

		helper = new JournalDBHelper(this);

		//        외부 저장소 확인해보기
		Log.i(TAG, getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
		Log.i(TAG, getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());
	}

	protected String getDate(int x) {
		long now = System.currentTimeMillis();
		now = now + (1000 * 60 * 60 * 24) * x;
		Date result_date = new Date(now);
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		String returnDate = simpleDate.format(result_date);

		return returnDate;
	}
	
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAddNewJournal:
//			DB 데이터 삽입 작업 수행
			SQLiteDatabase db = helper.getWritableDatabase();
			ContentValues value = new ContentValues();
			value.put(helper.COL_TITLE, etTitle.getText().toString());
			value.put(helper.COL_CONTENT, etContent.getText().toString());
			value.put(helper.COL_FEELING, etFeeling.getText().toString());
			value.put(helper.COL_DATE, tvNowDate.getText().toString());
			value.put(helper.COL_IMAGE, mCurrentPhotoPath);
			value.put(helper.COL_LOCATION, selectLocation);

			long count = db.insert(helper.TABLE_NAME, null, value);

			if (count > 0) {    // 정상수행에 따른 처리
				etTitle.setText("");
				etContent.setText("");
				etFeeling.setText("");
				ivInsertJournal.setImageResource(R.drawable.basic_image);
				tvLocation.setText("현재 위치 정보 없음");
				Toast.makeText(this, "새로운 일기가 추가되었습니다", Toast.LENGTH_SHORT).show();
			} else {        // 이상에 따른 처리
				Toast.makeText(this, "새로운 일기 추가에 실패하였습니다", Toast.LENGTH_SHORT).show();
			}

			helper.close();
			break;
		case R.id.btnAddNewJournalClose:
//			DB 데이터 삽입 취소 수행
			finish();
			break;
		case R.id.btnCapture:
			dispatchTakePictureIntent();
			break;
		case R.id.btnLocation:
			Intent intent = new Intent(InsertJournalActivity.this, LocationActivity.class);
			startActivityForResult(intent, REQUEST_GET_LOCATION);
		}
	}

	/*원본 사진 파일 저장*/
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			File photoFile = null;

			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (photoFile != null) {
				Uri photoUri = FileProvider.getUriForFile(this,
						"ddwu.mobile.finalproject.fileprovider",
						photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}

	}

	/*사진의 크기를 ImageView에서 표시할 수 있는 크기로 변경*/
	private void setPic() {
		// Get the dimensions of the View
		int targetW = ivInsertJournal.getWidth();
		int targetH = ivInsertJournal.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		ivInsertJournal.setImageBitmap(bitmap);
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
			setPic();
		}
		else if (requestCode == REQUEST_GET_LOCATION && resultCode == RESULT_OK) {
			String result = data.getStringExtra("location");
			tvLocation.setText(result);
			selectLocation = result;
		}
	}


}
