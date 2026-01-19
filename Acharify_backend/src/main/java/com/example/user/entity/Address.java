package com.example.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ADDRESS", schema = "HR")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @SequenceGenerator(name = "address_seq", sequenceName = "ADDRESS_SEQ", allocationSize = 1)
    @Column(name = "ADDRESSID")
    private Long id;

    @Size(max = 100)
    @Column(name = "STREET")
    private String street;

    @Size(max = 50)
    @Column(name = "CITY")
    private String city;

    @Size(max = 50)
    @Column(name = "STATE")
    private String state;

    @Size(max = 10)
    @Column(name = "ZIP")
    private String zip;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZip() { return zip; }
    public void setZip(String zip) { this.zip = zip; }
}
