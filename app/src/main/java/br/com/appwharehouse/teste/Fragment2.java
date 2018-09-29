package br.com.appwharehouse.teste;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Fragment2 extends Fragment {
    private TextView tv_coin2, tv_price2, tv_marketcap, tv_vol24h, tv_chan1h, tv_chan24h, tv_chan7d;

    private ImageView img_coin2, img_graph;
    private TextView[] tv_lab = new TextView[6];


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_coin2 = view.findViewById(R.id.tv_coin2);
        tv_price2 = view.findViewById(R.id.tv_price2);
        tv_marketcap = view.findViewById(R.id.tv_marketcap);
        tv_vol24h = view.findViewById(R.id.tv_vol24h);
        tv_chan1h = view.findViewById(R.id.tv_chan1h);
        tv_chan24h = view.findViewById(R.id.tv_chan24h);
        tv_chan7d = view.findViewById(R.id.tv_chan7d);

        tv_lab[0] = view.findViewById(R.id.tv_lab_marketcap);
        tv_lab[1] = view.findViewById(R.id.tv_lab_price);
        tv_lab[2] = view.findViewById(R.id.tv_lab_vol24h);
        tv_lab[3] = view.findViewById(R.id.tv_lab_chan1h);
        tv_lab[4] = view.findViewById(R.id.tv_lab_chan24h);
        tv_lab[5] = view.findViewById(R.id.tv_lab_chan7d);
        img_coin2 = view.findViewById(R.id.img_coin2);
        img_graph = view.findViewById(R.id.img_graph);

        if (getArguments() != null && !getArguments().isEmpty()) {
            String currency = getArguments().getString("currency");
            try {
                JSONObject coin = new JSONObject(currency);
                update(coin);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void update(JSONObject coin) {
        if (tv_coin2 != null) {
            try {
                for(int i=0; i<6; i++) tv_lab[i].setVisibility(View.VISIBLE);
                img_coin2.setVisibility(View.VISIBLE);
                img_graph.setVisibility(View.VISIBLE);

                int id = coin.getInt("id");
                String fileName = String.valueOf(id) + ".png";
                Bitmap icon = ImagesOff.openBitmap(getActivity(), fileName);
                if (icon != null) img_coin2.setImageBitmap(icon);

                fileName = "graph_" + fileName;
                Bitmap graph = ImagesOff.openBitmap(getActivity(), fileName);
                if (graph != null) {
                    img_graph.setImageBitmap(graph);
                    img_graph.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                else {
                    TaskGetGraph taskGetGraph = new TaskGetGraph();
                    taskGetGraph.execute(id);
                }

                DecimalFormat df = new DecimalFormat("#,##0.00");
                df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
                tv_coin2.setText(coin.getString("name"));
                JSONObject quotes = coin.getJSONObject("quotes");
                JSONObject usd = quotes.getJSONObject("USD");
                tv_price2.setText(df.format(usd.getDouble("price")));
                tv_marketcap.setText(df.format(usd.getDouble("market_cap")));
                tv_vol24h.setText(df.format(usd.getDouble("volume_24h")));
                tv_chan1h.setText(String.valueOf(usd.getDouble("percent_change_1h")) + "%");
                tv_chan24h.setText(String.valueOf(usd.getDouble("percent_change_24h")) + "%");
                tv_chan7d.setText(String.valueOf(usd.getDouble("percent_change_7d")) + "%");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @SuppressLint("StaticFieldLeak")
    private class TaskGetGraph extends AsyncTask<Integer, Void, Object[]> {
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
                ImagesOff.saveBitmap(getActivity(), fileName, bMap);
                img_graph.setImageBitmap(bMap);
                img_graph.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                Toast.makeText(getActivity(), "Download Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
