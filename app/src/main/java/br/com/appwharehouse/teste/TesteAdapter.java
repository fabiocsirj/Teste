package br.com.appwharehouse.teste;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

public class TesteAdapter extends RecyclerView.Adapter<TesteAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;
    private LayoutInflater layoutInflater;
    private JSONArray coins;
    private Map<Integer, Bitmap> mapIcons;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public TesteAdapter(Context context, JSONArray coins, Map<Integer, Bitmap> map) {
        this.layoutInflater = LayoutInflater.from(context);
        this.coins = coins;
        this.mapIcons = map;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TesteAdapter.ViewHolder holder, int position) {
        try {
            JSONObject coin = coins.getJSONObject(position);
            String name = coin.getString("name");
            holder.tv_coin.setText(name);

            JSONObject quotes = coin.getJSONObject("quotes");
            JSONObject usd = quotes.getJSONObject("USD");
            Double price = usd.getDouble("price");
            DecimalFormat df = new DecimalFormat("#,##0.00");
            df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
            String sPrice = df.format(price);
            holder.tv_price.setText(sPrice);

            holder.iv_coin.setImageBitmap(mapIcons.get(coin.getInt("id")));
            holder.iv_coin.setScaleType(ImageView.ScaleType.FIT_XY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return coins.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_coin, tv_price;
        ImageView iv_coin;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_coin = itemView.findViewById(R.id.tv_coin);
            tv_price = itemView.findViewById(R.id.tv_price);
            iv_coin = itemView.findViewById(R.id.iv_coin);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
