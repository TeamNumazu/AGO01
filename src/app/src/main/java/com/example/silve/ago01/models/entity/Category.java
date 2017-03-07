package com.example.silve.ago01.models.entity;

/**
 * categoryテーブルの1行データを保持します
 */

public class Category {

    private long _id = -1;
    private String categoryName = "";

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }
}
