package com.tgrajkowski.model.nbp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesDao extends JpaRepository<Rate, Long> {
    Rate findByCode(String code);
}
