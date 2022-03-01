package com.es.phoneshop.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PriceHistory implements Cloneable, Serializable {
    private static final SimpleDateFormat DATE_FORMATTER
            = new SimpleDateFormat("d MMM yyyy");

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
        return DATE_FORMATTER.format(startDate.getTime());
    }

    @Override
    public PriceHistory clone() throws CloneNotSupportedException {
        PriceHistory clonedHistory = (PriceHistory) super.clone();
        if (startDate != null) {
            clonedHistory.startDate = (Calendar) startDate.clone();
        }
        return clonedHistory;
    }
}
