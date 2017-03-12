package com.example.silve.ago01.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.Toast;

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
    static protected int REQUEST_USE_CAMERA_AND_STORAGE = 0x00000003;


    private Bitmap bitmap;
    private Uri bitmapUri;

    public void onClickCameraButton(View v) {
//        wakeupCamera(); // カメラ起動
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
        File mediaStorageDir = null;


        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //RUNTIME PERMISSION Android M
            if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)){
                mediaStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"IMG"
                );
            }else{
                requestCameraPermission(this);
                return;
            }

        }

        if(! mediaStorageDir.exists() & ! mediaStorageDir.mkdirs()) {
            Log.d("TAG", "Failed to create directory");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File bitmapFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".JPG");

        bitmapUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", bitmapFile);

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, bitmapUri);
        startActivityForResult(i, REQUEST_CODE_CAMERA);
    }

    private static void requestCameraPermission(final Context context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.WRITE_EXTERNAL_STORAGE)||
                ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.CAMERA)){
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    REQUEST_USE_CAMERA_AND_STORAGE);

        }else {
            // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    REQUEST_USE_CAMERA_AND_STORAGE);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0x00000003: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "成功やで",
                            Toast.LENGTH_SHORT).show();
                    wakeupCamera();

                } else {
                    Toast.makeText(this,
                            "失敗やで",
                            Toast.LENGTH_SHORT).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                return;
            }
        }
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
            ImageView itemView = (ImageView) findViewById(R.id.item_view);
            itemView.setImageURI(bitmapUri);
//            itemView.setImageBitmap(bitmap);

        }
    }


}
