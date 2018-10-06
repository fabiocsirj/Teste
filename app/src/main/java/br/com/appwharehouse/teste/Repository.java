package br.com.appwharehouse.teste;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Repository {
    private static volatile Repository INSTANCE;

    private Repository() { }

    public static synchronized Repository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

    public void getTicker(Application application) {
        Log.i("Teste", "getTicker()");
        new TaskGetTicker(application).execute();
    }

    public void getIcons(Integer[] ids) {
        new TaskGetIcons().execute(ids);
    }

    public void getGraph(Fragment2 context, int id) {
        new TaskGetGraph(context).execute(id);
    }

    private static class TaskGetTicker extends AsyncTask<Void, Void, JSONObject> {
        Application application;

        public TaskGetTicker(Application application) {
            this.application = application;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.i("Teste", "TaskGetTicker");
            return CoinMarketcap.getTicker();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json.length() > 0) {
                try {
                    CoinRepository coinRepository = new CoinRepository(application);
                    JSONObject data = json.getJSONObject("data");
                    for(Iterator<String> keys = data.keys(); keys.hasNext();) {
                        String key = keys.next();
                        JSONObject jCoin = data.getJSONObject(key);
                        int rank = jCoin.getInt("rank");
                        int id = jCoin.getInt("id");
                        String name = jCoin.getString("name");
                        JSONObject quotes = jCoin.getJSONObject("quotes");
                        JSONObject usd = quotes.getJSONObject("USD");
                        double price = usd.getDouble("price");
                        double market_cap = usd.getDouble("market_cap");
                        double volume_24h = usd.getDouble("volume_24h");
                        double percent_change_1h = usd.getDouble("percent_change_1h");
                        double percent_change_24h = usd.getDouble("percent_change_24h");
                        double percent_change_7d = usd.getDouble("percent_change_7d");
                        Coin coin = new Coin(rank,
                                id,
                                name,
                                price,
                                market_cap,
                                volume_24h,
                                percent_change_1h,
                                percent_change_24h,
                                percent_change_7d);
                        coinRepository.insert(coin);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i("Teste", "Download Error!");
            }
        }
    }

    private static class TaskGetIcons extends AsyncTask<Integer, Void, Map<Integer, Bitmap>> {
        @Override
        protected Map<Integer, Bitmap> doInBackground(Integer... ids) {
            Map<Integer, Bitmap> map = null;
            try {
                map = CoinMarketcap.getIcons(ids);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(Map<Integer, Bitmap> map) {
            if (map != null) {
                for(Map.Entry entry: map.entrySet()) {
                    if (entry.getValue() != null) {
                        String fileName = String.valueOf(entry.getKey()) + ".png";
                        Home instance = Home.getInstance();
                        if (instance != null) {
                            ImagesOff.saveBitmap(instance, fileName, (Bitmap)entry.getValue());
                        }
                    }
                }
            } else {
                Log.i("Teste", "Download Error!");
            }
        }
    }

    private static class TaskGetGraph extends AsyncTask<Integer, Void, Object[]> {
        private OnChangeGraph listener;

        public TaskGetGraph(OnChangeGraph listener) {
            this.listener = listener;
        }

        @Override
        protected Object[] doInBackground(Integer... id) {
            Object[] obj = new Object[2];
            obj[0] = id[0];
            Bitmap bMap;

            try {
                bMap = CoinMarketcap.getGraph(id[0]);
                obj[1] = bMap;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return obj;
        }

        @Override
        protected void onPostExecute(Object[] obj) {
            int id = (int) obj[0];
            Bitmap bMap = (Bitmap) obj[1];
            if (bMap != null) {
                String fileName = "graph_" + String.valueOf(id) + ".png";

                Home home_instance = Home.getInstance();
                if (home_instance != null) {
                    ImagesOff.saveBitmap(home_instance, fileName, bMap);
                }

                Fragment2 fragment2_instance = Fragment2.getInstance();
                if (fragment2_instance != null) {
                    listener.onChange(bMap);
                }

            } else {
                Log.i("Teste", "Download Error!");
            }
        }
    }
}
