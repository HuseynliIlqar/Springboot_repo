package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Table(name = "funds")
@Getter
@Setter
@NoArgsConstructor
public class Fund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalAmount = new BigDecimal("10000000");

    @Column(nullable = false)
    private BigDecimal availableAmount = new BigDecimal("10000000");

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Fund(User user) {
        this.user = user;
        this.totalAmount = new BigDecimal("10000000");
        this.availableAmount = new BigDecimal("10000000");
    }

    public void addMoney(BigDecimal amount) {
        this.totalAmount = this.totalAmount.add(amount);
        this.availableAmount = this.availableAmount.add(amount);
    }

    public boolean canInvest(BigDecimal amount) {
        return this.availableAmount.compareTo(amount) >= 0;
    }

    public void invest(BigDecimal amount) {
        this.availableAmount = this.availableAmount.subtract(amount);
    }
}