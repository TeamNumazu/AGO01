package com.example.silve.ago01.models.specification.sql.item;

import com.example.silve.ago01.models.AgoContract;
import com.example.silve.ago01.models.specification.sql.SqlSpecification;

/**
 * Created by silve on 2017/04/01.
 */

public class DeleteItemSpecification implements SqlSpecification {

    protected long mId = -1;

    public DeleteItemSpecification(Long id) {
        mId = id;

    }

    @Override
    public String toSqlQuery() {

        return String.format("DELETE FROM %1$S " + " WHERE  %2$S=%3$S ",
                AgoContract.Item.TABLE_NAME,
                AgoContract.Item._ID,
                mId
        );

    }
}
