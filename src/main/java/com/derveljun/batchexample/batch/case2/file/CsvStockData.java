package com.derveljun.batchexample.batch.case2.file;

import lombok.Data;

@Data
public class CsvStockData {

    public static final String[] CsvStockDataFields
            = new String[] {
                    "stockCode", "close", "volume",
                    "unknown", "date", "start",
                    "high", "low", "unknown1", "unknown2"
                };

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
