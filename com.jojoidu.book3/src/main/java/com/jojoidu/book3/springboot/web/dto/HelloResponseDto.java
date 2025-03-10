package com.jojoidu.book3.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class HelloResponseDto {

    private final String name;
    private final int amount;

    public HelloResponseDto(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
