package com.derveljun.batchexample.batch.case2.file;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table( name = "stock_hist")
public class StockData {

    @Id
    private String stockCode;

    private double close;
    private double volume;
    private LocalDate date;
    private double start;
    private double high;
    private double low;

}
