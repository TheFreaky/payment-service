package ru.kpfu.itis.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

/**
 * 30.10.18
 *
 * @author Kuznetsov Maxim
 */
@Data
@Builder
public class Payment {
    @Id
    private String contractId;
    private Date nextPaid;
}
