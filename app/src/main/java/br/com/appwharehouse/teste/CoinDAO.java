package br.com.appwharehouse.teste;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CoinDAO {

    @Query("SELECT * from coin_table ORDER BY cRank ASC")
    LiveData<List<Coin>> getCoins();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Coin coin);

//    @Query("DELETE FROM coin_table")
//    void clearCache();

}
