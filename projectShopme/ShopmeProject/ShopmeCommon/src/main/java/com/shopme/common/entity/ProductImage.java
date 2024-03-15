package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    public ProductImage() {
    }
}
