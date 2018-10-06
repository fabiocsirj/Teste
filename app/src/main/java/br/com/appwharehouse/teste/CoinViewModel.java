package br.com.appwharehouse.teste;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class CoinViewModel extends AndroidViewModel {

    private CoinRepository repository;
    private LiveData<List<Coin>> allCoins;

    public CoinViewModel(@NonNull Application application) {
        super(application);
        repository = new CoinRepository(application);
        allCoins = repository.getAllCoins();
    }

    public LiveData<List<Coin>> getAllCoins() {
        return allCoins;
    }

    void insert(Coin coin) {
        repository.insert(coin);
    }

}
