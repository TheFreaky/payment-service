package ru.kpfu.itis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 30.10.18
 *
 * @author Kuznetsov Maxim
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contract {

    @Id
    private String id;
    private Date created;
    private Date nextPaymentDate;
    private Integer paymentInterval;
    private Long userId;
}
