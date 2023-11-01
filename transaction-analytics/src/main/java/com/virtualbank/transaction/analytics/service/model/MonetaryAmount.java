package com.virtualbank.transaction.analytics.service.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

public record MonetaryAmount(long amount,
                             String currency) {

    @JsonIgnore
    public boolean isSpending() {
        return amount < 0;
    }

    @JsonIgnore
    public boolean isIncome() {
        return amount > 0;
    }
}
