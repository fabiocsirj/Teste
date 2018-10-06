package br.com.appwharehouse.teste;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TesteAdapter extends RecyclerView.Adapter<TesteAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;
    private LayoutInflater layoutInflater;
    private List<Coin> coins;
    private Map<Integer, Bitmap> mapIcons;

    public interface ItemClickListener {
        void onItemClick(Coin coin);
    }

    public TesteAdapter(Context context, List<Coin> coins, Map<Integer, Bitmap> map) {
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
        holder.tv_coin.setText(coins.get(position).getName());
        Double price = coins.get(position).getPrice();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ITALY));
        String sPrice = df.format(price);
        holder.tv_price.setText(sPrice);
        holder.iv_coin.setImageBitmap(mapIcons.get(coins.get(position).getId()));
        holder.iv_coin.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public int getItemCount() {
        return coins.size();
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
            if (itemClickListener != null) itemClickListener.onItemClick(coins.get(getAdapterPosition()));
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
