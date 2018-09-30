package com.example.megha.myapplication.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DaoAccess {
    @Insert
    void insertMultipleListRecord(List<SearchResultEntity> universities);

    @Query("SELECT * FROM SearchResultEntity WHERE title LIKE:text")
    List<SearchResultEntity> getRecords(String text);
}
