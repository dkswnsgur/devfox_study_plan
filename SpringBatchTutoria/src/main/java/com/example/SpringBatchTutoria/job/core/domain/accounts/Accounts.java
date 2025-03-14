package com.example.SpringBatchTutoria.job.core.domain.accounts;

import com.example.SpringBatchTutoria.job.core.domain.orders.Orders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@NoArgsConstructor
@Getter
@ToString
@Entity
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderItem;
    private Integer price;
    private Date orderDate;
    private Date accountDate;
    
    
	/*
	 * public Accounts() {
	 * 
	 * }
	 */
    
	public Accounts(Orders orders) {
    	this.id = orders.getId();
    	this.orderItem = orders.getOrderItem();
    	this.price = orders.getPrice();
    	this.orderDate = orders.getOrderDate();
    	this.accountDate = new Date();
    }
    
}
