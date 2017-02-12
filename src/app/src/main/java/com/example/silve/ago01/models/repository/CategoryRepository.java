package com.example.silve.ago01.models.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Category;
import com.example.silve.ago01.models.mapper.CategoryMapper;
import com.example.silve.ago01.models.specification.sql.Specification;
import com.example.silve.ago01.models.specification.sql.SqlSpecification;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by silve on 2017/02/12.
 */

public class CategoryRepository implements Repository<Category> {

    private final DataBaseHelper dataBaseHelper;

    private final CategoryMapper toCategoryMapper;

    /**
     * @param helper
     */
    public CategoryRepository(DataBaseHelper helper) {

        dataBaseHelper = helper;

        toCategoryMapper = new CategoryMapper();
    }

    /**
     * @param item
     */
    @Override
    public void add(Category item) {

    }

    /**
     * @param items
     */
    @Override
    public void add(Iterable<Category> items) {

    }

    /**
     * @param item
     */
    @Override
    public void update(Category item) {

    }

    /**
     * @param item
     */
    @Override
    public void remove(Category item) {

    }

    /**
     * @param specification
     */
    @Override
    public void remove(Specification specification) {

    }

    /**
     * @param specification
     * @return
     */
    @Override
    public List<Category> query(Specification specification) {

        final SqlSpecification sqlSpecification = (SqlSpecification) specification;
        final SQLiteDatabase database = dataBaseHelper.getReadableDatabase();

        final List<Category> categories = new ArrayList<>();


        try {

            final Cursor cursor = database.rawQuery(sqlSpecification.toSqlQuery(), new String[]{});

            for (int i = 0, size = cursor.getCount(); i < size; i++) {
                cursor.moveToPosition(i);

                categories.add(toCategoryMapper.map(cursor));
            }

            return categories;
        } finally {
            database.close();

        }
    }
}
