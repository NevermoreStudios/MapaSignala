package com.nevermore.mapasignala.DbStuff;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Entry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EntryDao entyDao();
}
