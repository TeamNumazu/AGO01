package com.example.silve.ago01.models.specification.sql.item;

import com.example.silve.ago01.models.AgoContract;
import com.example.silve.ago01.models.specification.sql.SqlSpecification;

/**
 * Created by silve on 2017/02/24.
 */

public class FindAllSpecification implements SqlSpecification {


    public FindAllSpecification() {

    }

    @Override
    public String toSqlQuery() {

        return "SELECT * FROM ITEM";

    }
}
