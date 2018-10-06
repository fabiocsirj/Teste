package br.com.appwharehouse.teste;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Fragment2 extends Fragment implements OnChangeGraph {
    private static Fragment2 instance;
    public static Fragment2 getInstance() { return instance; }

    private TextView tv_coin2, tv_price2, tv_marketcap, tv_vol24h, tv_chan1h, tv_chan24h, tv_chan7d;
    private ImageView img_coin2, img_graph;
    private TextView[] tv_lab = new TextView[6];

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        instance = this;

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
            Coin coin = (Coin) getArguments().getSerializable("coin");
            update(coin);
        }
    }

    @Override
    public void onDestroyView() {
        instance = null;
        super.onDestroyView();
    }

    @SuppressLint("SetTextI18n")
    public void update(Coin coin) {
        if (tv_coin2 != null) {
            for(int i=0; i<6; i++) tv_lab[i].setVisibility(View.VISIBLE);
            img_coin2.setVisibility(View.VISIBLE);
            img_graph.setVisibility(View.VISIBLE);

            int id = coin.getId();
            String fileName = String.valueOf(id) + ".png";
            Bitmap icon = ImagesOff.openBitmap(getActivity(), fileName);
            if (icon != null) img_coin2.setImageBitmap(icon);

            fileName = "graph_" + fileName;
            Bitmap graph = ImagesOff.openBitmap(getActivity(), fileName);
            if (graph != null) onChange(graph);
            else {
                Repository r = Repository.getInstance(); // Singleton class, aqui já deve estar instanciada
                r.getGraph(this, id);
            }

            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
            tv_coin2.setText(coin.getName());
            tv_price2.setText(df.format(coin.getPrice()));
            tv_marketcap.setText(df.format(coin.getMarket_cap()));
            tv_vol24h.setText(df.format(coin.getVolume_24h()));
            tv_chan1h.setText(String.valueOf(coin.getPercent_change_1h()) + "%");
            tv_chan24h.setText(String.valueOf(coin.getPercent_change_24h()) + "%");
            tv_chan7d.setText(String.valueOf(coin.getPercent_change_7d()) + "%");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onChange(Bitmap bMap) {
        img_graph.setImageBitmap(bMap);
        img_graph.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
