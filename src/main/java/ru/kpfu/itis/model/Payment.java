package ru.kpfu.itis.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 30.10.18
 *
 * @author Kuznetsov Maxim
 */
@Data
@Builder
public class Payment {
    private String id;
    private Date nextPaid;
}
