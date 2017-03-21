package com.example.silve.ago01.models.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.silve.ago01.models.AgoContract;

import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Category;
import com.example.silve.ago01.models.entity.Item;
import com.example.silve.ago01.models.mapper.ItemMapper;
import com.example.silve.ago01.models.specification.sql.Specification;
import com.example.silve.ago01.models.specification.sql.SqlSpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silve on 2017/03/07.
 */

public class ItemRepository implements Repository<Item> {

    private final DataBaseHelper mDataBaseHelper;
    private final ItemMapper mItemMapper;

    public ItemRepository(DataBaseHelper dbHelper) {
        mDataBaseHelper = dbHelper;
        mItemMapper = new ItemMapper();
    }

    @Override
    public void add(Item item) {

        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(AgoContract.Item.COLUMN_NAME_CATEGORYID, item.getCategoryId());
            values.put(AgoContract.Item.COLUMN_NAME_ITEMNAME, item.getItemName());
            values.put(AgoContract.Item.COLUMN_NAME_ITEMIMAGE, item.getItemImage());
            values.put(AgoContract.Item.COLUMN_NAME_EXPIRED_AT, item.getExpiredAt());
            values.put(AgoContract.Item.COLUMN_NAME_CREATED_AT, item.getCreatedAt());
            values.put(AgoContract.Item.COLUMN_NAME_UPDATED_AT, item.getUpdatedAt());
            values.put(AgoContract.Item.COLUMN_NAME_NUMBER, item.getNumber());

            db.insert(AgoContract.Item.TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            db.endTransaction();
            throw e;
        }
        db.endTransaction();
    }

    @Override
    public void add(Iterable<Item> items) {

    }

    @Override
    public void update(Item item) {

    }

    @Override
    public void remove(Item item) {

    }

    @Override
    public void remove(Specification specification) {

    }

    @Override
    public List<Item> query(Specification specification) {

        final SqlSpecification sqlSpecification = (SqlSpecification) specification;
        final SQLiteDatabase database = mDataBaseHelper.getReadableDatabase();

        final List<Item> items = new ArrayList<>();
        
        try {

            final Cursor cursor = database.rawQuery(sqlSpecification.toSqlQuery(), new String[]{});

            for (int i = 0, size = cursor.getCount(); i < size; i++) {
                cursor.moveToPosition(i);

                items.add(mItemMapper.map(cursor));
            }

            return items;
        } finally {
            database.close();
        }
    }

}
