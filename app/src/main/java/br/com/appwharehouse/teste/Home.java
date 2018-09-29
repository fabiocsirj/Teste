package br.com.appwharehouse.teste;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Home extends AppCompatActivity implements Fragment1.OnSelectedItem {
    private JSONArray coins = new JSONArray();
    private SharedPreferences cache;
    private Fragment1 frag1;
    private boolean saveInstanceState; // Só destruir o JobScheduler se for um destroy do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cache = getSharedPreferences("Teste", MODE_PRIVATE);
        saveInstanceState = false;

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            frag1 = new Fragment1();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.placeHolder, frag1);
            ft.commit();
        } else {
            frag1 = (Fragment1) getFragmentManager().findFragmentById(R.id.fragment1);
        }

        if (savedInstanceState == null) {
            startJobScheduler();
            TaskGetTicker taskGetTicker = new TaskGetTicker();
            taskGetTicker.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadCache();
    }

    private void reloadCache() {
        Log.i("Teste", "reloadCache()");
        try {
            coins = new JSONArray(cache.getString("coins", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateFragment1();
    }

    private void startJobScheduler() {
        ComponentName componentName = new ComponentName(this, TesteJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(80, componentName)
                .setPeriodic(15 * 60 * 1000)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        int resultCode = jobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.i("Teste", "Job agendado...");
        } else {
            Log.i("Teste", "Agendamento do Job FALHOU!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!saveInstanceState) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            assert jobScheduler != null;
            jobScheduler.cancel(80);
            Log.i("Teste", "Job Cancelado!");
        }
    }

    @Override
    public void onSelect(int position) {
        Fragment2 frag2;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            try {
                String currency = coins.get(position).toString();
                Bundle bundle = new Bundle();
                bundle.putString("currency", currency);
                frag2 = new Fragment2();
                frag2.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.placeHolder, frag2);
                ft.addToBackStack(null);
                ft.commit();
                reloadCache();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            frag2 = (Fragment2) getFragmentManager().findFragmentById(R.id.fragment2);
            try {
                frag2.update((JSONObject) coins.get(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class TaskGetTicker extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Download...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return CoinMarketcap.getTicker();
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json.length() > 0) {
                try {
                    JSONObject data = json.getJSONObject("data");
                    coins = new JSONArray();
                    for(Iterator<String> keys = data.keys(); keys.hasNext();) {
                        String key = keys.next();
                        JSONObject coin = data.getJSONObject(key);
                        coins.put(coin);
                    }
                    saveCache();
                    ImagesOff.clearGraph(getApplicationContext(), coins);
                    updateFragment1();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Download Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveCache() {
        SharedPreferences.Editor editCache = cache.edit();
        editCache.putString("coins", coins.toString());
        editCache.apply();
    }

    private void updateFragment1() {
        try{
            Map<Integer, Bitmap> mapIcons = new HashMap<>();
            Bitmap iconDefault = BitmapFactory.decodeResource(getResources(), R.drawable.coin_of_dollar);
            ArrayList<Integer> idImageNotFound = new ArrayList<>();
            for(int i=0; i<coins.length(); i++) {
                int id = ((JSONObject)coins.get(i)).getInt("id");
                String fileName = String.valueOf(id) + ".png";
                Bitmap icon = ImagesOff.openBitmap(getApplication(), fileName);
                if (icon != null) mapIcons.put(id, icon);
                else {
                    mapIcons.put(id, iconDefault);
                    idImageNotFound.add(id);
                }
            }
            frag1.update(coins, mapIcons);

            if (idImageNotFound.size() > 0) {
                Integer[] ids = new Integer[idImageNotFound.size()];
                idImageNotFound.toArray(ids);
                TaskGetIcons taskGetIcons = new TaskGetIcons();
                taskGetIcons.execute(ids);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class TaskGetIcons extends AsyncTask<Integer, Void, Map<Integer, Bitmap>> {
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
                        ImagesOff.saveBitmap(getApplication(), fileName, (Bitmap)entry.getValue());
                    }
                }

                updateFragment1();

            } else {
                Toast.makeText(getApplicationContext(), "Download Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveInstanceState = true;
//        outState.putString("coins", coins.toString());
    }

}
