package com.example.silve.ago01.models;

import android.provider.BaseColumns;

/**
 * agoプロジェクトで使用するテーブル定義をインナークラスで管理していきます
 */

public final class AgoContract {
    private AgoContract() {
    }

    /**
     * タグテーブル
     */
    public static class Category implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_CATEGORYNAME = "categoryname";

    }

    /**
     * 商品テーブル
     */
    public static class Item implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_CATEGORYID = "categoryid";
        public static final String COLUMN_NAME_ITEMNAME = "itemname";
        public static final String COLUMN_NAME_ITEMIMAGE = "itemimage";
        public static final String COLUMN_NAME_EXPIRED_AT = "expired_at";
        public static final String COLUMN_NAME_CREATED_AT = "created_at";
        public static final String COLUMN_NAME_UPDATED_AT = "updated_at";
        public static final String COLUMN_NAME_IS_BUY = "is_buy";
        public static final String COLUMN_NAME_NUMBER = "number";


    }
}

