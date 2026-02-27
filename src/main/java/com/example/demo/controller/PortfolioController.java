package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PortfolioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserRepository userRepository;

    public PortfolioController(PortfolioService portfolioService, UserRepository userRepository) {
        this.portfolioService = portfolioService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(defaultValue = "default") String sort, Model model) {

        User user = getCurrentUser(userDetails);
        model.addAttribute("fund", portfolioService.getFund(user));
        model.addAttribute("investments", portfolioService.getInvestments(user, sort));
        model.addAttribute("sort", sort);
        return "index";
    }

    @GetMapping("/investment/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("investment", portfolioService.getInvestmentById(id));
        return "detail";
    }

    @PostMapping("/investment/add")
    public String addInvestment(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String name, @RequestParam BigDecimal amount, @RequestParam String description, @RequestParam String sector, RedirectAttributes redirectAttributes) {

        User user = getCurrentUser(userDetails);
        portfolioService.addInvestment(user, name, amount, description, sector);

        redirectAttributes.addFlashAttribute("newInvestment", true);
        return "redirect:/";
    }

    @PostMapping("/investment/delete/{id}")
    public String deleteInvestment(@PathVariable Long id) {
        portfolioService.deleteInvestment(id);
        return "redirect:/";
    }

    @PostMapping("/investment/update/{id}")
    public String updateInvestment(@PathVariable Long id, @RequestParam String name) {
        portfolioService.updateInvestmentName(id, name);
        return "redirect:/";
    }

    @PostMapping("/fund/add")
    public String addToFund(@AuthenticationPrincipal UserDetails userDetails, @RequestParam BigDecimal amount) {

        User user = getCurrentUser(userDetails);
        portfolioService.addToFund(user, amount);
        return "redirect:/";
    }
}