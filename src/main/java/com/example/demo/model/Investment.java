package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
@Getter
@Setter
@NoArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    private String sector;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Manual constructor — yalnız lazım olan fieldlər
    public Investment(String name, BigDecimal amount, String description, String sector, User user) {
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.sector = sector;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}