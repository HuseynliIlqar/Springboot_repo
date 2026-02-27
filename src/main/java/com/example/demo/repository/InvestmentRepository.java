package com.example.demo.repository;

import com.example.demo.model.Investment;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByUser(User user);

    List<Investment> findByUserOrderByAmountAsc(User user);

    List<Investment> findByUserOrderByAmountDesc(User user);

    List<Investment> findByUserOrderByNameAsc(User user);
}