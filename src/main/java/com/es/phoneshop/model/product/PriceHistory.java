package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PriceHistory {
    private Calendar startDate;
    private BigDecimal price;

    public PriceHistory(Calendar startDate, BigDecimal price) {
        this.startDate = startDate;
        this.price = price;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStringDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
        return formatter.format(startDate.getTime());
    }
}
