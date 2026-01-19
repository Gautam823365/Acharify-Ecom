package com.example.user.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poid;

    private Long posupplierid;
    private Long pouserid;
    private LocalDateTime poorderdate;
    private LocalDate poexpecteddeliveryDate;
    private String podeliverystatus;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchaseOrderItem> items;

    // Getters & Setters
    public Long getPoid() { return poid; }
    public void setPoid(Long poid) { this.poid = poid; }

    public Long getPosupplierid() { return posupplierid; }
    public void setPosupplierid(Long posupplierid) { this.posupplierid = posupplierid; }

    public Long getPouserid() { return pouserid; }
    public void setPouserid(Long pouserid) { this.pouserid = pouserid; }

    public LocalDateTime getPoorderdate() { return poorderdate; }
    public void setPoorderdate(LocalDateTime poorderdate) { this.poorderdate = poorderdate; }

    public LocalDate getPoexpecteddeliveryDate() { return poexpecteddeliveryDate; }
    public void setPoexpecteddeliveryDate(LocalDate poexpecteddeliveryDate) { this.poexpecteddeliveryDate = poexpecteddeliveryDate; }

    public String getPodeliverystatus() { return podeliverystatus; }
    public void setPodeliverystatus(String podeliverystatus) { this.podeliverystatus = podeliverystatus; }

    public List<PurchaseOrderItem> getItems() { return items; }
    public void setItems(List<PurchaseOrderItem> items) { this.items = items; }
}
