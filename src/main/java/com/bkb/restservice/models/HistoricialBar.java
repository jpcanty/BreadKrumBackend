package com.bkb.restservice.models;
import java.util.List;

public record HistoricialBar(List<Bars> bars, String symbol, String nextPage){}
