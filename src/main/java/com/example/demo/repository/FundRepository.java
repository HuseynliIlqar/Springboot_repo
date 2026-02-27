package com.example.demo.repository;

import com.example.demo.model.Fund;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
    Optional<Fund> findByUser(User user);
}