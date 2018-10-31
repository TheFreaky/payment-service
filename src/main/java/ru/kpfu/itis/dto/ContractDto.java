package ru.kpfu.itis.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ContractDto {
    private Date created;
    private Integer paymentInterval;
    private Long userId;
}
