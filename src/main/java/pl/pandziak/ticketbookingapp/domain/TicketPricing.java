package pl.pandziak.ticketbookingapp.domain;

import java.math.BigDecimal;

public enum TicketPricing {

    CHILD(BigDecimal.valueOf(12.5)),
    STUDENT(BigDecimal.valueOf(18)),
    ADULT(BigDecimal.valueOf(25));

    private final BigDecimal price;

    TicketPricing(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

}
