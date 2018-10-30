package ru.kpfu.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.model.Contract;

/**
 * 30.10.18
 *
 * @author Kuznetsov Maxim
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
}
