package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 64)
    private String name;

    @Column(nullable = false,length = 3)
    private String symbol;

    @Column(nullable = false,length = 4)
    private String code;

    public Currency() {
    }

    public Currency( String name, String symbol, String code) {
        this.name = name;
        this.symbol = symbol;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name + " - " + this.code + " - " + this.symbol;
    }
}
