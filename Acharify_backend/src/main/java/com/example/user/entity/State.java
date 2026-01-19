package com.example.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "STATE")
public class State {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "state_seq_gen")
	@SequenceGenerator(name = "state_seq_gen", sequenceName = "state_seq", allocationSize = 1)
	@Column(name = "STATEID")
	private Long stateId;
    @Column(name = "STATENAME", nullable = false, length = 100)
    private String stateName;

    @Column(name = "STATECODE", length = 10)
    private String stateCode;

    @Column(name = "POPULATION")
    private Long population;

    @Column(name = "CAPITAL", length = 100)
    private String capital;

    // Constructors
    public State() {}

    public State(Long stateId, String stateName, String stateCode, Long population, String capital) {
        this.stateId = stateId;
        this.stateName = stateName;
        this.stateCode = stateCode;
        this.population = population;
        this.capital = capital;
    }

    // Getters and Setters
    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {	
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }
}
