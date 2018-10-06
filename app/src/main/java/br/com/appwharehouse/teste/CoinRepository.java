package br.com.appwharehouse.teste;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class CoinRepository {
    private CoinDAO coinDAO;
    private LiveData<List<Coin>> allCoins;

    public CoinRepository(Application application) {
        CoinRoomDatabase db = CoinRoomDatabase.getDatabase(application);
        coinDAO = db.coinDAO();
        allCoins = coinDAO.getCoins();
    }

    public LiveData<List<Coin>> getAllCoins() {
        return allCoins;
    }

    public void insert(Coin coin) {
        new insertAsyncTask(coinDAO).execute(coin);
    }

    private static class insertAsyncTask extends AsyncTask<Coin, Void, Void> {
        private CoinDAO asyncTaskDao;

        insertAsyncTask(CoinDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Coin... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
