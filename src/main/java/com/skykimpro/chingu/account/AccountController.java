package com.skykimpro.chingu.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {

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

        return "redirect:/";
    }
}
