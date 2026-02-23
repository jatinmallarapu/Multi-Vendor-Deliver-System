package com.RestaurantService.RestaurantService.dto;

import jakarta.persistence.*;
import java.io.Serializable;


@Embeddable
public class CardDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cardType;
    private int cardNumber;
    private int cvv;

    public CardDTO() {}

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
}
