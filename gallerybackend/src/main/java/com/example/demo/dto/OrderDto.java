package com.example.demo.dto;

import lombok.Getter;

@Getter
public class OrderDto {

    private String name;
    private String address;
    private String payment;
    private String cardNumber;
    private String items;

}
