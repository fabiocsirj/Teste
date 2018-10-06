package br.com.appwharehouse.teste;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Coin.class}, version = 10, exportSchema = false)
public abstract class CoinRoomDatabase extends RoomDatabase {
    public abstract CoinDAO coinDAO();

    private static volatile CoinRoomDatabase INSTANCE;

    static CoinRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CoinRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CoinRoomDatabase.class, "coin_database")
                            .fallbackToDestructiveMigration()
//                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            Log.i("Teste", "RoomDatabase.Callback.onOpen()");
//            new clearDBAsync(INSTANCE).execute();
//        }
//    };
//
//    private static class clearDBAsync extends AsyncTask<Void, Void, Void> {
//        private final CoinDAO coinDAO;
//
//        clearDBAsync(CoinRoomDatabase db) {
//            coinDAO = db.coinDAO();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//            Log.i("Teste", "Limpando cache...");
//            coinDAO.clearCache();
//            return null;
//        }
//    }

}
