package com.example.silve.ago01.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.silve.ago01.models.AgoContract;

/**
 * データベースを作成する
 * アプリ開始時に使用するテーブルとデータを挿入するクラスです
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //sqlite text型です
    private static final String TEXT_TYPE = " TEXT";

    //sqlite BLOB型です
    private static final String BLOB_TYPE = " BLOB";

    //sqlite INTEGER型です
    private static final String INTEGER_TYPE = " INTEGER";


    //カンマ
    private static final String COMMA_SEP = ",";

    //カテゴリータグのDDL文
    private static final String SQL_CREATE_CATEGORY =
            "CREATE TABLE " + AgoContract.Category.TABLE_NAME + " (" +
                    AgoContract.Category._ID + " INTEGER PRIMARY KEY," +
                    AgoContract.Category.COLUMN_NAME_CATEGORYNAME + TEXT_TYPE +
                    " )";

    //商品のDDL文
    private static final String SQL_CREATE_ITEM =
            "CREATE TABLE " + AgoContract.Item.TABLE_NAME + " (" +
                    AgoContract.Item._ID + " INTEGER PRIMARY KEY," +
                    AgoContract.Item.COLUMN_NAME_CATEGORYID + INTEGER_TYPE + COMMA_SEP +
                    AgoContract.Item.COLUMN_NAME_ITEMNAME + TEXT_TYPE + COMMA_SEP +
                    AgoContract.Item.COLUMN_NAME_EXPIRED_AT + TEXT_TYPE + COMMA_SEP +
                    AgoContract.Item.COLUMN_NAME_UPDATED_AT + TEXT_TYPE + COMMA_SEP +
                    AgoContract.Item.COLUMN_NAME_CREATED_AT + TEXT_TYPE + COMMA_SEP +
                    AgoContract.Item.COLUMN_NAME_IS_BUY + TEXT_TYPE + COMMA_SEP +
                    AgoContract.Item.COLUMN_NAME_ITEMIMAGE + BLOB_TYPE + COMMA_SEP +
                    AgoContract.Item.COLUMN_NAME_NUMBER + INTEGER_TYPE +
                    " )";

    private static final String SQL_DELETE_CATEGORY =
            "DROP TABLE IF EXISTS " + AgoContract.Category.TABLE_NAME;


    private static final String SQL_DELETE_ITEM =
            "DROP TABLE IF EXISTS " + AgoContract.Item.TABLE_NAME;

    private static String DB_NAME = "ago.db";
    private static final int DATABASE_VERSION = 17;


    /**
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    /**
     * データベースが作成されていないときに呼ばれる初期化処理
     * ここではテーブル作成と初期のデータ登録をおこなっています
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //必要テーブルのDDL実行
        db.execSQL(SQL_CREATE_CATEGORY);
        db.execSQL(SQL_CREATE_ITEM);

        //初期データ投入
        //初期カテゴリタグを追加
        String[] categoryNames = {"冷蔵庫", "冷凍庫", "調味料", "台所"};
        db.beginTransaction();
        try {

            for (String categoryName : categoryNames) {
                ContentValues values = new ContentValues();
                values.put(AgoContract.Category.COLUMN_NAME_CATEGORYNAME, categoryName);
                db.insert(AgoContract.Category.TABLE_NAME, null, values);

            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            db.endTransaction();
            throw e;
        }
        db.endTransaction();
    }

    /**
     * データベースをアップデートする必要があるときに使うらしい
     * テーブル定義の変更等があれば適宜修正する必要があります。
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CATEGORY);
        db.execSQL(SQL_DELETE_ITEM);
        onCreate(db);
    }

}
