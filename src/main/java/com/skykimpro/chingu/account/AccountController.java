package com.skykimpro.chingu.account;

import com.skykimpro.chingu.domain.Account;
import com.skykimpro.chingu.account.SignUpForm;
import com.skykimpro.chingu.account.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String singUpForm(Model model){
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String singUpSubmit(@Valid SignUpForm signUpForm, Errors errors){

        if(errors.hasErrors()){
            return "account/sign-up";
        }

        accountService.processNewAccount(signUpForm);
        return "redirect:/";
    }


}
