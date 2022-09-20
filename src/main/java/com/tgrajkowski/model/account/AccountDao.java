package com.tgrajkowski.model.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {

    @Query(value = "Select a From Account a LEFT JOIN FETCH a.fundList WHERE a.id= :id")
    Account findAccountWithFunds(Long id);

    @Query(value = "Select a From Account a LEFT JOIN FETCH a.fundList WHERE a.pesel= :pesel")
    Account findAccountByPesel(String pesel);

    boolean existsByPesel(String pesel);
}
