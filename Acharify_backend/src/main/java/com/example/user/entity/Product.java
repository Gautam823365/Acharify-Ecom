package com.example.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq_gen")
    @SequenceGenerator(
            name = "products_seq_gen",
            sequenceName = "products_seq",
            allocationSize = 1
    )
    @Column(name = "PRODUCTSID")
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY)
    private Long productsId;

    @Version
    @Column(name = "VERSION", nullable = false)
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY)
    private Long version;

    @Column(name = "PRODUCTSNAME", length = 100, nullable = false)
    private String productsName;

    @Column(name = "PRODUCTSDESCRIPTION", length = 255)
    private String productsDescription;

    @Column(name = "PRODUCTSUNITPRICE")
    private Double productsUnitPrice;

    @Column(name = "PRODUCTSQUANTITY")
    private Integer productsQuantity;

    @Column(name = "PRODUCTSTHRESHOLD")
    private Integer productsThreshold;

    @Column(name = "PRODUCTSIMAGE", length = 255)
    private String productsImage; // ✅ URL ONLY

    @Column(name = "STATENAME", length = 50)
    private String stateName;

    // 🔐 SINGLE SUPPLIER FK
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SUPPLIERID", nullable = false)
    @JsonIgnore
    private Supplier supplier;

    // ===== constructors =====
    public Product() {}

    // ===== getters & setters =====

    public Long getProductsId() {
        return productsId;
    }

    public Long getVersion() {
        return version;
    }

    public String getProductsName() {
        return productsName;
    }

    public void setProductsName(String productsName) {
        this.productsName = productsName;
    }

    public String getProductsDescription() {
        return productsDescription;
    }

    public void setProductsDescription(String productsDescription) {
        this.productsDescription = productsDescription;
    }

    public Double getProductsUnitPrice() {
        return productsUnitPrice;
    }

    public void setProductsUnitPrice(Double productsUnitPrice) {
        this.productsUnitPrice = productsUnitPrice;
    }

    public Integer getProductsQuantity() {
        return productsQuantity;
    }

    public void setProductsQuantity(Integer productsQuantity) {
        this.productsQuantity = productsQuantity;
    }

    public Integer getProductsThreshold() {
        return productsThreshold;
    }

    public void setProductsThreshold(Integer productsThreshold) {
        this.productsThreshold = productsThreshold;
    }

    public String getProductsImage() {
        return productsImage;
    }

    public void setProductsImage(String productsImage) {
        this.productsImage = productsImage;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
