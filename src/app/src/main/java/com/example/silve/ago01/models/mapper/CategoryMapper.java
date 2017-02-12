package com.example.silve.ago01.models.mapper;

import android.database.Cursor;

import com.example.silve.ago01.consts.CursorConst;
import com.example.silve.ago01.models.entity.Category;
import com.example.silve.ago01.models.AgoContract;
import com.example.silve.ago01.models.specification.database.CursorColumnExistSpecification;

/**
 * データベースカーソルからエンティティへ変換するクラス
 */
public class CategoryMapper implements Mapper<Cursor, Category> {

    @Override
    public Category map(Cursor from) {
        Category category = new Category();

        //idが選択されていれば取得する
        if (CursorColumnExistSpecification.isSatisfied(from.getColumnIndex(AgoContract.Category._ID))) {
            long id = from.getLong(from.getColumnIndex(AgoContract.Category._ID));
            category.set_id(id);
        }

        //categoryが選択されていれば取得する
        if (CursorColumnExistSpecification.isSatisfied(from.getColumnIndex(AgoContract.Category._ID))) {

            String categoryName = from.getString(
                    from.getColumnIndex(AgoContract.Category.COLUMN_NAME_CATEGORYNAME)
            );

            category.setCategoryName(categoryName);

        }

        return category;
    }
}
