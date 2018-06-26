package com._if.model;

public class Rate {
	private String name;
	private Double price;
	
	public Rate(String name, Double price) {
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public Double getPrice() {
		return price;
	}
	
}
