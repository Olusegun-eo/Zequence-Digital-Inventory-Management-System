package com.zequence.ZequenceIms.entity;

public enum StockStatus {

    OUTOFSTOCK("OUT-OF-STOCK"),
    INSTOCK("IN-STOCK"),
    RUNNINGOUT("RUNNING-OUT-OF-STOCK");
    public final String availability;

    StockStatus(String availability) {
        this.availability = availability;
    }
}
