package com.skykimpro.chingu.settings;

import com.skykimpro.chingu.account.AccountService;
import com.skykimpro.chingu.account.CurrentUser;
import com.skykimpro.chingu.domain.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    static final String SETTING_PROFILE_VIEW_NAME = "settings/profile";
    static final String SETTING_PROFILE_URL = "/settings/profile";

    static final String SETTING_PASSWORD_VIEW_NAME = "settings/password";
    static final String SETTING_PASSWORD_URL = "/settings/password";

    static final String SETTING_NOTIFICATIONS_VIEW_NAME = "settings/notifications";
    static final String SETTING_NOTIFICATIONS_URL = "/settings/notifications";

    private final AccountService accountService;

    @GetMapping(SETTING_PROFILE_URL)
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
        return SETTING_PROFILE_VIEW_NAME;
    }

    @PostMapping(SETTING_PROFILE_URL)
    public String updateProfileForm(@CurrentUser Account account, @Valid Profile profile, Errors errors,
                                Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTING_PROFILE_VIEW_NAME;
        }

        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:" + SETTING_PROFILE_URL;
    }

    @GetMapping(SETTING_PASSWORD_URL)
    public String updatePasswordForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTING_PASSWORD_VIEW_NAME;
    }

    @PostMapping(SETTING_PASSWORD_URL)
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors,
                                     Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTING_PASSWORD_VIEW_NAME;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message","비밀번호를 수정했습니다.");
        return "redirect:" + SETTING_PASSWORD_URL;
    }

    @GetMapping(SETTING_NOTIFICATIONS_URL)
    public String updateNotificatonForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Notifications(account));
        return SETTING_NOTIFICATIONS_VIEW_NAME;
    }

    @PostMapping(SETTING_NOTIFICATIONS_URL)
    public String updateNotificaton(@CurrentUser Account account, @Valid Notifications notifications, Errors errors,
                                     Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTING_NOTIFICATIONS_VIEW_NAME;
        }

        accountService.updateNotificatons(account, notifications);
        attributes.addFlashAttribute("message","알림 설정을 수정했습니다.");
        return "redirect:" + SETTING_NOTIFICATIONS_URL;
    }
}
