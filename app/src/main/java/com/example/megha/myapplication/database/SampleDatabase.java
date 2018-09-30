package com.example.megha.myapplication.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {SearchResultEntity.class}, version = 1)
public abstract class SampleDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
