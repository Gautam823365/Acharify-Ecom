	package com.example.user.entity;
	
	import com.fasterxml.jackson.annotation.JsonIgnore;
	
	import jakarta.persistence.*;
	
	@Entity
	@Table(name = "SUPPLIERS")
	public class Supplier {
	
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_seq")
		@SequenceGenerator(
		    name = "supplier_seq",
		    sequenceName = "SUPPLIERS_SEQ",
		    allocationSize = 1
		)
		@Column(name = "SUPPLIERSID")
	    private Long supplierId;
	
	    @Column(name = "SUPPLIERSNAME")
	    private String suppliersName;
	
	    // 🔁 Renamed column
	    @Column(name = "SUPPLIER_BUSINESS_NAME")
	    private String supplierBusinessName;
	
	    @Column(name = "SUPPLIERSPHONE")
	    private String suppliersPhone;
	
	    @Column(name = "SUPPLIERSEMAIL")
	    private String suppliersEmail;
	
	    @Column(name = "SUPPLIERSADDRESSID")
	    private String suppliersAddressId;
	
	    // ✅ New columns
	    @Column(name = "GST_ID_NO", length = 20, unique = true)
	    private String gstIdNo;
	
	    @Column(name = "CITY", length = 100)
	    private String city;
	
	    @Column(name = "PINCODE", length = 10)
	    private String pincode;
	
	    @Column(name = "STATE", length = 100)
	    private String state;
	
	    @Column(name = "STATUS", length = 10)
	    private String status; // ACTIVE / INACTIVE
	    
	    @JsonIgnore
	    @Column(name = "PASSWORD", length = 255)
	    private String password;
	
	    // Getter & Setter
	    public String getPassword() { return password; }
	    public void setPassword(String password) { this.password = password; }
	
	
	    // Constructors
	    public Supplier() {}
	
	    // Getters & Setters
	
	    public Long getSupplierId() {
	        return supplierId;
	    }
	
	    public void setSupplierId(Long supplierId) {
	        this.supplierId = supplierId;
	    }
	
	    public String getSuppliersName() {
	        return suppliersName;
	    }
	
	    public void setSuppliersName(String suppliersName) {
	        this.suppliersName = suppliersName;
	    }
	
	    public String getSupplierBusinessName() {
	        return supplierBusinessName;
	    }
	
	    public void setSupplierBusinessName(String supplierBusinessName) {
	        this.supplierBusinessName = supplierBusinessName;
	    }
	
	    public String getSuppliersPhone() {
	        return suppliersPhone;
	    }
	
	    public void setSuppliersPhone(String suppliersPhone) {
	        this.suppliersPhone = suppliersPhone;
	    }
	
	    public String getSuppliersEmail() {
	        return suppliersEmail;
	    }
	
	    public void setSuppliersEmail(String suppliersEmail) {
	        this.suppliersEmail = suppliersEmail;
	    }
	
	    public String getSuppliersAddressId() {
	        return suppliersAddressId;
	    }
	
	    public void setSuppliersAddressId(String suppliersAddressId) {
	        this.suppliersAddressId = suppliersAddressId;
	    }
	
	    public String getGstIdNo() {
	        return gstIdNo;
	    }
	
	    public void setGstIdNo(String gstIdNo) {
	        this.gstIdNo = gstIdNo;
	    }
	
	    public String getCity() {
	        return city;
	    }
	
	    public void setCity(String city) {
	        this.city = city;
	    }
	
	    public String getPincode() {
	        return pincode;
	    }
	
	    public void setPincode(String pincode) {
	        this.pincode = pincode;
	    }
	
	    public String getState() {
	        return state;
	    }
	
	    public void setState(String state) {
	        this.state = state;
	    }
	
	    public String getStatus() {
	        return status;
	    }
	
	    public void setStatus(String status) {
	        this.status = status;
	    }
	}
