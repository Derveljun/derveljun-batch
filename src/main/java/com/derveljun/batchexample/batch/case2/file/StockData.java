package com.derveljun.batchexample.batch.case2.file;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table( name = "stock_hist")
public class StockData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String stockCode;
    private double close;
    private double volume;
    private LocalDate date;
    private double start;
    private double high;
    private double low;

}
