package br.com.appwharehouse.teste;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class TesteJobService extends JobService {
    private JobParameters jobParameters;
    private SharedPreferences cache;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("Teste", "onStartJob()");
        cache = getSharedPreferences("Teste", MODE_PRIVATE);
        this.jobParameters = jobParameters;

        TaskGetTicker taskGetTicker = new TaskGetTicker();
        taskGetTicker.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("Teste", "onStopJob()");
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class TaskGetTicker extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            Log.i("Teste", "Job Scheduler Download...");
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return CoinMarketcap.getTicker();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json.length() > 0) {
                Log.i("Teste", "Job Scheduler Download... Completo!");
                try {
                    JSONObject data = json.getJSONObject("data");
                    JSONArray coins = new JSONArray();
                    for(Iterator<String> keys = data.keys(); keys.hasNext();) {
                        String key = keys.next();
                        JSONObject coin = data.getJSONObject(key);
                        coins.put(coin);
                    }
                    saveCache(coins);
                    ImagesOff.clearGraph(getApplicationContext(), coins);
                    //updateFragment1();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else Log.i("Teste", "Job Scheduler Download Error!");
            jobFinished(jobParameters, false);
        }
    }

    private void saveCache(JSONArray coins) {
        Log.i("Teste", "Atualizou Cache!");
        SharedPreferences.Editor editCache = cache.edit();
        editCache.putString("coins", coins.toString());
        editCache.apply();
    }

}
