package br.com.appwharehouse.teste;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
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

    public static void clearGraph(Context context) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.startsWith("graph_");
            }
        };
        File[] files = dir.listFiles(filter);
        for(File file: files) {
            file.delete();
        }
    }
}
