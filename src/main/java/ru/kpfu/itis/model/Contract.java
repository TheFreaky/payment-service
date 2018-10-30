package ru.kpfu.itis.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 30.10.18
 *
 * @author Kuznetsov Maxim
 */
@Data
@Builder
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date created;
    private Date last_paid;
    private Integer paymentInterval;
    private Long userId;
}
