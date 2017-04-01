package com.example.silve.ago01.models.specification.sql.item;

import com.example.silve.ago01.models.AgoContract;
import com.example.silve.ago01.models.specification.sql.SqlSpecification;

/**
 * Created by silve on 2017/02/24.
 */

public class FindByCategorySpecification implements SqlSpecification {

    protected int categoryId = -1;

    public FindByCategorySpecification(int id) {
        categoryId = id;
    }

    @Override
    public String toSqlQuery() {

        return String.format("SELECT * FROM ITEM WHERE %1$S WHERE " +
                        AgoContract.Item.COLUMN_NAME_CATEGORYID + "= %2$S",
                AgoContract.Item.TABLE_NAME,
                categoryId
        );

    }
}
