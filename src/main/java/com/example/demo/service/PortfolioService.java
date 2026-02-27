package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PortfolioService {

    private final InvestmentRepository investmentRepository;
    private final FundRepository fundRepository;

    public PortfolioService(InvestmentRepository investmentRepository, FundRepository fundRepository) {
        this.investmentRepository = investmentRepository;
        this.fundRepository = fundRepository;
    }

    public void addToFund(User user, BigDecimal amount) {
        Fund fund = fundRepository.findByUser(user).orElseThrow();
        fund.addMoney(amount);
        fundRepository.save(fund);
    }

    public void addInvestment(User user, String name, BigDecimal amount, String description, String sector) {
        Fund fund = fundRepository.findByUser(user).orElseThrow();
        if (!fund.canInvest(amount)) {
            throw new RuntimeException("Kifayət qədər balans yoxdur!");
        }
        fund.invest(amount);
        fundRepository.save(fund);
        Investment investment = new Investment(name, amount, description, sector, user);
        investmentRepository.save(investment);
    }

    public void deleteInvestment(Long id) {
        Investment investment = investmentRepository.findById(id).orElseThrow();

        Fund fund = fundRepository.findByUser(investment.getUser()).orElseThrow();
        fund.setAvailableAmount(fund.getAvailableAmount().add(investment.getAmount()));
        fundRepository.save(fund);

        investmentRepository.delete(investment);
    }

    public void updateInvestmentName(Long id, String newName) {
        Investment investment = investmentRepository.findById(id).orElseThrow();
        investment.setName(newName);
        investmentRepository.save(investment);
    }

    public List<Investment> getInvestments(User user, String sort) {
        return switch (sort) {
            case "asc"   -> investmentRepository.findByUserOrderByAmountAsc(user);
            case "desc"  -> investmentRepository.findByUserOrderByAmountDesc(user);
            case "alpha" -> investmentRepository.findByUserOrderByNameAsc(user);
            default      -> investmentRepository.findByUser(user);
        };
    }

    public Fund getFund(User user) {
        return fundRepository.findByUser(user).orElseThrow();
    }

    public Investment getInvestmentById(Long id) {
        return investmentRepository.findById(id).orElseThrow();
    }
}