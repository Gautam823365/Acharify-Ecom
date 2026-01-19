package com.example.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItem {

	@Id
	@SequenceGenerator(
	    name = "purchase_order_items_seq",
	    sequenceName = "purchase_order_items_seq",
	    allocationSize = 1)
	@GeneratedValue(
	    strategy = GenerationType.SEQUENCE,
	    generator = "purchase_order_items_seq")

    private Long productId;
    private Integer quantity;
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poid")
    private PurchaseOrder purchaseOrder;

   

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }
}
