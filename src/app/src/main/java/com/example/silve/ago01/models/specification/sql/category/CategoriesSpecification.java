package com.example.silve.ago01.models.specification.sql.category;

import com.example.silve.ago01.models.specification.sql.SqlSpecification;

import com.example.silve.ago01.models.AgoContract;

/**
 * Created by silve on 2017/02/12.
 */

public class CategoriesSpecification implements SqlSpecification {


    public CategoriesSpecification() {
    }

    @Override
    public String toSqlQuery() {

        return String.format(
                "SELECT * FROM %1$S",
                AgoContract.Category.TABLE_NAME
        );
    }
}
