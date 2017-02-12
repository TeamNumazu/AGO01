package com.example.silve.ago01.models.specification.database;

import com.example.silve.ago01.consts.CursorConst;

/**
 * Created by silve on 2017/02/12.
 */

public class CursorColumnExistSpecification {

    public static Boolean isSatisfied(int index) {
        return index > CursorConst.COLUMNS_NOT_EXISTS;
    }
}
