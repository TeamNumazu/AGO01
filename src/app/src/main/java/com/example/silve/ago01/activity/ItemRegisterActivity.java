package com.example.silve.ago01.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.silve.ago01.R;
import com.example.silve.ago01.fragments.DatePickerFragment;
import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Item;
import com.example.silve.ago01.models.repository.ItemRepository;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.database.Cursor;

public class ItemRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * datePickerを表示する
     *
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * 登録ボタンをクリックした時
     *
     * @param v
     */
    public void onClickRegisterButton(View v) {
        EditText itemName = (EditText) findViewById(R.id.item_name);
        EditText expiredAt = (EditText) findViewById(R.id.expired_at);
        EditText number = (EditText) findViewById(R.id.number);

        Item item = new Item();
        item.setItemName(itemName.getText().toString());
        item.setExpiredAt(expiredAt.getText().toString());
        item.setNumber(Long.parseLong(number.getText().toString()));

        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        ItemRepository iRepository = new ItemRepository(dbHelper);
        iRepository.add(item);
    }

    //カメラようだよーーー

    static protected int REQUEST_CODE_CAMERA  = 0x00000001;
    static protected int REQUEST_CODE_GALLERY = 0x00000002;


    private Bitmap bitmap;
    private Uri bitmapUri;

    public void onClickCameraButton(View v) {
        //選択項目を準備する。
        String[] str_items = {"カメラで撮影", "ギャラリーから選択", "キャンセル"};
        new AlertDialog.Builder(this)
                .setTitle("選択")
                .setItems(str_items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        wakeupCamera(); // カメラ起動
                                        break;
                                    case 1:
                                        wakeupGallery(); // ギャラリー起動
                                        break;
                                    default:
                                        // カメラ・ギャラリー以外(キャンセル)を想定
                                        break;
                                }
                            }
                        }
                ).show();

    }

    /**
     * カメラ起動するもよう
     */
    protected void wakeupCamera(){
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ), "PictureSaveDir"
        );

        if(! mediaStorageDir.exists() & ! mediaStorageDir.mkdirs()) {
            Log.d("TAG", "Failed to create directory");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File bitmapFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".JPG");
        Uri bitmapUri = Uri.fromFile(bitmapFile);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, bitmapUri);
        startActivityForResult(i, REQUEST_CODE_CAMERA);
    }

    /**
     * ギャラリーを起動だ！
     */
    protected void wakeupGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_CODE_GALLERY);
    }


    //ロストテクノロジー
    public void onActivityResult( int requestCode, int resultCode, Intent data ) {

        if(RESULT_OK == resultCode){
            if(null != bitmap){
                bitmap.recycle();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            switch(requestCode){
                case 0x00000001:
                    bitmap = BitmapFactory.decodeFile(bitmapUri.getPath(), options);

                    // 撮影した画像をギャラリーのインデックスに追加されるようにスキャンさせる
                    String[] paths = {bitmapUri.getPath()};
                    String[] mimeTypes = {"image/*"};
                    MediaScannerConnection.scanFile(
                                getApplicationContext(), paths, mimeTypes, new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                }
                            });
                    break;
                case 0x00000002:
                    try{
                        ContentResolver cr = getContentResolver();
                        String[] columns = { MediaStore.Images.Media.DATA };
                        Cursor c = cr.query(data.getData(), columns, null, null, null);
                        c.moveToFirst();
                        bitmapUri = Uri.fromFile(new File(c.getString(0)));

                        InputStream is = getContentResolver().openInputStream(data.getData());
                        bitmap = BitmapFactory.decodeStream(is, null, options);
                        is.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
            //こいつは受け取った写真のviewになる。
//            preview.setImageBitmap(bitmap);
        }
    }


}
