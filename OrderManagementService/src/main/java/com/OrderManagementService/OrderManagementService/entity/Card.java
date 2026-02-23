package com.OrderManagementService.OrderManagementService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Card implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private CardType cardType;
    private int cardno;
    private int cvv;
    @OneToMany(mappedBy = "card")
    @JsonIgnore
    private List<Order> order = new ArrayList<>();

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public int getCardno() {
        return cardno;
    }

    public void setCardno(int cardno) {
        this.cardno = cardno;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }
}
