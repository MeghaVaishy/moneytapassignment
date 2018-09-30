package com.example.megha.myapplication.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class SearchResultEntity {

    @ColumnInfo(name = "slNo")
    @PrimaryKey(autoGenerate = true)
    public int slNo;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "imageUrl")
    public String imageUrl;

    public SearchResultEntity(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

}
