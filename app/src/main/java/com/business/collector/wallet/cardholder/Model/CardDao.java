package com.business.collector.wallet.cardholder.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CardDao {
    @Insert
    void insert(Card card);

    @Update
    void update(Card card);

    @Delete
    void delete(Card card);

    @Query("DELETE FROM card_table")
    void deleteAllCards();

    @Query("SELECT * FROM card_table ORDER BY id DESC")
    LiveData<List<Card>> getAllCards();
}
