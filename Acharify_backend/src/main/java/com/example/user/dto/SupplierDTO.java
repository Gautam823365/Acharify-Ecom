package com.example.user.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupplierDTO {

    private Long suppliersId;           // Supplier primary key
    private String suppliersName;
    private String supplierBusinessName;
    private String suppliersEmail;
    private String suppliersPhone;
    private String city;
    private String state;
    private String pincode;
    private String gstIdNo;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 
    private String password;            // Can be written from request, not returned in response

    private String suppliersAddressId;  // Now a string to store address

    private String status;              // active/inactive

    // Constructors
    public SupplierDTO() {}

    public SupplierDTO(Long suppliersId, String suppliersName, String supplierBusinessName, String suppliersEmail,
                       String suppliersPhone, String city, String state, String pincode, String gstIdNo,
                       String password, String suppliersAddressId, String status) {
        this.suppliersId = suppliersId;
        this.suppliersName = suppliersName;
        this.supplierBusinessName = supplierBusinessName;
        this.suppliersEmail = suppliersEmail;
        this.suppliersPhone = suppliersPhone;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.gstIdNo = gstIdNo;
        this.password = password;
        this.suppliersAddressId = suppliersAddressId;
        this.status = status;
    }

    // Getters and Setters
    public Long getSuppliersId() {
        return suppliersId;
    }

    public void setSuppliersId(Long suppliersId) {
        this.suppliersId = suppliersId;
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

    public String getSuppliersEmail() {
        return suppliersEmail;
    }

    public void setSuppliersEmail(String suppliersEmail) {
        this.suppliersEmail = suppliersEmail;
    }

    public String getSuppliersPhone() {
        return suppliersPhone;
    }

    public void setSuppliersPhone(String suppliersPhone) {
        this.suppliersPhone = suppliersPhone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGstIdNo() {
        return gstIdNo;
    }

    public void setGstIdNo(String gstIdNo) {
        this.gstIdNo = gstIdNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSuppliersAddressId() {
        return suppliersAddressId;
    }

    public void setSuppliersAddressId(String suppliersAddressId) {
        this.suppliersAddressId = suppliersAddressId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
