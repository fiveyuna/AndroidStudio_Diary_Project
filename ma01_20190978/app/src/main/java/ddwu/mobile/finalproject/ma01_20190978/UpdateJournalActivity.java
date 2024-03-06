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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateJournalActivity extends AppCompatActivity {

    final static String TAG = "UpdateJournalActivity";

    EditText etTitle;
    EditText etContent;
    EditText etFeeling;

    JournalDto journalDto;
    JournalDBHelper helper;

    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int REQUEST_GET_LOCATION = 200;

    private ImageView ivUpdateJournal;
    private String mCurrentPhotoPath = null;

    TextView tvUpdateLocation;
    String selectLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_journal);

        journalDto = (JournalDto) getIntent().getSerializableExtra("journal");

        etTitle = findViewById(R.id.etUpdateTitle);
        etContent = findViewById(R.id.etUpdateContent);
        etFeeling = findViewById(R.id.etUpdateFeeling);
        ivUpdateJournal = (ImageView) findViewById(R.id.ivUpdateJournal);
        tvUpdateLocation = findViewById(R.id.tvUpdateLocation);

        etTitle.setText(journalDto.getTitle());
        etContent.setText(journalDto.getContent());
        etFeeling.setText(journalDto.getFeeling());
        mCurrentPhotoPath = journalDto.getImage();
        tvUpdateLocation.setText(journalDto.getLocation());

        if (mCurrentPhotoPath != null) {
            Log.d(TAG, "path: " + mCurrentPhotoPath);
            setPic();
        }

        helper = new JournalDBHelper(this);

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnUpdateJournal:
//                DB 데이터 업데이트 작업 수행
                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

                ContentValues row = new ContentValues();
                row.put(helper.COL_TITLE, etTitle.getText().toString());
                row.put(helper.COL_CONTENT, etContent.getText().toString());
                row.put(helper.COL_FEELING, etFeeling.getText().toString());
                row.put(helper.COL_IMAGE, mCurrentPhotoPath);
                row.put(helper.COL_LOCATION, selectLocation);

                String whereClause = helper.COL_ID + "=?";
                String[] whereArgs = new String[] { String.valueOf(journalDto.getId()) };

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
            case R.id.btnUpdateJournalClose:
//                DB 데이터 업데이트 작업 취소
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.btnUpdateCapture:
                dispatchTakePictureIntent();
            break;

            case R.id.btnUpdateLocation:
                Intent intent = new Intent(UpdateJournalActivity.this, LocationActivity.class);
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
//        int targetW = ivUpdateJournal.getWidth();
//        int targetH = ivUpdateJournal.getHeight();
//
//        Log.d(TAG, "tW: " + targetW + " tH: " + targetH);

        int targetW = 300;
        int targetH = 200;

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
        ivUpdateJournal.setImageBitmap(bitmap);
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
            tvUpdateLocation.setText(result);
            selectLocation = result;
        }
    }


}
