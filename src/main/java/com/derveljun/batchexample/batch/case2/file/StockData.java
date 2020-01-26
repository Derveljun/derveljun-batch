package com.derveljun.batchexample.batch.case2.file;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table( name = "stock_hist")
public class StockData {

    //,137500,70348,9711,20190816,142000,142000,135000,,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String stockCode;
    private double close;
    private double volume;
    private double unknown;
    private String date;
    private double start;
    private double high;
    private double low;
    private String unknown1;
    private String unknown2;

}
