package com.example.award.service;

import com.example.award.model.Gift;

public final class CalcService {
    private final double concertPrice;    // фиксированная стоимость концерта

    public CalcService(double concertPrice) { this.concertPrice = concertPrice; }

    /** @return итоговая сумма с учётом концерта и скидки */
    public double total(Gift gift, boolean withConcert, boolean loyal) {
        if (gift == null) return 0;
        double sum = gift.price() + (withConcert ? concertPrice : 0);
        if (loyal) sum *= 0.9; // скидка 10%
        return Math.round(sum); // округлим до рублей
    }
}