package br.com.appwharehouse.teste;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class CoinMarketcap {

    public static JSONObject getTicker() {
        JSONObject json = new JSONObject();

        try {
            URL url = new URL("https://api.coinmarketcap.com/v2/ticker/?limit=10");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                StringBuilder resp = new StringBuilder();
                Scanner s = new Scanner(conn.getInputStream());
                while (s.hasNext()) resp.append(s.nextLine());
                resp = new StringBuilder(resp.toString().trim());
                Log.i("CoinMarketcap: ", resp.toString());
                json = new JSONObject(resp.toString());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static Map<Integer, Bitmap> getIcons(Integer[] ids) {
        Map<Integer, Bitmap> map = new HashMap<>();
        for(int i=0; i<ids.length; i++) {
            Bitmap bMap = getBitmap("https://s2.coinmarketcap.com/static/img/coins/16x16/" + ids[i] + ".png");
            map.put(ids[i], bMap);
        }
        return map;
    }

    public static Bitmap getGraph(int id) {
        return getBitmap("https://s2.coinmarketcap.com/generated/sparklines/web/7d/usd/" + id + ".png");
    }

    private static Bitmap getBitmap(String urlImage) {
        DataInputStream dis;
        ByteArrayOutputStream bos;
        Bitmap bMap = null;
        try {
            Log.i("Teste", "Download: " + urlImage);
            URL url = new URL(urlImage);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                dis = new DataInputStream(conn.getInputStream());
                bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int xr;
                while ((xr = dis.read(buffer)) > 0) bos.write(buffer, 0, xr);
                bos.flush();
                bMap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size());
                bos.close();
                dis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bMap;
    }
}
