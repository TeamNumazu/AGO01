package com.example.silve.ago01.models.mapper;

import android.database.Cursor;

import com.example.silve.ago01.models.AgoContract;
import com.example.silve.ago01.models.entity.Item;
import com.example.silve.ago01.models.specification.database.CursorColumnExistSpecification;

/**
 * データベースカーソルからエンティティへ変換するクラス
 */
public class ItemMapper implements Mapper<Cursor, Item> {

    @Override
    public Item map(Cursor from) {
        Item item = new Item();

        //idが選択されていれば取得する
        if (CursorColumnExistSpecification.isSatisfied(from.getColumnIndex(AgoContract.Item._ID))) {
            long id = from.getLong(from.getColumnIndex(AgoContract.Item._ID));
            item.set_id(id);
        }

        //itemが選択されていれば取得する
        if (CursorColumnExistSpecification.isSatisfied(from.getColumnIndex(AgoContract.Item._ID))) {

            String itemName = from.getString(
                    from.getColumnIndex(AgoContract.Item.COLUMN_NAME_CATEGORYID)
            );

            item.setItemName(itemName);

        }

        return item;
    }
}
