package com.nevermore.mapasignala.DbStuff;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EntryDao {
    @Query("SELECT * FROM Entry")
    List<Entry> getAll();

    @Insert
    void insertAll(Entry... en);

    @Update
    void updateUsers(Entry... entries);

    @Delete
    void delete(Entry... entries);
}
