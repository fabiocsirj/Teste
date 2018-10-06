package br.com.appwharehouse.teste;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "coin_table")
public class Coin implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "cRank")
    private int rank;

    @ColumnInfo(name = "cId")
    private int id;

    @ColumnInfo(name = "cName")
    private String name;

    @ColumnInfo(name = "cPrice")
    private double price;

    @ColumnInfo(name = "cMarket_cap")
    private double market_cap;

    @ColumnInfo(name = "cVolume_24h")
    private double volume_24h;

    @ColumnInfo(name = "cPercent_change_1h")
    private double percent_change_1h;

    @ColumnInfo(name = "cPercent_change_24h")
    private double percent_change_24h;

    @ColumnInfo(name = "cPercent_change_7d")
    private double percent_change_7d;

    public Coin(
                int rank,
                int id,
                String name,
                double price,
                double market_cap,
                double volume_24h,
                double percent_change_1h,
                double percent_change_24h,
                double percent_change_7d
    ) {
            this.rank = rank;
            this.id = id;
            this.name = name;
            this.price = price;
            this.market_cap = market_cap;
            this.volume_24h = volume_24h;
            this.percent_change_1h = percent_change_1h;
            this.percent_change_24h = percent_change_24h;
            this.percent_change_7d = percent_change_7d;
    }

    public int getRank() {
        return rank;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getMarket_cap() {
        return market_cap;
    }

    public double getVolume_24h() {
        return volume_24h;
    }

    public double getPercent_change_1h() {
        return percent_change_1h;
    }

    public double getPercent_change_24h() {
        return percent_change_24h;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }
}
