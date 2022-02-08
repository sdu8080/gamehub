package com.mytest.gamehub.controller;

import com.mytest.gamehub.entity.Account;
import com.mytest.gamehub.entity.*;
import com.mytest.gamehub.repository.AccountRepository;
import com.mytest.gamehub.repository.AuthorityRepository;
import com.mytest.gamehub.repository.GameLinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AccountController {

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    GameLinkRepository gameLinkRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    // signup a new account, render the add-account.html page
    @GetMapping("/signup")
    public String showSignUpForm(Account account) {
        return "add-account";
    }

    // logic to add an account in DB
    @PostMapping("/addaccount")
    public String addAccount(@Valid Account account, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error("failed to add account. " + result.getAllErrors());
            return "add-account";
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        authorityRepository.save(new Authority(account.getEmail(), "ROLE_USER"));

        return "redirect:/login";
    }

    // render to the new game page
    @GetMapping("/newgame")
    public String showNewGameForm(GameLink gameLink) {
        return "add-gamelink";
    }

    // add a new GameLink in DB
    @PostMapping("/addgamelink")
    public String addGameLink(@Valid GameLink gameLink, BindingResult result, Model model) {

        if (result.hasErrors()) {
            logger.error("failed to add account. " + result.getAllErrors());
            return "add-gamelink";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        gameLink.setEmail(email);
        gameLinkRepository.save(gameLink);

        Account account = accountRepository.findByEmail(email).get();
        List<GameLink> gameLinks = gameLinkRepository.findByEmail(email);

        model.addAttribute("account", account);
        model.addAttribute("links", gameLinks);
        return "read-account";
    }

    @GetMapping("/deletegame/{id}")
    public String deleteGame(@PathVariable("id") Long id, Model model) {
        GameLink link = gameLinkRepository.findById(id).get();
        gameLinkRepository.delete(link);
        return "redirect:/";
    }

    @GetMapping("/index")
    public String index(Model model){
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = null;
        if (null != auth && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String email = auth.getName();
            logger.info("username: " + auth.getName());
            account = accountRepository.findByEmail(email).get();
            model.addAttribute("account", account);

            List<GameLink> gameLinks = gameLinkRepository.findByEmail(email);
            model.addAttribute("links", gameLinks);
            return "read-account";
        } else {
            return "home";
        }
    }

    @GetMapping("/login")
    public String login() {
        logger.info("render login page");
        return "/login";
    }

    @GetMapping("/403")
    public String error403() {
        logger.info("render 403 page");
        return "/error/403";
    }
}
