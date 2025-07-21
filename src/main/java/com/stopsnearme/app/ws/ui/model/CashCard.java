package com.stopsnearme.app.ws.ui.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CashCard {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	public Long id() {
        return id;
    }

	public CashCard(Double amount, String owner){
		this.owner = owner;
		this.amount = amount;
	}

	public CashCard(){
	}

    public void setId(Long id) {
        this.id = id;
    }
    private Double amount;
	private String owner;
    public Double amount() {
        return amount;
    }
    public void amount(Double amount) {
        this.amount = amount;
    }
    public String owner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
