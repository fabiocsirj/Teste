package br.com.appwharehouse.teste;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class ImagesOff {
    public static void saveBitmap(Context context, String fileName, Bitmap bMap) {
        String status = Environment.getExternalStorageState();

        try {
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                java.io.File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                java.io.File file = new java.io.File(dir, fileName);
                OutputStream out = new FileOutputStream(file);
                bMap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap openBitmap(Context context, String fileName) {
        Bitmap bMap = null;
        java.io.File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        java.io.File file = new java.io.File(dir, fileName);

        if (file.exists()) bMap = BitmapFactory.decodeFile(file.getAbsolutePath());

        return bMap;
    }

    private static void deleteBitmap(Context context, String fileName) {
        java.io.File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        java.io.File file = new java.io.File(dir, fileName);

        if (file.exists()) file.delete();
    }

    public static void clearGraph(Context context, JSONArray coins) {
        for(int i=0; i<coins.length(); i++) {
            try {
                int id = ((JSONObject)coins.get(i)).getInt("id");
                String fileName = "graph_" + String.valueOf(id) + ".png";
                deleteBitmap(context, fileName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
