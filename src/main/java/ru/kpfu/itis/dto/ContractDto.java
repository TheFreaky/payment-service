package ru.kpfu.itis.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ContractDto {
    private Date created;
    private Integer paymentInterval;
    private Long userId;
}
