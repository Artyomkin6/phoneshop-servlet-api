package com.es.phoneshop.model.product;

import com.es.phoneshop.model.abstract_dao.ItemWithId;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Product implements Cloneable, Serializable, ItemWithId {
    private Long id;
    private String code;
    private String description;
    /** null means there is no price because the product is outdated or new */
    private BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;
    private int stock;
    private String imageUrl;
    private List<PriceHistory> histories;

    public Product() {
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<PriceHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<PriceHistory> histories) {
        this.histories = histories;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Product clone() throws CloneNotSupportedException {
        Product clonedProduct = (Product) super.clone();
        clonedProduct.histories = histories.stream()
                .map(history -> {
                    try {
                        return history.clone();
                    } catch (CloneNotSupportedException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .collect(Collectors.toList());
        return clonedProduct;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Product product = (Product) object;
        return stock == product.stock
                && Objects.equals(id, product.id)
                && Objects.equals(code, product.code)
                && Objects.equals(description, product.description)
                && Objects.equals(price, product.price)
                && Objects.equals(currency, product.currency)
                && Objects.equals(imageUrl, product.imageUrl)
                && Objects.equals(histories, product.histories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, description, price, currency, stock, imageUrl, histories);
    }
}
